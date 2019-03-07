package com.example.admin.theoji.ModelClass;

public class StateModel {

    private String sstate_id;
    private String sstate_name;

    public StateModel(String sstate_id, String sstate_name) {

        this.sstate_id=sstate_id;
        this.sstate_name=sstate_name;


    }

    public String getSstate_id() {
        return sstate_id;
    }

    public void setSstate_id(String sstate_id) {
        this.sstate_id = sstate_id;
    }

    public String getSstate_name() {
        return sstate_name;
    }

    public void setSstate_name(String sstate_name) {
        this.sstate_name = sstate_name;
    }
}
