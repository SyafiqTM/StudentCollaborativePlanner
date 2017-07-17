package com.example.syafiq.smartplanner.database_task;
//Created by syafiq on 29/12/2016.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.example.syafiq.smartplanner.CustomComparator;
import com.example.syafiq.smartplanner.R;
import com.example.syafiq.smartplanner.database.DataProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.R.id.progress;

public class ListTaskAdapter extends ArrayAdapter {

    private ArrayList<String> date;
    private ArrayList<Date> dateList = new ArrayList<Date>();

    ArrayList list = new ArrayList();
    private int count=0;

    public ListTaskAdapter(Context context, int resource){
        super(context,resource);
    }

    static class LayoutHandler{
        TextView taskTitle,taskSubject,taskType, taskDuedate, taskDuetime;
        CircleProgressView circleProgressView;
    }
    public void add(Object object){
        super.add(object);
        list.add(object);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void clear()
    {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_task_layout,parent,false);
            layoutHandler= new LayoutHandler();
            layoutHandler.taskTitle = (TextView)row.findViewById(R.id.tvTitleTask);
            layoutHandler.taskSubject = (TextView)row.findViewById(R.id.tvTaskSubject);
            layoutHandler.taskType = (TextView)row.findViewById(R.id.tvTaskType);
            layoutHandler.taskDuedate = (TextView)row.findViewById(R.id.tvTaskDate);
            layoutHandler.taskDuetime = (TextView)row.findViewById(R.id.tvTaskTime);
            layoutHandler.circleProgressView = (CircleProgressView)row.findViewById(R.id.circle_progress_view1);
            row.setTag(layoutHandler);

        }
        else
        {
            layoutHandler = (LayoutHandler)row.getTag();

        }
        TaskProvider taskProvider = (TaskProvider) this.getItem(position);
        layoutHandler.taskTitle.setText(taskProvider.getTaskTitle());
        layoutHandler.taskSubject.setText(taskProvider.getTaskSubject());
        layoutHandler.taskType.setText(taskProvider.getTaskType());
        layoutHandler.taskDuedate.setText(taskProvider.getTaskDate());
        layoutHandler.taskDuetime.setText(taskProvider.getTaskTime());
        String task_percentage = taskProvider.getTaskPercentage();
        Float percentage = Float.parseFloat(task_percentage);
        layoutHandler.circleProgressView.setProgressWithAnimation(percentage, 1500);
        return row;
    }



    public void sortListAscending() throws ParseException {



        /*for (String dateString : list.get(4) ) {

            try {
                dateList.add(sdf.parse(dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(dateList);*/
        Toast.makeText(getContext(), "Date : " + date, Toast.LENGTH_SHORT).show();
    }
}
