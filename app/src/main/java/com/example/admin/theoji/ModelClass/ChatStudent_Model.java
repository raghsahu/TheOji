package com.example.admin.theoji.ModelClass;

public class ChatStudent_Model {
    private String user_id;
    private String firstname;
    private String lastname;



    public ChatStudent_Model(String user_id, String firstname, String lastname) {
        this.firstname=firstname;
        this.user_id=user_id;
        this.lastname=lastname;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
