package com.example.admin.theoji.ModelClass;

import java.io.Serializable;

public class Student_Perform_Model implements Serializable {

        String performance_id;
        String user_id;
        String january;
        String february;
        String march;
        String april;
        String may;
        String june;
        String july;
        String august;
        String september;
        String october;
        String november;
        String december;
      private  String firstname;
        String umeta_value;



    public Student_Perform_Model(String performance_id, String user_id, String january, String february, String march, String april,
                                 String may, String june, String july, String august, String september, String october, String november,
                                 String december, String firstname, String umeta_value) {


                this.performance_id=performance_id;
                this.user_id=user_id;
                this.january=january;
                this.february=february;
                this.march=march;
                this.april=april;
                this.may=may;
                this.july=july;
                this.june=june;
                this.august=august;
                this.september=september;
                this.october=october;
                this.november=november;
                this.december=december;
                this.firstname=firstname;
                this.umeta_value=umeta_value;

    }

        public String getUser_id() {
                return user_id;
        }

        public void setUser_id(String user_id) {
                this.user_id = user_id;
        }

        public String getUmeta_value() {
                return umeta_value;
        }

        public void setUmeta_value(String umeta_value) {
                this.umeta_value = umeta_value;
        }

        public String getOctober() {
                return october;
        }

        public String getFirstname() {
                return firstname;
        }

        public void setFirstname(String firstname) {
                this.firstname = firstname;
        }

        public String getDecember() {
                return december;
        }

        public void setDecember(String december) {
                this.december = december;
        }

        public String getNovember() {
                return november;
        }

        public void setNovember(String november) {
                this.november = november;
        }

        public void setOctober(String october) {
                this.october = october;
        }

        public String getApril() {
                return april;
        }

        public String getSeptember() {
                return september;
        }

        public void setSeptember(String september) {
                this.september = september;
        }

        public String getAugust() {
                return august;
        }

        public void setAugust(String august) {
                this.august = august;
        }

        public String getJuly() {
                return july;
        }

        public void setJuly(String july) {
                this.july = july;
        }

        public String getJune() {
                return june;
        }

        public void setJune(String june) {
                this.june = june;
        }

        public String getMay() {
                return may;
        }

        public void setMay(String may) {
                this.may = may;
        }

        public void setApril(String april) {
                this.april = april;
        }

        public String getJanuary() {
                return january;
        }

        public String getMarch() {
                return march;
        }

        public void setMarch(String march) {
                this.march = march;
        }

        public String getFebruary() {
                return february;
        }

        public void setFebruary(String february) {
                this.february = february;
        }

        public void setJanuary(String january) {
                this.january = january;
        }

        public String getPerformance_id() {
                return performance_id;
        }

        public void setPerformance_id(String performance_id) {
                this.performance_id = performance_id;
        }
}
