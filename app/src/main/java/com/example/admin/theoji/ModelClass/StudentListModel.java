package com.example.admin.theoji.ModelClass;

public class StudentListModel {

    private static String user_id;
    private String email;
    private String firstname;
    private String address;
    private String mobileno;
    private String city;
    private String st_class;


    public StudentListModel(String user_id, String email, String firstname, String address, String mobileno, String city, String st_class) {
        this.user_id=user_id;
        this.email=email;
        this.firstname=firstname;
        this.address=address;
        this.mobileno=mobileno;
        this.city=city;
        this.st_class=st_class;
    }

    public static String getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getSt_class() {
        return st_class;
    }

    public void setSt_class(String st_class) {
        this.st_class = st_class;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void setUser_id(String user_id) {
        StudentListModel.user_id = user_id;
    }
}
