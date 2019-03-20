package com.example.admin.theoji.ModelClass;

public class ClassSectionListModel {
    private String class_id;
    private String m_name;
    private String section;


    public ClassSectionListModel(String class_id, String m_name, String section) {
        this.class_id=class_id;
        this.m_name=m_name;
        this.section=section;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getM_name() {
        return m_name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }
}
