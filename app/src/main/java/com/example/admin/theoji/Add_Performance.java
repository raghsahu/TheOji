package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.StudentListAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.SectionModel;
import com.example.admin.theoji.ModelClass.StudentModel;
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

public class Add_Performance extends AppCompatActivity {

    Spinner spin_student;
    ArrayList<StudentModel> studentList=new ArrayList<StudentModel>();
    private ArrayAdapter<String> ParentAdapter;
    ArrayList<String> ChooseStudent=new ArrayList<>();


    Spinner spin_class;
    ArrayList<String> ChooseClass =new ArrayList<>();
    private ArrayList<ClassModel> classList=new ArrayList<>();
    private ArrayAdapter<String> classListAdapter;

    Spinner spin_section;
    ArrayList<String> ChooseSection =new ArrayList<>();
    private ArrayList<SectionModel> sectionList=new ArrayList<>();
    private ArrayAdapter<String> sectionListAdapter;

    public HashMap<Integer, ClassModel> ClassHashMap = new HashMap<Integer, ClassModel>();
    public HashMap<Integer, SectionModel> SectionHashMap = new HashMap<Integer, SectionModel>();
    HashMap<Integer, StudentModel> ParentHashMap=new HashMap<>();

    Spinner spin_month;

    EditText et_perform_num;
    Button Btn_perform;
    String Spin_Class, Spin_Student,Spin_Section,Et_perform_num;
    String StudentID, Spin_Month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__performance);

        spin_class = (Spinner)findViewById(R.id.spin_class);
        spin_student = (Spinner)findViewById(R.id.class_students);
        spin_section = (Spinner)findViewById(R.id.stud_section);
        spin_month = (Spinner)findViewById(R.id.spin_month);
        et_perform_num = findViewById(R.id.et_perform_num);
        Btn_perform = findViewById(R.id.btn_perform);


            if (Connectivity.isConnected(Add_Performance.this)){
                new spinnerClassExecuteTask().execute();

            }else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }


        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //strSid = stateList.get(position).getState_id();
                try{
                    if(sectionList.size() !=0)
                    {
                        ChooseSection.clear();

                        spin_section.setAdapter(null);
                        sectionListAdapter.notifyDataSetChanged();

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < ClassHashMap.size(); i++)
                {

                    if (ClassHashMap.get(i).getM_name().equals(spin_class.getItemAtPosition(position)))
                    {
                        new SectionExecuteTask(ClassHashMap.get(i).getM_id()).execute();
                    }
                    // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //******************************************************
        spin_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                    if(studentList.size() !=0)
                    {
                        ChooseStudent.clear();

                        studentList.clear();
                        spin_student.setAdapter(null);
                        ParentAdapter.notifyDataSetChanged();

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < SectionHashMap.size(); i++)
                {

                    if (SectionHashMap.get(i).getM_name().equals(spin_section.getItemAtPosition(position)))
                    {
                        new spinnerStudentExecuteTask(SectionHashMap.get(i).getM_id()).execute();
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
//**********************************************
        spin_student.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < ParentHashMap.size(); i++)
                {

                    if (ParentHashMap.get(i).getFirstname().equals(spin_student.getItemAtPosition(position)))
                    {
                        StudentID=ParentHashMap.get(i).getUser_id();
                        Toast.makeText(Add_Performance.this, "s_Id"+StudentID, Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
     //**********************************************************************
        Btn_perform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Et_perform_num =et_perform_num.getText().toString();
               Spin_Month =spin_month.getSelectedItem().toString();


                if (Connectivity.isNetworkAvailable(Add_Performance.this)) {
                    if (!Et_perform_num.isEmpty() && !Spin_Month.equals("-Select month-")) {

                        new Add_Update_PerformTask().execute();
                    }else {
                        if (Et_perform_num.isEmpty()){
                            et_perform_num.setError("Please Enter Number");
                        }
                        if (Spin_Month.equals("-Select month-")){
                            Toast.makeText(Add_Performance.this, "Please select month", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else {
                    Toast.makeText(Add_Performance.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });






//***************************************************
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
                    + AppPreference.getUserid(Add_Performance.this);


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

                            classList.add(new ClassModel(m_id, m_name));
                            ChooseClass.add(m_name);
                            ClassHashMap.put(i, new ClassModel(m_id,m_name));

                        }

                        classListAdapter = new ArrayAdapter<String>(Add_Performance.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);

                    }else {
                        Toast.makeText(Add_Performance.this, "no class found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //***************************************************************************
    private class SectionExecuteTask extends AsyncTask<String,Integer,String> {

        String output = "";

        String strMId;

        public SectionExecuteTask(String m_id) {
            this.strMId=m_id;
        }

        @Override
        protected String doInBackground(String... params) {
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_section?m_id="+strMId;

            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object=new JSONObject(output);
                    String res=object.getString("responce");

                    if (res.equals("true")) {

                        JSONArray jsonArray = object.getJSONArray("section");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String m_id = jsonObject1.getString("m_id");
                            String m_name = jsonObject1.getString("m_name");
                            String type = jsonObject1.getString("type");
                            String parent = jsonObject1.getString("parent");
                            String school_id = jsonObject1.getString("school_id");

                            sectionList.add(new SectionModel(m_id, m_name));
                            SectionHashMap.put(i, new SectionModel(m_id,m_name));
                            ChooseSection.add(m_name);

                        }

                        sectionListAdapter = new ArrayAdapter<String>(Add_Performance.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                        sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_section.setAdapter(sectionListAdapter);


                        // reloadAllData();

                    }else {
                        Toast.makeText(Add_Performance.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        }

        //*********************************************


    public class spinnerStudentExecuteTask extends AsyncTask<String, Integer,String> {

        String output = "";
        String M_id;

        public spinnerStudentExecuteTask(String m_id) {
            this.M_id=m_id;
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_student_by_section?section_id="+ M_id;


            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {
                    int p;
//                    Toast.makeText(RegistrationActivity.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(output);
                    String res=object.getString("responce");

                    if (res.equals("true")) {

                        JSONArray jsonArray = object.getJSONArray("student");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String user_id = jsonObject1.getString("user_id");
                            String firstname = jsonObject1.getString("firstname");
                            String lastname = jsonObject1.getString("lastname");

                            studentList.add(new StudentModel(user_id, firstname));
                            ChooseStudent.add(firstname);
                            ParentHashMap.put(i, new StudentModel(user_id, firstname));

                        }
                        ParentAdapter = new ArrayAdapter<String>(Add_Performance.this, android.R.layout.simple_spinner_dropdown_item, ChooseStudent);
                        ParentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_student.setAdapter(ParentAdapter);

                    }else {
                        try{

                            ChooseStudent.clear();
                            ParentAdapter = new ArrayAdapter<String>(Add_Performance.this, android.R.layout.simple_spinner_dropdown_item, ChooseStudent);
                            spin_student.setAdapter(ParentAdapter);
                            ParentAdapter.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Toast.makeText(Add_Performance.this, "no student found", Toast.LENGTH_SHORT).show();
                    }


                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        }
//*****************************************************************
    private class Add_Update_PerformTask extends AsyncTask<String, Integer, String> {
    ProgressDialog dialog;

    protected void onPreExecute() {
        dialog = new ProgressDialog(Add_Performance.this);
        dialog.show();

    }

    @Override
    protected String doInBackground(String... params) {

        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/update_performance");

            String id= AppPreference.getUserid(Add_Performance.this);
            JSONObject postDataParams = new JSONObject();

            postDataParams.put("school_id", id);
            postDataParams.put("student_id", StudentID);
             postDataParams.put("month",Spin_Month);
            postDataParams.put("performance",Et_perform_num);



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
                 Toast.makeText(Add_Performance.this, "result is" + result, Toast.LENGTH_SHORT).show();


                JSONObject object = new JSONObject(result);
                String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//



                if (!res.equalsIgnoreCase("true")) {

                    Toast.makeText(Add_Performance.this, "unsuccess", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Add_Performance.this, "success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Add_Performance.this, Performance_Activity.class);
                    startActivity(intent);
                    finish();

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
