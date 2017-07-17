package com.example.syafiq.smartplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class HomeActivity extends AppCompatActivity{

    private ActionBarDrawerToggle mToggle;
    private TextView tvMessage;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //<------------------ Activity Layout variables ---------------------------------->
        //Button mFactButton = (Button) findViewById(R.id.checkIDButton);

        ImageView imgSubject = (ImageView) findViewById(R.id.imgSubjects);
        TextView tvSubject = (TextView) findViewById(R.id.tvSubjects);

        TextView tvTask = (TextView)findViewById(R.id.tvTask);
        ImageView imgTask = (ImageView)findViewById(R.id.imgTask);

        TextView tvCalendar = (TextView)findViewById(R.id.tvCalendar);
        ImageView imgCalendar = (ImageView)findViewById(R.id.imgCalendar);

        TextView tvDiscuss = (TextView)findViewById(R.id.tvDiscussion);
        ImageView imgDiscuss = (ImageView)findViewById(R.id.imgDiscussion);

        //<------------------ Add Drawer Layout ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);



        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerHomepage);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        mToggle.syncState();

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        //<------------------ Get navigation (drawer) header ---------------------------------->
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_viewHome);
        View headerview = navigationView.getHeaderView(0);
        //GridView grid = (GridView)findViewById(R.id.Grid_id);


        final RelativeLayout header = (RelativeLayout)headerview.findViewById(R.id.header); //accessing header in relative layout (drawer_hearder)
        TextView email_header = (TextView)header.findViewById(R.id.user_email_header); //accessing text view from header
        email_header.setText(userID);

        //<------------------ Set On click listener for Drawer Header ---------------------------------->
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"My Account Selected",Toast.LENGTH_LONG).show();
                Intent drawer_start = new Intent(HomeActivity.this, MyProfileActivity.class);
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
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(),"Activity Home",Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(a);
                        break;

                    case R.id.nav_group:
                        Toast.makeText(getApplicationContext(),"Group Discussions",Toast.LENGTH_SHORT).show();
                        Intent e = new Intent(HomeActivity.this, ViewDiscussion.class);
                        startActivity(e);
                        break;

                    case R.id.nav_subject:
                        Toast.makeText(getApplicationContext(),"View Subjects",Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(HomeActivity.this, ViewSubjects.class);
                        startActivity(b);
                        break;

                    case R.id.nav_task:
                        Toast.makeText(getApplicationContext(),"View Tasks",Toast.LENGTH_SHORT).show();
                        Intent c = new Intent(HomeActivity.this, ViewTask.class);
                        startActivity(c);
                        break;

                    case R.id.nav_calendar:
                        Toast.makeText(getApplicationContext(),"Calendar Academic",Toast.LENGTH_SHORT).show();
                        Intent d = new Intent(HomeActivity.this, ViewCalendar.class);
                        startActivity(d);
                        break;

                }
                return true;
            }
        });





        //<------------------ Add Drawer Fragment ---------------------------------->


        imgDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,ViewDiscussion.class);
                startActivity(intent);
            }
        });

        tvDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,ViewDiscussion.class);
                startActivity(intent);
            }
        });


        //<------------------ Set on click listener for image view (subjects)  ---------------------------------->
        imgSubject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(HomeActivity.this, ViewSubjects.class);
                startActivity(intent);
            }
        });

        //<------------------ Set on click listener for text view (subjects)  ---------------------------------->
        tvSubject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(HomeActivity.this, ViewSubjects.class);
                startActivity(intent);
            }
        });


        //<------------------ Set on click listener for image view (task)  ---------------------------------->
        imgTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(HomeActivity.this, ViewTask.class);
                startActivity(intent);
            }
        });

        //<------------------ Set on click listener for text view (task)  ---------------------------------->
        tvTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(HomeActivity.this, ViewTask.class);
                startActivity(intent);
            }
        });

        //<------------------ Set on click listener for image view (calendar)  ---------------------------------->
        imgCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(HomeActivity.this, ViewCalendar.class);
                startActivity(intent);
            }
        });

        //<------------------ Set on click listener for text view (calendar)  ---------------------------------->
        tvCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(HomeActivity.this, ViewCalendar.class);
                startActivity(intent);
            }
        });

        //<------------------ Set on click listener for fun fact button  ---------------------------------->
       /* mFactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                *//*
                String[] facts = {
                        "Ants stretch when they wake up in the morning.",
                        "Ostriches can run faster than horses.",
                        "Olympic gold medals are actually made mostly of silver.",
                        "You are born with 300 bones; by the time you are an adult you will have 206.",
                        "It takes about 8 minutes for light from the Sun to reach Earth.",
                        "Some bamboo plants can grow almost a meter in just one day.",
                        "The state of Florida is bigger than England.",
                        "Some penguins can leap 2-3 meters out of the water.",
                        "On average, it takes 66 days to form a new habit.",
                        "Never Stop Learning, Because Life Never Stops Teaching",
                        "Mammoths still walked the earth when the Great Pyramid was being built." };

                String fact = "";
                Random randomG = new Random();
                int randomNumber = randomG.nextInt(facts.length);
                fact = facts[randomNumber];
                tvMessage.setText(fact);
                *//*

                Context context = getApplicationContext();
                CharSequence text = "email : "+ userID;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });*/
    }

    //<------------------ Disable going back to the Login Screen ---------------------------------->
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_welcome,menu);
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
            case R.id.action_signout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);

                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("userID");//remove current session
                editor.apply();//apply changes

                return true;//return true is must put at end to avoid unreachable statement
        }
        return super.onOptionsItemSelected(item);
    }
}