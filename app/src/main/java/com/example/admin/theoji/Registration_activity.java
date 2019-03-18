package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.StateModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Registration_activity extends AppCompatActivity {

    Spinner type,state,board;

    ArrayList<String> ChooseState;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList;

    ArrayList<String> ChooseBoard= new ArrayList<>();
    private ArrayAdapter<String> boardAdapter;
   // private ArrayList<BoardModel> boardList =new ArrayList<>();

    EditText sch_code, full_name, father_name, email_id,mobile_no, passw,city, sch_name, sales_lead;
    String Sch_code, Full_name, Father_name, Email_id,Mobile_no, Passw,City, Sch_name, Sales_lead;

    TextInputLayout tv_city,tv_sch_code,tv_fullname,tv_fathername,tv_lead,tv_sch_name;
    CardView card_board;
    Button btn_register;
    CheckBox check_box;

    private ArrayAdapter<String> typeAdapter;
    private ArrayList<String> typeList;
    String Type,State,Board;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

        sch_code=(EditText)findViewById(R.id.sch_code);
        full_name=(EditText)findViewById(R.id.full_name);
        father_name=(EditText)findViewById(R.id.father_name);
        email_id=(EditText)findViewById(R.id.email);
        passw=(EditText)findViewById(R.id.reg_pw);
        mobile_no=(EditText)findViewById(R.id.mobile);
        city=(EditText)findViewById(R.id.city);
        sch_name=(EditText)findViewById(R.id.sch_name);
        sales_lead=(EditText)findViewById(R.id.sales_lead);

        tv_city=(TextInputLayout)findViewById(R.id.tv_city);
        tv_fullname=(TextInputLayout)findViewById(R.id.tv_fullname);
        tv_fathername=(TextInputLayout)findViewById(R.id.tv_father_name);
        tv_sch_code=(TextInputLayout)findViewById(R.id.tv_code);
        tv_sch_name=(TextInputLayout)findViewById(R.id.tv_sch_name);
        tv_lead=(TextInputLayout)findViewById(R.id.tv_saleslead);
        card_board=(CardView)findViewById(R.id.card_views_board);
        check_box=findViewById(R.id.checkbox);
        btn_register=findViewById(R.id.cont_reg1);

        type = (Spinner)findViewById(R.id.type);
        state=(Spinner)findViewById(R.id.state_type);
        board=(Spinner)findViewById(R.id.sch_boardname);

        new BoardExecuteTask().execute();

        stateList = new ArrayList<>();
        ChooseState = new ArrayList<>();
        new state1ExecuteTask().execute();
        typeList = new ArrayList<>();
        typeList.add("Select your Type");
        typeList.add("School");
        typeList.add("Parent");
//********************************************************
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Type=type.getSelectedItem().toString();
                 State=state.getSelectedItem().toString();
                 City=city.getText().toString();
                 Board=board.getSelectedItem().toString();
                 Sch_name=sch_name.getText().toString();
                 Email_id=email_id.getText().toString();
                 Mobile_no=mobile_no.getText().toString();
                 Sales_lead=sales_lead.getText().toString();
                 Passw=passw.getText().toString();


                if (Connectivity.isNetworkAvailable(Registration_activity.this)) {
                    if (check_box.isChecked()) {
                         new registerExecuteTask().execute();
                    } else {
                        Toast.makeText(Registration_activity.this, "please accept terms & conditions", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Registration_activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text=type.getSelectedItem().toString();
                if(text.equalsIgnoreCase("Parent")) {
                    tv_city.setVisibility(View.GONE);
                    tv_sch_name.setVisibility(View.GONE);
                    tv_lead.setVisibility(View.GONE);
                    card_board.setVisibility(View.GONE);
                }else {
                    tv_city.setVisibility(View.VISIBLE);
                    tv_sch_name.setVisibility(View.VISIBLE);
                    tv_lead.setVisibility(View.VISIBLE);
                    card_board.setVisibility(View.VISIBLE);
                }
                if(text.equalsIgnoreCase("School")) {
                    tv_sch_code.setVisibility(View.GONE);
                    tv_fullname.setVisibility(View.GONE);
                   tv_fathername.setVisibility(View.GONE);

                }else {
                    tv_sch_code.setVisibility(View.VISIBLE);
                    tv_fullname.setVisibility(View.VISIBLE);
                    tv_fathername.setVisibility(View.VISIBLE);
                }


                Type = typeAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeAdapter = new ArrayAdapter<String>(Registration_activity.this, R.layout.support_simple_spinner_dropdown_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);


    }

    protected void hideKeyboard(View view) {

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

        class state1ExecuteTask extends AsyncTask<String, Integer, String> {

            String output = "";
            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
                String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(Registration_activity.this);


                output = HttpHandler.makeServiceCall(sever_url);
                System.out.println("getcomment_url" + output);
                return output;
            }

            @Override
            protected void onPostExecute(String output) {
                if (output == null) {
                } else {
                    try {

//                    Toast.makeText(RegistrationActivity.this, "result is" + output, Toast.LENGTH_SHORT).show();
                        JSONObject object = new JSONObject(output);
                        String res = object.getString("responce");

                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String sstate_id = jsonObject1.getString("sstate_id");
                            String sstate_name = jsonObject1.getString("sstate_name");


                            stateList.add(new StateModel(sstate_id, sstate_name));
                            ChooseState.add(sstate_name);

                        }

                        stateAdapter = new ArrayAdapter<String>(Registration_activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        state.setAdapter(stateAdapter);

                        super.onPostExecute(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    private class registerExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Registration_activity.this);
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
                Toast.makeText(Registration_activity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                   // user_id=data.getString("user_id");
//                    String username1=data.getString("username");
//                   // firstname=data.getString("firstname");
//                    String lastname1=data.getString("lastname");
//                   // email=data.getString("email");
//                    String mobileno1=data.getString("mobileno");
//                    String date1=data.getString("date");
//                    String user_type1=data.getString("user_type");
//                    String gender1=data.getString("gender");
//                    String dob1=data.getString("dob");
//                    String city1=data.getString("city");
//                    String address1=data.getString("address");
//                    String status1=data.getString("status");
//                    String block_unblock1=data.getString("block_unblock");
//                   // ref_id=data.getString("ref_id");
//                    String about1=data.getString("about");
//                    String school_code1=data.getString("school_code");
//                    String profileupdate1=data.getString("profileupdate");
//                    String latest_post1=data.getString("latest_post");
//                    String latest_event1=data.getString("latest_event");
//                    String latest_activities1=data.getString("latest_activities");
//                    String latest_news1=data.getString("latest_news");
//                    String sales_lead_name1=data.getString("sales_lead_name");
//                    String latest_noticboard1=data.getString("latest_noticboard");
//                    String notice1=data.getString("notice");
//                    String stutotalfees1=data.getString("stutotalfees");
//                    String sturemainfee1=data.getString("sturemainfee");
//                    String stulastfee1=data.getString("stulastfee");




                    if (res.equals("true")) {

                        Toast.makeText(Registration_activity.this, "Registration success", Toast.LENGTH_SHORT).show();

                    } else {
                       // manager.setLogin(true);
//                        AppPreference.setRefid(LoginActivity.this,ref_id);
//                        AppPreference.setFirstname(LoginActivity.this,firstname);
//                        AppPreference.setEmail(LoginActivity.this,email);
//
//                        AppPreference.setUserid(LoginActivity.this,user_id);
//                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
//                        startActivity(intent);
//                        finish();
                        Toast.makeText(Registration_activity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String PostData(String[] values) {
        try {

            URL url = new URL("http://jntrcpl.com/theoji/index.php/Api/Registration");

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("type",Type);
            postDataParams.put("email",Email_id);
           postDataParams.put("mobileno",Mobile_no);
            postDataParams.put("city",City);
            postDataParams.put("firstname",Sch_name);
            postDataParams.put("sales_lead_name",Sales_lead);
            postDataParams.put("password",Passw);
            postDataParams.put("stateschool",State);
            postDataParams.put("board",Board);


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

//        String sch_code = school_code.getText().toString();
//        String pass = password.getText().toString();
        String mobile = mobile_no.getText().toString();
        String login_type = type.getSelectedItem().toString();


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

            Toast.makeText(Registration_activity.this,"Please select Registration type!", Toast.LENGTH_LONG).show();
        } else {
            valid = true;
            login_type = type.getSelectedItem().toString();

        }
        return valid;

    }

//*************************************************
    private class BoardExecuteTask extends AsyncTask<String, Integer, String> {

        String output = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_board";
            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

//                    Toast.makeText(RegistrationActivity.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(output);
                    String res = object.getString("responce");

                    JSONArray jsonArray = object.getJSONArray("class");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String m_id = jsonObject1.getString("m_id");
                        String m_name = jsonObject1.getString("m_name");
                        String type = jsonObject1.getString("type");
                        String parent = jsonObject1.getString("parent");
                        String school_id = jsonObject1.getString("school_id");


//                        boardList.add(new BoardModel(m_id, m_name));
                        ChooseBoard.add(m_name);

                    }

                    boardAdapter = new ArrayAdapter<String>(Registration_activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseBoard);
                    boardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    board.setAdapter(boardAdapter);

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

