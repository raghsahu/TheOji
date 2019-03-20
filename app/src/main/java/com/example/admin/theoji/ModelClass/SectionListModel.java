package com.example.admin.theoji.ModelClass;

public class SectionListModel {
    private String m_id;
    private String m_name;
    private String school_id;


    public SectionListModel(String m_id, String m_name, String school_id) {
        this.m_id=m_id;
        this.m_name=m_name;
        this.school_id=school_id;


    }

    public String getM_id() {
        return m_id;
    }

    public String getM_name() {
        return m_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }
}
