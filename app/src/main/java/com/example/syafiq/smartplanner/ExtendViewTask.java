package com.example.syafiq.smartplanner;//Created by syafiq on 4/1/2017.

import android.app.Dialog;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syafiq.smartplanner.database.UserDbHelper;
import com.example.syafiq.smartplanner.database_subTopic.SubtopicDbHelper;
import com.example.syafiq.smartplanner.database_task.TaskDbHelper;
import com.example.syafiq.smartplanner.database_topic.TopicDbHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class ExtendViewTask extends AppCompatActivity {


    View view;
    SQLiteDatabase sqLiteDatabase;

    TaskDbHelper taskDbHelper;
    Cursor cursor;

    SQLiteDatabase sqliteTopic;
    SQLiteDatabase sqliteWriteTopic;
    Cursor topicCursor;

    TopicDbHelper topicDbHelper;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<Parent> expandableListTitle;
    ArrayList<Child> childList;

    String task_type = "";
    TextView extend_view_title,extend_view_subject,extend_view_date,extend_view_time,extend_view_details;
    CheckBox checkPercentage;

    FloatingActionsMenu fab;
    FloatingActionButton addTopic;
    FloatingActionButton addSubTopic;

    String outputTopic;

    Inflater inflater;

    double detect_tick=0;
    double group = 0;

    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_task);
        checkPercentage = (CheckBox)findViewById(R.id.checkBox);

//<------------------ Add Drawer Layout ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        fab = (FloatingActionsMenu)findViewById(R.id.multiple_actions);
        extend_view_title = (TextView)findViewById(R.id.extend_task_tvTitle);
        extend_view_subject = (TextView)findViewById(R.id.extend_task_tvSubject);
        extend_view_date = (TextView)findViewById(R.id.extend_task_tvDate);
        extend_view_time = (TextView)findViewById(R.id.extend_task_tvTime);
        extend_view_details = (TextView)findViewById(R.id.extend_task_tvDetails);

        Bundle getAll = getIntent().getExtras();
        final String task_title = getAll.getString("task_title");
        String task_subject = getAll.getString("task_subject");
        task_type = getAll.getString("task_type");

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
                String TaskPercentage;

                email = cursor.getString(0);
                TaskSubject= cursor.getString(1);
                TaskType = cursor.getString(2);
                TaskTitle = cursor.getString(3);
                TaskDesc = cursor.getString(4);
                TaskDate = cursor.getString(5);
                TaskTime = cursor.getString(6);
                TaskPercentage = cursor.getString(7);

                if(task_title.equalsIgnoreCase(TaskTitle))
                {
                    extend_view_title.setText(TaskTitle);
                    extend_view_subject.setText(TaskSubject);
                    extend_view_date.setText(TaskDate);
                    extend_view_time.setText(TaskTime);
                    extend_view_details.setText(TaskDesc);
                    getSupportActionBar().setTitle(TaskType); //change App title into type of task


                    if(TaskPercentage.equalsIgnoreCase("100"))
                    {
                        checkPercentage.setChecked(!checkPercentage.isChecked());
                    }
                }

            }while (cursor.moveToNext());
        }

        //<--------------------- extend listview task start here ------------------------------------>

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        initData();

        expandableListAdapter = new CustomExpandableListAdapter(this,listDataHeader,listHash);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
             /*   Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
*/
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
              /*  Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        //<--------------------- expand listview ends here


        //<--------------------------------- Check if percentage is 100 , then disable expandable listview------------------------>
        if(checkPercentage.isChecked()){
            expandableListView.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.INVISIBLE);

        }
        else
        {
            expandableListView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }

        //<--------------------------------- Check if user click on percentage ------------------------>
        checkPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPercentage.isChecked())
                {
                    expandableListView.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);

                    String TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage;

                    //<------------------ Get user ID from shared preferences ---------------------------------->
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    final String user_email = prefs.getString("userID",null);

                    TaskSubject = extend_view_subject.getText().toString();
                    TaskType = task_type;
                    String task_title = extend_view_title.getText().toString();
                    TaskTitle = extend_view_title.getText().toString();
                    TaskDesc = extend_view_details.getText().toString();
                    TaskDate = extend_view_date.getText().toString();
                    TaskTime = extend_view_time.getText().toString();
                    TaskPercentage = "0";
                    Toast.makeText(ExtendViewTask.this, "not completed", Toast.LENGTH_SHORT).show();

                    //<------------------ Insert Data into Sqlite Database ---------------------------------->
                    taskDbHelper.updatePercentage(task_title,user_email,TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage,sqLiteDatabase);
                }
                else{
                    expandableListView.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);

                    String TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage;

                    //<------------------ Get user ID from shared preferences ---------------------------------->
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    final String user_email = prefs.getString("userID",null);

                    TaskSubject = extend_view_subject.getText().toString();
                    TaskType = task_type;
                    String task_title = extend_view_title.getText().toString();
                    TaskTitle = extend_view_title.getText().toString();
                    TaskDesc = extend_view_details.getText().toString();
                    TaskDate = extend_view_date.getText().toString();
                    TaskTime = extend_view_time.getText().toString();
                    TaskPercentage = "100";
                    Toast.makeText(ExtendViewTask.this, "completed", Toast.LENGTH_SHORT).show();

                    //<------------------ Insert Data into Sqlite Database ---------------------------------->
                    taskDbHelper.updatePercentage(task_title,user_email,TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage,sqLiteDatabase);
                }

            }
        });


        addTopic = (FloatingActionButton)findViewById(R.id.action_a);
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(ExtendViewTask.this);
                inputAlert.setTitle("Add Topic");
                final EditText userInput = new EditText(ExtendViewTask.this);
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        String topic_tick = "false";


                        topicDbHelper = new TopicDbHelper(ExtendViewTask.this);
                        sqliteWriteTopic = topicDbHelper.getWritableDatabase();

                        //<------------------ Get user ID from shared preferences ---------------------------------->
                        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        final String user_email = prefs.getString("userID",null);

                        //<------------------ Insert Data into Sqlite Database ---------------------------------->
                        topicDbHelper.addTopicInformation(user_email,task_title,userInputValue,topic_tick,sqliteWriteTopic);

                        CharSequence text = "Topic added";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();

                        topicDbHelper.close();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        fab.collapse();
                    }
                });
                inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        fab.collapse();
                    }
                });
                AlertDialog alertDialog = inputAlert.create();
                alertDialog.show();

            }
        });

        addSubTopic = (FloatingActionButton)findViewById(R.id.action_b);
        addSubTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ExtendViewTask.this);
                dialog.setContentView(R.layout.custom_topic);

                final Spinner choiceTopic = (Spinner)dialog.findViewById(R.id.topicdropdown);
                final EditText inputsubtopic = (EditText)dialog.findViewById(R.id.inputsubtopic);


                //<------------------ Get user ID from shared preferences ---------------------------------->
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                final String username = prefs.getString("userID",null);

                topicDbHelper = new TopicDbHelper(getApplicationContext());
                sqliteTopic = topicDbHelper.getReadableDatabase();
                topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);
                List<String> obj = new ArrayList<String>();

                if(topicCursor.moveToFirst())
                {
                    do {
                        String topicUsername,topicTask,topicTitle,topicTick;
                        topicUsername = topicCursor.getString(0);
                        topicTask = topicCursor.getString(1);
                        topicTitle = topicCursor.getString(2);
                        topicTick = topicCursor.getString(3);

                        Toast.makeText(ExtendViewTask.this, "title : " + topicTask, Toast.LENGTH_SHORT).show();

                        if(topicUsername.equalsIgnoreCase(username))
                        {
                            if (topicTask.equalsIgnoreCase(extend_view_title.getText().toString())){

                                obj.add(topicTitle);
                            }

                        }

                    }while (topicCursor.moveToNext());
                }

                topicDbHelper.close();

                // Creating adapter for spinner
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ExtendViewTask.this, android.R.layout.simple_spinner_item, obj);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                // attaching data adapter to spinner
                choiceTopic.setAdapter(dataAdapter);

                // Spinner click listener
                choiceTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        // On selecting a spinner item
                        outputTopic = adapterView.getItemAtPosition(i).toString();
                        // Showing selected spinner item
                        Toast.makeText(adapterView.getContext(), "Selected: " + outputTopic, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



                Button btnSubmit = (Button)dialog.findViewById(R.id.btnOK);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SubtopicDbHelper subtopicDB = new SubtopicDbHelper(ExtendViewTask.this);
                        SQLiteDatabase sqlWriteSubtopic = subtopicDB.getWritableDatabase();

                        String userInputValue = inputsubtopic.getText().toString();
                        //task title, topic, subtopic
                        if(!validate())
                        {
                            return;
                        }

                        //<------------------ Insert Data into Sqlite Database ---------------------------------->
                        subtopicDB.addsubTopicInformation(extend_view_title.getText().toString(),outputTopic,userInputValue,sqlWriteSubtopic);

                        CharSequence text = "Subtopic added";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();

                        subtopicDB.close();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        dialog.dismiss();
                        fab.collapse();
                    }

                    private boolean validate() {
                        boolean valid = true;
                        if(inputsubtopic.getText().toString().isEmpty())
                        {
                            inputsubtopic.setError("Field is blank!");
                            valid=false;
                        }
                        return valid;
                    }
                });

                Button buttoncancel = (Button)dialog.findViewById(R.id.btnCancel);
                buttoncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        fab.collapse();
                    }
                });

                dialog.show();


            }
        });
    }


    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String username = prefs.getString("userID",null);

        Toast.makeText(this, "" + username, Toast.LENGTH_SHORT).show();

        topicDbHelper = new TopicDbHelper(getApplicationContext());
        sqliteTopic = topicDbHelper.getReadableDatabase();
        topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);

        int count = 0;
        if(topicCursor.moveToFirst())
        {
            do {
                String topicUsername,topicTask,topicTitle,topicTick;
                topicUsername = topicCursor.getString(0);
                topicTask = topicCursor.getString(1);
                topicTitle = topicCursor.getString(2);
                topicTick = topicCursor.getString(3);

//                Toast.makeText(this, "task title: " + topicTask + "    task topic: " + topicTitle, Toast.LENGTH_SHORT).show();
                if(topicUsername.equalsIgnoreCase(username))
                {
                    if (topicTask.equalsIgnoreCase(extend_view_title.getText().toString())){
                        listDataHeader.add(topicTitle);
//                        Toast.makeText(this, "" + topicTitle, Toast.LENGTH_SHORT).show();
                        count++;
                    }
                }

            }while (topicCursor.moveToNext());
        }

//        listDataHeader.add("EDMTDev");
//        listDataHeader.add("Android");
//        listDataHeader.add("Xamarin");
//        listDataHeader.add("UWP");

        List <String> edmtDev = new ArrayList<String>();


        SubtopicDbHelper subTopicDbhelper = new SubtopicDbHelper(getApplicationContext());
        SQLiteDatabase sqliteSubtopic = subTopicDbhelper.getReadableDatabase();
        Cursor subtopicCursor = subTopicDbhelper.getSubTopicInformation(sqliteSubtopic);

        if(subtopicCursor.moveToFirst())
        {
            do {
                String subtopicTask,subtopicTitle,subtopicTopic;
                subtopicTask = subtopicCursor.getString(0);
                subtopicTitle = subtopicCursor.getString(1);
                subtopicTopic = subtopicCursor.getString(2);

//                Toast.makeText(this, "Task : " + subtopicTask + "\nTopic :" + subtopicTitle + "\nSubtopic :" + subtopicTopic, Toast.LENGTH_SHORT).show();
                for (int m =0 ; m<count; m++){

                    listHash.put(listDataHeader.get(m), edmtDev);
                    if (subtopicTask.equalsIgnoreCase(extend_view_title.getText().toString()))
                    {
                        if (subtopicTitle.equalsIgnoreCase(listDataHeader.get(m)))
                        {
//                            Toast.makeText(this, "Task : " + subtopicTask + "\nTopic :" + subtopicTitle + "\nSubtopic :" + subtopicTopic, Toast.LENGTH_SHORT).show();
                            edmtDev.add(subtopicTopic);
//                            Toast.makeText(this, "check edmt list :" + edmtDev, Toast.LENGTH_SHORT).show();
                            listHash.put(listDataHeader.get(m),edmtDev);
                        }


                    }
                }



            }while (subtopicCursor.moveToNext());
        }


        subTopicDbhelper.close();
        sqliteSubtopic.close();

//            listHash.put(listDataHeader.get(m),edmtDev);

        topicDbHelper.close();
        sqliteTopic.close();
    }


    // <------------------ Disable going back to the LoginActivity ---------------------------------->
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String username = prefs.getString("userID",null);

        TopicDbHelper topicDbHelper = new TopicDbHelper(getApplicationContext());
        SQLiteDatabase sqliteTopic = topicDbHelper.getReadableDatabase();
        Cursor topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);



        group = expandableListAdapter.getGroupCount();
        if(topicCursor.moveToFirst())
        {
            do {
                String topicUsername,topicTask,topicTitle,topicTick;
                topicUsername = topicCursor.getString(0);
                topicTask = topicCursor.getString(1);
                topicTitle = topicCursor.getString(2);
                topicTick = topicCursor.getString(3);


                if(topicUsername.equalsIgnoreCase(username))
                {
                    if(topicTask.equalsIgnoreCase(extend_view_title.getText().toString()))
                    {
                        if (topicTick.equalsIgnoreCase("true")){
                            detect_tick++;
                        }

                    }
                }

            }while (topicCursor.moveToNext());

            double total_tick= detect_tick/group *100;
            //calculate percentage
            String TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage;

            //<------------------ Get user ID from shared preferences ---------------------------------->
            SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            final String user_email = preferences.getString("userID",null);

            TaskSubject = extend_view_subject.getText().toString();
            TaskType = task_type;
            String task_title = extend_view_title.getText().toString();
            TaskTitle = extend_view_title.getText().toString();
            TaskDesc = extend_view_details.getText().toString();
            TaskDate = extend_view_date.getText().toString();
            TaskTime = extend_view_time.getText().toString();
            TaskPercentage = String.valueOf(total_tick);;

            //<------------------ Insert Data into Sqlite Database ---------------------------------->
            taskDbHelper.updatePercentage(task_title,user_email,TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage,sqLiteDatabase);

            Intent intent = new Intent(ExtendViewTask.this,ViewTask.class);
            startActivity(intent);
        }


//        total_tick = (detect_tick/group) * 100;
//        String parseTotalTick = String.valueOf(total_tick);

    }

    //<------------------ get options task ---------------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_extend_task,menu);
        return true;
    }

    //<------------------ action edit ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_edit:
                Bundle getAll = getIntent().getExtras();
                String task_title = getAll.getString("task_title");
                String task_subject = getAll.getString("task_subject");
                String task_type = getAll.getString("task_type");

                Bundle newBundle = new Bundle();
                newBundle.putString("extendTaskTitle", task_title);
                newBundle.putString("extendTaskSubject", task_subject);
                newBundle.putString("extendTaskType", task_type);

                Intent intent = new Intent(ExtendViewTask.this,EditTask.class);
                intent.putExtras(newBundle);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
