package com.example.admin.theoji.ModelClass;

public class PayfeesDetailListModel {

    private String f_id;
    private String f_student_id;
    private String f_school_id;
    private String f_feespaydate;
    private String f_student_class;
    private String f_status;
    private String f_payfess;

    public PayfeesDetailListModel(String f_id, String f_student_id, String f_school_id, String f_feespaydate, String f_student_class,
                                  String f_status, String f_payfess) {

        this.f_id=f_id;
        this.f_student_id=f_student_id;
        this.f_school_id=f_school_id;
        this.f_feespaydate=f_feespaydate;
        this.f_student_class=f_student_class;
        this.f_status=f_status;
        this.f_payfess=f_payfess;

    }

    public String getF_id() {
        return f_id;
    }

    public String getF_student_id() {
        return f_student_id;
    }

    public String getF_payfess() {
        return f_payfess;
    }

    public void setF_payfess(String f_payfess) {
        this.f_payfess = f_payfess;
    }

    public String getF_status() {
        return f_status;
    }

    public void setF_status(String f_status) {
        this.f_status = f_status;
    }

    public String getF_student_class() {
        return f_student_class;
    }

    public void setF_student_class(String f_student_class) {
        this.f_student_class = f_student_class;
    }

    public String getF_feespaydate() {
        return f_feespaydate;
    }

    public void setF_feespaydate(String f_feespaydate) {
        this.f_feespaydate = f_feespaydate;
    }

    public String getF_school_id() {
        return f_school_id;
    }

    public void setF_school_id(String f_school_id) {
        this.f_school_id = f_school_id;
    }

    public void setF_student_id(String f_student_id) {
        this.f_student_id = f_student_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }
}
