package com.example.syafiq.smartplanner.database_topic;
//Created by syafiq on 22/1/2017.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TopicDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TOPICINFOSSS.DB";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_QUERY =
            "CREATE TABLE "+ TopicContract.NewTopic.TABLE_NAME+"("+
                    TopicContract.NewTopic.USER_NAME+" TEXT,"+
                    TopicContract.NewTopic.TOPIC_TASK+" TEXT,"+
                    TopicContract.NewTopic.TOPIC_TITLE+" TEXT,"+
                    TopicContract.NewTopic.TOPIC_TICK+" TEXT);";

    public TopicDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATION", "Database Topic opened...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATION","Table Topic Created...");
    }


    public void addTopicInformation(String username,String topic_task,String topic_title, String topic_tick,SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(TopicContract.NewTopic.USER_NAME,username);
        contentValues.put(TopicContract.NewTopic.TOPIC_TASK,topic_task);
        contentValues.put(TopicContract.NewTopic.TOPIC_TITLE,topic_title);
        contentValues.put(TopicContract.NewTopic.TOPIC_TICK,topic_tick);
        db.insert(TopicContract.NewTopic.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS","One Row Inserted...");
    }

    public void deleteTopicInformation(String plant_name, SQLiteDatabase db){
        String selection = TopicContract.NewTopic.TOPIC_TITLE + " LIKE ?";
        String[] selection_args = {plant_name};
        db.delete(TopicContract.NewTopic.TABLE_NAME,selection,selection_args);
        Log.e("DATABASE OPERATIONS","One Row Deleted...");

    }

    public int updateTopicInformation(String oldTopictitle,String username,String topic_task,String topic_title, String topic_tick, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TopicContract.NewTopic.USER_NAME,username);
        contentValues.put(TopicContract.NewTopic.TOPIC_TASK,topic_task);
        contentValues.put(TopicContract.NewTopic.TOPIC_TITLE,topic_title);
        contentValues.put(TopicContract.NewTopic.TOPIC_TICK,topic_tick);
        String selection = TopicContract.NewTopic.TOPIC_TITLE + " LIKE ?";
        String[] selection_args = {oldTopictitle};
        int count = db.update(TopicContract.NewTopic.TABLE_NAME,contentValues,selection,selection_args);
        Log.e("DATABASE OPERATION", "One Topic updated...");
        return  count;

    }


    public Cursor getTopicInformation(SQLiteDatabase db)
    {
        Cursor cursor;
        String [] projections = {
                TopicContract.NewTopic.USER_NAME,
                TopicContract.NewTopic.TOPIC_TASK,
                TopicContract.NewTopic.TOPIC_TITLE,
                TopicContract.NewTopic.TOPIC_TICK};
        cursor = db.query(TopicContract.NewTopic.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop, alter table

    }
}
