package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.Shared_prefrence.SessionManager;

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

public class LoginActivity extends AppCompatActivity {
    LinearLayout layout;
    Button btn_login;
    Spinner type;
    EditText school_code;
    EditText password;
    EditText mobile_no;
    TextView click_reg;

    SessionManager manager;

    String user_id="";
//    TextView click_here;
    private ArrayAdapter<String> typeAdapter;
    private ArrayList<String> typeList;
    String Type;
    String ref_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        btn_login  = (Button)findViewById(R.id.btn_login);
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

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text=type.getSelectedItem().toString();
                if(text.equals("School")) {
                    mobile_no.setVisibility(View.GONE);
                }else {
                    mobile_no.setVisibility(View.VISIBLE);
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
    }

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
//                Toast.makeText(LoginActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject responce = new JSONObject(result);
                    String res = responce.getString("responce");

                    JSONObject data= new JSONObject(result).getJSONObject("data");
                     user_id=data.getString("user_id");
                    String username1=data.getString("username");
                    String firstname1=data.getString("firstname");
                    String lastname1=data.getString("lastname");
                    String email1=data.getString("email");
                    String mobileno1=data.getString("mobileno");
                    String date1=data.getString("date");
                    String user_type1=data.getString("user_type");
                    String gender1=data.getString("gender");
                    String dob1=data.getString("dob");
                    String city1=data.getString("city");
                    String address1=data.getString("address");
                    String status1=data.getString("status");
                    String block_unblock1=data.getString("block_unblock");
                    ref_id=data.getString("ref_id");
                    String about1=data.getString("about");
                    String school_code1=data.getString("school_code");
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

                    AppPreference.setRefid(LoginActivity.this,ref_id);


                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(LoginActivity.this, "Invalid details login error", Toast.LENGTH_SHORT).show();

                    } else {
                        manager.setLogin(true);
//                    Toast.makeText(LoginActivity.this, "login error", Toast.LENGTH_SHORT).show();
                        AppPreference.setUserid(LoginActivity.this,user_id);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
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
}
