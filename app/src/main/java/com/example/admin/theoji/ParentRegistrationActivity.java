package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class ParentRegistrationActivity extends AppCompatActivity {

    Spinner state;
    ArrayList<String> ChooseState=new ArrayList<>();
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList=new ArrayList<>();

    EditText st_fullname,st_fater,st_email,st_mobile,st_pw;
    String Sch_code,St_fullname,St_fater,St_email,St_mobile,St_pw;
    String State,Type,Class_ID,School_Name;

    TextView tv_sch_code,tv_sch_name;

    Button btn_reg;
    CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_registration);

       // sch_code=(EditText)findViewById(R.id.sch_code);
        st_fullname=(EditText)findViewById(R.id.full_name);
        st_fater=(EditText)findViewById(R.id.father_name);
        st_email=(EditText)findViewById(R.id.email);
       st_pw=(EditText)findViewById(R.id.reg_pw);
       tv_sch_code=(TextView) findViewById(R.id.text_school_code);
       tv_sch_name=(TextView) findViewById(R.id.text_school_name);
        st_mobile=(EditText)findViewById(R.id.mobile);
        btn_reg=findViewById(R.id.cont_reg1);
        checkBox=(CheckBox)findViewById(R.id.checkbox);


        Sch_code=getIntent().getStringExtra("school_code");
        tv_sch_code.setText(Sch_code);
        Toast.makeText(this, "schcode"+Sch_code, Toast.LENGTH_SHORT).show();

        Type=getIntent().getStringExtra("reg_type");
        Toast.makeText(this, "type"+Type, Toast.LENGTH_SHORT).show();

        Class_ID=getIntent().getStringExtra("class_id");
        Toast.makeText(this, "classid"+Class_ID, Toast.LENGTH_SHORT).show();

        School_Name=getIntent().getStringExtra("sch_name");
        tv_sch_name.setText(School_Name);
        Toast.makeText(this, "classid"+Class_ID, Toast.LENGTH_SHORT).show();


        state=(Spinner)findViewById(R.id.state_type);
        new stateExecuteTask().execute();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                State=state.getSelectedItem().toString();
                St_fullname=st_fullname.getText().toString();
                St_fater=st_fater.getText().toString();
                St_email=st_email.getText().toString();
                St_mobile=st_mobile.getText().toString();
                St_pw=st_pw.getText().toString();

                if (Connectivity.isNetworkAvailable(ParentRegistrationActivity.this)) {
                    if (checkBox.isChecked()) {
                        new registerParentExecuteTask().execute();
                    } else {
                        Toast.makeText(ParentRegistrationActivity.this, "please accept terms & conditions", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ParentRegistrationActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private class stateExecuteTask extends AsyncTask<String, Integer, String> {

        String output = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(ParentRegistrationActivity.this);


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

                    stateAdapter = new ArrayAdapter<String>(ParentRegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    state.setAdapter(stateAdapter);

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class registerParentExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(ParentRegistrationActivity.this);
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
                   // Toast.makeText(ParentRegistrationActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                   // user_id=data.getString("user_id");
//                    String username1=data.getString("username");
//                   // firstname=data.getString("firstname");
//                    String lastname1=data.getString("lastname");
//



                    if (res.equals("true")) {

                        Toast.makeText(ParentRegistrationActivity.this, "Registration success", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ParentRegistrationActivity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
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
            postDataParams.put("state",State);
            postDataParams.put("email",St_email);
            postDataParams.put("mobileno",St_mobile);
            postDataParams.put("firstname",St_fullname);
            postDataParams.put("password",St_pw);
            postDataParams.put("school_code",State);
            postDataParams.put("sclass",Class_ID);
            postDataParams.put("lastname",St_fater);

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
}
