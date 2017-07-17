package com.example.syafiq.smartplanner;
//Created by syafiq on 5/1/2017.

import com.example.syafiq.smartplanner.database_task.TaskProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class CustomComparator implements Comparator<TaskProvider> {// may be it would be Model

    private String m = "";
    @Override
    public int compare(TaskProvider obj1, TaskProvider obj2) {
//        Date dateOne = new Date();
//        Date dateTwo = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        String date1 = obj1.getTaskDate();
//        String date2 = obj2.getTaskDate();
//        try {
//           dateOne = sdf.parse(date1);
//            dateTwo = sdf.parse(date2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return dateOne.compareTo(dateTwo);

        m = obj1.getTaskDate();
       return obj1.getTaskDate().compareTo(obj2.getTaskDate());
    }

    public String toString(){
        String str = "";
        str = "" + m;
        return str;
    }
}