package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

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

public class Forgot_activity extends AppCompatActivity {

    EditText forget_email;
     Button btn_forget;
     String ForgetEmaill;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);


        forget_email=findViewById(R.id.forget_email);
        btn_forget=findViewById(R.id.btn_forget);

    btn_forget.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ForgetEmaill=forget_email.getText().toString();

            if (!ForgetEmaill.isEmpty()){

                if (Connectivity.isNetworkAvailable(Forgot_activity.this)){
                    new ForgetPwTask().execute();
                }else {
                    Toast.makeText(Forgot_activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        }
    });

    }

    public class ForgetPwTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Forgot_activity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

//            String res = PostData(params);
//
//            return res;
            try {


                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/forgetpass");

                JSONObject postDataParams = new JSONObject();
//                String id = AppPreference.getUserid(Forgot_activity.this);

                postDataParams.put("email", ForgetEmaill);

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
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());

            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    Toast.makeText(Forgot_activity.this, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject responce = new JSONObject(result);
                    String res = responce.getString("responce");

                    if (res.equals("true")) {

                        Toast.makeText(Forgot_activity.this, "Password send Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Forgot_activity.this, ShowAttendenceActivity.class);
//                        startActivity(intent);
//                        finish();

                    } else {
                        Toast.makeText(Forgot_activity.this, "Please try again", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
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
