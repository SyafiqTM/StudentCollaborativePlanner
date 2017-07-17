package com.example.syafiq.smartplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.syafiq.smartplanner.phprequest.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //<------------------ declare variables ---------------------------------->
    private static final int REQUEST_SIGNUP = 0;

    private Button _loginButton;

    @Bind(R.id.StudentEmail) EditText _emailText;
    @Bind(R.id.StudentPass) EditText _passwordText;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Email = "emailKey";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


       /* Window window = LoginActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(LoginActivity.this.getResources().getColor(R.color.black));*/




        //<------------------ support toolbar because NoActionBarActivity were applied ---------------------------------->
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //<------------------ declare variables ---------------------------------->
        _loginButton = (Button)findViewById(R.id.signIn);
        TextView _signupLink = (TextView) findViewById(R.id.signUp);


        //<------------------ Set on click listener for signup  ---------------------------------->
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        //<------------------ Set on click listener for login  ---------------------------------->
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String email = _emailText.getText().toString();
                final String password = _passwordText.getText().toString();

                if (!validate()) {
                    onLoginFailed();
                    return;
                }


                //<------------------ Response Listener ---------------------------------->
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success;
                            success = jsonResponse.getBoolean("success");

                            if(success)
                            {

                                final String username = jsonResponse.getString("username"); //get username from jsonresponse
                                //<------------------ Set Preferences Value to Send Into Storage ---------------------------------->
                                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                                editor.putString("userID",username);
                                editor.apply();


                                //final String email = _emailText.getText().toString(); // get email string from edit text email
                                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Authenticating...");
                                progressDialog.show();

                                // TODO: Implement your own authentication logic here.
                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                // On complete call either onLoginSuccess or onLoginFailed
                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                LoginActivity.this.startActivity(intent);

                                                Context context = getApplicationContext();
                                                CharSequence text = "welcome " + username;
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                                finish();

                                                // onLoginFailed();
                                                progressDialog.dismiss();
                                            }
                                        }, 3000);
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Email or Password is Incorrect!")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };

                //send email and password key to login request class for processing
                LoginRequest loginRequest = new LoginRequest(email,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this); //method to handle login request
                queue.add(loginRequest);

            }
        });
    }//end of onCreate method

    //<------------------ Disable going back to the Splash Screen ---------------------------------->
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //<------------------Login Fail Message ---------------------------------->
    public void onLoginFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true); // set availability of login button if user fail to loginn
    }

    //<------------------Input Validation ---------------------------------->
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,10}";

        if (email.isEmpty()) {
            _emailText.setError("Email cannot be empty");
            valid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailText.setError("Enter a valid email address");
            valid = false;
        }
        else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Password cannot be empty");
            valid = false;
        }
        else if (!password.matches(pattern)){
            _passwordText.setError("Must at be least 6-10 characters with an upper case & contain digit");
            valid = false;
            _passwordText.setText("");
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
