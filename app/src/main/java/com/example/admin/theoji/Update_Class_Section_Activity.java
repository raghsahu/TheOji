package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.ClassSectionAdapter;
import com.example.admin.theoji.Adapter.SectionAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassSectionListModel;
import com.example.admin.theoji.ModelClass.SectionListModel;
import com.example.admin.theoji.ModelClass.TeacherListModel;
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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Update_Class_Section_Activity extends AppCompatActivity {

    EditText edit_class;
    Button btn_update_class;

    RecyclerView recyclerschool_section;
    String server_url;
    ArrayList<SectionListModel> SectionList=new ArrayList<>();
    private SectionAdapter sectionAdapter;

    public static HashMap<Integer , SectionListModel> sectionHashMap = new HashMap<>();
     String ClassUpdate_id;
     String Edit_Class;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recyclerschool_section = (RecyclerView)findViewById(R.id.recycler_section);
        edit_class=findViewById(R.id.edit_class);
        btn_update_class=findViewById(R.id.btn_class_update);

        ClassUpdate_id=getIntent().getStringExtra("c_id");
        Toast.makeText(this, "C_U_id"+ClassUpdate_id, Toast.LENGTH_SHORT).show();

        if (Connectivity.isNetworkAvailable(Update_Class_Section_Activity.this)){
            new UpdateClassSectionList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        //************************************************
        btn_update_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Edit_Class =edit_class.getText().toString();

                if (Connectivity.isNetworkAvailable(Update_Class_Section_Activity.this)){
                    new UpdateClass().execute();
                }else {
                    Toast.makeText(Update_Class_Section_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class UpdateClassSectionList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private TeacherListModel teacherListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Update_Class_Section_Activity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/edit_class_section?class_id="+ClassUpdate_id ;


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            //   Log.e("getcomment_url", output);
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

                    JSONObject dataJsonObject = new JSONObject(output);
                    String res = dataJsonObject.getString("responce");


                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("class");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            String m_id = c.getString("m_id");
                            String m_name = c.getString("m_name");
                            String type = c.getString("type");
                            String parent = c.getString("parent");
                            String school_id = c.getString("school_id");

                            edit_class.setText(m_name);
                        }
                            JSONArray Data_array1 = dataJsonObject.getJSONArray("section");
                            for (int i = 0; i < Data_array1.length(); i++) {
                                JSONObject c = Data_array1.getJSONObject(i);

                                String m_id = c.getString("m_id");
                                String m_name = c.getString("m_name");
                                String type = c.getString("type");
                                String parent = c.getString("parent");
                                String school_id = c.getString("school_id");

                            SectionList.add(i, new SectionListModel(m_id,m_name,school_id));
                            sectionHashMap.put(i,new SectionListModel(m_id,m_name,school_id));
                        }


                        sectionAdapter = new SectionAdapter(Update_Class_Section_Activity.this, SectionList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Update_Class_Section_Activity.this);
                        recyclerschool_section.setLayoutManager(mLayoutManager);
                        recyclerschool_section.setItemAnimator(new DefaultItemAnimator());
                        recyclerschool_section.setAdapter(sectionAdapter);

//
                    }else {

                        Toast.makeText(Update_Class_Section_Activity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }

    private class UpdateClass extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Update_Class_Section_Activity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/update_class_section");

                JSONObject postDataParams = new JSONObject();
               // String id= AppPreference.getUserid(Update_Class_Section_Activity.this);

                postDataParams.put("class",Edit_Class);
                postDataParams.put("class_id",ClassUpdate_id);

//                postDataParams.put("section",S_Add_secction);
//                postDataParams.put("parent",Class_ID);

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
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    Toast.makeText(Update_Class_Section_Activity.this, "result is" + result, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {
                        Toast.makeText(Update_Class_Section_Activity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Update_Class_Section_Activity.this, ShowClassActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Update_Class_Section_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();
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
