package com.example.syafiq.smartplanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.syafiq.smartplanner.phprequest.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;

public class SignupActivity extends AppCompatActivity {


    private Button _signupButton;
    private EditText registerEmail;
    private EditText registerPhone;
    private EditText registerPassword;
    private EditText registerUsername;
    private EditText registerProgramme;
    private EditText registerSession;

    CharSequence[] session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        TextView login = (TextView) findViewById(R.id.signIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        //<------------------ assign variables ---------------------------------->
        _signupButton = (Button)findViewById(R.id.signUp);
        registerEmail = (EditText)findViewById(R.id.etRegisterEmail);
        registerPhone = (EditText)findViewById(R.id.etRegisterPhone);
        registerPassword = (EditText)findViewById(R.id.etRegisterPassword);
        registerUsername = (EditText)findViewById(R.id.etRegisterUsername);

        registerSession = (EditText) findViewById(R.id.etRegisterSession);
        session = new CharSequence[0];
        session = new CharSequence[] {"December 2016 - April 2017"};
        registerSession.setText(session[0]);




//<---------------------------------- Choose Programme ---------------------------------->
        registerProgramme = (EditText)findViewById(R.id.etRegisterProgramme);
        final CharSequence[] programme_level = new CharSequence[] {"Diploma", "Degree"};

        registerProgramme.setText(programme_level[0]); //set text first then let user pick

        registerProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignupActivity.this);
                builder.setTitle("Choose Programme");
                builder.setItems(programme_level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        registerProgramme.setText(programme_level[which]);

                        if(which==0) {
                            session = new CharSequence[] {"December 2016 - April 2017"};
                            registerSession.setText(session[0]); //set text first then let user pick
                        }

                        else
                        {
                            session = new CharSequence[]{"September 2016 - January 2017"};
                            registerSession.setText(session[0]); //set text first then let user pick
                        }
                    }
                });
                builder.show();
            }
        });



//<---------------------------------- Choose Session ---------------------------------->


            registerSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignupActivity.this);
                    builder.setTitle("Choose Session");
                    builder.setItems(session, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            registerSession.setText(session[which]);
                        }
                    });
                    builder.show();
                }
            });






        //<------------------ Set on click listener for Sign Up Button ---------------------------------->
        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // <-------- call method input validation ------>
                if (!validate()) {
                    onSignUpFailed();
                    return;
                }

                final String email = registerEmail.getText().toString();

                final String phoneNo = registerPhone.getText().toString();
                final String password = registerPassword.getText().toString();
                final String username = registerUsername.getText().toString();
                final String programme = registerProgramme.getText().toString();
                final String session = registerSession.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            /*JSONArray jsonResponse = new JSONArray(response);
                            JSONObject jsonObject = jsonResponse.getJSONObject(0);
                            boolean success = jsonObject.getBoolean("success");*/
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {


                                //<------------------ Set Preferences Value to Send Into Storage ---------------------------------->
                                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                                editor.putString("userID",email);
                                editor.apply();

                                final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Signing Up...");
                                progressDialog.show();

                                // TODO: Implement your own authentication logic here.
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                // On complete call either onLoginSuccess or onLoginFailed
                                                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                                SignupActivity.this.startActivity(intent);

                                                Context context = getApplicationContext();
                                                CharSequence text = "welcome " + email;
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                                finish();

                                                // onLoginFailed();
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("Account already exists!")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username,password,email,phoneNo,programme,session, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(registerRequest);
            }
        });
    }


    // <------------------ Move task back to Login Screen ---------------------------------->
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    //<------------------ on Singup fail ---------------------------------->
    public void onSignUpFailed() {
        //Toast.makeText(getBaseContext(), "Please Filled Up Empty Fields!", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }


    //<------------------Input Validation ---------------------------------->
    public boolean validate() {
        boolean valid = true;

        String username = registerUsername.getText().toString();
        String email = registerEmail.getText().toString();
        String phoneNo = registerPhone.getText().toString();
        String password = registerPassword.getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}";
        String patternphone = "^(?=.*[0-9]).{10,}$";


//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])  # a special character must occur at least once
//        (?=\S+$)          # no whitespace allowed in the entire string
//        .{8,}             # anything, at least eight places though
//        $                 # end-of-string

        if(username.isEmpty())
        {
            registerUsername.setError("Username cannot be empty");
            valid = false;
        }
        else{
            registerUsername.setError(null);
        }

        //<------------------Check email ---------------------------------->
        if (email.isEmpty()) {
            registerEmail.setError("Email cannot be empty");
            valid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError("Enter a valid email address");
            valid = false;
        }
        else {
            registerEmail.setError(null);
        }

        //<------------------Check Phone No ---------------------------------->
        if (phoneNo.isEmpty()) {
            registerPhone.setError("Phone Number cannot be empty");
            valid = false;
        }
        else if (!phoneNo.matches(patternphone)){
           registerPhone.setError("Phone number must at least have 10 digits");
           valid = false;
        }
        else {
            registerPhone.setError(null);
        }

        //<------------------Check Password ---------------------------------->
        if (password.isEmpty()) {
            registerPassword.setError("Password cannot be empty");
            valid = false;
        }
        else if (!password.matches(pattern)){
            registerPassword.setError("Must at be least 6 characters with an upper case & contain digit");
            registerPassword.setText("");
            valid = false;
        } else {
            registerPassword.setError(null);
        }


        return valid;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
