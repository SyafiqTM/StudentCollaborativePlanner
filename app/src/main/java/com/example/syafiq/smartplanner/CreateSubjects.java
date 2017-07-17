package com.example.syafiq.smartplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class CreateSubjects extends AppCompatActivity{
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    EditText sub_name, sub_code, sub_group, sub_class,lec_name, lect_contact;
    Context context = this;
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;

    TextView tvSubjectName, tvSubjectCode, tvSubjectGroup, tvSubjectClass,tvLecName, tvLecContact;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsubjects);

        sub_name = (EditText)findViewById(R.id.etSubject_name);
        sub_code = (EditText)findViewById(R.id.etSubject_code);
        sub_group = (EditText)findViewById(R.id.etSubject_group);
        sub_class = (EditText)findViewById(R.id.etSubject_class);
        lec_name = (EditText) findViewById(R.id.etLecturer_name);
        lect_contact = (EditText) findViewById(R.id.etLecturer_contact);

        sub_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    tvSubjectName = (TextView)findViewById(R.id.tvSubject_name);
                    tvSubjectName.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    tvSubjectName = (TextView)findViewById(R.id.tvSubject_name);
                    tvSubjectName.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        sub_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    tvSubjectCode = (TextView)findViewById(R.id.tvSubject_code);
                    tvSubjectCode.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    tvSubjectCode = (TextView)findViewById(R.id.tvSubject_code);
                    tvSubjectCode.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        sub_group.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    tvSubjectGroup = (TextView)findViewById(R.id.tvSubject_group);
                    tvSubjectGroup.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    tvSubjectGroup = (TextView)findViewById(R.id.tvSubject_group);
                    tvSubjectGroup.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        sub_class.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    tvSubjectClass = (TextView)findViewById(R.id.tvSubject_class);
                    tvSubjectClass.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    tvSubjectClass = (TextView)findViewById(R.id.tvSubject_class);
                    tvSubjectClass.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        lec_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    tvLecName = (TextView)findViewById(R.id.tvLecturer_name);
                    tvLecName.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    tvLecName = (TextView)findViewById(R.id.tvLecturer_name);
                    tvLecName.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });



        lect_contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    tvLecContact = (TextView)findViewById(R.id.tvLecturer_contact);
                    tvLecContact.setTextColor(getResources().getColor(R.color.darker_blue));
                }
                else {
                    tvLecContact = (TextView)findViewById(R.id.tvLecturer_contact);
                    tvLecContact.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });


        //<------------------ show current time on text view (Time Start) ---------------------------------->
       /* Date a = new Date();
        a.setTime(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String dateStringStart = sdf.format(a);

        TimeStart.setText(dateStringStart);
        //TimeStart.setVisibility(View.INVISIBLE); // we invisible it first

        //<------------------ Time Picker ---------------------------------->
        TimeStart.setOnClickListener(new View.OnClickListener() {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateSubjects.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String time = selectedHour + ":" + selectedMinute;
                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = fmt.parse(time);
                        }
                        catch (ParseException e){
                            e.printStackTrace();
                        }
                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");
                        String formattedTime = fmtOut.format(date);
                        TimeStart.setText(formattedTime);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/

        //<------------------ get Duration of Class ---------------------------------->
        /*showDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                CharSequence colors[] = new CharSequence[] {"1 Hour", "2 Hour", "3 Hour", "4 Hour"};

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSubjects.this);
                builder.setTitle("Class Duration");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        dialog.dismiss();
                        switch(which){
                            case 0:
                                Toast.makeText(CreateSubjects.this,
                                        "Duration Class : 1 Hour", Toast.LENGTH_SHORT).show();
                                         showDuration.setText("1 Hour");
                                break;
                            case 1:
                                Toast.makeText(CreateSubjects.this,
                                        "Duration Class : 2 Hour", Toast.LENGTH_SHORT).show();
                                        showDuration.setText("2 Hour");
                                break;
                            case 2:
                                Toast.makeText(CreateSubjects.this,
                                        "Duration Class : 3 Hour", Toast.LENGTH_SHORT).show();
                                         showDuration.setText("3 Hour");
                                break;
                            case 3:
                                Toast.makeText(CreateSubjects.this,
                                        "Duration Class : 4 Hour", Toast.LENGTH_SHORT).show();
                                         showDuration.setText("4 Hour");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });*/


    }


    //<------------------Input Validation ---------------------------------->
    public boolean validate() {
        boolean valid = true;

        String subjectName = sub_name.getText().toString();
        String subjectCode = sub_code.getText().toString();
        String subjectGroup = sub_group.getText().toString();
        String subjectClassroom = sub_class.getText().toString();
        String lectureName = lec_name.getText().toString();
        String lectureContact = lect_contact.getText().toString();

        if (subjectName.isEmpty()) {
            sub_name.setError("Subject name cannot be empty");
            valid = false;
        }
        else {
            sub_name.setError(null);
        }


        if (subjectCode.isEmpty()) {
            sub_code.setError("Subject code cannot be empty");
            valid = false;
        }
        else {
            sub_code.setError(null);
        }


        if (subjectGroup.isEmpty()) {
            sub_group.setError("Group cannot be empty");
            valid = false;
        }
        else {
            sub_group.setError(null);
        }


        if (subjectClassroom.isEmpty()) {
            sub_class.setError("Classroom cannot be empty");
            valid = false;
        }
        else {
            sub_class.setError(null);
        }

        return valid;
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

                if (!validate()) {
                    return true;
                }

                String subjectName = sub_name.getText().toString();
                String subjectCode = sub_code.getText().toString();
                String subjectGroup = sub_group.getText().toString();
                String subjectClassroom = sub_class.getText().toString();
                String lectureName = lec_name.getText().toString();
                String lectureContact = lect_contact.getText().toString();

                if(lectureName.isEmpty())
                {
                    lectureName = "no detail";
                }

                if(lectureContact.isEmpty())
                {
                    lectureContact = "no detail";
                }

                userDbHelper = new UserDbHelper(context);
                sqLiteDatabase = userDbHelper.getWritableDatabase();

                //<------------------ Get user ID from shared preferences ---------------------------------->
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                final String user_email = prefs.getString("userID",null);

                //<------------------ Insert Data into Sqlite Database ---------------------------------->
                userDbHelper.addSubjectInformation(user_email,subjectName,subjectCode,subjectGroup,subjectClassroom,lectureName,lectureContact,sqLiteDatabase);

                CharSequence text = "Subject saved";
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
