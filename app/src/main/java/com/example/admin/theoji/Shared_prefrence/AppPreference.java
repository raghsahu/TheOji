package com.example.admin.theoji.Shared_prefrence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.admin.theoji.LoginActivity;


public class AppPreference {

    public static final String SHARED_PREFERENCE_NAME = "EXPENSEMGT";
    public static final String USERID = "userId";
    public static final String LUSERID = "luserId";
    public static final String POSTID = "postId";
    public static final String REFID = "refId";
    public static final String FIRSTNAME = "firstname";
    public static final String EMAIL = "email";
    public static final String SchoolCode = "schoolcode";
    public static final String ProfileImage = "profileImg";
    public static final String User_Type = "userType";

    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";


    public static void setRefid(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REFID, value);
        editor.commit();
    }
    public static String getRefid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(REFID, "");
    }

    public static void setUserid(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERID, value);
        editor.commit();
    }

    public static String getUserid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(USERID, "");
    }

    public static void setLuserid(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LUSERID, value);
        editor.commit();
    }

    public static String getLuserid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(LUSERID, "");
    }

    public static String getPOSTID(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(SHARED_PREFERENCE_NAME,0);
        return preferences.getString(POSTID,"");

    }

    public static void setPostid(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(POSTID, value);
        editor.commit();
    }

    public static void setFirstname(Context context, String firstname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIRSTNAME, firstname);
        editor.commit();
    }

    public static String getFirstname(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(FIRSTNAME, "");
    }
    public static void setEmail(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(EMAIL, "");
    }

    public static void setSchoolCode(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SchoolCode, value);
        editor.commit();
    }
    public static String getSchoolCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(SchoolCode, "");
    }

    public static void setProfileImage(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ProfileImage, value);
        editor.commit();
    }

    public static String getProfileImage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(ProfileImage, "");
    }


    public static void setuser_type(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(User_Type, value);
        editor.commit();
    }

    public static String getUser_Type(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(User_Type, "");
    }


    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
