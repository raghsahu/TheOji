package com.example.admin.theoji.ModelClass;

public class ProjectListModel {
    private String name;
    private String email;
    private String content;
    private String userimg;
    private String postimg;
    private String date;
    private static String post_id;
    private String title;



    public ProjectListModel(String post_id, String name, String email, String date, String title, String content, String userimg, String postimg) {
        this.name = name;
        this.email = email;
        this.content=content;
        this.post_id=post_id;
        this.postimg=postimg;
        this.userimg = userimg;
        this.date = date;
        this.title=title;


    }

    public String getContent() {
        return content;
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

    public String getName() {
        return name;
    }

    public String getPostimg() {
        return postimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostimg(String postimg) {
        this.postimg = postimg;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public void setName(String name) {
        this.name = name;
    }



}
