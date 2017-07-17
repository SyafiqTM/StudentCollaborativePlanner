package com.example.syafiq.smartplanner;
//Created by syafiq on 19/12/2016.

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syafiq.smartplanner.database_task.TaskDbHelper;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;


public class ViewCalendar extends AppCompatActivity {

    public static final int BLACK = -16777216;
    public static final int BLUE = -16776961;
    public static final int CYAN = -16711681;
    public static final int GREEN = -16711936;
    public static final int MAGENTA = -65281;
    public static final int RED = -65536;
    public static final int YELLOW = -256;


    SQLiteDatabase sqLiteDatabase;
    TaskDbHelper taskDbHelper;
    Cursor cursor;

    private static final String TAG = "ViewCalendar";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    private ActionBarDrawerToggle mToggle;
    CompactCalendarView compactCalendarView;

    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private SimpleDateFormat dateTitleDisplay = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    TextView dateCalendar,dateTitle,dateNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcalendar);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        //<------------------ List View Declaration ---------------------------------->
        final List<String> mutableBookings = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mutableBookings);

        final ListView bookingsListView = (ListView)findViewById(R.id.bookings_listview);
        bookingsListView.setAdapter(adapter);
        bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                // When clicked, show a toast with the TextView text


                String value = ((TextView) view).getText().toString();

                taskDbHelper = new TaskDbHelper(getApplicationContext());
                sqLiteDatabase = taskDbHelper.getReadableDatabase();
                cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);
                String email2,TaskSubject2 = null,TaskType2 = null,TaskTitle2 = null,TaskDesc2 = null,TaskDate2 = null,TaskTime2 = null;

                if(cursor.moveToFirst())
                {
                    do {
                        String email,TaskSubject ,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime;

                        email = cursor.getString(0);
                        TaskSubject= cursor.getString(1);
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


                if(TaskType2!=null){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewCalendar.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(TaskType2);

                    // Setting Dialog Message
                    alertDialog.setMessage("Title\t\t\t: " + TaskTitle2 + "\nDetails \t: " + TaskDesc2 + "\nSubject \t: " + TaskSubject2 + "\nDate \t\t\t: " + TaskDate2 + "\nTime \t\t: " + TaskTime2);


                    // Setting OK Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            dialog.dismiss();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }


            }

        });
        //<------------------ end of list view ---------------------------------->



//<------------------ Calendar Coding Start Here ---------------------------------->

        //Text View for  Month of the Year
        dateCalendar = (TextView) findViewById(R.id.monthYear) ;

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        // uncomment below to show indicators above small indicator events
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        compactCalendarView.setUseThreeLetterAbbreviation(true);


        final ImageView showPreviousMonthBut = (ImageView) findViewById(R.id.prev_button);
        final ImageView showNextMonthBut = (ImageView) findViewById(R.id.next_button);

        //set initial title
        dateCalendar.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));


        //<------------------ This method call load events ---------------------------------->
        loadEvents();
        try {
            loadPublicHoliday();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // refresh calendar
        compactCalendarView.invalidate();

        //<------------------ This method gives log by month ---------------------------------->
        //logEventsByMonth(compactCalendarView);
        dateTitle = (TextView)findViewById(R.id.dateTitle);
        dateNothing = (TextView)findViewById(R.id.dateNothing);
        //<------------------ What happen if calendar were click / scroll ---------------------------------->
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //Toast.makeText(ViewCalendar.this, "Date : " + dateClicked.toString(), Toast.LENGTH_SHORT).show();
                dateTitle.setText(dateTitleDisplay.format(dateClicked));
                dateNothing.setVisibility(View.VISIBLE);

                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));

                if (bookingsFromMap != null) {
                    Log.d(TAG, bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                        dateNothing.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Changes toolbar title on monthChange
                dateCalendar.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }

        });

        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showPreviousMonth();
            }
        });

        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showNextMonth();
            }
        });

    }

    private void loadPublicHoliday() throws ParseException {

        String date="01/01/2017"; // new year
        String date2 = "28/01/2017"; // chinese new year
        String date3 = "29/01/2017"; // chinese new year
        String date4 = "01/05/2017"; // labour day
        String date5 = "10/05/2017"; // wesak
        String date6 = "31/8/2017"; // national day
        String date7 = "1/9/2017"; // Hari Raya Haji
        String date8 = "16/9/2017"; //Malaysia Day
        String date9 = "25/12/2017"; //Christmas

        String [] dateHoliday = {date,date2,date3,date4,date5,date6,date7,date8,date9};

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Calendar c[]= new Calendar[dateHoliday.length];

        for(int i=0 ; i<dateHoliday.length; i++)
        {
            c[i] = Calendar.getInstance();
            c[i].setTime(sdf.parse(dateHoliday[i]));
        }

        for(int co=0; co<2; co++){ // 0 = 2017

            Event event = new Event(getResources().getColor(R.color.black),c[0].getTimeInMillis(), "New Year");
            compactCalendarView.addEvent(event, true);

            Event event2 = new Event(getResources().getColor(R.color.black),c[1].getTimeInMillis(), "Chinese New Year");
            compactCalendarView.addEvent(event2, true);

            Event event3 = new Event(getResources().getColor(R.color.black),c[2].getTimeInMillis(), "Chinese New Year");
            compactCalendarView.addEvent(event3, true);

            Event event4 = new Event(getResources().getColor(R.color.black),c[3].getTimeInMillis(), "Labour Day");
            compactCalendarView.addEvent(event4, true);

            Event event5 = new Event(getResources().getColor(R.color.black),c[4].getTimeInMillis(), "Wesak");
            compactCalendarView.addEvent(event5, true);

            Event event6 = new Event(getResources().getColor(R.color.black),c[5].getTimeInMillis(), "National Day");
            compactCalendarView.addEvent(event6, true);

            Event event7 = new Event(getResources().getColor(R.color.black),c[6].getTimeInMillis(), "Hari Raya Haji");
            compactCalendarView.addEvent(event7, true);

            Event event8 = new Event(getResources().getColor(R.color.black),c[7].getTimeInMillis(), "Malaysia Day");
            compactCalendarView.addEvent(event8, true);

            Event event9 = new Event(getResources().getColor(R.color.black),c[8].getTimeInMillis(), "Christmas");
            compactCalendarView.addEvent(event9, true);

            for(int m=0; m<dateHoliday.length; m++)
            {
                c[m].add(Calendar.YEAR, 1);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dateCalendar.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        // Set to current day on resume to set calendar to latest day
        // toolbar.setTitle(dateFormatForMonth.format(new Date()));
    }



    private void loadEvents() {
        //addEvents(-1, -1);
        //addEvents(Calendar.DECEMBER, -1);
        //addEvents(Calendar.AUGUST, -1);

        int temp = 0;

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        taskDbHelper = new TaskDbHelper(getApplicationContext());
        sqLiteDatabase = taskDbHelper.getReadableDatabase();
        cursor = taskDbHelper.getTaskInformation(sqLiteDatabase);

        int[] facts = {BLUE};
        int fact;
        Random randomG = new Random();
        long timeInMilliseconds = 0;

        if(cursor.moveToFirst())
        {
            do {
                int randomNumber = randomG.nextInt(facts.length);
                fact = facts[randomNumber];

                String email,TaskSubject ,TaskType,TaskTitle,TaskDesc,TaskDate,TaskTime;

                email = cursor.getString(0);
                TaskSubject= cursor.getString(1);
                TaskType = cursor.getString(2);
                TaskTitle = cursor.getString(3);
                TaskDesc = cursor.getString(4);
                TaskDate = cursor.getString(5);
                TaskTime = cursor.getString(6);

                DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                Date startDate;

                try {
                    startDate = df.parse(TaskDate);
                    timeInMilliseconds = startDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(email.equalsIgnoreCase(userID)) {
                    Event event = new Event(fact, timeInMilliseconds, TaskTitle);
                    compactCalendarView.addEvent(event, true);

                }

            }while (cursor.moveToNext());


            //Toast.makeText(getApplicationContext(), "Wait a sec, calendar is loading ☺",Toast.LENGTH_LONG).show();
        }

        //You can create event like this by calling new Event() method
        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event entrance_survey = new Event(getResources().getColor(R.color.fuchsia),1473004800000L, "Proses Entrance Survey - 2 minggu");
        compactCalendarView.addEvent(entrance_survey,true);

        Event cuti_khas = new Event(getResources().getColor(R.color.fuchsia),1473609600000L,"Cuti Khas Perayaan - 1 Minggu");
        compactCalendarView.addEvent(cuti_khas,true);

        Event mid_sem = new Event(getResources().getColor(R.color.fuchsia),1480262400000L, "Cuti Pertengahan Semester - 1 Minggu");
        compactCalendarView.addEvent(mid_sem,true);

        Event sufo = new Event(getResources().getColor(R.color.fuchsia),1479052800000L,"Student Feedback Online (SuFO) - 6 Minggu");
        compactCalendarView.addEvent(sufo,true);

        Event exitSurvey = new Event(getResources().getColor(R.color.fuchsia),1481472000000L,"Proses Exit Survey - 2 minggu");
        compactCalendarView.addEvent(exitSurvey,true);

        Event cuti_ulangkaji = new Event(getResources().getColor(R.color.fuchsia),1482681600000L,"Cuti Ulangkaji");
        compactCalendarView.addEvent(cuti_ulangkaji,true);

        Event cuti_ulangkaji2 = new Event(getResources().getColor(R.color.fuchsia),1482768000000L,"Cuti Ulangkaji");
        compactCalendarView.addEvent(cuti_ulangkaji2,true);

        Event cuti_ulangkaji3 = new Event(getResources().getColor(R.color.fuchsia),1482854400000L,"Cuti Ulangkaji");
        compactCalendarView.addEvent(cuti_ulangkaji3,true);

        Event finalExam = new Event(getResources().getColor(R.color.fuchsia),1482940800000L,"Peperiksaan Akhir - 3 Minggu");
        compactCalendarView.addEvent(finalExam,true);

        Event ExamResult = new Event(getResources().getColor(R.color.fuchsia),1487260800000L,"Pengumuman Keputusan Peperiksaan Akhir");
        compactCalendarView.addEvent(ExamResult,true);

        Event ExamKhas = new Event(getResources().getColor(R.color.fuchsia),1488124800000L,"Peperiksaan Semester Khas - 3 Hari");
        compactCalendarView.addEvent(ExamKhas,true);

        Event cuti_sem = new Event(getResources().getColor(R.color.fuchsia),1485014400000L,"Cuti Semester - 7 Minggu");
        compactCalendarView.addEvent(cuti_sem,true);
    }


    /*private void logEventsByMonth(CompactCalendarView compactCalendarView) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        currentCalender.set(Calendar.MONTH, Calendar.AUGUST);
        List<String> dates = new ArrayList<>();
        for (Event e : compactCalendarView.getEventsForMonth(new Date())) {
            dates.add(dateFormatForDisplaying.format(e.getTimeInMillis()));
        }
        Log.d(TAG, "Events for Aug with simple date formatter: " + dates);
        Log.d(TAG, "Events for Aug month using default local and timezone: " + compactCalendarView.getEventsForMonth(currentCalender.getTime()));
    }*/

    // Adding events from 1 to 6 days
//    private void addEvents(int month, int year) {
//        currentCalender.setTime(new Date());
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
//        Date firstDayOfMonth = currentCalender.getTime();
//
//        //set date task
//        for (int i = 0; i < 6; i++) {
//
//            currentCalender.setTime(firstDayOfMonth);
//            if (month > -1) {
//                currentCalender.set(Calendar.MONTH, month);
//            }
//            if (year > -1) {
//                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
//                currentCalender.set(Calendar.YEAR, year);
//            }
//
//            //put task date onto calendar
//            currentCalender.add(Calendar.DATE, i);
//
//            //setToMidnight(currentCalender);
//            long timeInMillis = currentCalender.getTimeInMillis();
//
//            List<Event> events = getEvents(timeInMillis, i);
//            compactCalendarView.addEvents(events);
//
//            //compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(255, 255, 255, 255)), false);
//        }
//    }

    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)));
        } else if ( day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
            //new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
    }

    /*private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public void gotoToday() {
        // Set any date to navigate to particular date
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
    }*/


//<------------------ Calendar Coding ends here ---------------------------------->

    

    //<------------------ toggle button drawer ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch (item.getItemId())
        {

            case R.id.action_diploma:


                return true;//return true is must put at end to avoid unreachable statement
        }
        return super.onOptionsItemSelected(item);
    }
}