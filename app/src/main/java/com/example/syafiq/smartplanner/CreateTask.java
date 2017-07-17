package com.example.syafiq.smartplanner;
//Created by syafiq on 1/12/2016.

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.syafiq.smartplanner.database.UserDbHelper;
import com.example.syafiq.smartplanner.database_task.TaskDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;


public class CreateTask extends AppCompatActivity{
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    SQLiteDatabase sqLiteDatabase;
    UserDbHelper userDbHelper;
    Cursor cursor;
    Calendar cal;
    Date time_start;

    String[] result;
    String resultTypeofTask;

    EditText etChooseSubject,etTaskType,etTaskTitle,etTaskDesc,etDate,TimeStart,etReminder;
    Context context = this;


    int parseHour,parseMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createtask);

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);


        int count=0;
        userDbHelper = new UserDbHelper(getApplicationContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = userDbHelper.getInformation(sqLiteDatabase);
        ArrayList <String> obj = new ArrayList<>();

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


        etChooseSubject = (EditText) findViewById(R.id.etChoose);
        etChooseSubject.setText(colors[0]); //set text first then let user pick

        etChooseSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTask.this);
                builder.setTitle("Pick Subject");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        etChooseSubject.setText(colors[which]);
                    }
                });
                builder.show();
            }
        });

//<------------------ Edit Text Type of Task ---------------------------------->
        final CharSequence[] task = new CharSequence[] {"Assignment", "Revision", "Exam"};

        result = new String[task.length];

        for (int index = 0; index < task.length; index++) {
            result[index] = task[index].toString();
        }
        sortAscending();

        final CharSequence[]task2 = result;
        final TextView tvTaskTitle = (TextView)findViewById(R.id.tvTaskTitle);



        //<------------------ Edit Text Type of Task ---------------------------------->
        final CharSequence[] reminder = new CharSequence[] {"None","10 Minutes Before", "20 Minutes Before", "30 Minutes Before", "1 Hour Before","2 Hour Before"};

        etReminder = (EditText) findViewById(R.id.etReminder);
        etReminder.setText(reminder[0]); //set text first then let user pick

        etReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTask.this);
                builder.setTitle("Reminder");
                builder.setItems(reminder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        etReminder.setText(reminder[which]);
                    }
                });
                builder.show();
            }
        });

        //Toast.makeText(context, "task order : "+ result[1], Toast.LENGTH_SHORT).show();

       /* etTaskType = (EditText) findViewById(R.id.etType);
        etTaskType.setText(task[0]); //set text first then let user pick

        etTaskType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTask.this);
                builder.setTitle("Choose Task");
                builder.setItems(task, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if(task[which]=="Revision")
                        {
                            etTaskTitle.setVisibility(View.GONE);
                            tvTaskTitle.setVisibility(View.GONE);

                        }

                        else
                        {
                            etTaskTitle.setVisibility(View.VISIBLE);
                            tvTaskTitle.setVisibility(View.VISIBLE);
                        }
                        etTaskType.setText(task[which]);
                    }
                });
                builder.show();
            }
        });*/


        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.etType);


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Assignment");
        categories.add("Revision");
        categories.add("Exam");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // On selecting a spinner item
                resultTypeofTask = adapterView.getItemAtPosition(i).toString();
                // Showing selected spinner item
//                Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//<------------------ Edit Text Type of Task ---------------------------------->
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        String current_date = dateFormat.format(date);

        etDate = (EditText) findViewById(R.id.etDate);
        etDate.setText(current_date);

        etDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");

            }
        });


//<------------------ Time Start Picker ---------------------------------->
        TimeStart = (EditText) findViewById(R.id.etTime);
        DateFormat timeFormat = new SimpleDateFormat("h:mm aaa");
        Date time = new Date();
        String current_time = timeFormat.format(time);
        TimeStart.setText(current_time);

        TimeStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour + ":" + selectedMinute;
                        parseHour = selectedHour;
                        parseMinute = selectedMinute;
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                        time_start = null;
                        try {
                            time_start = sdf.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat fmt = new SimpleDateFormat("h:mm aa");
                        String formatted = fmt.format(time_start);
                        TimeStart.setText(formatted);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        etTaskTitle = (EditText) findViewById(R.id.etTaskTitle);

        /*etTaskTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Set the background resource of EditText widget
                    etTaskTitle.setBackgroundResource(R.drawable.edittext_border_normal);

                    // Or you can use this method
                    *//*
                        setBackground (Drawable background)
                            Set the background to a given Drawable, or remove
                            the background. If the background has padding,
                            this View's padding is set to the background's padding.
                     *//*
                    //et.setBackground(getDrawable(R.drawable.edittext_bg));

                    // Create a border programmatically
                    ShapeDrawable shape = new ShapeDrawable(new RectShape());

                    shape.getPaint().setColor(Color.BLUE);
                    shape.getPaint().setStyle(Paint.Style.STROKE);
                    shape.getPaint().setStrokeWidth(3);

                    // Assign the created border to EditText widget
                    etTaskTitle.setBackground(shape);
                }
            }
        });*/



        etTaskDesc = (EditText) findViewById(R.id.etTaskDesc);
        userDbHelper.close();
    }

    public void sortAscending () {
        List<String> sortedMonthsList = Arrays.asList(result);
        Collections.sort(sortedMonthsList);

        result = (String[]) sortedMonthsList.toArray();
    }


    //<------------------Input Validation ---------------------------------->
    public boolean validate() {
        boolean valid = true;

        String title = etTaskTitle.getText().toString();
        String desc = etTaskDesc.getText().toString();

        if (title.isEmpty()) {
            etTaskTitle.setError("Task title cannot be empty");
            valid = false;
        }
        else {
            etTaskTitle.setError(null);
        }


        if (desc.isEmpty()) {
            etTaskDesc.setError("Task description cannot be empty");
            valid = false;
        }
        else {
            etTaskDesc.setError(null);
        }

        return valid;
    }




    // <------------------ Disable going back to the LoginActivity ---------------------------------->
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateTask.this,ViewTask.class);
        startActivity(intent);
    }

    //<------------------ get menu options ---------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_create_task,menu);
        return true;
    }

    //<------------------ action setting ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:


                if (!validate()) {
                    return true;
                }

                String chooseSubject = etChooseSubject.getText().toString();
                String taskType = resultTypeofTask;    //etTaskType.getText().toString();
                String taskTitle = etTaskTitle.getText().toString();
                String task_desc = etTaskDesc.getText().toString();
                String task_date = etDate.getText().toString();
                String task_time = TimeStart.getText().toString();
                String task_percentage = "0";
                //String email, String task_subject, String task_type, String task_title, String task_desc, String task_date, String task_time,

                if (task_desc.equals("")) {
                    task_desc = "no details";
                }

                TaskDbHelper taskDbHelper = new TaskDbHelper(context);
                sqLiteDatabase = taskDbHelper.getWritableDatabase();

                //<------------------ Get user ID from shared preferences ---------------------------------->
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                final String user_email = prefs.getString("userID", null);

                //<------------------ Insert Data into Sqlite Database ---------------------------------->
                taskDbHelper.addTaskInformation(user_email, chooseSubject, taskType, taskTitle, task_desc, task_date, task_time, task_percentage, sqLiteDatabase);

                CharSequence text = taskType + " Created";
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();

                taskDbHelper.close();

                String CurrentString = etDate.getText().toString();
                String[] separated = CurrentString.split("-");
                String day = separated[0];
                String month = separated[1];
                String year = separated[2];
                cal = Calendar.getInstance();
                int parsemonth;

                if (month.equalsIgnoreCase("Jan")) {
                    parsemonth = 0;
                } else if (month.equalsIgnoreCase("Feb")) {
                    parsemonth = 1;
                } else if (month.equalsIgnoreCase("Mar")) {
                    parsemonth = 2;
                } else if (month.equalsIgnoreCase("Apr")) {
                    parsemonth = 3;
                } else if (month.equalsIgnoreCase("May")) {
                    parsemonth = 4;
                } else if (month.equalsIgnoreCase("Jun")) {
                    parsemonth = 5;
                } else if (month.equalsIgnoreCase("Jul")) {
                    parsemonth = 6;
                } else if (month.equalsIgnoreCase("Aug")) {
                    parsemonth = 7;
                } else if (month.equalsIgnoreCase("Sept")) {
                    parsemonth = 8;
                } else if (month.equalsIgnoreCase("Oct")) {
                    parsemonth = 9;
                } else if (month.equalsIgnoreCase("Nov")){
                    parsemonth = 10;
                }else if (month.equalsIgnoreCase("Dec")){
                    parsemonth=11;
                }else {
                    parsemonth=0;
                }

                int parseDay = Integer.parseInt(day);
                int parseYear = Integer.parseInt(year);

//                Toast.makeText(context, "day :" + day + "\nmonth :" + parsemonth + "\nyear :" + year, Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "hour:" + parseHour + "minute :" + parseMinute, Toast.LENGTH_SHORT).show();


                cal.set(Calendar.DATE,parseDay);  //1-31
                cal.set(Calendar.MONTH,parsemonth);  //first month is 0!!! January is zero!!!
                cal.set(Calendar.YEAR,parseYear);//year...

                cal.set(Calendar.HOUR_OF_DAY, parseHour);  //HOUR
                cal.set(Calendar.MINUTE, parseMinute);       //MIN
                cal.set(Calendar.SECOND, 0);       //SEC



                // Create a new PendingIntent and add it to the AlarmManager
                Intent intent = new Intent(CreateTask.this, MyBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateTask.this, 2444,intent, 0);

                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                String reminder = etReminder.getText().toString();

                if (reminder.equalsIgnoreCase("None"))
                {
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
                if(reminder.equalsIgnoreCase("10 Minutes Before"))
                {
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-600000, pendingIntent);
                }
                else if (reminder.equalsIgnoreCase("20 Minutes Before"))
                {
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-1200000, pendingIntent);
                }
                else if(reminder.equalsIgnoreCase("30 Minutes Before"))
                {
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-1800000, pendingIntent);
                }

                else if(reminder.equalsIgnoreCase("1 Hour Before"))
                {
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-3600000, pendingIntent);
                }
                else if(reminder.equalsIgnoreCase("2 Hour Before"))
                {
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()-7200000, pendingIntent);
                }



                Intent start = new Intent(getApplicationContext(),ViewTask.class);
                startActivity(start);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
