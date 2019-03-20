package com.example.admin.theoji.ModelClass;

public class AttendanceListModel {
    private String date;
    private String attendance_id;
    private String section_name;
    private String class_name;
    private String remark;
    private String teacher_name;
    private String student_all;
    private String present;
    private String absent;


    public AttendanceListModel(String date, String attendance_id, String section_name, String class_name, String remark,
                               String teacher_name, String student_all, String present, String absent) {

        this.date=date;
        this.attendance_id=attendance_id;
        this.section_name=section_name;
        this.class_name=class_name;
        this.remark=remark;
        this.teacher_name=teacher_name;
        this.present=present;
        this.student_all=student_all;
        this.absent=absent;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public String getStudent_all() {
        return student_all;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public void setStudent_all(String student_all) {
        this.student_all = student_all;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }
}
