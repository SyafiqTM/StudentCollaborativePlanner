package com.example.syafiq.smartplanner;
//Created by syafiq on 6/12/2016.

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class MyProfileActivity extends AppCompatActivity {
    public static final String TAG = MyProfileActivity.class.getSimpleName();

    public static final int RESULT_LOAD_IMAGE = 1;
    FloatingActionButton fab;
    private Bitmap bitmap;
    ImageView profile_image;

    ProgressDialog progressBar;
    private static final String JsonURL = "http://syafiqzahir1994.hol.es/upload_image.php";
    private static final String ProfileURL = "http://syafiqzahir1994.hol.es/fetch_profile.php";

    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        ButterKnife.bind(this);

        //<------------------ Add Drawer Layout ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerProfile);
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

        fab = (FloatingActionButton)findViewById(R.id.fabulous);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);

            }
        });

        profile_image = (ImageView)findViewById(R.id.profile_image);
        String url = "http://syafiqzahir1994.hol.es/uploads/" + userID +".jpg";
        url=url.replaceAll(" ", "%20");
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
              profile_image.setImageBitmap(response);

            }
        }, 0, 0, null, null);

        rq.add(ir);


        //<--------------------------- download plant information ----------------------------------->
        RequestQueue requestQueue = Volley.newRequestQueue(this); // Creates the Volley request queue

        JsonArrayRequest arrayreq = new JsonArrayRequest(ProfileURL, new Response.Listener<JSONArray>() {
            String student_name="";
            String student_email="";
            String student_phone="";

            // Takes the response from the JSON request
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        student_name = jsonObject.getString("username");
                        student_email = jsonObject.getString("email");
                        student_phone = jsonObject.getString("phoneNo");


                        if(student_name.equalsIgnoreCase(userID))
                        {
                            TextView textName = (TextView)findViewById(R.id.stdemail);
                            TextView textPhone = (TextView)findViewById(R.id.stdphone);

                            textName.setText(student_email);
                            textPhone.setText(student_phone);
                        }

                    }

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            // Handles errors that occur due to Volley
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error");
            }
        }
        );

        requestQueue.add(arrayreq);// Adds the JSON array request "arrayreq" to the request queue

     /*   // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        //<---------------------------------------------------->*/
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null)
        {
            Uri filePath = data.getData();
            Toast.makeText(this, "" + filePath, Toast.LENGTH_SHORT).show();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profile_image.setImageBitmap(bitmap);

                onPreExecute();

                //<------------------ Get user ID from shared preferences ---------------------------------->
                SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                final String userID = prefs.getString("userID", null);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, JsonURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Disimissing the progress dialog
//
                                System.out.println(response);
                                Toast.makeText(MyProfileActivity.this,response,Toast.LENGTH_LONG).show();
                                onPostExecute();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MyProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                onPostExecute();

                            }
                        }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        //Converting Bitmap to String
                        String image = getStringImage(bitmap);

                        params.put("profileImage",image);
                        params.put("username", userID);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(MyProfileActivity.this);
                requestQueue.add(stringRequest);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected void onPreExecute() {
        progressBar = new ProgressDialog(MyProfileActivity.this);
        progressBar.setMessage("Uploading Image......");
        progressBar.show();
    }

    protected void onPostExecute() {
        if (progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    //<------------------ toggle button drawer ---------------------------------->
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }*/
}
