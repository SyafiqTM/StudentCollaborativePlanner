package com.example.syafiq.smartplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syafiq.smartplanner.database.DataProvider;
import com.example.syafiq.smartplanner.database.ListDataAdapter;
import com.example.syafiq.smartplanner.database.UserDbHelper;

import java.util.HashMap;
import java.util.Map;

import de.timroes.android.listview.EnhancedListView;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class ViewSubjects extends AppCompatActivity {

    private ActionBarDrawerToggle mToggle;
    SQLiteDatabase sqLiteDatabase;
    UserDbHelper userDbHelper;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsubjects);

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


        //<------------------ sqlite database ---------------------------------->//
        EnhancedListView listView = (EnhancedListView)findViewById(R.id.list_view);
        final ListDataAdapter listDataAdapter = new ListDataAdapter(getApplicationContext(),R.layout.row_layout);
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

                if(email.equalsIgnoreCase(userID))
                {
                    DataProvider dataProvider = new DataProvider(subname,subcode,subgroup,classroom,lec_name,lec_contact);
                    listDataAdapter.add(dataProvider);
                    listView.setAdapter(listDataAdapter);
                }

            }while (cursor.moveToNext());
        }
        //<------------------ sqlite database ---------------------------------->//

        //Toast.makeText(this, "size array : " + listDataAdapter.getCount(), Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvSubjectName = (TextView)view.findViewById(R.id.subjectName);
                TextView tvSubjectCode = (TextView)view.findViewById(R.id.subjectCode);

                final Bundle bundle = new Bundle();
                bundle.putString("subject_name",tvSubjectName.getText().toString());
                bundle.putString("subject_code",tvSubjectCode.getText().toString());

                Intent intent = new Intent(ViewSubjects.this,ExtendViewSubject.class);
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


                TextView tv = (TextView) arg1.findViewById(R.id.subjectName);
                String value = tv.getText().toString();

                userDbHelper = new UserDbHelper(getApplicationContext());
                sqLiteDatabase = userDbHelper.getReadableDatabase();
                cursor = userDbHelper.getInformation(sqLiteDatabase);
                String email2 = null,subjectName2 = null,subjectCode2 = null,subjectClass2 = null;

                if(cursor.moveToFirst())
                {
                    do {
                        String email,subjectName,subjectCode,subjectClass;
                        email = cursor.getString(0);
                        subjectName = cursor.getString(1);
                        subjectCode = cursor.getString(2);
                        subjectClass = cursor.getString(3);


                        if(value.equalsIgnoreCase(subjectName))
                        {
                            email2 = cursor.getString(0);
                            subjectName2 = cursor.getString(1);
                            subjectCode2 = cursor.getString(2);
                            subjectClass2 = cursor.getString(3);

                        }

                    }while (cursor.moveToNext());
                }

                final String finalEventTitle = subjectName2;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewSubjects.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this subject?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.icon_delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        userDbHelper = new UserDbHelper(getApplicationContext());
                        sqLiteDatabase = userDbHelper.getReadableDatabase();
                        userDbHelper.deleteSubjectInformation(finalEventTitle,sqLiteDatabase);
                        Toast.makeText(ViewSubjects.this, "Subject Deleted", Toast.LENGTH_LONG).show();

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

                final DataProvider dataProvider = (DataProvider)  listDataAdapter.getItem(i);
                // Remove the item from the adapter


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewSubjects.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this subject?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.icon_delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        userDbHelper = new UserDbHelper(getApplicationContext());
                        sqLiteDatabase = userDbHelper.getReadableDatabase();
                        userDbHelper.deleteSubjectInformation(dataProvider.getSubjectName(),sqLiteDatabase);
                        Toast.makeText(ViewSubjects.this, "Subject Deleted", Toast.LENGTH_LONG).show();

                        listDataAdapter.remove(i);
                        listDataAdapter.notifyDataSetChanged();
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




        com.getbase.floatingactionbutton.FloatingActionButton iconAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.imgAdd);

        //<------------------ Set on click listener for icon add  ---------------------------------->
        iconAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ViewSubjects activity
                Intent intent = new Intent(ViewSubjects.this, CreateSubjects.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }



    //<------------------ Toggle option drawer ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
