package com.example.syafiq.smartplanner;
//Created by syafiq on 4/1/2017.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.eralp.circleprogressview.ProgressAnimationListener;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class SortDateList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_date_list);
        List<Date> dateList = new ArrayList<Date>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            dateList.add(sdf.parse("01/12/2013"));
            dateList.add(sdf.parse("02/11/2013"));
            dateList.add(sdf.parse("03/12/2013"));
            dateList.add(sdf.parse("04/10/2013"));
            dateList.add(sdf.parse("05/12/2013"));
            dateList.add(sdf.parse("06/12/2013"));
            dateList.add(new Date());

            Collections.sort(dateList);
            Comparator<Date> cmp = new Comparator<Date>() {

                @Override
                public int compare(Date o1, Date o2) {
                    if(o1.getTime() > o2.getTime()) return 1;
                    else if(o1.getTime() < o2.getTime()) return -1;
                    else return 0;
                }
            };
            Toast.makeText(this, "date : "+dateList, Toast.LENGTH_SHORT).show();
            Collator collator = Collator.getInstance();
            List<Date>  out_of_date_list = new ArrayList<Date>();
            for(Iterator<Date> i = dateList.iterator(); i.hasNext(); ){
                Date date = i.next();
                System.out.println(">>> date = "+sdf.format(date));
                Toast.makeText(this, ">>> date = " + sdf.format(date), Toast.LENGTH_SHORT).show();
                System.out.println(">>> out of date "+cmp.compare(new Date(), date));
                if(cmp.compare(date,new Date())<0){
                    out_of_date_list.add(date);
                }
            }
            System.out.println(">>> print all out of date ");
            for(Date date : out_of_date_list){
                System.out.println(">>> date = "+sdf.format(date));

            }
        } catch (ParseException e) {// TODO Auto-generated catch block
            e.printStackTrace();
        }

        CircleProgressView mCircleProgressView1 = (CircleProgressView) findViewById(R.id.circle_progress_view1);
        mCircleProgressView1.setProgressWithAnimation(65, 1500);


    }
}
