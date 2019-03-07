package com.example.admin.theoji.ModelClass;

public class AboutListModel {
    private String ad_id;
    private String ad_description;
    private String ad_date;
    private String ad_title;
    private String user_id;


    public AboutListModel(String ad_id, String ad_description, String ad_date, String ad_title, String user_id) {

        this.ad_date=ad_date;
        this.ad_description=ad_description;
        this.ad_id=ad_id;
        this.ad_title=ad_title;
        this.user_id=user_id;

    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_date() {
        return ad_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public void setAd_date(String ad_date) {
        this.ad_date = ad_date;
    }

    public String getAd_description() {
        return ad_description;
    }

    public void setAd_description(String ad_description) {
        this.ad_description = ad_description;
    }
}
