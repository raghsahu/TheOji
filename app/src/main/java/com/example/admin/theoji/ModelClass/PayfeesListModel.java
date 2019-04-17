package com.example.admin.theoji.ModelClass;

public class PayfeesListModel {
    private String f_id;
    private String admission_no;
    private String firstname;
    private String sf_annualfess;
    private String total_pay;
    private String due_payment;
    private String st_class;
    private String last_pay_date;


    public PayfeesListModel(String f_id, String admission_no, String firstname, String sf_annualfess, String total_pay,
                            String due_payment, String st_class, String last_pay_date) {

        this.f_id=f_id;
        this.admission_no=admission_no;
        this.firstname=firstname;
        this.sf_annualfess=sf_annualfess;
        this.total_pay=total_pay;
        this.due_payment=due_payment;
        this.st_class=st_class;
        this.last_pay_date=last_pay_date;
    }

    public String getF_id() {
        return f_id;
    }

    public String getAdmission_no() {
        return admission_no;
    }

    public void setAdmission_no(String admission_no) {
        this.admission_no = admission_no;
    }

    public String getSf_annualfess() {
        return sf_annualfess;
    }

    public void setSf_annualfess(String sf_annualfess) {
        this.sf_annualfess = sf_annualfess;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSt_class() {
        return st_class;
    }

    public String getLast_pay_date() {
        return last_pay_date;
    }

    public void setLast_pay_date(String last_pay_date) {
        this.last_pay_date = last_pay_date;
    }

    public void setSt_class(String st_class) {
        this.st_class = st_class;
    }

    public String getTotal_pay() {
        return total_pay;
    }

    public String getDue_payment() {
        return due_payment;
    }

    public void setDue_payment(String due_payment) {
        this.due_payment = due_payment;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay = total_pay;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }
}
