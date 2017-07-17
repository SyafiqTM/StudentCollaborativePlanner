package com.example.syafiq.smartplanner;//Created by syafiq on 1/1/2017.

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syafiq.smartplanner.database_task.TaskDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class CreateDiscussion extends AppCompatActivity {

    private static final String URL = "http://syafiqzahir1994.hol.es/create_discussion.php";


    EditText discussion_title, discussion_desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discussion);
    }

    //<------------------Input Validation ---------------------------------->
    public boolean validate() {
        boolean valid = true;

        String title = discussion_title.getText().toString();
        String desc = discussion_desc.getText().toString();

        if (title.isEmpty()) {
            discussion_title.setError("Discussion title cannot be empty");
            valid = false;
        }
        else {
            discussion_title.setError(null);
        }


        if (desc.isEmpty()) {
            discussion_desc.setError("Discussion code cannot be empty");
            valid = false;
        }
        else {
            discussion_desc.setError(null);
        }

        return valid;
    }

    public void PostDiscussion()
    {

        Bundle pass_pos_id = getIntent().getExtras();

        String pos_id = pass_pos_id.getString("count_id");

        discussion_title =(EditText) findViewById(R.id.etDiscussionTitle);
        final String title = discussion_title.getText().toString();

        discussion_desc = (EditText)findViewById(R.id.etDiscussionDesc);
        final String content = discussion_desc.getText().toString();

        if(!validate())
        {
            return;
        }


        int add_count = Integer.parseInt(pos_id);
        int new_pos_id = add_count+1;

        final String new_post_id = String.valueOf(new_pos_id);
        final String comment = "0";

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");//dd/MM/yyyy
        Date now = new Date();
        final String date = sdfDate.format(now);


        Toast.makeText(this, "pos id  : " + new_post_id, Toast.LENGTH_SHORT).show();

        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Toast.makeText(CreateDiscussion.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateDiscussion.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();


                params.put("email", userID);
                params.put("title", title);
                params.put("content", content);
                params.put("date",date);
                params.put("comment", comment);
                params.put("postID", new_post_id);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        final ProgressDialog progressDialog = new ProgressDialog(CreateDiscussion.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Posting...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        Intent intent = new Intent(CreateDiscussion.this, ViewDiscussion.class);
                        CreateDiscussion.this.startActivity(intent);


                        finish();

                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
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

        switch (item.getItemId())
        {
            case R.id.action_save:

                PostDiscussion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
