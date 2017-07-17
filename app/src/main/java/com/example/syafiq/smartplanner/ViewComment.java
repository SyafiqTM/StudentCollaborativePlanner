package com.example.syafiq.smartplanner;//Created by syafiq on 31/12/2016.

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syafiq.smartplanner.Comment.CommentProvider;
import com.example.syafiq.smartplanner.Comment.ListCommentProvider;
import com.example.syafiq.smartplanner.FakeClass.FakeProvider;
import com.example.syafiq.smartplanner.FakeClass.ListFakeProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class ViewComment extends AppCompatActivity {
    private static final String URL = "http://syafiqzahir1994.hol.es/insertComment.php";
    String JsonURL = "http://syafiqzahir1994.hol.es/comment_info.php";
    private static final String URLCOMMENT = "http://syafiqzahir1994.hol.es/number_of_comments.php";
    int count_comment=0;
    String string_count_comment="0";
    String blogtitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Bundle bundle = getIntent().getExtras();

        blogtitle = bundle.getString("BlogTitle");
        String blogusername = bundle.getString("BlogUsername");
        String blogdate = bundle.getString("BlogDate");
        String blogdesc = bundle.getString("BlogDesc");
        String commentNumber = bundle.getString("BlogCommentNumber");
        final String post_number = bundle.getString("PostID");


        TextView title = (TextView)findViewById(R.id.titleBlog);
        TextView username = (TextView)findViewById(R.id.usernameBlog);
        TextView date = (TextView)findViewById(R.id.blog_date);
        TextView comment_num = (TextView)findViewById(R.id.comment_number);
        TextView desc = (TextView)findViewById(R.id.blog_content);

        title.setText(blogtitle);
        username.setText(blogusername);
        date.setText(blogdate);
        desc.setText(blogdesc);
        comment_num.setText(commentNumber);


        //<------------------------------------------------ retrieve comment section -------------------------------------------------------------------------------->
        RequestQueue requestQueue = Volley.newRequestQueue(this); // Creates the Volley request queue

        final ListView listView = (ListView)findViewById(R.id.list_comment);
        final ListCommentProvider listCommentProvider= new ListCommentProvider(getApplicationContext(),R.layout.activity_comment_row); // edit here adapter
        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURL, new Response.Listener<JSONArray>() {
            String postID="";
            String email="";
            String comment = "";


            // Takes the response from the JSON request
            @Override
            public void onResponse(JSONArray response) {
                try {


                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        postID = jsonObject.getString("PostID");
                        email = jsonObject.getString("Email");
                        comment = jsonObject.getString("Comment");

                        if(postID.equalsIgnoreCase(post_number))
                        {
                            CommentProvider commentProvider= new CommentProvider(postID,email,comment);
                            listCommentProvider.add(commentProvider);
                            listView.setAdapter(listCommentProvider);
                            count_comment++;
                            string_count_comment = String.valueOf(count_comment);
                        }



                    }

                    // Adds the data string to the TextView "results"
                    onCall();

                }
                // Try and catch are included to handle any errors due to JSON
                catch (JSONException e) {
                    // If an error occurs, this prints the error to the log
                    e.printStackTrace();
                }
            }
        },


                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );

        requestQueue.add(arrayreq);// Adds the JSON array request "arrayreq" to the request queue


        //<----------------------------- calculate comments --------------------------------------->









        final EditText messageText=(EditText) findViewById(R.id.editMessage);
        messageText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (messageText.getRight() - messageText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        final ProgressDialog loading = ProgressDialog.show(ViewComment.this,"Please Wait...","Upload Comment...",false,false);
                        final String message = messageText.getText().toString();

                        Bundle bundle = getIntent().getExtras();
                        final String postID = bundle.getString("PostID");

                        //<------------------ Get user ID from shared preferences ---------------------------------->
                        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        final String userID = prefs.getString("userID", null);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        Toast.makeText(ViewComment.this,response,Toast.LENGTH_LONG).show();
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(getIntent());
                                        overridePendingTransition(0, 0);
                                        loading.dismiss();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ViewComment.this,error.toString(),Toast.LENGTH_LONG).show();
                                        loading.dismiss();
                                    }
                                }){
                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("postID", postID);
                                params.put("email", userID);
                                params.put("comment", message);
                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(ViewComment.this);
                        requestQueue.add(stringRequest);

                        //clear text message
                        messageText.setText("");

                        return true;
                    }
                }
                return false;
            }
        });


    }

    private void onCall() {


        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        StringRequest stringCommentRequest = new StringRequest(Request.Method.POST, URLCOMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog
//                                loading.dismiss();
                        System.out.println(response);
                        Toast.makeText(ViewComment.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Disimissing the progress dialog
//                                loading.dismiss();

                        Toast.makeText(ViewComment.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("username",userID);
                params.put("title", blogtitle);
                params.put("comments", string_count_comment);

                return params;
            }

        };

        RequestQueue commentQueue = Volley.newRequestQueue(ViewComment.this);
        commentQueue.add(stringCommentRequest);
    }


    //<------------------------------------------------ send comment request -------------------------------------------------------------------------------->
    public void SendData(View v)
    {
        final ProgressDialog loading = ProgressDialog.show(ViewComment.this,"Please Wait...","Upload Comment...",false,false);
        final EditText messageText=(EditText) findViewById(R.id.editMessage);
        final String message = messageText.getText().toString();

        Bundle bundle = getIntent().getExtras();
        final String postID = bundle.getString("PostID");

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Toast.makeText(ViewComment.this,response,Toast.LENGTH_LONG).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewComment.this,error.toString(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("postID", postID);
                params.put("email", userID);
                params.put("comment", message);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        //clear text message
        messageText.setText("");
    }
}
