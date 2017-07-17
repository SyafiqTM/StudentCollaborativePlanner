package com.example.syafiq.smartplanner.database_topic;
//Created by syafiq on 5/12/2016.

import android.database.sqlite.SQLiteOpenHelper;

import com.clough.android.androiddbviewer.ADBVApplication;
import com.example.syafiq.smartplanner.database_task.TaskDbHelper;

public class CustomApplicationTopic extends ADBVApplication {

    @Override
    public SQLiteOpenHelper getDataBase() {
        return new TopicDbHelper(getApplicationContext());
    }

}