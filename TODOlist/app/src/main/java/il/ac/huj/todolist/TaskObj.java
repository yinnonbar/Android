package il.ac.huj.todolist;

/**
 * Created by Yinnon Bratspiess on 06/04/2016.
 */
public class TaskObj {


    private String task;
    private String date;
    private int id;
    public TaskObj() {
    }
    public TaskObj(String task, String date) {
        this.task = task;
        this.date = date;
    }
    public String getTask() {
        return task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTask(String task) {
        this.task = task;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String toString() {
        return "task [task=" + this.task + ", date=" + this.date +", task NO.=" + this.id+ "]";
    }

}
