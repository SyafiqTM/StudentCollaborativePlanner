package com.example.syafiq.smartplanner.database_task;
//Created by syafiq on 29/12/2016.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TASKINFORMATION.DB";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_QUERY =
            "CREATE TABLE "+ TaskContract.NewTaskInfo.TABLE_NAME+"("+
                    TaskContract.NewTaskInfo.USER_EMAIL+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_SUBJECT+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_TYPE+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_TITLE+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_DESC+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_DATE+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_TIME+" TEXT,"+
                    TaskContract.NewTaskInfo.TASK_PERCENTAGE+" TEXT);";

    public TaskDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATION", "Database task opened...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATION","Table Created...");
    }

    public void addTaskInformation(String email, String task_subject, String task_type, String task_title, String task_desc, String task_date, String task_time,String task_percentage, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(TaskContract.NewTaskInfo.USER_EMAIL,email);
        contentValues.put(TaskContract.NewTaskInfo.TASK_SUBJECT,task_subject);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TYPE,task_type);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TITLE,task_title);
        contentValues.put(TaskContract.NewTaskInfo.TASK_DESC,task_desc);
        contentValues.put(TaskContract.NewTaskInfo.TASK_DATE,task_date);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TIME,task_time);
        contentValues.put(TaskContract.NewTaskInfo.TASK_PERCENTAGE,task_percentage);

        db.insert(TaskContract.NewTaskInfo.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS","One Row Inserted...");
    }

    public void deleteTaskInformation(String taskTitle,SQLiteDatabase db){
        String selection = TaskContract.NewTaskInfo.TASK_TITLE + " LIKE ?";
        String[] selection_args = {taskTitle};
        db.delete(TaskContract.NewTaskInfo.TABLE_NAME,selection,selection_args);

    }

    public int updateTaskInformation(String oldTitle, String new_email, String new_task_subject, String new_task_type, String new_task_title, String new_task_desc, String new_task_date, String new_task_time, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.NewTaskInfo.USER_EMAIL,new_email);
        contentValues.put(TaskContract.NewTaskInfo.TASK_SUBJECT,new_task_subject);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TYPE,new_task_type);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TITLE,new_task_title);
        contentValues.put(TaskContract.NewTaskInfo.TASK_DESC,new_task_desc);
        contentValues.put(TaskContract.NewTaskInfo.TASK_DATE,new_task_date);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TIME,new_task_time);
        String selection = TaskContract.NewTaskInfo.TASK_TITLE + " LIKE ?";
        String[] selection_args = {oldTitle};
        int count = db.update(TaskContract.NewTaskInfo.TABLE_NAME,contentValues,selection,selection_args);
        return  count;
    }

    public int updatePercentage(String oldTitle, String new_email, String new_task_subject, String new_task_type, String new_task_title, String new_task_desc, String new_task_date, String new_task_time,String new_task_percentage, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.NewTaskInfo.USER_EMAIL,new_email);
        contentValues.put(TaskContract.NewTaskInfo.TASK_SUBJECT,new_task_subject);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TYPE,new_task_type);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TITLE,new_task_title);
        contentValues.put(TaskContract.NewTaskInfo.TASK_DESC,new_task_desc);
        contentValues.put(TaskContract.NewTaskInfo.TASK_DATE,new_task_date);
        contentValues.put(TaskContract.NewTaskInfo.TASK_TIME,new_task_time);
        contentValues.put(TaskContract.NewTaskInfo.TASK_PERCENTAGE,new_task_percentage);
        String selection = TaskContract.NewTaskInfo.TASK_TITLE + " LIKE ?";
        String[] selection_args = {oldTitle};
        int count = db.update(TaskContract.NewTaskInfo.TABLE_NAME,contentValues,selection,selection_args);
        return  count;
    }

    public Cursor getTaskInformation(SQLiteDatabase db)
    {
        Cursor cursor;
        String [] projections = {TaskContract.NewTaskInfo.USER_EMAIL,
                TaskContract.NewTaskInfo.TASK_SUBJECT,
                TaskContract.NewTaskInfo.TASK_TYPE,
                TaskContract.NewTaskInfo.TASK_TITLE,
                TaskContract.NewTaskInfo.TASK_DESC,
                TaskContract.NewTaskInfo.TASK_DATE,
                TaskContract.NewTaskInfo.TASK_TIME,
                TaskContract.NewTaskInfo.TASK_PERCENTAGE};
        cursor = db.query(TaskContract.NewTaskInfo.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop, alter table
    }
}
