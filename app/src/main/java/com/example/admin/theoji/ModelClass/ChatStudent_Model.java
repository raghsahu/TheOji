package com.example.admin.theoji.ModelClass;

public class ChatStudent_Model {
    private String user_id;
    private String firstname;
    private String online_status;
    private String umeta_value;



    public ChatStudent_Model(String user_id, String firstname, String online_status, String umeta_value) {
        this.firstname=firstname;
        this.user_id=user_id;
        this.online_status=online_status;
        this.umeta_value=umeta_value;
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

    public String getUmeta_value() {
        return umeta_value;
    }

    public void setUmeta_value(String umeta_value) {
        this.umeta_value = umeta_value;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }
}
