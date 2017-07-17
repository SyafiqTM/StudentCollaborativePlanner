package com.example.syafiq.smartplanner.Comment;
//Created by syafiq on 31/12/2016.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.syafiq.smartplanner.R;

import java.util.ArrayList;

public class ListCommentProvider extends ArrayAdapter {

    ArrayList list = new ArrayList();
    public ListCommentProvider(Context context, int resource){
        super(context,resource);
    }

    static class LayoutHandler{
        TextView email,comment;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if(row==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_comment_row,parent,false);
            layoutHandler= new LayoutHandler();
            layoutHandler.email = (TextView)row.findViewById(R.id.commentEmail);
            layoutHandler.comment = (TextView)row.findViewById(R.id.commentDesc);
            row.setTag(layoutHandler);
        }
        else
        {
            layoutHandler = (LayoutHandler)row.getTag();

        }
        CommentProvider commentProvider = (CommentProvider) this.getItem(position);
        layoutHandler.email.setText(commentProvider.getEmail() + " wrote : ");
        layoutHandler.comment.setText(commentProvider.getComment());
        return row;
    }
}
