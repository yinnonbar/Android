package com.yinnonbar.securedwallet;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yinnon Bratspiess on 06/04/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private int dbCounter = 1;
    private static final String LOG = "DatabaseHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database info
    private static final String DATABASE_NAME = "wallet_db";
    private static final String TABLE_NAME_USERS = "users";
    private static final String TABLE_NAME_USERS_DATA = "usersData";
    //common column
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USER = "_user";
    //users table column
    private static final String COLUMN_PASS = "_pass";
    //users data table column
    private static final String COLUMN_DATA_KEY = "_data";
    private static final String COLUMN_DATA_VALUE = "_value";

    // SQL statement to create the tables
    private static final String CREATE_USERS_TABLE = " CREATE TABLE " + TABLE_NAME_USERS + "( " + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER + " TEXT NOT NULL UNIQUE, " + COLUMN_PASS +
            " TEXT NOT NULL)";


    private static final String CREATE_USERS_DATA_TABLE = " CREATE TABLE " + TABLE_NAME_USERS_DATA + "( "
            + COLUMN_USER + " TEXT NOT NULL, " + COLUMN_DATA_KEY + " TEXT NOT NULL, "
            + COLUMN_DATA_VALUE + " TEXT NOT NULL, " +
            " PRIMARY KEY ("+COLUMN_USER + ", " + COLUMN_DATA_KEY +"))";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USERS_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS_DATA);

        this.onCreate(db);

    }

    //----------------------------------------------------------------------------
    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_USER, COLUMN_PASS};

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        user.setId(dbCounter);
        dbCounter++;

        ContentValues values = new ContentValues();

        values.put(COLUMN_USER, user.getUserName());
        values.put(COLUMN_PASS, user.getPassword());

        db.insert(TABLE_NAME_USERS, null, values);
        db.close();
    }


    public User getUser(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USERS, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();


        User u = new User();
        u.setId(Integer.parseInt(cursor.getString(0)));
        u.setUserName(cursor.getString(1));
        u.setPassword(cursor.getString(2));

        db.close();

        return u;
    }

    public Boolean verification(String userName, String password) {
        int count = -1;
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_NAME_USERS + " WHERE " + COLUMN_USER + " = ? AND " + COLUMN_PASS + " = ?";
            c = db.rawQuery(query, new String[]{userName, password});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }

    public Boolean checkExistence(String userName) {
        int count = -1;
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_NAME_USERS + " WHERE " + COLUMN_USER + " = ?";
            c = db.rawQuery(query, new String[]{userName});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }

    public Boolean checkItemExistence(String userName, String key) {
        int count = -1;
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // + TABLE_NAME_USERS + " WHERE " + COLUMN_USER + " = ? AND " + COLUMN_PASS + " = ?";
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_NAME_USERS_DATA + " WHERE " + COLUMN_USER + " = ? AND " + COLUMN_DATA_KEY + " = ?";
            c = db.rawQuery(query, new String[]{userName, key});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }


    public void deleteUser(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME_USERS, COLUMN_USER + " = ?", new String[]{username});
        db.delete(TABLE_NAME_USERS_DATA, COLUMN_USER + " = ?", new String[]{username});
        db.close();
    }


    public ArrayList<WalletItem> getAllItems(String username) {
        ArrayList<WalletItem> items = new ArrayList<WalletItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_USERS_DATA + " WHERE " + COLUMN_USER + " = ?";
        //Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{username});
        Log.e(LOG, selectQuery);
       // Log.e(LOG, "c size is : " + c.toString());
        WalletItem currItem = null;
        if (c.moveToFirst()) {
            do {
                currItem = new WalletItem();
                currItem.setKey(c.getString(c.getColumnIndex(COLUMN_DATA_KEY)));
                currItem.setValue(c.getString(c.getColumnIndex(COLUMN_DATA_VALUE)));
                Log.e(LOG, "in get all items " + currItem.getKey());
                items.add(currItem);
            } while (c.moveToNext());
        }
        return items;
    }


    public void addData(String username, String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(LOG, "in add new data in DB username is : " + username + " key is : " + key);
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER, username);
        values.put(COLUMN_DATA_KEY, key);
        values.put(COLUMN_DATA_VALUE, value);
        Log.e(LOG, "values user is  " + values.get(COLUMN_USER));
        Log.e(LOG, "values key is  " + values.get(COLUMN_DATA_KEY));
        Log.e(LOG, "values value is  " + values.get(COLUMN_DATA_VALUE));
        long res = db.insert(TABLE_NAME_USERS_DATA, null, values);
        Log.e(LOG, "result for insert is : " + res);
        db.close();
        ArrayList<WalletItem> currlist = getAllItems(username);
        Log.e(LOG, "list size is : " + currlist.size());
        for (WalletItem item : currlist) {
            Log.e(LOG, "after adding new item this is the items in table : " + item.getKey());
        }
    }

    public int updateItem(String userName, WalletItem item, String oldKey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER, userName);
        values.put(COLUMN_DATA_KEY, item.getKey());
        values.put(COLUMN_DATA_VALUE, item.getValue());
        //if (checkItemExistence(userName, item.getKey())){
          //  return -1;
        //}
        // updating row
        return db.update(TABLE_NAME_USERS_DATA, values, COLUMN_USER + " = ? AND "
                        + COLUMN_DATA_KEY + " = ?", new String[] {userName, oldKey});
    }


    public void deleteItemByKey(String username, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_USERS_DATA, COLUMN_USER + " = ? AND "
                + COLUMN_DATA_KEY + " = ?", new String[]{username, key});
    }
}