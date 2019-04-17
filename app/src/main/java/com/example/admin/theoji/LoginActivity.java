package com.example.admin.theoji;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.Shared_prefrence.SessionManager;
import com.example.admin.theoji.Utils.CustomAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    LinearLayout layout;
    Button btn_login,btn_next;
    Spinner type;
    EditText school_code;
    EditText password;
    EditText mobile_no;
    TextView click_reg,txt_forgot_pw;

    SessionManager manager;

    String user_id="";
//    TextView click_here;
    private ArrayAdapter<String> typeAdapter;
    private ArrayList<String> typeList;
    String Type;
    String ref_id;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
     Toolbar toolbar;
    public String firstname,email;
     String school_code1;

    Spinner stud_child;
    ArrayList<String> ChooseStudent=new ArrayList<>();
    private ArrayAdapter<String> ParentAdapter;
    private ArrayList<ParentStudentModel> ParentStudentList=new ArrayList<>();

     TextInputLayout et_mobile,et_pw,et_schCode;
     CardView card_stud;
     String output="";
    HashMap<Integer, ParentStudentModel> ParentHashMap=new HashMap<Integer, ParentStudentModel>();
     String Stude_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

        // otherside tab soft keyboard closed
        LinearLayout layout = (LinearLayout) findViewById(R.id.login_body);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        manager = new SessionManager(LoginActivity.this);

        school_code = (EditText) findViewById(R.id.sch_code);
        password = (EditText) findViewById(R.id.login_pw);
        mobile_no = (EditText) findViewById(R.id.login_mobile);
        click_reg = (TextView) findViewById(R.id.tv_click);
        txt_forgot_pw = (TextView) findViewById(R.id.txt_forgot);

       et_mobile = findViewById(R.id.mobile_view);
        et_pw = findViewById(R.id.pw_view);
        stud_child =  findViewById(R.id.students_child);
        card_stud =  findViewById(R.id.card_stud_child);
        et_schCode =  findViewById(R.id.view_schCode);

        btn_login  = (Button)findViewById(R.id.btn_login);
        btn_next  = (Button)findViewById(R.id.btn_next);
        type = (Spinner)findViewById(R.id.type);

        typeList = new ArrayList<>();
        typeList.add("Select your Type");
        typeList.add("School");
        typeList.add("Parent");
        typeList.add("Teacher");
//        typeList.add("Student");

        click_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Registration_activity.class);
                startActivity(intent);
            }
        });

        txt_forgot_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Forgot_activity.class);
                startActivity(intent);
            }
        });

        //click login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isNetworkAvailable(LoginActivity.this)){
                    if (validate()) {
                        String sch_code = school_code.getText().toString();
                        String pass = password.getText().toString();
                        String mobile = mobile_no.getText().toString();
                        String login_type = type.getSelectedItem().toString();

                        new ExecuteTask().execute();
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }



            }
        });

        //***************************************
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isNetworkAvailable(LoginActivity.this)){

                    String sch_code = school_code.getText().toString();
                    String mobile = mobile_no.getText().toString();

                    if (!sch_code.isEmpty() && !mobile.isEmpty()) {

                        new Search_Stud_Task().execute();
                    }else {
                        Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }



            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                    if(ParentStudentList.size() !=0)
                    {
                        ChooseStudent.clear();
                        stud_child.setAdapter(null);
                        ParentAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                String text=type.getSelectedItem().toString();
                if(text.equals("School")) {
                    et_mobile.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    card_stud.setVisibility(View.GONE);
                    et_schCode.setVisibility(View.VISIBLE);
                    et_pw.setVisibility(View.VISIBLE);
                }else {
                    et_mobile.setVisibility(View.VISIBLE);
                }

                if(text.equalsIgnoreCase("Teacher")) {
                    btn_next.setVisibility(View.GONE);
                    card_stud.setVisibility(View.GONE);
                    et_schCode.setVisibility(View.VISIBLE);
                    et_pw.setVisibility(View.VISIBLE);
                }else {

                }

                if(text.equalsIgnoreCase("Parent")) {

                    et_pw.setVisibility(View.GONE);
                    btn_login.setVisibility(View.GONE);
                    card_stud.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);

                    et_schCode.setVisibility(View.VISIBLE);
                    //et_pw.setVisibility(View.VISIBLE);
                }else {
                    et_pw.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                }


                Type = typeAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeAdapter = new ArrayAdapter<String>(LoginActivity.this, R.layout.support_simple_spinner_dropdown_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);
    //****************************************************************************
        stud_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //strSid = stateList.get(position).getState_id();
//                try{
//
//                    if(studentList.size() !=0)
//                    {
//                        ChooseStudent.clear();
//
//                        spin_student.setAdapter(null);
//                        studentListAdapter.notifyDataSetChanged();
//
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
                for (int i = 0; i < ParentHashMap.size(); i++)
                {

                    if (ParentHashMap.get(i).getFirstname().equals(stud_child.getItemAtPosition(position)))
                    {
                       // new spinnerStudentExecuteTask(SectionHashMap.get(i).getM_id()).execute();
                        Stude_ID=ParentHashMap.get(i).getUser_id().toString();
                        // Toast.makeText(AttendenceActivity.this, "sec_Id"+Section_ID, Toast.LENGTH_SHORT).show();
                    }
                    // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }
//*********************************************************************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.about_us:
                if (Connectivity.isNetworkAvailable(LoginActivity.this)){
                    startActivity(new Intent(this, AboutUsActivity.class));
                    //finish();

                }else {
                    //Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
//******************************************************************************
   private  boolean checkAndRequestPermissions() {
    int permissionCamara = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
    int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    int permissionStorage1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    int permissionPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

    List<String> listPermissionsNeeded = new ArrayList<>();
    if (permissionCamara != PackageManager.PERMISSION_GRANTED) {
        listPermissionsNeeded.add(Manifest.permission.CAMERA);
    }
    if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    if (permissionStorage1 != PackageManager.PERMISSION_GRANTED) {
        listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    if (permissionPhone != PackageManager.PERMISSION_GRANTED) {
        listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
    }
    if (!listPermissionsNeeded.isEmpty()) {
        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        return false;
    }
    return true;
}
//*************************************************************************
    class ExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData(params);

            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
               Toast.makeText(LoginActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject responce = new JSONObject(result);
                    String res = responce.getString("responce");
                    String profileimage = responce.getString("profileimage");

                    JSONObject data= new JSONObject(result).getJSONObject("data");
                     user_id=data.getString("user_id");
                    String username1=data.getString("username");
                    String firstname=data.getString("firstname");
                    String lastname1=data.getString("lastname");
                     email=data.getString("email");
                    String mobileno1=data.getString("mobileno");
                    String date1=data.getString("date");
                    String user_type=data.getString("user_type");
                    String gender1=data.getString("gender");
                    String dob1=data.getString("dob");
                    String city1=data.getString("city");
                    String address1=data.getString("address");
                    String status1=data.getString("status");
                    String block_unblock1=data.getString("block_unblock");
                    ref_id=data.getString("ref_id");
                    String about1=data.getString("about");
                    school_code1=data.getString("school_code");
                    String profileupdate1=data.getString("profileupdate");
                    String latest_post1=data.getString("latest_post");
                    String latest_event1=data.getString("latest_event");
                    String latest_activities1=data.getString("latest_activities");
                    String latest_news1=data.getString("latest_news");
                    String sales_lead_name1=data.getString("sales_lead_name");
                    String latest_noticboard1=data.getString("latest_noticboard");
                    String notice1=data.getString("notice");
                    String stutotalfees1=data.getString("stutotalfees");
                    String sturemainfee1=data.getString("sturemainfee");
                    String stulastfee1=data.getString("stulastfee");

                    AppPreference.setFirstname(LoginActivity.this,firstname);


                    if (!res.equalsIgnoreCase("true")) {
                        Toast.makeText(LoginActivity.this, "Invalid details login error", Toast.LENGTH_SHORT).show();
                    } else {
                        manager.setLogin(true);
                        AppPreference.setuser_type(LoginActivity.this,user_type);

                        AppPreference.setProfileImage(LoginActivity.this,profileimage);
                        Toast.makeText(LoginActivity.this, "pp" + AppPreference.getProfileImage(LoginActivity.this), Toast.LENGTH_SHORT).show();

                        AppPreference.setRefid(LoginActivity.this,ref_id);
                        AppPreference.setEmail(LoginActivity.this,email);
                        AppPreference.setSchoolCode(LoginActivity.this,school_code1);
                       // Toast.makeText(LoginActivity.this, ""+AppPreference.getRefid(LoginActivity.this), Toast.LENGTH_SHORT).show();

                        AppPreference.setUserid(LoginActivity.this,user_id);
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();

                        //**********************************hide/ view action************************************************
//                        if (AppPreference.getUser_Type(LoginActivity.this).equals(2)){
//                            add_attendence
//
//                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String PostData(String[] values) {
        try {

            URL url = new URL("http://jntrcpl.com/theoji/index.php/Api/login");

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Select_your_login_type",type.getSelectedItem().toString());
            postDataParams.put("School_code",school_code.getText().toString());
            postDataParams.put("Password",password.getText().toString());
            postDataParams.put("Mobileno",mobile_no.getText().toString());
            postDataParams.put("user_id",Stude_ID);


            Log.e("postDataParams", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds*/);
            conn.setConnectTimeout(15000  /*milliseconds*/);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line);
                }
                r.close();
                return result.toString();

            } else {
                return new String("false : " + responseCode);
            }
        }
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public boolean validate() {
        boolean valid = false;

        String sch_code = school_code.getText().toString();
        String pass = password.getText().toString();
        String mobile = mobile_no.getText().toString();
        String login_type = type.getSelectedItem().toString();

        if (sch_code.isEmpty()) {
            valid = false;
            school_code.setError("Please enter school code!");
        } else {
            valid = true;
            school_code.setError(null);
        }
        if (pass.isEmpty()) {
            valid = false;
            password.setError("Please enter password!");
        } else {
            valid = true;
            password.setError(null);
        }

        if (mobile.isEmpty()) {
            valid = false;
            mobile_no.setError("Please enter valid Mobile no.!");
        } else {
            if (mobile_no.length() == 10) {
                valid = true;
                mobile_no.setError(null);
            } else {
                valid = false;
                mobile_no.setError("Invalid mobile no.!");
            }
        }

        if (login_type.equals("Select your Type")) {
            valid = false;

            Toast.makeText(LoginActivity.this,"Please select Login type!", Toast.LENGTH_LONG).show();
        } else {
            valid = true;
            login_type = type.getSelectedItem().toString();

        }
        return valid;

    }


    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
//*****************************************
    private class Search_Stud_Task extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("processing");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "http://jntrcpl.com/theoji/index.php/Api/search_student?school_code=" + school_code.getText().toString()
                    + "&mobile=" + mobile_no.getText().toString();
            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    //Toast.makeText(Registration_activity.this, "result is" + result, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");
                    if (res.equals("true")) {

                        Toast.makeText(LoginActivity.this, "succedd", Toast.LENGTH_SHORT).show();
                        et_pw.setVisibility(View.VISIBLE);
                        card_stud.setVisibility(View.VISIBLE);
                        btn_login.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.GONE);
                        et_schCode.setVisibility(View.GONE);
                        et_mobile.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String user_id = jsonObject1.getString("user_id");
                            String firstname = jsonObject1.getString("firstname");

                            ParentStudentList.add(new ParentStudentModel(user_id, firstname));
                            ChooseStudent.add(firstname);
                            ParentHashMap.put(i, new ParentStudentModel(user_id, firstname));

                        }

                        ParentAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseStudent);
                        ParentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stud_child.setAdapter(ParentAdapter);


                    } else {

                        et_pw.setVisibility(View.GONE);
                        card_stud.setVisibility(View.GONE);
                        btn_login.setVisibility(View.GONE);
                        btn_next.setVisibility(View.VISIBLE);
                        et_schCode.setVisibility(View.VISIBLE);
                        et_mobile.setVisibility(View.VISIBLE);

                         Toast.makeText(LoginActivity.this, "No details Found", Toast.LENGTH_SHORT).show();

                        ChooseStudent.clear();
                        ParentAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseStudent);
                        stud_child.setAdapter(ParentAdapter);
                        ParentAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }
