package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
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

import javax.net.ssl.HttpsURLConnection;

public class AddClassActivity extends AppCompatActivity {

    Spinner spin_class;
    ArrayList<String> ChooseClass =new ArrayList<>();
    private ArrayList<ClassModel> classList=new ArrayList<>();
    private ArrayAdapter<String> classListAdapter;

    EditText Add_class, Add_section;
    Button btn_Add_class,btn_add_section;

    String S_Add_class,S_Add_secction,Spin_Class;
    public HashMap<Integer, ClassModel> ClassHashMap = new HashMap<Integer, ClassModel>();
    String Class_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        spin_class=findViewById(R.id.classess22);
        Add_class=findViewById(R.id.classname);
        Add_section=findViewById(R.id.et_class_section);
        btn_Add_class=findViewById(R.id.btn_class_add);
        btn_add_section=findViewById(R.id.btn_section_add);

        new spinnerClassExecuteTask().execute();

        btn_Add_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S_Add_class=Add_class.getText().toString();
                if (Connectivity.isNetworkAvailable(AddClassActivity.this)) {
                    if (!S_Add_class.isEmpty()){
                        new AddClassExecuteTask().execute();

                    }else {
                        Toast.makeText(AddClassActivity.this, "please enter class name", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
                }
            }
        });
//**************************************
        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < ClassHashMap.size(); i++)
                {

                    if (ClassHashMap.get(i).getM_name().equals(spin_class.getItemAtPosition(position)))
                    {

                        Class_ID=ClassHashMap.get(i).getM_id();
                        Toast.makeText(AddClassActivity.this, "class_Id"+Class_ID, Toast.LENGTH_SHORT).show();
                    }
                    // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
            //*****************************************
        btn_add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S_Add_secction=Add_section.getText().toString();
                Spin_Class=spin_class.getSelectedItem().toString();

                if (Connectivity.isNetworkAvailable(AddClassActivity.this)) {
                    if (!S_Add_secction.isEmpty() && !Spin_Class.equals("select")){
                        new AddClassExecuteTask().execute();
                    }else {
                        if (S_Add_secction.isEmpty()){
                            Toast.makeText(AddClassActivity.this, "please enter section", Toast.LENGTH_SHORT).show();
                        }if (Spin_Class.equals("select")){
                            Toast.makeText(AddClassActivity.this, "please select class", Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {
                    CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
                }
            }
        });
    }

    private class spinnerClassExecuteTask extends AsyncTask<String, Integer,String> {

        String output = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_class?school_id="
                    + AppPreference.getUserid(AddClassActivity.this);


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
                    String res=object.getString("responce");

                    if (res.equals("true")) {

                        JSONArray jsonArray = object.getJSONArray("class");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String m_id = jsonObject1.getString("m_id");
                            String m_name = jsonObject1.getString("m_name");
                            String type = jsonObject1.getString("type");
                            String parent = jsonObject1.getString("parent");
                            String school_id = jsonObject1.getString("school_id");


                            if ( i==0){
                                 //classList.add(new ClassModel("0","Select class"));
                                ChooseClass.add("select class");
                            }

                            classList.add(new ClassModel(m_id, m_name));
                            ChooseClass.add(m_name);
                            ClassHashMap.put(i, new ClassModel(m_id,m_name));

                        }

                        classListAdapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);


                    }else {
                        Toast.makeText(AddClassActivity.this, "no class found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//*************************************
    private class AddClassExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddClassActivity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/add_class_section");

                JSONObject postDataParams = new JSONObject();
                String id= AppPreference.getUserid(AddClassActivity.this);

                postDataParams.put("class",S_Add_class);
                postDataParams.put("school_id",id);

                postDataParams.put("section",S_Add_secction);
                postDataParams.put("parent",Class_ID);

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
                Toast.makeText(AddClassActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {
                        Toast.makeText(AddClassActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddClassActivity.this, ShowClassActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(AddClassActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();
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
