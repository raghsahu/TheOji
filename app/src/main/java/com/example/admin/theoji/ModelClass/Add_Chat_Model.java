package com.example.admin.theoji.ModelClass;

public class Add_Chat_Model {
    private String school_chat_id;
    private String to;
    private String from;
    private String message;
    private String datecd;
    private String notice_school;
    private String read_status;


    public Add_Chat_Model(String school_chat_id, String to, String from, String message, String datecd, String notice_school,
                          String read_status) {

        this.school_chat_id=school_chat_id;
        this.to=to;
        this.from=from;
        this.message=message;
        this.datecd=datecd;
        this.notice_school=notice_school;
        this.read_status=read_status;

    }

    public String getSchool_chat_id() {
        return school_chat_id;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getNotice_school() {
        return notice_school;
    }

    public void setNotice_school(String notice_school) {
        this.notice_school = notice_school;
    }

    public String getDatecd() {
        return datecd;
    }

    public void setDatecd(String datecd) {
        this.datecd = datecd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSchool_chat_id(String school_chat_id) {
        this.school_chat_id = school_chat_id;
    }
}
