package il.ac.huj.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodoListManagerActivity extends AppCompatActivity {
    private static final String TAG = "tag";
    private static final String PHONE_REGEX = "(\\+\\d{2,3})*(\\d{2,3})*(\\-)*(\\d{7}){1}";
    ArrayList<TaskObj> tasksArr;
    ListView list;
    MyAdapter adapter;
    DBHelper dbhelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list_manager);
        initList();
    }

    private void initList() {
        dbhelper = new DBHelper(getBaseContext());
        list = (ListView) findViewById(R.id.lstTodoItems);
        tasksArr = dbhelper.getAllTasks();
        adapter = new MyAdapter(this, 0, tasksArr);
        list.setAdapter(adapter);


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TodoListManagerActivity.this);
                TaskObj currLine = tasksArr.get(position);
                String currTask = currLine.getTask();
                String pattern = PHONE_REGEX;
                final Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(currTask);
                final int pos = position;
                // if the current task contains the word call and a legal phone number
                if ((currTask.contains("call") || currTask.contains("Call") ||
                        currTask.contains("CALL")) && m.find()) {
                    final String phoneNumber = m.group(0);
                    alertDialogBuilder.setPositiveButton("Call " + phoneNumber,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // on pressing button call
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                                    startActivity(callIntent);
                                }
                            }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbhelper.deleteTask(tasksArr.get((position)));
                            tasksArr.remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialogBuilder.show();
                    // case that the task is not a call task
                } else {
                    alertDialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tasksArr.remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialogBuilder.show();
                }
                return false;
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //on clicking add send to add new item activity
                Intent addNewItemIntent = new Intent(this, AddNewTodoItemActivity.class);
                startActivityForResult(addNewItemIntent, 42);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    //  when data is sent back from add item intent
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == 42 && resCode == RESULT_OK) {
            // adding the data as a task
            String task = data.getStringExtra("task");
            String date = data.getStringExtra("date");
            TaskObj resTask = new TaskObj(task,date);
            tasksArr.add(resTask);
            dbhelper.addTask(resTask);
            adapter.notifyDataSetChanged();
        }
    }
}