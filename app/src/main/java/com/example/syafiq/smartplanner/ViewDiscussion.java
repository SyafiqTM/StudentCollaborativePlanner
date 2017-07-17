package com.example.syafiq.smartplanner;
//Created by syafiq on 29/12/2016.
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.syafiq.smartplanner.FakeClass.FakeProvider;
import com.example.syafiq.smartplanner.FakeClass.ListFakeProvider;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;


public class ViewDiscussion extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ActionBarDrawerToggle mToggle;
    TextView results;

    String JsonURL = "http://syafiqzahir1994.hol.es/blog_info.php";

    String data = "";

    RequestQueue requestQueue;
    ListFakeProvider listFakeProvider;
    ListView listView;
    ArrayList<FakeProvider> arrayList = new ArrayList<>();

    int count = 0;

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);

        onPreExecute();

        //<------------------ Add Drawer Layout ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_search);
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

        requestQueue = Volley.newRequestQueue(this); // Creates the Volley request queue


        listView = (ListView)findViewById(R.id.listViewFake);
        listFakeProvider= new ListFakeProvider(getApplicationContext(),R.layout.activity_fake_row);
        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURL, new Response.Listener<JSONArray>() {
            String name="";
            String title="";
            String desc = "";
            String date = "";
            String comment = "";
            String postID = "";



            // Takes the response from the JSON request
            @Override
            public void onResponse(JSONArray response) {
                try {


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);


                        name = jsonObject.getString("Name");
                        title = jsonObject.getString("Title");
                        desc = jsonObject.getString("Desc");
                        date = jsonObject.getString("Date");
                        comment = jsonObject.getString("Comments");
                        postID = jsonObject.getString("PostID");

                        arrayList.add(new FakeProvider(name,title,desc,date,comment,postID));
                        FakeProvider fakeProvider= new FakeProvider(name,title,desc,date,comment,postID);
                        listFakeProvider.add(fakeProvider);
                        listView.setAdapter(listFakeProvider);
                        onPostExecute();
                        count++;
                    }

                    // Adds the data string to the TextView "results"


                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    onPostExecute();
                    e.printStackTrace();
                }
            }
        },


                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        onPostExecute();
                        Log.e("Volley", "Error");
                    }
                }
        );

        requestQueue.add(arrayreq);// Adds the JSON array request "arrayreq" to the request queue



       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               TextView read = (TextView) view.findViewById(R.id.tvBlogButton);
               TextView tvBlogTitle = (TextView)view.findViewById(R.id.tvBlogTitle);
               TextView tvBlogUsername = (TextView)view.findViewById(R.id.tvBlogUsername);
               TextView tvBlogDesc = (TextView)view.findViewById(R.id.tvBlogDescription);
               TextView tvBlogDate = (TextView)view.findViewById(R.id.tvBlogDate);
               TextView comment = (TextView)view.findViewById(R.id.tvCountComment);
               TextView posID = (TextView)view.findViewById(R.id.tvPosID);

               final Bundle bundle = new Bundle();
               bundle.putString("BlogTitle",tvBlogTitle.getText().toString());
               bundle.putString("BlogUsername",tvBlogUsername.getText().toString());
               bundle.putString("BlogDesc", tvBlogDesc.getText().toString());
               bundle.putString("BlogDate",tvBlogDate.getText().toString());
               bundle.putString("BlogCommentNumber",comment.getText().toString());
               bundle.putString("PostID", posID.getText().toString());


               Intent intent = new Intent(ViewDiscussion.this, ViewComment.class);
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });



        //<------------------ button add discussion ---------------------------------->

        FloatingActionButton imageAdd = (FloatingActionButton)findViewById(R.id.imgAddDiscussion);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String parse_count = String.valueOf(count);

                Bundle pass_pos_id = new Bundle();

                pass_pos_id.putString("count_id",parse_count);

                Intent intent = new Intent(ViewDiscussion.this,CreateDiscussion.class);
                intent.putExtras(pass_pos_id);
                startActivity(intent);
            }
        });
    }


    protected void onPreExecute() {
        progressBar = new ProgressDialog(ViewDiscussion.this);
        progressBar.setMessage("Loading Discussions...");
        progressBar.show();
    }

    protected void onPostExecute() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_view_discussion,menu);

        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*//*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listFakeProvider.getFilter().filter(newText);
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });*/

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);



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

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<FakeProvider> newList = new ArrayList<>();
        for (FakeProvider fakeprovide : arrayList)
        {
            String name = fakeprovide.getTitle().toLowerCase();
            if(name.contains(newText))
            {
                newList.add(fakeprovide);
            }
        }
        listFakeProvider.setFilter(newList);
        return false;
    }
}