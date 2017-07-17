package com.example.syafiq.smartplanner.FakeClass;
//Created by syafiq on 10/12/2016.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.syafiq.smartplanner.R;

import java.util.ArrayList;
import java.util.List;

public class ListFakeProvider extends ArrayAdapter {

    ArrayList list = new ArrayList();
    public ListFakeProvider(Context context, int resource){
        super(context,resource);
    }

    static class LayoutHandler{
        TextView username,title,desc,date,countcomment,posID;
        TextView clickItem;
    }
    public void add(Object object){
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public List getList()
    {
        return list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_fake_row,parent,false);
            layoutHandler= new LayoutHandler();
            layoutHandler.title = (TextView)row.findViewById(R.id.tvBlogTitle);
            layoutHandler.username = (TextView)row.findViewById(R.id.tvBlogUsername);
            layoutHandler.desc = (TextView)row.findViewById(R.id.tvBlogDescription);
            layoutHandler.date = (TextView)row.findViewById(R.id.tvBlogDate);
            layoutHandler.countcomment = (TextView)row.findViewById(R.id.tvCountComment);
            layoutHandler.posID = (TextView)row.findViewById(R.id.tvPosID);
            layoutHandler.clickItem = (TextView)row.findViewById(R.id.tvBlogButton) ;
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)row.getTag();

        }
        FakeProvider fakeProvider = (FakeProvider)this.getItem(position);
        layoutHandler.title.setText(fakeProvider.getTitle());
        layoutHandler.username.setText(fakeProvider.getName());
        layoutHandler.desc.setText(fakeProvider.getDesc());
        layoutHandler.date.setText(fakeProvider.getDate());
        layoutHandler.countcomment.setText(fakeProvider.getComment());
        layoutHandler.posID.setText(fakeProvider.getPostID());
        return row;
    }

    public void setFilter(ArrayList<FakeProvider> newList){
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
