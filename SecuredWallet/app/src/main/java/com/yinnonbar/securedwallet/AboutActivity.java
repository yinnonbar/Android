package com.yinnonbar.securedwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Yinnon Bratspiess on 03/05/2016.
 */
public class AboutActivity extends Activity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final Intent intent = new Intent();
        TextView myMail = (TextView) findViewById(R.id.myMailTextView);
        myMail.setText(Html.fromHtml("<a href=\"mailto:yinnonbar@gmail.com\">Contact Me</a>"));
        myMail.setMovementMethod(LinkMovementMethod.getInstance());
        Button closeBtn = (Button) findViewById(R.id.aboutCloseBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}