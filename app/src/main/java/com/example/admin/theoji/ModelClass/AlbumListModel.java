package com.example.admin.theoji.ModelClass;

public class AlbumListModel {

  private String pmeta_value;




    public AlbumListModel(String post_id, String post_author, String post_date, String post_title, String post_content, String post_type,
                          String ref_id, String pmeta_id, String pmeta_key, String pmeta_value) {

   this.pmeta_value=pmeta_value;


    }
    public String getPmeta_value() {
        return pmeta_value;
    }

    public void setPmeta_value(String pmeta_value) {
        this.pmeta_value = pmeta_value;
    }
}
