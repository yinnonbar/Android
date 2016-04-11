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
 * Created by Yinnon Bratspiess on 10/04/2016.
 */
public class EditItemActivity extends Activity {
    private static final String ITEMLOG = "DatabaseHelper";
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        final Intent intent = getIntent();
        Button okBtn = (Button) findViewById(R.id.editItembtnOK);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final String oldKey = intent.getStringExtra("oldKey");
        final int itemPos = intent.getIntExtra("position", -1);
        final String oldValue = intent.getStringExtra("oldValue");
        Log.e(ITEMLOG, "item pos is " + itemPos);
        Log.e(ITEMLOG, "old key is " + oldKey);
        Log.e(ITEMLOG, "old value is " + oldValue);
        ((EditText) findViewById((R.id.editItemKey))).setText(oldKey);
        ((EditText) findViewById((R.id.editItemValue))).setText(oldValue);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById((R.id.editItemKey))).getText().toString().isEmpty()
                        || ((EditText) findViewById((R.id.editItemValue))).getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter A Key & A Value", Toast.LENGTH_SHORT).show();
                } else if (((EditText) findViewById((R.id.editItemKey))).getText().toString().equals(oldKey)
                && ((EditText) findViewById((R.id.editItemValue))).getText().toString().equals(oldValue)){
                    Toast.makeText(getApplicationContext(), "No change was made", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(ITEMLOG, "key in add new item is " + ((EditText) findViewById((R.id.editItemKey))).getText().toString());
                    intent.putExtra("position", itemPos);
                    intent.putExtra("oldKey", oldKey);
                    intent.putExtra("newKey", ((EditText) findViewById((R.id.editItemKey))).getText().toString());
                    intent.putExtra("newValue", ((EditText) findViewById((R.id.editItemValue))).getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.editItembtnCancel);
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
