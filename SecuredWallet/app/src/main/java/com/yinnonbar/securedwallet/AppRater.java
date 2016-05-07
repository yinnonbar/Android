package com.yinnonbar.securedwallet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Yinnon Bratspiess on 07/05/2016.
 */
public class AppRater {
    private final static String APP_TITLE = "Secured Wallet";
    private final static String APP_PNAME = "com.yinnonbar.securedwallet";

    //min number of days till prompt
    private final static int DAYS_UNTIL_PROMPT = 3;
    //min number of launches
    private final static int LAUNCHES_UNTIL_PROMPT = 3;
    //time consts
    private final static int HOURS_A_DAY = 24;
    private final static int MINUTES_A_HOUR = 60;
    private final static int SECONDS_A_MINUTES = 60;
    private final static int MILSECOND = 1000;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * HOURS_A_DAY * MINUTES_A_HOUR * SECONDS_A_MINUTES * MILSECOND)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("rate " + APP_TITLE);
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(Color.parseColor("#3F51B5"));
        TextView tv = new TextView(mContext);
        TextView tvTitle = new TextView(mContext);
        tvTitle.setText(R.string.rateSecuredWallet);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setBackgroundColor(Color.parseColor("#303F9F"));
        tvTitle.setTextSize(18);
        tvTitle.setTextColor(Color.WHITE);
        ll.addView(tvTitle);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(15);
        tv.setTextColor(Color.WHITE);
        tv.setWidth(420);
        tv.setPadding(15, 3, 4, 0);
        ll.addView(tv);
        Button rateBtn = new Button(mContext);
        rateBtn.setText("Rate " + APP_TITLE);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll.addView(rateBtn);

        Button remindLaterBtn = new Button(mContext);
        remindLaterBtn.setText("Remind me later");
        remindLaterBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(remindLaterBtn);

        Button noRateBtn = new Button(mContext);
        noRateBtn.setText("No, thanks");
        noRateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(noRateBtn);

        dialog.setContentView(ll);
        dialog.show();
    }
}