package com.example.admin.theoji.ModelClass;

public class AnnualListModel {
    private String sf_id;
    private String sf_school_id;
    private String sf_school_class;
    private String sf_annualfess;
    private String sf_date;
    private String sf_status;



    public AnnualListModel(String sf_id, String sf_school_id, String sf_school_class, String sf_annualfess, String sf_date,
                           String sf_status) {


        this.sf_id=sf_id;
        this.sf_school_id=sf_school_id;
        this.sf_school_class=sf_school_class;
        this.sf_annualfess=sf_annualfess;
        this.sf_date=sf_date;
        this.sf_status=sf_status;
    }

    public String getSf_school_id() {
        return sf_school_id;
    }

    public void setSf_school_id(String sf_school_id) {
        this.sf_school_id = sf_school_id;
    }

    public String getSf_status() {
        return sf_status;
    }

    public void setSf_status(String sf_status) {
        this.sf_status = sf_status;
    }

    public String getSf_school_class() {
        return sf_school_class;
    }

    public String getSf_date() {
        return sf_date;
    }

    public void setSf_date(String sf_date) {
        this.sf_date = sf_date;
    }

    public String getSf_annualfess() {
        return sf_annualfess;
    }

    public void setSf_annualfess(String sf_annualfess) {
        this.sf_annualfess = sf_annualfess;
    }

    public void setSf_school_class(String sf_school_class) {
        this.sf_school_class = sf_school_class;
    }

    public String getSf_id() {
        return sf_id;
    }

    public void setSf_id(String sf_id) {
        this.sf_id = sf_id;
    }
}
