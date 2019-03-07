package com.example.admin.theoji.ModelClass;

public class SecondStepModel {

    private String student_gardian_name;
    private String student_mother_name;
    private String student_city;
    private String student_address;
    private String student_pincode;
    private String student_mobile;
    private String student_alternate_mobile;
    private String student_state;
    private String student_country;
    private String student_cast;
    private String student_rte_type;
    private String student_bank_ifsc_code;
    private String student_sssmid;

    public SecondStepModel(String student_gardian_name, String student_mother_name, String student_city,
                           String student_address, String student_pincode, String student_mobile, String student_alternate_mobile,
                           String student_state, String student_country, String student_cast, String student_rte_type,
                           String student_bank_ifsc_code, String student_sssmid)
    {

   this.student_gardian_name=student_gardian_name;
   this.student_mother_name=student_mother_name;
   this.student_city=student_city;
   this.student_address=student_address;
   this.student_pincode=student_pincode;
   this.student_alternate_mobile=student_alternate_mobile;
   this.student_mobile=student_mobile;
   this.student_state=student_state;
   this.student_country=student_country;
   this.student_cast=student_cast;
   this.student_rte_type=student_rte_type;
   this.student_bank_ifsc_code=student_bank_ifsc_code;
   this.student_sssmid=student_sssmid;

    }

    public String getStudent_gardian_name() {
        return student_gardian_name;
    }

    public String getStudent_pincode() {
        return student_pincode;
    }

    public String getStudent_country() {
        return student_country;
    }

    public void setStudent_country(String student_country) {
        this.student_country = student_country;
    }

    public String getStudent_state() {
        return student_state;
    }

    public void setStudent_state(String student_state) {
        this.student_state = student_state;
    }

    public String getStudent_alternate_mobile() {
        return student_alternate_mobile;
    }

    public void setStudent_alternate_mobile(String student_alternate_mobile) {
        this.student_alternate_mobile = student_alternate_mobile;
    }

    public String getStudent_mobile() {
        return student_mobile;
    }

    public void setStudent_mobile(String student_mobile) {
        this.student_mobile = student_mobile;
    }

    public void setStudent_pincode(String student_pincode) {
        this.student_pincode = student_pincode;
    }

    public String getStudent_address() {
        return student_address;
    }

    public void setStudent_address(String student_address) {
        this.student_address = student_address;
    }

    public String getStudent_city() {
        return student_city;
    }

    public void setStudent_city(String student_city) {
        this.student_city = student_city;
    }

    public String getStudent_mother_name() {
        return student_mother_name;
    }

    public void setStudent_mother_name(String student_mother_name) {
        this.student_mother_name = student_mother_name;
    }

    public String getStudent_cast() {
        return student_cast;
    }

    public String getStudent_bank_ifsc_code() {
        return student_bank_ifsc_code;
    }

    public String getStudent_sssmid() {
        return student_sssmid;
    }

    public void setStudent_sssmid(String student_sssmid) {
        this.student_sssmid = student_sssmid;
    }

    public void setStudent_bank_ifsc_code(String student_bank_ifsc_code) {
        this.student_bank_ifsc_code = student_bank_ifsc_code;
    }

    public void setStudent_cast(String student_cast) {
        this.student_cast = student_cast;
    }

    public String getStudent_rte_type() {
        return student_rte_type;
    }

    public void setStudent_rte_type(String student_rte_type) {
        this.student_rte_type = student_rte_type;
    }

    public void setStudent_gardian_name(String student_gardian_name) {
        this.student_gardian_name = student_gardian_name;






    }
}
