package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final DBHelper dbhelper =  new DBHelper(getBaseContext());
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            // first time task
            Intent welcomeIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(welcomeIntent);
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        Button createUserBtn = (Button) findViewById(R.id.createUserButton);
        //on clicking create user button starting the Add new user activity
        createUserBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((EditText) findViewById((R.id.usernameText))).setText("");
                ((EditText) findViewById((R.id.passwordText))).setText("");
                Intent addNewItemIntent = new Intent(getApplicationContext(), AddNewUserActivity.class);
                startActivity(addNewItemIntent);
            }
        });
        //on clicking login button
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String userNameForLogin = ((EditText) findViewById((R.id.usernameText))).getText().toString();
                String passwordForLogin = ((EditText) findViewById((R.id.passwordText))).getText().toString();
                //checks if username and password exists on DB
                if (dbhelper.verification(userNameForLogin, passwordForLogin)) {
                    ((EditText) findViewById((R.id.usernameText))).setText("");
                    ((EditText) findViewById((R.id.passwordText))).setText("");
                    //user in DB going to his wallet
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    walletIntent.putExtra("username", userNameForLogin);
                    startActivity(walletIntent);
                //user not exists in DB or a wrong pass
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.invalidUser).setMessage(R.string.pleaseEnterValidUserAndPass)
                            .setNeutralButton(R.string.close, null).show();
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //about app
            case R.id.menu_about_item:
                Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutIntent);
                break;
            //exit app
            case R.id.menu_exit_item:
                AlertDialog.Builder exitAlertDialog = new AlertDialog.Builder(this);
                exitAlertDialog.setTitle(R.string.exit).setMessage(R.string.areYouSureYouWantToLeave)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}