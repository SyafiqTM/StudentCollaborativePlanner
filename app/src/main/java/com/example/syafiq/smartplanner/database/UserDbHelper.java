package com.example.syafiq.smartplanner.database;
//Created by syafiq on 4/12/2016.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SUBJECTINFOS.DB";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_QUERY =
            "CREATE TABLE "+ UserContract.NewSubjectInfo.TABLE_NAME+"("+ UserContract.NewSubjectInfo.USER_EMAIL+" TEXT,"+
                    UserContract.NewSubjectInfo.SUBJECT_NAME+" TEXT,"+
                    UserContract.NewSubjectInfo.SUBJECT_CODE+" TEXT,"+
                    UserContract.NewSubjectInfo.SUBJECT_GROUP+" TEXT,"+
                    UserContract.NewSubjectInfo.SUBJECT_CLASSROOM+" TEXT,"+
                    UserContract.NewSubjectInfo.LEC_NAME+" TEXT,"+
                    UserContract.NewSubjectInfo.LEC_CONTACT+" TEXT);";

    public UserDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATION", "Database subject created / opened...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATION","Table Created...");
    }

    public void addSubjectInformation(String email, String name,String code,String group, String classroom,String lec_name, String lec_contact,SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.NewSubjectInfo.USER_EMAIL,email);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_NAME,name);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_CODE,code);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_GROUP,group);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_CLASSROOM,classroom);
        contentValues.put(UserContract.NewSubjectInfo.LEC_NAME,lec_name);
        contentValues.put(UserContract.NewSubjectInfo.LEC_CONTACT,lec_contact);
        db.insert(UserContract.NewSubjectInfo.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS","One Row Inserted...");
    }

    public void deleteSubjectInformation(String subjectName, SQLiteDatabase db){
        String selection = UserContract.NewSubjectInfo.SUBJECT_NAME + " LIKE ?";
        String[] selection_args = {subjectName};
        db.delete(UserContract.NewSubjectInfo.TABLE_NAME,selection,selection_args);

    }

    public int updateSubjectInformation(String oldsubject, String new_email, String new_name, String new_code,String new_group, String new_class,String new_lec_name, String new_lec_contact, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.NewSubjectInfo.USER_EMAIL,new_email);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_NAME,new_name);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_CODE,new_code);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_GROUP,new_group);
        contentValues.put(UserContract.NewSubjectInfo.SUBJECT_CLASSROOM,new_class);
        contentValues.put(UserContract.NewSubjectInfo.LEC_NAME,new_lec_name);
        contentValues.put(UserContract.NewSubjectInfo.LEC_CONTACT,new_lec_contact);
        String selection = UserContract.NewSubjectInfo.SUBJECT_NAME + " LIKE ?";
        String[] selection_args = {oldsubject};
        int count = db.update(UserContract.NewSubjectInfo.TABLE_NAME,contentValues,selection,selection_args);
        return  count;
    }

    public Cursor getInformation(SQLiteDatabase db)
    {
        Cursor cursor;
        String [] projections = {UserContract.NewSubjectInfo.USER_EMAIL,
                UserContract.NewSubjectInfo.SUBJECT_NAME,
                UserContract.NewSubjectInfo.SUBJECT_CODE,
                UserContract.NewSubjectInfo.SUBJECT_GROUP,
                UserContract.NewSubjectInfo.SUBJECT_CLASSROOM,
                UserContract.NewSubjectInfo.LEC_NAME,
                UserContract.NewSubjectInfo.LEC_CONTACT};
        cursor = db.query(UserContract.NewSubjectInfo.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop, alter table
    }
}
