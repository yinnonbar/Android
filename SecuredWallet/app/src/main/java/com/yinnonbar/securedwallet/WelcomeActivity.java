package com.yinnonbar.securedwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Yinnon Bratspiess on 05/05/2016.
 */
public class WelcomeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button NextBtn = (Button) findViewById(R.id.welcomeNextBtn);
        NextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent welcome2Intent = new Intent(getApplicationContext(), Welcome2Activity.class);
                startActivity(welcome2Intent);
            }
        });
    }
}