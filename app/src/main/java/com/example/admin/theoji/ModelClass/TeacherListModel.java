package com.example.admin.theoji.ModelClass;

public class TeacherListModel {
    private String user_id;
    private String email;
    private String firstname;
    private String address;
    private String mobileno;
    private String umeta_value;

    public TeacherListModel(String user_id, String email, String firstname, String address, String mobileno, String umeta_value) {

        this.user_id=user_id;
        this.email=email;
        this.firstname=firstname;
        this.address=address;
        this.mobileno=mobileno;
        this.umeta_value=umeta_value;

    }

    public String getUmeta_value() {
        return umeta_value;
    }

    public void setUmeta_value(String umeta_value) {
        this.umeta_value = umeta_value;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
