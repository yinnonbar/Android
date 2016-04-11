package il.ac.huj.todolist;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.GregorianCalendar;

/**
 * Created by Yinnon Bratspiess on 12/03/2016.
 */
public class AddNewTodoItemActivity extends Activity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        final Intent intent = new Intent();
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Button okBtn = (Button) findViewById(R.id.btnOK);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById((R.id.edtNewItem))).getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No Task Was Entered", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("task", ((EditText) findViewById((R.id.edtNewItem))).getText().toString());
                    GregorianCalendar taskDate = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(), datePicker.getDayOfMonth());
                    String sDate = DateFormat.getDateInstance(DateFormat.SHORT).format(taskDate.getTime());
                    intent.putExtra("date", sDate);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.btnCancel);
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
