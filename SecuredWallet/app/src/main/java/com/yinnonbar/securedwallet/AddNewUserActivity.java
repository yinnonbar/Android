package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Yinnon Bratspiess on 06/04/2016.
 */
public class AddNewUserActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
        final DBHelper dbhelper = new DBHelper(getBaseContext());;
        Button acceptBtn = (Button) findViewById(R.id.addNewUserBtn);
        Button cancelBtn = (Button) findViewById(R.id.cancelAddNewUserBtn);
        final CheckBox showHidePassChbox = (CheckBox) findViewById(R.id.showHidePassChbox);
        ((EditText) findViewById((R.id.newPassword))).setTransformationMethod(PasswordTransformationMethod.getInstance());
        showHidePassChbox.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //((EditText) findViewById((R.id.newPassword))).setInputType(InputType.TYPE_CLASS_TEXT);
                    ((EditText) findViewById((R.id.newPassword))).setTransformationMethod(SingleLineTransformationMethod.getInstance());
                } else if (!isChecked){
                    //((EditText) findViewById((R.id.newPassword))).setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ((EditText) findViewById((R.id.newPassword))).setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        }));

        acceptBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String newUserName = ((EditText) findViewById((R.id.newUserName))).getText().toString();
                String newPassword = ((EditText) findViewById((R.id.newPassword))).getText().toString();
                if (newUserName.isEmpty()
                        || newPassword.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please Enter Username And Password", Toast.LENGTH_SHORT).show();
                }

                else if (dbhelper.checkExistence(newUserName)) {
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                }
                else {

                    User newUser = new User(newUserName, newPassword);
                    dbhelper.addUser(newUser);
                    Toast.makeText(getApplicationContext(), newUserName + " was created", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
