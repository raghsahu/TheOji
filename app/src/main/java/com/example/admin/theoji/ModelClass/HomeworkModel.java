package com.example.admin.theoji.ModelClass;

public class HomeworkModel {
    private String post_id;
    private String post_date;
    private String post_title;
    private String post_content;
    private String email;
    private String umeta_value;
   // private String participant;
    private String studentclass;
    private String actvitydate;
    private String imagename;
    private String firstname;
    private String student_alot;


    public HomeworkModel(String post_id, String post_date, String post_title, String post_content, String email, String umeta_value,
                         String studentclass, String actvitydate, String imagename, String firstname, String student_alot) {
        this.post_id=post_id;
        this.post_date=post_date;
        this.post_title=post_title;
        this.post_content=post_content;
        this.email=email;
        this.umeta_value=umeta_value;
       // this.participant=participant;
        this.studentclass=studentclass;
        this.actvitydate=actvitydate;
        this.imagename=imagename;
        this.firstname=firstname;
        this.student_alot=student_alot;


    }



    public String getImagename() {
        return imagename;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getStudentclass() {
        return studentclass;
    }

    public String getStudent_alot() {
        return student_alot;
    }

    public void setStudent_alot(String student_alot) {
        this.student_alot = student_alot;
    }

    public String getActvitydate() {
        return actvitydate;
    }

    public void setActvitydate(String actvitydate) {
        this.actvitydate = actvitydate;
    }

    public void setStudentclass(String studentclass) {
        this.studentclass = studentclass;
    }


    public String getUmeta_value() {
        return umeta_value;
    }

    public void setUmeta_value(String umeta_value) {
        this.umeta_value = umeta_value;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost_date() {
        return post_date;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
