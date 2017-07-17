package com.example.syafiq.smartplanner;//Created by syafiq on 4/1/2017.

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syafiq.smartplanner.database.DataProvider;
import com.example.syafiq.smartplanner.database.UserDbHelper;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class ExtendViewSubject extends AppCompatActivity {
    private ActionBarDrawerToggle mToggle;
    SQLiteDatabase sqLiteDatabase;
    UserDbHelper userDbHelper;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_subject);

        //<------------------ Add Drawer Layout ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chrome_reader_mode_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView topLabel = (TextView)findViewById(R.id.Toplabel) ;

        TextView extend_code = (TextView)findViewById(R.id.extend_subjectCode);
        TextView extend_group = (TextView)findViewById(R.id.extend_subjectGroup);
        TextView extend_class = (TextView)findViewById(R.id.extend_subjectClass);
        TextView extend_lec_name = (TextView)findViewById(R.id.extend_lec_name);
        TextView extend_lec_contact = (TextView)findViewById(R.id.extend_lec_contact);


        Bundle getAll = getIntent().getExtras();
        String subject_name = getAll.getString("subject_name");
        String subject_code = getAll.getString("subject_code");
        topLabel.setText(subject_name);



        userDbHelper = new UserDbHelper(getApplicationContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = userDbHelper.getInformation(sqLiteDatabase);

        if(cursor.moveToFirst())
        {
            do {
                String email,subname,subcode,subgroup,classroom,lec_name,lec_contact;
                email = cursor.getString(0);
                subname = cursor.getString(1);
                subcode = cursor.getString(2);
                subgroup = cursor.getString(3);
                classroom = cursor.getString(4);
                lec_name = cursor.getString(5);
                lec_contact = cursor.getString(6);

                if(subject_name.equalsIgnoreCase(subname))
                {
                    extend_code.setText(subcode);
                    extend_group.setText(subgroup);
                    extend_class.setText(classroom);
                    extend_lec_name.setText(lec_name);
                    extend_lec_contact.setText(lec_contact);
                }

            }while (cursor.moveToNext());
        }


    }

    //<------------------ get options save ---------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_extend_subject,menu);
        return true;
    }

    //<------------------ action save ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_edit:
                Bundle getAll = getIntent().getExtras();
                String subject_name = getAll.getString("subject_name");
                String subject_code = getAll.getString("subject_code");

                Bundle newBundle = new Bundle();
                newBundle.putString("extendSubjectName", subject_name);
                newBundle.putString("extendSubjectCode", subject_code);

                Intent intent = new Intent(ExtendViewSubject.this,EditSubject.class);
                intent.putExtras(newBundle);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
