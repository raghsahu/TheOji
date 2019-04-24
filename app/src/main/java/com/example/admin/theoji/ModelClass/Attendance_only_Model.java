package com.example.admin.theoji.ModelClass;

public class Attendance_only_Model {
    private String teacher_name;
    private String date;
    private String status;

    public Attendance_only_Model(String teacher_name, String date, String status) {

        this.teacher_name=teacher_name;
        this.date=date;
        this.status=status;

    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}

