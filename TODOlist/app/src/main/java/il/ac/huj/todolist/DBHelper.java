package il.ac.huj.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Yinnon Bratspiess on 06/04/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private int dbCounter = 1;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database info
    private static final String DATABASE_NAME = "todo_db";
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TASK= "_task";
    private static final String COLUMN_DATE = "_date";
    // SQL statement to create the db
    private static final String CRAETE_DB = " CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK + " TEXT NOT NULL, " + COLUMN_DATE +
            " TEXT NOT NULL)";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRAETE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME) ;

        this.onCreate(db);

    }

    //----------------------------------------------------------------------------
    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_TASK, COLUMN_DATE};

    public void addTask(TaskObj taskObj)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        taskObj.setId(dbCounter);
        dbCounter++;

        ContentValues values = new ContentValues();

        values.put(COLUMN_TASK, taskObj.getTask());
        values.put(COLUMN_DATE, taskObj.getDate());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public TaskObj getTask(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();


        TaskObj t = new TaskObj();
        t.setId(Integer.parseInt(cursor.getString(0)));
        t.setTask(cursor.getString(1));
        t.setDate(cursor.getString(2));

        db.close();

        return t;
    }

    public ArrayList<TaskObj> getAllTasks() {
        ArrayList<TaskObj> tasks = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        TaskObj taskObj = null;
        if (cursor.moveToFirst()) {
            do {
                taskObj = new TaskObj();
                taskObj.setId(Integer.parseInt(cursor.getString(0)));
                taskObj.setTask(cursor.getString(1));
                taskObj.setDate(cursor.getString(2));

                tasks.add(taskObj);
            } while (cursor.moveToNext());
        }

        return tasks;
    }

    public void deleteTask(TaskObj taskObj) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(taskObj.getId())});
        db.close();
    }
}
