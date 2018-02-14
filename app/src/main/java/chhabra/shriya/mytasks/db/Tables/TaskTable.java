package chhabra.shriya.mytasks.db.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import chhabra.shriya.mytasks.Models.Task;
import chhabra.shriya.mytasks.db.Consts;

import static chhabra.shriya.mytasks.db.Consts.*;
/**
 * Created by LENOVO on 1/12/2018.
 */

public class TaskTable {

 private TaskTable() {
 }

 public static final String Table_Name="taskTable";

    public interface Columns {
        String ID="id";
        String TaskName= "name";
        String Address= "address";
        String Radius = "radius";
        String Latitude="latitude";
        String Longitude="longitude";
        String Notification="notification";
        String Done = "done";
    }

    public static final String CMD_CREATE_TABLE =
            CMD_CREATE_TABLE_INE +  Table_Name
                    + LBR
                    + Columns.ID + TYPE_INT + TYPE_PK_AI + COMMA
                    + Columns.TaskName + TYPE_TEXT + COMMA
                    + Columns.Address + TYPE_TEXT + COMMA
                    + Columns.Radius + TYPE_INT + COMMA
                    + Columns.Latitude + TYPE_DOUBLE + COMMA
                    + Columns.Longitude + TYPE_DOUBLE + COMMA
                    + Columns.Notification + TYPE_BOOLEAN + COMMA
                    + Columns.Done + TYPE_BOOLEAN
                    + RBR ;

    public static long insertTask(Task t, SQLiteDatabase db) {
        ContentValues newTask= new ContentValues();
        newTask.put(Columns.TaskName,t.getTaskName());
        newTask.put(Columns.Address,t.getPlaceAddress());
        newTask.put(Columns.Radius,t.getRadius());
        newTask.put(Columns.Latitude,t.getLatitude());
        newTask.put(Columns.Longitude,t.getLongitude());
        newTask.put(Columns.Notification,t.getNotification());
        newTask.put(Columns.Done,t.isDone());
        return db.insert(Table_Name,null,newTask);
    }

    public static Task getTask(SQLiteDatabase db,String name){
        Task task=new Task();
        Cursor cur=db.query(
                Table_Name,
                new String[]{Columns.TaskName,Columns.Address,Columns.Radius,Columns.Latitude,Columns.Longitude,Columns.Notification,Columns.Done},
                Columns.TaskName +" = \""+name+"\"",
                null,null,null,null
        );
        cur.moveToFirst();
        task.setTaskName(name);
        task.setDone(cur.getInt(cur.getColumnIndex(Columns.Done))>0);
        task.setLatitude(cur.getLong(cur.getColumnIndex(Columns.Latitude)));
        task.setLongitude(cur.getLong(cur.getColumnIndex(Columns.Longitude)));
        task.setNotification(cur.getInt(cur.getColumnIndex(Columns.Notification))>0);
        task.setPlaceAddress(cur.getString(cur.getColumnIndex(Columns.Address)));
        task.setRadius(cur.getInt(cur.getColumnIndex(Columns.Radius)));
        cur.close();
        return task;
    }

    public static void flipDone(SQLiteDatabase db,String name,Boolean type){
        Log.d("TAG",name+" "+type);
        ContentValues values = new ContentValues();
        values.put(Columns.Done,type);
        db.update(
                Table_Name,
                values,
                Columns.TaskName +" = \""+name+"\"",
                null
        );
    }

    public static ArrayList<Task> getAllTasks(SQLiteDatabase db)
    {
        Cursor c=db.query(
                Table_Name,
                new String[]{Columns.TaskName,Columns.Address,Columns.Radius,
                        Columns.Latitude,Columns.Longitude,Columns.Notification,Columns.Done},
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<Task> allTasks= new ArrayList<>();
        c.moveToFirst();

        int idIndex=c.getColumnIndex(Columns.ID);
        int TaskNameIndex=c.getColumnIndex(Columns.TaskName);
        int AddressIndex=c.getColumnIndex(Columns.Address);
        int RadiusIndex=c.getColumnIndex(Columns.Radius);
        int LatitudeIndex=c.getColumnIndex(Columns.Latitude);
        int LongitudeIndex=c.getColumnIndex(Columns.Longitude);
        int NotiIndex=c.getColumnIndex(Columns.Notification);
        int DoneIndex = c.getColumnIndex(Columns.Done);

        while (!c.isAfterLast()) {
            allTasks.add(new Task(
                    c.getString(TaskNameIndex),
                    c.getString(AddressIndex),
                    c.getInt(RadiusIndex),
                    c.getDouble(LatitudeIndex),
                    c.getDouble(LongitudeIndex),
                    c.getInt(NotiIndex)==1,
                    c.getInt(DoneIndex) > 0
            ));
            c.moveToNext();
        }
        c.close();
        return allTasks;
    }
}