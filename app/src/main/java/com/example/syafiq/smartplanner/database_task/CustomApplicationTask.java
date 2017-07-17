package com.example.syafiq.smartplanner.database_task;
//Created by syafiq on 5/12/2016.

import android.database.sqlite.SQLiteOpenHelper;

import com.clough.android.androiddbviewer.ADBVApplication;
import com.example.syafiq.smartplanner.database.UserDbHelper;

public class CustomApplicationTask extends ADBVApplication {

    @Override
    public SQLiteOpenHelper getDataBase() {
        return new TaskDbHelper(getApplicationContext());
    }

}