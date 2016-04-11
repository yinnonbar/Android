package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final DBHelper dbhelper =  new DBHelper(getBaseContext());
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        Button createUserBtn = (Button) findViewById(R.id.createUserButton);
        createUserBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((EditText) findViewById((R.id.usernameText))).setText("");
                ((EditText) findViewById((R.id.passwordText))).setText("");
                Intent addNewItemIntent = new Intent(getApplicationContext(), AddNewUserActivity.class);
                startActivity(addNewItemIntent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String userNameForLogin = ((EditText) findViewById((R.id.usernameText))).getText().toString();
                String passwordForLogin = ((EditText) findViewById((R.id.passwordText))).getText().toString();
                if (dbhelper.verification(userNameForLogin, passwordForLogin)) {
                    ((EditText) findViewById((R.id.usernameText))).setText("");
                    ((EditText) findViewById((R.id.passwordText))).setText("");
                    Intent walletIntent = new Intent(getApplicationContext(), WalletActivity.class);
                    walletIntent.putExtra("username", userNameForLogin);
                    startActivity(walletIntent);

                } else {
                    new AlertDialog.Builder(com.yinnonbar.securedwallet.LoginActivity.this)
                            .setTitle("Invalid User").setMessage("Please enter a valid user and password")
                            .setNeutralButton("Close", null).show();
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
            case R.id.menu_about_item:
                AlertDialog.Builder aboutAlertDialog = new AlertDialog.Builder(this);
                aboutAlertDialog.setTitle("Secured Wallet").setMessage("Secured Wallet was created by Yinnon Bratspiess. \n" +
                        "Contact me @ yinnonbar@gmail.com")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                }
                }).show();
                break;
            case R.id.menu_exit_item:
                AlertDialog.Builder exitAlertDialog = new AlertDialog.Builder(this);
                exitAlertDialog.setTitle("Exit").setMessage("Are you sure you want to leave ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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