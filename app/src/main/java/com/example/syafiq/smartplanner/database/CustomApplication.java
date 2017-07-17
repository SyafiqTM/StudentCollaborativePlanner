package com.example.syafiq.smartplanner.database;
//Created by syafiq on 5/12/2016.

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.clough.android.androiddbviewer.ADBVApplication;
import com.example.syafiq.smartplanner.database.UserDbHelper;

public class CustomApplication extends ADBVApplication {

    @Override
    public SQLiteOpenHelper getDataBase() {
        return new UserDbHelper(getApplicationContext());
    }

}