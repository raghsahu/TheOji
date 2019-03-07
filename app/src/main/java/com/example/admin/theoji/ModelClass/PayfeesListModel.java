package com.example.admin.theoji.ModelClass;

public class PayfeesListModel {
    private String f_student_id;
    private String f_payfess;
    private String f_feespaydate;
    private String umeta_value;
    private String firstname;
    private String sf_annualfess;

    public PayfeesListModel(String f_student_id, String f_payfess, String f_feespaydate, String umeta_value, String firstname, String sf_annualfess) {

        this.f_student_id=f_student_id;
        this.f_payfess=f_payfess;
        this.firstname=firstname;
        this.f_feespaydate=f_feespaydate;
        this.umeta_value=umeta_value;
        this.sf_annualfess=sf_annualfess;


    }

    public String getSf_annualfess() {
        return sf_annualfess;
    }

    public void setSf_annualfess(String sf_annualfess) {
        this.sf_annualfess = sf_annualfess;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUmeta_value() {
        return umeta_value;
    }

    public void setUmeta_value(String umeta_value) {
        this.umeta_value = umeta_value;
    }

    public String getF_feespaydate() {
        return f_feespaydate;
    }

    public void setF_feespaydate(String f_feespaydate) {
        this.f_feespaydate = f_feespaydate;
    }

    public String getF_payfess() {
        return f_payfess;
    }

    public void setF_payfess(String f_payfess) {
        this.f_payfess = f_payfess;
    }

    public String getF_student_id() {
        return f_student_id;
    }

    public void setF_student_id(String f_student_id) {
        this.f_student_id = f_student_id;
    }
}
