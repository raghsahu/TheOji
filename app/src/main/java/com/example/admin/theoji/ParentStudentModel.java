package com.example.admin.theoji;

public class ParentStudentModel {
    private String user_id;
    private String firstname;


    public ParentStudentModel(String user_id, String firstname) {
        this.user_id=user_id;
        this.firstname=firstname;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
