package il.ac.huj.todolist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Created by Yinnon Bratspiess on 09/03/2016.
 */
public class MyAdapter extends ArrayAdapter<TaskObj> {

    private static final String TAG = "tag";
    private static final String MIL = "20";
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int YEAR = 3;
    private static final String DATE_REGEX = "(\\d*)\\/(\\d*)\\/(\\d*)";

    private Activity activity;
    private static LayoutInflater inflater = null;
    private ArrayList<TaskObj> tasks = null;

    public static class ViewHolder {
        public TextView task;
        public TextView taskDate;
    }
    public MyAdapter(Context context, int resource, ArrayList<TaskObj> missionsArr) {
        super(context, resource, missionsArr);
        try {
            this.activity = (Activity) context;
            this.tasks = missionsArr;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                view = inflater.inflate(R.layout.row, null);
                holder = new ViewHolder();
                holder.task = (TextView) view.findViewById(R.id.taskTitle);
                holder.taskDate = (TextView) view.findViewById(R.id.taskDate);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.task.setText(tasks.get(position).getTask());
            String date = tasks.get(position).getDate();
            holder.taskDate.setText(date);
            String pattern = DATE_REGEX;
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(date);
            //catches the dates and parsing
            if (m.find()) {
                int monthInt = Integer.parseInt(m.group(MONTH));
                int dayInt = Integer.parseInt(m.group(DAY));
                int yearInt = Integer.parseInt(MIL + m.group(YEAR));
                //creating calender with current time
                Date currDate = Calendar.getInstance().getTime();
                // creating calender with the parsed dates
                Calendar taskDate = Calendar.getInstance();
                taskDate.set(yearInt, monthInt - 1, dayInt);
                // if the task time already passed then draw in red
                if (taskDate.getTimeInMillis() < currDate.getTime()) {
                    view.setBackgroundColor(Color.rgb(255, 78, 78));
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
        catch (Exception e) {
        }
        return view;

    }
}
