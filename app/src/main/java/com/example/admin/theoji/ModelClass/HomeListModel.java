package com.example.admin.theoji.ModelClass;

public class HomeListModel {

    private String name;
    private String email;
    private String content;
    private String userimg;
    private String postimg;
    private String date;
    private  String post_id;
    private String firstname;

    public HomeListModel(String post_id, String name, String email, String date, String content, String userimg, String postimg, String firstname) {

        this.name = name;
        this.email = email;
        this.content=content;
        this.post_id=post_id;
        this.postimg=postimg;
        this.userimg = userimg;
        this.date = date;
        this.firstname=firstname;


    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getUserimg() {
        return userimg;
    }

    public String getPostimg() {
        return postimg;
    }

    public void setPostimg(String postimg) {
        this.postimg = postimg;
    }

    public void setUserimg(String userimg) {

        this.userimg = userimg;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
