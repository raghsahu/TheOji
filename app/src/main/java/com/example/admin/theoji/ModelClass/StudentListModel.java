package com.example.admin.theoji.ModelClass;

public class StudentListModel {

    private String user_id;
    private String email;
    private String firstname;
    private String address;
    private String mobileno;
    private String city;
    private String st_class;
    private String status;


    public StudentListModel(String user_id, String email, String firstname, String address, String mobileno, String city,
                            String st_class, String status) {
        this.user_id=user_id;
        this.email=email;
        this.firstname=firstname;
        this.address=address;
        this.mobileno=mobileno;
        this.city=city;
        this.st_class=st_class;
        this.status=status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
