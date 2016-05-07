package com.yinnonbar.securedwallet;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Yinnon Bratspiess on 07/04/2016.
 */
public class WalletActivity extends AppCompatActivity {
    private static final int ADD_ITEM = 2;
    private static final int EDIT_ITEM = 3;

    DBHelper dbhelper;
    ArrayList<WalletItem> itemsArr;
    ArrayList<WalletItem> searchResults;
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
        final EditText search = (EditText) findViewById(R.id.searchBox);
        ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + getString(R.string.SecuredWallet));
        itemsArr = dbhelper.getAllItems(currUserName);
        searchResults = new ArrayList<WalletItem>(itemsArr);
        adapter = new MyAdapter(this, 0, searchResults);
        list.setAdapter(adapter);
        if (itemsArr.isEmpty()) {
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName +
                    getString(R.string.SecuredWallet) + "\n"  +
                    getString(R.string.seemsLikeItsEmpty));
        }else {
            ((TextView) findViewById(R.id.walletTitle)).setText(currUserName + getString(R.string.SecuredWallet));
        }

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = search.getText().toString();
                int textLength = searchString.length();
                searchResults.clear();
                for(int i = 0; i < itemsArr.size(); i++){
                    String itemName = itemsArr.get(i).getKey();
                    if (textLength <= itemName.length()){
                        if(searchString.equalsIgnoreCase(itemName.substring(0, textLength))){
                            searchResults.add(itemsArr.get(i));
                        }
                    }
                }
                adapter = new MyAdapter(com.yinnonbar.securedwallet.WalletActivity.this, 0, searchResults);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder showItemAlertDialogBuilder = new AlertDialog.Builder(WalletActivity.this);
                //a custom title for the dialog
                TextView customTitle = new TextView(getApplicationContext());
                customTitle.setText(adapter.getItem(position).getKey());
                customTitle.setTextSize(20);
                customTitle.setPadding(10, 10, 10, 10);
                customTitle.setTextColor(Color.BLUE);
                customTitle.setGravity(Gravity.CENTER);

                showItemAlertDialogBuilder.setCustomTitle(customTitle)
                        .setMessage(adapter.getItem(position).getValue())
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton(R.string.copy, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", adapter.getItem(position).getValue());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), R.string.valueWasCopied
                                , Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        //on item long click listener
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder longClickAlertDialogBuilder = new AlertDialog.Builder(WalletActivity.this);
                final String longClickedItemKey = adapter.getItem(position).getKey();
                TextView customTitle = new TextView(getApplicationContext());
                customTitle.setText(adapter.getItem(position).getKey());
                customTitle.setTextSize(20);
                customTitle.setPadding(10, 10, 10, 10);
                customTitle.setTextColor(Color.BLUE);
                customTitle.setGravity(Gravity.CENTER);
                longClickAlertDialogBuilder.setCustomTitle(customTitle).setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
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
                                        itemsArr.remove(adapter.getItem(position));
                                        if (adapter.getCount() != itemsArr.size()) {
                                            adapter.remove(adapter.getItem(position));
                                        }

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
                        editItemIntent.putExtra("oldValue", adapter.getItem(position).getValue());
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
                adapter = new MyAdapter(this, 0, itemsArr);
                list.setAdapter(adapter);
                itemsArr.add(newItem);

                dbhelper.addData(currUserName, keyData, valueData);

                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(getApplicationContext(), R.string.itemWithTheSameKeyAlreadyExists,
                        Toast.LENGTH_SHORT).show();
            }
            //adapter.notifyDataSetChanged();
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
                adapter.getItem(itemPos).setKey(newKey);
                adapter.getItem(itemPos).setValue(newValue);
                dbhelper.updateItem(currUserName, newItem, oldKey);
                adapter = new MyAdapter(this, 0, itemsArr);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), R.string.itemWasEditedSuccessfully,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.couldntEditItemAlreadyExists, Toast.LENGTH_LONG).show();
            }
        }
    }

}
