package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
   // private static final String WALLETLOG = "DatabaseHelper";
    private static final int ADD_ITEM = 2;
    private static final int EDIT_ITEM = 3;

    DBHelper dbhelper;
    ArrayList<WalletItem> itemsArr;
    ListView list;
    String currUserName;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        list = (ListView) findViewById(R.id.listView);
        Intent walletIntent = getIntent();
        final Intent editItemIntent = new Intent(this, EditItemActivity.class);
        dbhelper =  new DBHelper(getBaseContext());
        currUserName = walletIntent.getStringExtra("username");
        ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + getString(R.string.SecuredWallet));
        itemsArr = dbhelper.getAllItems(currUserName);
        adapter = new MyAdapter(this, 0, itemsArr);
        list.setAdapter(adapter);
        if (itemsArr.isEmpty()) {
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName +
                    getString(R.string.SecuredWallet) + "\n"  +
                    getString(R.string.seemsLikeItsEmpty));
        }else {
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + getString(R.string.SecuredWallet));
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder showItemAlertDialogBuilder = new AlertDialog.Builder(WalletActivity.this);
                //a custom title for the dialog
                TextView customTitle = new TextView(getApplicationContext());
                customTitle.setText(itemsArr.get(position).getKey());
                customTitle.setTextSize(20);
                customTitle.setPadding(10, 10, 10, 10);
                customTitle.setTextColor(Color.BLUE);
                customTitle.setGravity(Gravity.CENTER);

                showItemAlertDialogBuilder.setCustomTitle(customTitle)
                        .setMessage(itemsArr.get(position).getValue())
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        //on item long click listener
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder longClickAlertDialogBuilder = new AlertDialog.Builder(WalletActivity.this);
                final String longClickedItemKey = itemsArr.get(position).getKey();
                longClickAlertDialogBuilder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog deleteItemAlertDialog = new AlertDialog.Builder(WalletActivity.this)
                                .setTitle(R.string.delete).setMessage(getString(R.string.sureToDelete)
                                        + longClickedItemKey + " ?")
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //deleting the item
                                        dbhelper.deleteItemByKey(currUserName, longClickedItemKey);
                                        itemsArr.remove(position);
                                        adapter.notifyDataSetChanged();
                                        //if now the list is empty then show the matching message
                                        if (itemsArr.isEmpty()) {
                                            ((TextView) findViewById(R.id.walletTitle))
                                                    .setText(currUserName +
                                                            getString(R.string.SecuredWallet) + "\n"  +
                                                            getString(R.string.seemsLikeItsEmpty));
                                        } else {
                                            ((TextView) findViewById(R.id.walletTitle))
                                                    .setText(currUserName + getString(R.string.SecuredWallet));
                                        }
                                        Toast.makeText(getApplicationContext(), longClickedItemKey
                                                + getString(R.string.wasRemoved), Toast.LENGTH_SHORT).show();
                                    }
                                }).setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }).setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                //case of edit item
                }).setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editItemIntent.putExtra("oldKey", longClickedItemKey);
                        editItemIntent.putExtra("oldValue", itemsArr.get(position).getValue());
                        editItemIntent.putExtra("position", position);
                        startActivityForResult(editItemIntent, EDIT_ITEM);
                    }
                }).show();
                return true;
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
                startActivityForResult(addNewItemIntent, ADD_ITEM);
                break;
            //case of remove user
            case (R.id.menu_remove_user_item):
                AlertDialog.Builder removeUserAlertDialog = new AlertDialog.Builder(this);
                removeUserAlertDialog.setTitle(R.string.deleteUser).
                        setMessage(R.string.areYouSureYouWantToRemoveYourself)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbhelper.deleteUser(currUserName);
                                adapter.notifyDataSetChanged();
                                finish();
                                Toast.makeText(getApplicationContext(), currUserName +
                                        getString(R.string.wasRemoved), Toast.LENGTH_LONG).show();

                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                }).show();
                break;
            case (R.id.menu_logout_item):
                AlertDialog.Builder logoutAlertDialog = new AlertDialog.Builder(this);
                logoutAlertDialog.setTitle(R.string.logout).setMessage(R.string.areYouSureYouWantToLogout)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Toast.makeText(getApplicationContext(), R.string.loggedOutSuccessfully,
                                        Toast.LENGTH_SHORT).show();

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
    //  when data is sent back from intents
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        //case of add new item intent returns data
        if (reqCode == ADD_ITEM && resCode == RESULT_OK) {
            // adding the data as a new item
            String keyData = data.getStringExtra("key");
            String valueData = data.getStringExtra("value");
            WalletItem newItem = new WalletItem(keyData, valueData);
            //case that the given key is not exists yet
            if (!dbhelper.checkItemExistence(currUserName, keyData)){
                itemsArr.add(newItem);
                dbhelper.addData(currUserName, keyData, valueData);
            }else{
                Toast.makeText(getApplicationContext(), R.string.itemWithTheSameKeyAlreadyExists,
                        Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + getString(R.string.SecuredWallet));
        }
        //case of edit new item intent returns data
        else if (reqCode == EDIT_ITEM && resCode == RESULT_OK) {
            int itemPos = data.getIntExtra("position", -1);
            String oldKey = data.getStringExtra("oldKey");
            String newKey = data.getStringExtra("newKey");
            String newValue = data.getStringExtra("newValue");
            //if new key doesn't exists for the current user or we just changing the value of the key
            //and not the key itself then update
            if (!dbhelper.checkItemExistence(currUserName, newKey) ||oldKey.equals(newKey)) {
                WalletItem newItem = new WalletItem(newKey, newValue);
                itemsArr.set(itemPos, newItem);
                dbhelper.updateItem(currUserName, newItem, oldKey);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), R.string.itemWasEditedSuccessfully,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.couldntEditItemAlreadyExists, Toast.LENGTH_LONG).show();
            }
        }
    }

}
