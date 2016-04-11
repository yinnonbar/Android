package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Yinnon Bratspiess on 07/04/2016.
 */
public class WalletActivity extends AppCompatActivity {
    private static final String WALLETLOG = "DatabaseHelper";
    DBHelper dbhelper;
    ArrayList<WalletItem> itemsArr;
    ListView list;
    String currUserName;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        //walletTitle

        list = (ListView) findViewById(R.id.listView);
        Intent walletIntent = getIntent();
        final Intent editItemIntent = new Intent(this, EditItemActivity.class);
        dbhelper =  new DBHelper(getBaseContext());
        currUserName = walletIntent.getStringExtra("username");
        ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet");
        itemsArr = dbhelper.getAllItems(currUserName);
        adapter = new MyAdapter(this, 0, itemsArr);
        list.setAdapter(adapter);
        if (itemsArr.isEmpty()) {
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet \n" +
                    " Seems like it's empty");
        }else {
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet");
        }
        /*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WalletActivity.this);
                final String longClickedItemKey = itemsArr.get(position).getKey();
                alertDialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog deleteItemAlertDialog = new AlertDialog.Builder(WalletActivity.this)
                                .setTitle("Delete").setMessage("Are you sure you want to delete " + longClickedItemKey + " ?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbhelper.deleteItemByKey(currUserName, longClickedItemKey);
                                        itemsArr.remove(position);
                                        adapter.notifyDataSetChanged();
                                        if (itemsArr.isEmpty()) {
                                            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet \n" +
                                                    "Seems like it's empty");
                                        } else {
                                            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet");
                                        }
                                        Toast.makeText(getApplicationContext(), longClickedItemKey + " was Removed", Toast.LENGTH_SHORT).show();
                                    }
                                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("Edit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editItemIntent.putExtra("oldKey", longClickedItemKey);
                        //editItemIntent.putExtra("key", longClickedItemKey);
                        editItemIntent.putExtra("oldValue", itemsArr.get(position).getValue());
                        editItemIntent.putExtra("position", position);
                        startActivityForResult(editItemIntent, 3);
                    }
                }).show();
                return false;
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.wallet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_item:
                //on clicking add send to add new item activity
                Intent addNewItemIntent = new Intent(this, AddNewItemActivity.class);
                startActivityForResult(addNewItemIntent, 2);
                break;
            case (R.id.menu_remove_user_item):
                AlertDialog.Builder removeUserAlertDialog = new AlertDialog.Builder(this);
                removeUserAlertDialog.setTitle("Delete User").setMessage("Are you sure you want to remove yourself from the database ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbhelper.deleteUser(currUserName);
                                adapter.notifyDataSetChanged();
                                finish();
                                Toast.makeText(getApplicationContext(), currUserName + " was removed", Toast.LENGTH_LONG).show();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                }).show();
                break;
            case (R.id.menu_logout_item):
                AlertDialog.Builder logoutAlertDialog = new AlertDialog.Builder(this);
                logoutAlertDialog.setTitle("Logout").setMessage("Are you sure you want to logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

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
    //  when data is sent back from add item intent
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == 2 && resCode == RESULT_OK) {
            // adding the data as a task
            String keyData = data.getStringExtra("key");
            String valueData = data.getStringExtra("value");
            WalletItem newItem = new WalletItem(keyData, valueData);
            if (!dbhelper.checkItemExistence(currUserName, keyData)){
                itemsArr.add(newItem);
                dbhelper.addData(currUserName, keyData, valueData);
            }else{
                Toast.makeText(getApplicationContext(), "Item with the same key already exists", Toast.LENGTH_SHORT).show();
            }
            ArrayList<WalletItem> currlist = dbhelper.getAllItems(currUserName);
            for (WalletItem item : currlist) {
                Log.e(WALLETLOG, "in wallet activity get curr key " +item.getKey());
            }
            adapter.notifyDataSetChanged();
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet");
        }
        else if (reqCode == 3 && resCode == RESULT_OK) {
            int itemPos = data.getIntExtra("position", -1);
            String oldKey = data.getStringExtra("oldKey");
            String newKey = data.getStringExtra("newKey");
            String newValue = data.getStringExtra("newValue");
            if (!dbhelper.checkItemExistence(currUserName, newKey) ||oldKey.equals(newKey)) {
                WalletItem newItem = new WalletItem(newKey, newValue);
                itemsArr.set(itemPos, newItem);
                dbhelper.updateItem(currUserName, newItem, oldKey);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Item was edited successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Could'nt edit item because an item with " +
                        "the same key is already exists", Toast.LENGTH_LONG).show();
            }

          //  ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + " Secured Wallet");

        }
    }

}
