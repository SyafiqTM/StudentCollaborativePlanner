package com.example.syafiq.smartplanner.database_subTopic;
//Created by syafiq on 22/1/2017.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SubtopicDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SUBTOPICINFO.DB";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_QUERY =
            "CREATE TABLE "+ SubtopicContract.NewSubtopic.TABLE_NAME+"("+
                    SubtopicContract.NewSubtopic.SUBTOPIC_TASK+" TEXT,"+
                    SubtopicContract.NewSubtopic.SUBTOPIC_TITLE+" TEXT,"+
                    SubtopicContract.NewSubtopic.SUBTOPIC_TOPIC+" TEXT);";

    public SubtopicDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATION", "Database Subtopic opened...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATION","Table Subtopic Created...");
    }

    public void addsubTopicInformation(String topic_task,String topic_title, String topic_tick,SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();

        contentValues.put(SubtopicContract.NewSubtopic.SUBTOPIC_TASK,topic_task);
        contentValues.put(SubtopicContract.NewSubtopic.SUBTOPIC_TITLE,topic_title);
        contentValues.put(SubtopicContract.NewSubtopic.SUBTOPIC_TOPIC,topic_tick);
        db.insert(SubtopicContract.NewSubtopic.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS","One Row Inserted...");
    }


    public Cursor getSubTopicInformation(SQLiteDatabase db)
    {
        Cursor cursor;
        String [] projections = {
                SubtopicContract.NewSubtopic.SUBTOPIC_TASK,
                SubtopicContract.NewSubtopic.SUBTOPIC_TITLE,
                SubtopicContract.NewSubtopic.SUBTOPIC_TOPIC};
        cursor = db.query(SubtopicContract.NewSubtopic.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop, alter table

    }
}
