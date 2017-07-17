package com.example.syafiq.smartplanner.FakeClass;
//Created by syafiq on 29/12/2016.

public class FakeProvider {
    private String name="";
    private String title="";
    private String desc = "";
    private String date = "";
    private String comment="";
    private String postID="";


    public FakeProvider(String name, String title, String desc, String date, String comment,String postID) {
        this.name = name;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.comment = comment;
        this.postID = postID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
