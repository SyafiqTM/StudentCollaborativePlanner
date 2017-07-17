package com.example.syafiq.smartplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syafiq.smartplanner.database.UserDbHelper;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;


public class EditSubject extends AppCompatActivity{
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    EditText edit_sub_name, edit_sub_code, edit_sub_group, edit_sub_class,edit_lec_name, edit_lect_contact;
    Context context = this;

    SQLiteDatabase sqLiteDatabase;
    UserDbHelper userDbHelper;
    Cursor cursor;

    TextView extend_edit_tvSubjectName,extend_edit_tvSubjectCode,extend_edit_tvSubjectGroup,extend_edit_tvSubjectClass,extend_edit_tvLecturerName,extend_edit_tvLecturerContact;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extend_edit_subject);

        edit_sub_name = (EditText)findViewById(R.id.extend_edit_etSubjectName);
        edit_sub_code = (EditText)findViewById(R.id.extend_edit_etSubjectCode);
        edit_sub_group = (EditText)findViewById(R.id.extend_edit_etSubjectGroup);
        edit_sub_class = (EditText)findViewById(R.id.extend_edit_etSubjectClass);
        edit_lec_name = (EditText) findViewById(R.id.extend_edit_etLecturerName);
        edit_lect_contact = (EditText) findViewById(R.id.extend_edit_etLecturerContact);

        Bundle newBundle = getIntent().getExtras();
        String subject_name = newBundle.getString("extendSubjectName");
        String subject_code = newBundle.getString("extendSubjectCode");

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
                    edit_sub_name.setText(subname);
                    edit_sub_code.setText(subcode);
                    edit_sub_group.setText(subgroup);
                    edit_sub_class.setText(classroom);
                    edit_lec_name.setText(lec_name);
                    edit_lect_contact.setText(lec_contact);
                }

            }while (cursor.moveToNext());
        }

        edit_sub_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_tvSubjectName = (TextView)findViewById(R.id.extend_edit_tvSubjectName);
                    extend_edit_tvSubjectName.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_tvSubjectName = (TextView)findViewById(R.id.extend_edit_tvSubjectName);
                    extend_edit_tvSubjectName.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        edit_sub_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_tvSubjectCode = (TextView)findViewById(R.id.extend_edit_tvSubjectCode);
                    extend_edit_tvSubjectCode.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_tvSubjectCode = (TextView)findViewById(R.id.extend_edit_tvSubjectCode);
                    extend_edit_tvSubjectCode.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        edit_sub_group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_tvSubjectGroup = (TextView)findViewById(R.id.extend_edit_tvSubjectGroup);
                    extend_edit_tvSubjectGroup.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_tvSubjectGroup = (TextView)findViewById(R.id.extend_edit_tvSubjectGroup);
                    extend_edit_tvSubjectGroup.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        edit_sub_class.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_tvSubjectClass = (TextView)findViewById(R.id.extend_edit_tvSubjectClass);
                    extend_edit_tvSubjectClass.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_tvSubjectClass = (TextView)findViewById(R.id.extend_edit_tvSubjectClass);
                    extend_edit_tvSubjectClass.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        edit_lec_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_tvLecturerName = (TextView)findViewById(R.id.extend_edit_tvLecturerName);
                    extend_edit_tvLecturerName.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_tvLecturerName = (TextView)findViewById(R.id.extend_edit_tvLecturerName);
                    extend_edit_tvLecturerName.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });



        edit_lect_contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_tvLecturerContact = (TextView)findViewById(R.id.extend_edit_tvLecturerContact);
                    extend_edit_tvLecturerContact.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_tvLecturerContact = (TextView)findViewById(R.id.extend_edit_tvLecturerContact);
                    extend_edit_tvLecturerContact.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });
    }



    //<------------------ get options save ---------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_create_subject,menu);
        return true;
    }

    //<------------------ action save ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_save:

                Bundle newBundle = getIntent().getExtras();
                String subject_name = newBundle.getString("extendSubjectName");
                String subject_code = newBundle.getString("extendSubjectCode");

                String edit_save_subject_name = edit_sub_name.getText().toString();
                String edit_save_subject_code = edit_sub_code.getText().toString();
                String edit_save_subject_group = edit_sub_group.getText().toString();
                String edit_save_subject_class = edit_sub_class.getText().toString();
                String edit_save_lec_name = edit_lec_name.getText().toString();
                String edit_save_lec_contact = edit_lect_contact.getText().toString();

                if(edit_save_lec_name.isEmpty())
                {
                    edit_save_lec_name = "no detail";
                }

                if(edit_save_lec_contact.isEmpty())
                {
                    edit_save_lec_contact = "no detail";
                }


                userDbHelper = new UserDbHelper(context);
                sqLiteDatabase = userDbHelper.getWritableDatabase();

                //<------------------ Get user ID from shared preferences ---------------------------------->
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                final String user_email = prefs.getString("userID",null);

                //<------------------ Insert Data into Sqlite Database ---------------------------------->
                userDbHelper.updateSubjectInformation(subject_name,user_email,edit_save_subject_name,edit_save_subject_code,edit_save_subject_group,edit_save_subject_class,edit_save_lec_name,edit_save_lec_contact,sqLiteDatabase);

                CharSequence text = "Subject Altered";
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();

                userDbHelper.close();


                Intent intent = new Intent(this,ViewSubjects.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
