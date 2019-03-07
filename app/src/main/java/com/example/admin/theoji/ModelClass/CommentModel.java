package com.example.admin.theoji.ModelClass;

public class CommentModel {

    private String post_id;
    private String firstname;
    private String cdescription;
    private String comment_date;


    public CommentModel(String post_id, String firstname, String cdescription, String comment_date) {
        this.post_id=post_id;
        this.cdescription=cdescription;
        this.firstname=firstname;
        this.comment_date=comment_date;

    }

    public String getFirstname() {
        return firstname;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getCdescription() {
        return cdescription;
    }

    public void setCdescription(String cdescription) {
        this.cdescription = cdescription;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


}