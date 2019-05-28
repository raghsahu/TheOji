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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
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
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class Teacher_Edit_Activity extends AppCompatActivity {
    LinearLayout layout;
    Button btn_submit;
    EditText teacher_name, teacher_email, teacher_mobile,teacher_pw,teacher_address;
    String server_url;
     String name,email,pass,address,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__edit);


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
        teacher_address = (EditText) findViewById(R.id.teacher_address);

        btn_submit  = (Button)findViewById(R.id.btn_submit);


        if (Connectivity.isNetworkAvailable(Teacher_Edit_Activity.this)) {

            new TeacherEditExecuteTask().execute();
        }else {
            Toast.makeText(Teacher_Edit_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Connectivity.isNetworkAvailable(Teacher_Edit_Activity.this)) {

                         name = teacher_name.getText().toString();
                         pass = teacher_pw.getText().toString();
                         mobile = teacher_mobile.getText().toString();
                         email = teacher_email.getText().toString();
                         address = teacher_address.getText().toString();


                        new TeacherUpdateExecuteTask().execute();

                }else {
                    Toast.makeText(Teacher_Edit_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
//***************************************************************************************
    private class TeacherEditExecuteTask  extends AsyncTask<String, Void, String> {

    String output = "";
    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(Teacher_Edit_Activity.this);
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {

        try {

            server_url = "https://jntrcpl.com/theoji/index.php/Api/get_student_detail?id="
                    +getIntent().getStringExtra("TId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sever_url>>>", server_url);
        output = HttpHandler.makeServiceCall(server_url);
        System.out.println("getcomment_url" + output);
        return output;
    }


    @Override
    protected void onPostExecute(String output) {
        if (output == null) {
            dialog.dismiss();
        } else {
            try {
                dialog.dismiss();

                JSONObject responce = new JSONObject(output);
                String res = responce.getString("responce");
                JSONObject dataJsonObject = new JSONObject(output);

                if (res.equals("true")) {

                    JSONArray Data_array = dataJsonObject.getJSONArray("data");
                    for (int i = 0; i < Data_array.length(); i++) {
                        JSONObject c = Data_array.getJSONObject(i);

                        String user_id=c.getString("user_id");
                        teacher_name.setText(c.getString("firstname"));
                        teacher_email.setText(c.getString("email"));
                        teacher_mobile.setText(c.getString("mobileno"));
                        teacher_address.setText(c.getString("address"));

                    }


                }else {

                    Toast.makeText(Teacher_Edit_Activity.this, "no details update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //  dialog.dismiss();
            }
            super.onPostExecute(output);
        }

    }

    }

    private class TeacherUpdateExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Teacher_Edit_Activity.this);
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
                Log.e("result",result);
                try {
                   // Toast.makeText(Teacher_Edit_Activity.this, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//                    String email=data.getString("firstname");
//                    String mobile=data.getString("lastname");
//                    String pass=data.getString("email");
//                    String alotclass=data.getString("mobileno");
//                    String address=data.getString("date");



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(Teacher_Edit_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Teacher_Edit_Activity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Teacher_Edit_Activity.this, TeacherActivity.class);
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

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/update_teacher");

            JSONObject postDataParams = new JSONObject();

            postDataParams.put("firstname",name);
            postDataParams.put("email",email);
            postDataParams.put("mobileno",mobile);
            postDataParams.put("password",pass);
            postDataParams.put("address",address);
            postDataParams.put("user_id",getIntent().getStringExtra("TId"));


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
