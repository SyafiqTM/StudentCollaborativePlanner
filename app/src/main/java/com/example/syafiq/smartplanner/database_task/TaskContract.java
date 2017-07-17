package com.example.syafiq.smartplanner.database_task;
//Created by syafiq on 4/12/2016.

public class TaskContract {
    public static abstract class NewTaskInfo
    {
        public static final String USER_EMAIL = "task_email";
        public static final String TASK_SUBJECT = "task_subject";
        public static final String TASK_TYPE = "task_type";
        public static final String TASK_TITLE = "task_title";
        public static final String TASK_DESC = "task_description";
        public static final String TASK_DATE = "task_duedate";
        public static final String TASK_TIME = "task_duetime";
        public static final String TASK_PERCENTAGE = "task_percentage";
        public static final String TABLE_NAME = "task_info";
    }
}
