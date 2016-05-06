package com.yinnonbar.securedwallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
                //key or value are empty
                if (((EditText) findViewById((R.id.edtNewKey))).getText().toString().isEmpty()
                        || ((EditText) findViewById((R.id.edtNewValue))).getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.pleaseEnterKeyAndValue, Toast.LENGTH_SHORT).show();
                } else {
                    //sending back to wallet intent the key and the value
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
                alertDialogBuilder.setTitle(R.string.cancel)
                        .setMessage(R.string.sureToCancel)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialogBuilder.show();
            }
        });
    }
}
