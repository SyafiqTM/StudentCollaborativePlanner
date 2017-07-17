package com.example.syafiq.smartplanner.Comment;
//Created by syafiq on 31/12/2016.


public class CommentProvider {
    private String post_id;
    private String email;
    private String comment;

    public CommentProvider(String post_id, String email, String comment) {
        this.post_id = post_id;
        this.email = email;
        this.comment = comment;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
