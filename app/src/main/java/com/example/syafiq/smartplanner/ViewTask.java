package com.example.syafiq.smartplanner;//Created by syafiq on 3/12/2016.

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.syafiq.smartplanner.database.DataProvider;
import com.example.syafiq.smartplanner.database.ListDataAdapter;
import com.example.syafiq.smartplanner.database.UserDbHelper;
import com.example.syafiq.smartplanner.database_task.ListTaskAdapter;
import com.example.syafiq.smartplanner.database_task.TaskDbHelper;
import com.example.syafiq.smartplanner.database_task.TaskProvider;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.timroes.android.listview.EnhancedListView;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class ViewTask extends AppCompatActivity {
    private ActionBarDrawerToggle mToggle;
    SQLiteDatabase sqLiteDatabase;
    TaskDbHelper taskDbHelper;
    Cursor cursor;

    EnhancedListView listView;
    ListTaskAdapter listTaskAdapter;

    ArrayList<String> newList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//set up notitle
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_viewtask);

        //<------------------ Add Drawer Layout ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        mToggle.syncState();



        //<------------------ Get navigation (drawer) header ---------------------------------->
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);


        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        final RelativeLayout header = (RelativeLayout)headerview.findViewById(R.id.header); //accessing header in relative layout (drawer_hearder)
        TextView email_header = (TextView)header.findViewById(R.id.user_email_header); //accessing text view from header
        email_header.setText(userID);

        //<------------------ Set On click listener for Drawer Header ---------------------------------->
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"My Account Selected",Toast.LENGTH_LONG).show();
                Intent drawer_start = new Intent(getApplicationContext(), MyProfileActivity.class);
                header.setFocusable(true);
                header.setFocusableInTouchMode(true);
                header.requestFocus();
                //header.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
                startActivity(drawer_start);
            }
        });


        View mActionBarView = getLayoutInflater().inflate(R.layout.my_action_bar, null); // spinner view layout
        final Spinner spinner = (Spinner) mActionBarView.findViewById(R.id.spinner1);


        final ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.task_array, R.layout.task_spinner_layout);
        spinner.setAdapter(adapter);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setCustomView(mActionBarView);
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        //when user navigate through drawer items (activities)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(),"Activity Home",Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(a);
                        break;

                    case R.id.nav_group:
                        Toast.makeText(getApplicationContext(),"Group Discussions",Toast.LENGTH_SHORT).show();
                        Intent e = new Intent(getApplicationContext(), ViewDiscussion.class);
                        startActivity(e);
                        break;

                    case R.id.nav_subject:
                        Toast.makeText(getApplicationContext(),"View Subjects",Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(getApplicationContext(), ViewSubjects.class);
                        startActivity(b);
                        break;

                    case R.id.nav_task:
                        Toast.makeText(getApplicationContext(),"View Tasks",Toast.LENGTH_SHORT).show();
                        Intent c = new Intent(getApplicationContext(), ViewTask.class);
                        startActivity(c);
                        break;

                    case R.id.nav_calendar:
                        Toast.makeText(getApplicationContext(),"Calendar Academic",Toast.LENGTH_SHORT).show();
                        Intent d = new Intent(getApplicationContext(), ViewCalendar.class);
                        startActivity(d);
                        break;


                }
                return true;
            }
        });



        //<------------------ Declare Variables ---------------------------------->
        listView = (EnhancedListView)findViewById(R.id.list_view_task);

        listTaskAdapter = new ListTaskAdapter(getApplicationContext(),R.layout.row_task_layout);
        taskDbHelper = new TaskDbHelper(getApplicationContext());
        sqLiteDatabase = taskDbHelper.getReadableDatabase();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long id) {

                TextView selectedText = (TextView) arg0.getChildAt(0);

                if (selectedText != null) {

                    selectedText.setTextColor(Color.WHITE);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    selectedText.setTypeface(boldTypeface);
                    Toast.makeText(ViewTask.this, "current : " + selectedText.getText().toString(), Toast.LENGTH_SHORT).show();


                    listTaskAdapter.clear();
                    listTaskAdapter.notifyDataSetChanged();
                    listView.setAdapter(null);



                    if(selectedText.getText().toString().equalsIgnoreCase("Tasks")){
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


                                if(email.equalsIgnoreCase(userID))
                                {


                                    TaskProvider taskProvider= new TaskProvider(TaskSubject,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime,TaskPercentage);

                                    listTaskAdapter.sort(new Comparator<TaskProvider>()
                                    {
                                        public int compare(TaskProvider lhs, TaskProvider rhs) {
                                            System.out.println("laluan sini");
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                                            String date1 = lhs.getTaskDate();
                                            String date2 = lhs.getTaskDate();
                                            int result = 0;
                                            Date dateStart = null, dateEnd = null;
                                            try {
                                                dateStart = sdf.parse(date1);
                                                dateEnd = sdf.parse(date2);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            return dateStart.compareTo(dateEnd);
                                        }
                                    });
                                    listTaskAdapter.notifyDataSetChanged();
                                    listTaskAdapter.add(taskProvider);
                                    listView.setAdapter(listTaskAdapter);
                                    listTaskAdapter.notifyDataSetChanged();
                                }

                            }while (cursor.moveToNext());
                        }

                    } // task selection

                    else if(selectedText.getText().toString().equalsIgnoreCase("Assignment"))
                    {


                        cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);
                        if(cursor.moveToFirst()) {
                            do {
                                String email;
                                String TaskSubject;
                                String TaskType;
                                String TaskTitle;
                                String TaskDesc;
                                String TaskDate;
                                String TaskTime;
                                String TaskPercentage;

                                email = cursor.getString(0);
                                TaskSubject = cursor.getString(1);
                                TaskType = cursor.getString(2);
                                TaskTitle = cursor.getString(3);
                                TaskDesc = cursor.getString(4);
                                TaskDate = cursor.getString(5);
                                TaskTime = cursor.getString(6);
                                TaskPercentage = cursor.getString(7);

                                if (email.equalsIgnoreCase(userID)) {
                                    if (TaskType.equalsIgnoreCase("Assignment")) {
                                        TaskProvider taskProvider = new TaskProvider(TaskSubject, TaskType, TaskTitle, TaskDesc, TaskDate, TaskTime,TaskPercentage);
//                                        Collections.sort(listView,new CustomComparator());
                                        listTaskAdapter.add(taskProvider);
                                        listView.setAdapter(listTaskAdapter);
                                        listTaskAdapter.notifyDataSetChanged();
                                    }

                                }

                            } while (cursor.moveToNext());
                        }
                    }//Assignment Selection


                    else if(selectedText.getText().toString().equalsIgnoreCase("Revision"))
                    {
                        cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);
                        if(cursor.moveToFirst()) {
                            do {
                                String email;
                                String TaskSubject;
                                String TaskType;
                                String TaskTitle;
                                String TaskDesc;
                                String TaskDate;
                                String TaskTime;
                                String TaskPercentage;

                                email = cursor.getString(0);
                                TaskSubject = cursor.getString(1);
                                TaskType = cursor.getString(2);
                                TaskTitle = cursor.getString(3);
                                TaskDesc = cursor.getString(4);
                                TaskDate = cursor.getString(5);
                                TaskTime = cursor.getString(6);
                                TaskPercentage = cursor.getString(7);

                                if (email.equalsIgnoreCase(userID)) {
                                    if (TaskType.equalsIgnoreCase("Revision")) {
                                        TaskProvider taskProvider = new TaskProvider(TaskSubject, TaskType, TaskTitle, TaskDesc, TaskDate, TaskTime,TaskPercentage);

                                        listTaskAdapter.add(taskProvider);
                                        listTaskAdapter.sort(new Comparator<TaskProvider>() {
                                            public int compare(TaskProvider lhs, TaskProvider rhs) {
                                                return  rhs.getTaskDate().compareTo(lhs.getTaskDate());
                                            }
                                        });
                                        listTaskAdapter.notifyDataSetChanged();
                                        listView.setAdapter(listTaskAdapter);
                                        listTaskAdapter.notifyDataSetChanged();
                                    }

                                }

                            } while (cursor.moveToNext());
                        }
                    }// Revision Selection


                    else if(selectedText.getText().toString().equalsIgnoreCase("Exam"))
                    {
                        cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);
                        if(cursor.moveToFirst()) {
                            do {
                                String email;
                                String TaskSubject;
                                String TaskType;
                                String TaskTitle;
                                String TaskDesc;
                                String TaskDate;
                                String TaskTime;
                                String TaskPercentage;

                                email = cursor.getString(0);
                                TaskSubject = cursor.getString(1);
                                TaskType = cursor.getString(2);
                                TaskTitle = cursor.getString(3);
                                TaskDesc = cursor.getString(4);
                                TaskDate = cursor.getString(5);
                                TaskTime = cursor.getString(6);
                                TaskPercentage = cursor.getString(7);

                                if (email.equalsIgnoreCase(userID)) {
                                    if (TaskType.equalsIgnoreCase("Exam")) {
                                        TaskProvider taskProvider = new TaskProvider(TaskSubject, TaskType, TaskTitle, TaskDesc, TaskDate, TaskTime,TaskPercentage);


//                                        CircleProgressView mCircleProgressView1 = (CircleProgressView) findViewById(R.id.circle_progress_view1);
//                                        mCircleProgressView1.setProgressWithAnimation(65, 1500);
                                        listTaskAdapter.add(taskProvider);
                                        listView.setAdapter(listTaskAdapter);
                                        listTaskAdapter.notifyDataSetChanged();
                                    }

                                }

                            } while (cursor.moveToNext());
                        }
                    }// Exam Selection
//else if statement ends here

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // do stuff
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView extend_task_title = (TextView)view.findViewById(R.id.tvTitleTask);
                TextView extend_task_subject = (TextView)view.findViewById(R.id.tvTaskSubject);
                TextView extend_task_type = (TextView)view.findViewById(R.id.tvTaskType);

                final Bundle bundle = new Bundle();
                bundle.putString("task_title",extend_task_title.getText().toString());
                bundle.putString("task_subject",extend_task_subject.getText().toString());
                bundle.putString("task_type",extend_task_type.getText().toString());

                Intent intent = new Intent(ViewTask.this,ExtendViewTask.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked","pos: " + pos);


                TextView tv = (TextView) arg1.findViewById(R.id.tvTitleTask);
                String value = tv.getText().toString();

                taskDbHelper = new TaskDbHelper(getApplicationContext());
                sqLiteDatabase = taskDbHelper.getReadableDatabase();
                cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);
                String email2;
                String TaskSubject2;
                String TaskType2;
                String TaskTitle2 = null;
                String TaskDesc2;
                String TaskDate2;
                String TaskTime2;

                if(cursor.moveToFirst())
                {
                    do {
                        String email;
                        String TaskSubject;
                        String TaskType;
                        String TaskTitle;
                        String TaskDesc;
                        String TaskDate;
                        String TaskTime;

                        email = cursor.getString(0);
                        TaskSubject = cursor.getString(1);
                        TaskType = cursor.getString(2);
                        TaskTitle = cursor.getString(3);
                        TaskDesc = cursor.getString(4);
                        TaskDate = cursor.getString(5);
                        TaskTime = cursor.getString(6);


                        if(value.equalsIgnoreCase(TaskTitle))
                        {
                            email2 = cursor.getString(0);
                            TaskSubject2 = cursor.getString(1);
                            TaskType2 = cursor.getString(2);
                            TaskTitle2 = cursor.getString(3);
                            TaskDesc2 = cursor.getString(4);
                            TaskDate2 = cursor.getString(5);
                            TaskTime2 = cursor.getString(6);

                        }

                    }while (cursor.moveToNext());
                }

                final String finalEventTitle = TaskTitle2;
                final String show_task = TaskTitle2;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewTask.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this task?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.icon_delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        taskDbHelper = new TaskDbHelper(getApplicationContext());
                        sqLiteDatabase = taskDbHelper.getReadableDatabase();
                        taskDbHelper.deleteTaskInformation(finalEventTitle,sqLiteDatabase);
                        Toast.makeText(ViewTask.this, show_task + " Deleted",  Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),ViewTask.class));
                        dialog.dismiss();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                return true;
            }
        });


        listView.setDismissCallback(new EnhancedListView.OnDismissCallback()
        {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int i)
            {

                final TaskProvider taskProvider = (TaskProvider)  listTaskAdapter.getItem(i);
                // Remove the item from the adapter

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewTask.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this task?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.icon_delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        taskDbHelper = new TaskDbHelper(getApplicationContext());
                        sqLiteDatabase = taskDbHelper.getReadableDatabase();
                        taskDbHelper.deleteTaskInformation(taskProvider.getTaskTitle(),sqLiteDatabase);
                        Toast.makeText(ViewTask.this, "Task Deleted", Toast.LENGTH_LONG).show();

                        listTaskAdapter.remove(i);
                        listTaskAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                return null;
            }

        });

        listView.enableSwipeToDismiss();


        //<------------------ Set on click listener for icon add  ---------------------------------->
        FloatingActionButton iconAdd = (FloatingActionButton) findViewById(R.id.imgAddTask2);

        iconAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(getApplicationContext(), CreateTask.class);
                startActivity(intent);
            }
        });
    }

    /*public void sortlistbyPrice(){
        Collections.sort(listTaskAdapter, new Comparator<TaskProvider>() {
            @Override
            public int compare(TaskProvider lhs, TaskProvider rhs) {
                return lhs.getTaskDate().compareTo(rhs.getTaskDate());
            }
        });
        listTaskAdapter.notifyDataSetChanged();

    }
*/
    // <------------------ Disable going back to the LoginActivity ---------------------------------->
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewTask.this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        listTaskAdapter.clear();
        listTaskAdapter.notifyDataSetChanged();
        listView.setAdapter(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_view_task,menu);

        return true;
    }
    //<------------------ toggle button drawer ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch (item.getItemId())
        {
            case R.id.sort_date:

//                Collections.sort((List<TaskProvider>) listView,new Comparator<TaskProvider>() {
//                    @Override
//                    public int compare(TaskProvider lhs, TaskProvider rhs) {
//                        return lhs.getTaskDate().compareTo(rhs.getTaskDate());
//                    }
//                });

        }
        return super.onOptionsItemSelected(item);
    }

}
