package com.example.syafiq.smartplanner.database_task;
//Created by syafiq on 29/12/2016.

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskProvider {
    private String email;
    private String TaskSubject ;
    private String TaskType;
    private String TaskTitle;
    private String TaskDesc;
    private String TaskDate;
    private String TaskTime;
    private String TaskPercentage;

    public TaskProvider(String taskSubject, String taskType, String taskTitle, String taskDesc, String taskDate, String taskTime, String taskPercentage) {
        this.email = email;
        TaskSubject = taskSubject;
        TaskType = taskType;
        TaskTitle = taskTitle;
        TaskDesc = taskDesc;
        TaskDate = taskDate;
        TaskTime = taskTime;
        TaskPercentage = taskPercentage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaskSubject() {
        return TaskSubject;
    }

    public void setTaskSubject(String taskSubject) {
        TaskSubject = taskSubject;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public String getTaskDesc() {
        return TaskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        TaskDesc = taskDesc;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public String getTaskTime() {
        return TaskTime;
    }

    public void setTaskTime(String taskTime) {
        TaskTime = taskTime;
    }

    public String getTaskPercentage() {
        return TaskPercentage;
    }

    public void setTaskPercentage(String taskPercentage) {
        TaskPercentage = taskPercentage;
    }
}

