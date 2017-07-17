package com.example.syafiq.smartplanner.database;
//Created by syafiq on 10/12/2016.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.syafiq.smartplanner.R;

import java.util.ArrayList;

public class ListDataAdapter extends ArrayAdapter {

    ArrayList list = new ArrayList();
    public ListDataAdapter(Context context, int resource){
        super(context,resource);
    }

    static class LayoutHandler{
        TextView subName, subCode;
    }
    public void add(Object object){
        super.add(object);
        list.add(object);
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
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
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            layoutHandler= new LayoutHandler();
            layoutHandler.subName = (TextView)row.findViewById(R.id.subjectName);
            layoutHandler.subCode = (TextView)row.findViewById(R.id.subjectCode);
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)row.getTag();

        }
        DataProvider dataProvider = (DataProvider)this.getItem(position);
        layoutHandler.subName.setText(dataProvider.getSubjectName());
        layoutHandler.subCode.setText(dataProvider.getSubjectCode());
        return row;
    }



}
