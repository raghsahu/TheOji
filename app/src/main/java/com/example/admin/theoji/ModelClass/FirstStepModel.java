package com.example.admin.theoji.ModelClass;

import java.io.Serializable;

public class FirstStepModel implements Serializable {
    public String student_email;
    public String student_password;
    public String student_name;
    public String student_pre_school_name;
    public String student_dob;
    public String student_aadhar;
    public String student_category;
    public String student_bank_account;
    public String student_bank_name;
    public String student_sex;



    public FirstStepModel(String student_email, String student_password, String student_name, String student_pre_school_name,
                          String student_dob, String student_aadhar, String student_category, String student_bank_account,
                          String student_bank_name, String student_sex) {
    }

    public String getStudent_email() {
        return this.student_email;
    }

    public String getStudent_category() {
        return this.student_category;
    }

    public void setStudent_category(String student_category) {
        this.student_category = student_category;
    }

    public String getStudent_aadhar() {
        return this.student_aadhar;
    }

    public void setStudent_aadhar(String student_aadhar) {
        this.student_aadhar = student_aadhar;
    }

    public String getStudent_dob() {
        return this.student_dob;
    }

    public void setStudent_dob(String student_dob) {
        this.student_dob = student_dob;
    }

    public String getStudent_pre_school_name() {
        return this.student_pre_school_name;
    }

    public void setStudent_pre_school_name(String student_pre_school_name) {
        this.student_pre_school_name = student_pre_school_name;
    }

    public String getStudent_name() {
        return this.student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_password() {
        return this.student_password;
    }

    public String getStudent_bank_account() {
        return this.student_bank_account;
    }

    public String getStudent_sex() {
        return this.student_sex;
    }

    public void setStudent_sex(String student_sex) {
        this.student_sex = student_sex;
    }

    public String getStudent_bank_name() {
        return this.student_bank_name;
    }

    public void setStudent_bank_name(String student_bank_name) {
        this.student_bank_name = student_bank_name;
    }

    public void setStudent_bank_account(String student_bank_account) {
        this.student_bank_account = student_bank_account;
    }

    public void setStudent_password(String student_password) {
        this.student_password = student_password;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }
}
