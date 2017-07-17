package com.example.syafiq.smartplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;
//Created by syafiq on 12/11/2016.

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //<------------------ Check if the user has login ---------------------------------->
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String userID = prefs.getString("userID", null);

        if (userID != null) {
            int SPLASH_TIME_OUT = 4000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(homeIntent);
                    finish();

                    Context context = getApplicationContext();
                    CharSequence text = "welcome back " + userID;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

        else
        {
            int SPLASH_TIME_OUT = 4000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    }

}
