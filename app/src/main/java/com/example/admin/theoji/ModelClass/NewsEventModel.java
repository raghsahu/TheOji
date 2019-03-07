package com.example.admin.theoji.ModelClass;

import java.io.Serializable;

public class NewsEventModel implements Serializable {

    private String name;
    private String email;
    private String content;
    private String userimg;
    private String postimg;
    private String title;
    private String date;
   private static String post_id1;

    public NewsEventModel(String post_id1, String name, String email, String title, String date, String content, String userimg, String postimg) {
        this.name = name;
        this.email = email;
        this.content=content;
        this.title=title;
        this.postimg=postimg;
        this.userimg = userimg;
        this.date = date;
        this.post_id1=post_id1;
    }

    public static String getPost_id1() {
        return post_id1;
    }

    public void setPost_id1(String post_id1) {
        this.post_id1 = post_id1;
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

    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
