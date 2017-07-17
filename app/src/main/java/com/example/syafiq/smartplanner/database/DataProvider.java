package com.example.syafiq.smartplanner.database;
//Created by syafiq on 10/12/2016.

public class DataProvider {
    private String email;
    private String subjectName ;
    private String subjectCode;
    private String subjectGroup;
    private String classroom;
    private String lectureName;
    private String lectureContact;


    public DataProvider(String subjectName, String subjectCode, String subjectGroup, String classroom, String lectureName, String lectureContact) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.subjectGroup = subjectGroup;
        this.classroom = classroom;
        this.lectureName = lectureName;
        this.lectureContact = lectureContact;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(String subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLectureContact() {
        return lectureContact;
    }

    public void setLectureContact(String lectureContact) {
        this.lectureContact = lectureContact;
    }
}
