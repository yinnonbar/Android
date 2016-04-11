package com.yinnonbar.securedwallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Yinnon Bratspiess on 07/04/2016.
 */
public class AddNewItemActivity extends Activity {
    private static final String ITEMLOG = "DatabaseHelper";
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        final Intent intent = new Intent();
        Button okBtn = (Button) findViewById(R.id.newItembtnOK);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById((R.id.edtNewKey))).getText().toString().isEmpty()
                        || ((EditText) findViewById((R.id.edtNewValue))).getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter A Key & A Value", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(ITEMLOG, "key in add new item is " +((EditText) findViewById((R.id.edtNewKey))).getText().toString());
                    intent.putExtra("key", ((EditText) findViewById((R.id.edtNewKey))).getText().toString());
                    intent.putExtra("value", ((EditText) findViewById((R.id.edtNewValue))).getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.newItembtnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialogBuilder.setTitle("Cancel")
                        .setMessage("Are you sure you want to cancel ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialogBuilder.show();
            }
        });
    }
}
