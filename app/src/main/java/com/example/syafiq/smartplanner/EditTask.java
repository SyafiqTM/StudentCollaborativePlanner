package com.example.syafiq.smartplanner;//Created by syafiq on 4/1/2017.

import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.syafiq.smartplanner.database.UserDbHelper;
import com.example.syafiq.smartplanner.database_task.TaskDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;


public class EditTask extends AppCompatActivity{

    EditText edit_text_subject, edit_text_type, edit_text_title, edit_text_description, edit_text_date, edit_text_time;
    Context context = this;

    SQLiteDatabase sqLiteDatabase;
    TaskDbHelper taskDbHelper;
    Cursor cursor;

    Date time_start;

    TextView extend_edit_task_tvTaskTitle,extend_edit_task_tvTaskDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extend_edit_task);


        edit_text_subject = (EditText)findViewById(R.id.extend_edit_task_etChoose);
        edit_text_type = (EditText)findViewById(R.id.extend_edit_task_etType);
        edit_text_title = (EditText)findViewById(R.id.extend_edit_task_etTaskTitle);
        edit_text_description = (EditText)findViewById(R.id.extend_edit_task_etTaskDesc);
        edit_text_date = (EditText) findViewById(R.id.extend_edit_task_etDate);
        edit_text_time = (EditText) findViewById(R.id.extend_edit_task_etTime);

        Bundle newBundle = getIntent().getExtras();
        String task_title = newBundle.getString("extendTaskTitle");
        String task_subject = newBundle.getString("extendTaskSubject");
        String task_type = newBundle.getString("extendTaskType");

        taskDbHelper = new TaskDbHelper(getApplicationContext());
        sqLiteDatabase = taskDbHelper.getReadableDatabase();
        cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);

        if(cursor.moveToFirst())
        {
            do {
                String email;
                String TaskSubject ;
                String TaskType;
                String TaskTitle;
                String TaskDesc;
                String TaskDate;
                String TaskTime;

                email = cursor.getString(0);
                TaskSubject= cursor.getString(1);
                TaskType = cursor.getString(2);
                TaskTitle = cursor.getString(3);
                TaskDesc = cursor.getString(4);
                TaskDate = cursor.getString(5);
                TaskTime = cursor.getString(6);

                if(task_title.equalsIgnoreCase(TaskTitle))
                {
                    edit_text_title.setText(TaskTitle);
                    edit_text_subject.setText(TaskSubject);
                    edit_text_date.setText(TaskDate);
                    edit_text_time.setText(TaskTime);
                    edit_text_description.setText(TaskDesc);
                    edit_text_type.setText(TaskType);
                    getSupportActionBar().setTitle(TaskType); //change App title into type of task
                }

            }while (cursor.moveToNext());
        }

 //<---------------------------------------------- Alteration Begins ---------------------------------------------------------->

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        UserDbHelper userDbHelper = new UserDbHelper(getApplicationContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = userDbHelper.getInformation(sqLiteDatabase);
        ArrayList<String> obj = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                String email,subname,subcode,classroom;
                email = cursor.getString(0);
                subname = cursor.getString(1);
                subcode = cursor.getString(2);
                classroom = cursor.getString(3);

                if(email.equalsIgnoreCase(userID))
                {
                    obj.add(subname);
                }

            }while (cursor.moveToNext());
        }

        //<------------------ Edit Text Choose Subject ---------------------------------->
        final CharSequence[] colors;

        if(obj.size()==0)
        {
            colors = new CharSequence[]{"-"};
        }

        else{
            colors = obj.toArray(new CharSequence[obj.size()]);
        }


        edit_text_subject = (EditText) findViewById(R.id.extend_edit_task_etChoose);
        edit_text_subject.setText(colors[0]); //set text first then let user pick

        edit_text_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setTitle("Pick Subject");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        edit_text_subject.setText(colors[which]);
                    }
                });
                builder.show();
            }
        });


        //<------------------ Edit Text Type of Task ---------------------------------->
        final CharSequence[] task = new CharSequence[] {"Assignment", "Revision", "Exam"};


        edit_text_type = (EditText) findViewById(R.id.extend_edit_task_etType);
        edit_text_type.setText(task[0]); //set text first then let user pick

        edit_text_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
                builder.setTitle("Choose Task");
                builder.setItems(task, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        edit_text_type.setText(task[which]);
                    }
                });
                builder.show();
            }
        });


        edit_text_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    extend_edit_task_tvTaskTitle = (TextView)findViewById(R.id.extend_edit_task_tvTaskTitle);
                    extend_edit_task_tvTaskTitle.setTextColor(getResources().getColor(R.color.darker_blue));
                    }
                else {
                    extend_edit_task_tvTaskTitle = (TextView)findViewById(R.id.extend_edit_task_tvTaskTitle);
                    extend_edit_task_tvTaskTitle.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });

        edit_text_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    extend_edit_task_tvTaskDesc = (TextView)findViewById(R.id.extend_edit_task_tvTaskDesc);
                    extend_edit_task_tvTaskDesc.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    extend_edit_task_tvTaskDesc = (TextView)findViewById(R.id.extend_edit_task_tvTaskDesc);
                    extend_edit_task_tvTaskDesc.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        //<------------------ Edit Text Type of Task ---------------------------------->
        edit_text_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");

            }
        });


        //<------------------ Edit Text Time Task ---------------------------------->
        edit_text_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour + ":" + selectedMinute;
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                        time_start = null;
                        try {
                            time_start = sdf.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat fmt = new SimpleDateFormat("h:mm aa");
                        String formatted = fmt.format(time_start);
                        edit_text_time.setText(formatted);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }


    //<------------------Input Validation ---------------------------------->
    public boolean validate() {
        boolean valid = true;

        String title = edit_text_title.getText().toString();
        String desc = edit_text_description.getText().toString();

        if (title.isEmpty()) {
            edit_text_title.setError("Task title cannot be empty");
            valid = false;
        }
        else {
            edit_text_description.setError(null);
        }


        if (desc.isEmpty()) {
            edit_text_description.setError("Task description cannot be empty");
            valid = false;
        }
        else {
            edit_text_description.setError(null);
        }

        return valid;
    }



    //<------------------ get options save ---------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_create_task,menu);
        return true;
    }

    //<------------------ action save ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_save:

                if (!validate()) {
                    return true;
                }

                Bundle newBundle = getIntent().getExtras();
                String task_title = newBundle.getString("extendTaskTitle");
                String task_subject = newBundle.getString("extendTaskSubject");
                String task_type = newBundle.getString("extendTaskType");

                String TaskSubject = edit_text_subject.getText().toString();
                String TaskType = edit_text_type.getText().toString();
                String TaskTitle = edit_text_title.getText().toString();
                String TaskDesc = edit_text_description.getText().toString();
                String TaskDate = edit_text_date.getText().toString();
                String TaskTime = edit_text_time.getText().toString();


                taskDbHelper = new TaskDbHelper(context);
                sqLiteDatabase = taskDbHelper.getWritableDatabase();

                //<------------------ Get user ID from shared preferences ---------------------------------->
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                final String user_email = prefs.getString("userID",null);

                //<------------------ Insert Data into Sqlite Database ---------------------------------->
                taskDbHelper.updateTaskInformation(task_title,user_email,TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,sqLiteDatabase);

                CharSequence text = "Task Altered";
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();

                taskDbHelper.close();


                Intent intent = new Intent(this,ViewTask.class);
                startActivity(intent);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
