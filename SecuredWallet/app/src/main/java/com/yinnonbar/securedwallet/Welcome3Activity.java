package com.yinnonbar.securedwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Yinnon Bratspiess on 05/05/2016.
 */
public class Welcome3Activity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3welcome);
        Button NextBtn = (Button) findViewById(R.id.welcome3NextBtn);
        NextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent welcome4Intent = new Intent(getApplicationContext(), Welcome4Activity.class);
                startActivity(welcome4Intent);
            }
        });
    }

}
