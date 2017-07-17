package com.example.syafiq.smartplanner;
//Created by syafiq on 29/12/2016.

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText txtdate;
    public DateDialog(View view){
        txtdate=(EditText)view;
    }




    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Calendar c;
        int year,month,day;
// Use the current date as the default date in the dialog
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);


    }

    int a , b , c;

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box
        a = day; b = month; c = year;

        //String date=day+"-"+(month+1)+"-"+year;

        String month_in_words= "";
        if(month==0)
        {
            month_in_words = "Jan";
        }

        else if (month==1)
        {
            month_in_words = "Feb";
        }

        else if (month==2)
        {
            month_in_words = "Mar";
        }
        else if (month==3)
        {
            month_in_words = "Apr";
        }
        else if (month==4)
        {
            month_in_words = "May";
        }
        else if (month==5)
        {
            month_in_words = "Jun";
        }
        else if (month==6)
        {
            month_in_words = "Jul";
        }
        else if (month==7)
        {
            month_in_words = "Aug";
        }
        else if (month==8)
        {
            month_in_words = "Sept";
        }
        else if (month==9)
        {
            month_in_words = "Oct";
        }
        else if (month==10)
        {
            month_in_words = "Nov";
        }
        else if (month==11)
        {
            month_in_words = "Dec";
        }

        String date=day+"-"+ month_in_words +"-"+year;
        txtdate.setText(date);

    }

    public int getDay(){
        return a;
    }

    public int getMonth(){
        return b;
    }

    public int getYear(){
        return c;
    }

}