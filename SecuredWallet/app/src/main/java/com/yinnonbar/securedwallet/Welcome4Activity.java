package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yinnon Bratspiess on 05/05/2016.
 */
public class Welcome4Activity extends AppCompatActivity{
    boolean wasRead = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4welcome);
        Button NextBtn = (Button) findViewById(R.id.welcome4NextBtn);

        TextView termsText = (TextView) findViewById(R.id.termsOfUseText);
        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasRead = true;
                new AlertDialog.Builder(Welcome4Activity.this)
                        .setTitle(R.string.termsOfUse).setMessage(R.string.disclaimerMessage)
                        .setNeutralButton(R.string.close, null).show();
            }
        });
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) findViewById(R.id.agreeTermsCheckBox);
                if (!wasRead || !checkBox.isChecked()){
                    Toast.makeText(getApplicationContext(), R.string.readAndAcceptTerms,
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
    }
}
