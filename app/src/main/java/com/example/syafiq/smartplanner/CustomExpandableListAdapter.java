package com.example.syafiq.smartplanner;
//Created by syafiq on 9/1/2017.

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syafiq.smartplanner.database_topic.TopicDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.example.syafiq.smartplanner.LoginActivity.MyPREFERENCES;


public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHashMap = null;
    private final Set<Pair<Long, Long>> mCheckedItems = new HashSet<Pair<Long, Long>>();


    public CustomExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }


    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
            return listHashMap.get(listDataHeader.get(i)).size();

    }
/*

    @Override
    public int getChildrenCount(int i) {
//        return listHashMap.get(listDataHeader.get(i)).size();
    }
*/

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int listPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setText(listTitle);

        final CheckBox listTitleCheckBox = (CheckBox)convertView.findViewById(R.id.checkBoxGroup);


        //<------------------ Get user ID from shared preferences ---------------------------------->
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String username = prefs.getString("userID",null);


        listTitleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listTitleCheckBox.isChecked()) {
                    TopicDbHelper topicDbHelper = new TopicDbHelper(context);
                    SQLiteDatabase sqliteTopic = topicDbHelper.getReadableDatabase();
                    Cursor topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);

                    if(topicCursor.moveToFirst())
                    {
                        do {
                            String topicUsername,topicTask,topicTitle,topicTick;
                            topicUsername = topicCursor.getString(0);
                            topicTask = topicCursor.getString(1);
                            topicTitle = topicCursor.getString(2);
                            topicTick = topicCursor.getString(3);


                            if(topicUsername.equalsIgnoreCase(username))
                            {
                                if (topicTitle.equalsIgnoreCase(listTitle)){
                                    TopicDbHelper readtopicDbHelper = new TopicDbHelper(context);
                                    SQLiteDatabase sqLiteDatabase = readtopicDbHelper.getWritableDatabase();

                                    topicTick = "true";

                                    //<------------------ Insert Data into Sqlite Database ---------------------------------->
                                    readtopicDbHelper.updateTopicInformation(listTitle,username,topicTask,topicTitle,topicTick,sqLiteDatabase);
                                    Toast.makeText(context, "username :" + username + "\ntask :" + topicTask + "\ntopic :" + listTitle + "\ntick :" + topicTick, Toast.LENGTH_SHORT).show();
                                }

                            }

                        }while (topicCursor.moveToNext());
                    }
                }
                else {
                    TopicDbHelper topicDbHelper = new TopicDbHelper(context);
                    SQLiteDatabase sqliteTopic = topicDbHelper.getReadableDatabase();
                    Cursor topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);

                    if (topicCursor.moveToFirst()) {
                        do {
                            String topicUsername, topicTask, topicTitle, topicTick;
                            topicUsername = topicCursor.getString(0);
                            topicTask = topicCursor.getString(1);
                            topicTitle = topicCursor.getString(2);
                            topicTick = topicCursor.getString(3);


                            if (topicUsername.equalsIgnoreCase(username)) {
                                if (topicTitle.equalsIgnoreCase(listTitle)) {
                                    TopicDbHelper readtopicDbHelper = new TopicDbHelper(context);
                                    SQLiteDatabase sqLiteDatabase = readtopicDbHelper.getWritableDatabase();

                                    topicTick = "false";

                                    //<------------------ Insert Data into Sqlite Database ---------------------------------->
                                    readtopicDbHelper.updateTopicInformation(listTitle, username, topicTask, listTitle, topicTick, sqLiteDatabase);
                                    Toast.makeText(context, "username :" + username + "\ntask :" + topicTask + "\ntopic :" + topicTitle + "\ntick :" + topicTick, Toast.LENGTH_SHORT).show();
                                }

                            }

                        } while (topicCursor.moveToNext());
                    }
                }
            }
        });

        /*listTitleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do stuff
                if(!listTitleCheckBox.isChecked())
                {

                    TopicDbHelper topicDbHelper = new TopicDbHelper(context);
                    SQLiteDatabase sqliteTopic = topicDbHelper.getReadableDatabase();
                    Cursor topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);

                    if(topicCursor.moveToFirst())
                    {
                        do {
                            String topicUsername,topicTask,topicTitle,topicTick;
                            topicUsername = topicCursor.getString(0);
                            topicTask = topicCursor.getString(1);
                            topicTitle = topicCursor.getString(2);
                            topicTick = topicCursor.getString(3);


                            if(topicUsername.equalsIgnoreCase(username))
                            {
                                if (topicTitle.equalsIgnoreCase(listTitle)){
                                    TopicDbHelper readtopicDbHelper = new TopicDbHelper(context);
                                    SQLiteDatabase sqLiteDatabase = topicDbHelper.getWritableDatabase();

                                    topicTick = "true";

                                    //<------------------ Insert Data into Sqlite Database ---------------------------------->
                                    readtopicDbHelper.updateTopicInformation(listTitle,topicTask,username,topicTitle,topicTick,sqLiteDatabase);
                                    Toast.makeText(context, "username :" + username + "\ntask :" + topicTask + "\ntopic :" + topicTitle + "\ntick :" + topicTick, Toast.LENGTH_SHORT).show();
                                }

                            }

                        }while (topicCursor.moveToNext());
                    }

                    topicDbHelper.close();

                }
                if (!isChecked)
                {

                    TopicDbHelper topicDbHelper = new TopicDbHelper(context);
                    SQLiteDatabase sqliteTopic = topicDbHelper.getReadableDatabase();
                    Cursor topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);

                    if(topicCursor.moveToFirst()) {
                        do {
                            String topicUsername, topicTask, topicTitle, topicTick;
                            topicUsername = topicCursor.getString(0);
                            topicTask = topicCursor.getString(1);
                            topicTitle = topicCursor.getString(2);
                            topicTick = topicCursor.getString(3);


                            if (topicUsername.equalsIgnoreCase(username)) {
                                if (topicTitle.equalsIgnoreCase(listTitle)) {
                                    TopicDbHelper readtopicDbHelper = new TopicDbHelper(context);
                                    SQLiteDatabase sqLiteDatabase = topicDbHelper.getWritableDatabase();

                                    topicTick = "false";

                                    //<------------------ Insert Data into Sqlite Database ---------------------------------->
                                    readtopicDbHelper.updateTopicInformation(listTitle, topicTask, username, listTitle, topicTick, sqLiteDatabase);
                                    Toast.makeText(context, "username :" + username + "\ntask :" + topicTask + "\ntopic :" + topicTitle + "\ntick :" + topicTick, Toast.LENGTH_SHORT).show();
                                }

                            }

                        } while (topicCursor.moveToNext());

                        topicDbHelper.close();
                    }
                }
            }
        });*/


        TopicDbHelper topicDbHelper = new TopicDbHelper(context);
        SQLiteDatabase sqliteTopic = topicDbHelper.getReadableDatabase();
        Cursor topicCursor = topicDbHelper.getTopicInformation(sqliteTopic);

        if(topicCursor.moveToFirst())
        {
            do {
                String topicUsername,topicTask,topicTitle,topicTick;
                topicUsername = topicCursor.getString(0);
                topicTask = topicCursor.getString(1);
                topicTitle = topicCursor.getString(2);
                topicTick = topicCursor.getString(3);


                if(topicUsername.equalsIgnoreCase(username))
                {
                    if (topicTitle.equalsIgnoreCase(listTitle)){

                        if(topicTick.equalsIgnoreCase("true"))
                        {
                            listTitleCheckBox.setChecked(true);
                        }
                        else
                        {
                            listTitleCheckBox.setChecked(false);
                        }

                    }

                }

            }while (topicCursor.moveToNext());
        }


        // Set CheckUpdateListener for CheckBox (see below CheckUpdateListener class)
/*

        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBoxGroup);
        // add tag to remember groupId/childId
        final Long tag = Long.valueOf(getGroupId(listPosition));
        cb.setTag(tag);
        // set checked if groupId/childId in checked items
        cb.setChecked(mCheckedItems.contains(tag));
        // set OnClickListener to handle checked switches

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CheckBox cb = (CheckBox) view;
                final Pair<Long, Long> tag = (Pair<Long, Long>) view.getTag();
                if (cb.isChecked()) {
                    mCheckedItems.add(tag);
                } else {
                    mCheckedItems.remove(tag);
                }
            }
        });
*/

        return convertView;
    }




    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}