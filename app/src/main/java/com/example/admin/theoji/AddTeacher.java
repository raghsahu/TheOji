package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class AddTeacher extends AppCompatActivity {
    LinearLayout layout;
    Button btn_submit;
    EditText teacher_name, teacher_email, teacher_mobile,teacher_pw,teacher_address;

    String user_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        // otherside tab soft keyboard closed
        LinearLayout layout = (LinearLayout) findViewById(R.id.body);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        teacher_name = (EditText) findViewById(R.id.teacher_name);
        teacher_email = (EditText) findViewById(R.id.teacher_email);
        teacher_mobile = (EditText) findViewById(R.id.teacher_mobile);
        teacher_pw = (EditText) findViewById(R.id.teacher_pw);
       // teacher_alotclass = (EditText) findViewById(R.id.teacher_class);
        teacher_address = (EditText) findViewById(R.id.teacher_address);

        btn_submit  = (Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isNetworkAvailable(AddTeacher.this)) {
                    if (validate()) {
                        String name = teacher_name.getText().toString();
                        String pass = teacher_pw.getText().toString();
                        String mobile = teacher_mobile.getText().toString();
                        String email = teacher_email.getText().toString();
                      //  String alotclass = teacher_alotclass.getText().toString();
                        String address = teacher_address.getText().toString();


                        new TeacherExecuteTask().execute();
                    }
                    }else {
                    Toast.makeText(AddTeacher.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class TeacherExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddTeacher.this);
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
               Toast.makeText(AddTeacher.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject responce = new JSONObject(result);
                    String res = responce.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//                    String email=data.getString("firstname");
//                    String mobile=data.getString("lastname");
//                    String pass=data.getString("email");
//                    String alotclass=data.getString("mobileno");
//                    String address=data.getString("date");



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(AddTeacher.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddTeacher.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddTeacher.this, TeacherActivity.class);
                        startActivity(intent);
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String PostData(String[] values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/add_teacher");

            JSONObject postDataParams = new JSONObject();
            String id= AppPreference.getUserid(AddTeacher.this);
            postDataParams.put("Teacher_name",teacher_name.getText().toString());
            postDataParams.put("Teacher_email",teacher_email.getText().toString());
            postDataParams.put("Teacher_mobile",teacher_mobile.getText().toString());
            postDataParams.put("Teacher_password",teacher_pw.getText().toString());
           // postDataParams.put("Teacher_alot_class",teacher_alotclass.getText().toString());
            postDataParams.put("Teacher_address",teacher_address.getText().toString());
            postDataParams.put("id",id);


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

        String Teacher_name = teacher_name.getText().toString();
        String pass = teacher_pw.getText().toString();
        String mobile = teacher_mobile.getText().toString();
       // String alotclass = teacher_alotclass.getText().toString();
        String address = teacher_address.getText().toString();
        String email = teacher_email.getText().toString();


        if (Teacher_name.isEmpty()) {
            valid = false;
            teacher_name.setError("Please enter teacher name!");
        } else {
            valid = true;
            teacher_name.setError(null);
        }
        if (pass.isEmpty()) {
            valid = false;
            teacher_pw.setError("Please enter teacher password!");
        } else {
            valid = true;
            teacher_pw.setError(null);
        }

        if (mobile.isEmpty()) {
            valid = false;
            teacher_mobile.setError("Please enter valid Mobile no.!");
        } else {
            if (mobile.length() == 10) {
                valid = true;
                teacher_mobile.setError(null);
            } else {
                valid = false;
                teacher_mobile.setError("Invalid mobile no.!");
            }
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            teacher_email.setError("Please enter valid email!");
            return false;
        } else {
            valid = true;
            teacher_email.setError(null);
        }
        if (address.isEmpty()) {
            valid = false;
            teacher_address.setError("Please enter teacher address!");
        } else {
            valid = true;
            teacher_address.setError(null);
        }
//        if (alotclass.isEmpty()) {
//            valid = false;
//            teacher_alotclass.setError("Please enter teacher Alot class!");
//        } else {
//            valid = true;
//            teacher_alotclass.setError(null);
//        }

        return valid;

    }


    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
