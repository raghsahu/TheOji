package com.example.admin.theoji.ModelClass;

public class P_A_Model {

private String date;
private String teacher;
private String firstname;
private String pa_class;
private String section;

    public P_A_Model(String date, String teacher, String firstname, String pa_class, String section) {

        this.date=date;
        this.teacher=teacher;
        this.firstname=firstname;
        this.pa_class=pa_class;
        this.section=section;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPa_class() {
        return pa_class;
    }

    public void setPa_class(String pa_class) {
        this.pa_class = pa_class;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
