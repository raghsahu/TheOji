package com.example.admin.theoji;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.Student_Chat_Adapter;
import com.example.admin.theoji.Adapter.Student_Perform_Adapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ChatStudent_Model;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.SectionModel;
import com.example.admin.theoji.ModelClass.Student_Perform_Model;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Performance_Activity extends AppCompatActivity {

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

    private ArrayList<Student_Perform_Model> student_perform_List=new ArrayList<>();
    private Student_Perform_Adapter student_perform_adapter;
    public static HashMap<Integer , String> student_perform_HashMap = new HashMap<>();


    ImageView view_performance;
    RecyclerView recyclercPerformance;
    Button find_performance;
     String ClassID,SectionID;
    int btnInt=0;
    String sever_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_);

        view_performance=findViewById(R.id.view_performance);
        spin_class=(Spinner)findViewById(R.id.search_class1);
        spin_section = (Spinner)findViewById(R.id.search_section1);
        recyclercPerformance = (RecyclerView) findViewById(R.id.recycler_performance);
        find_performance =findViewById(R.id.find_seach);

        view_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Performance_Activity.this, Add_Performance.class);
                startActivity(intent);
                finish();
            }
        });

        if (Connectivity.isNetworkAvailable(Performance_Activity.this)){
            new spinnerClassExecuteTask().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        if (Connectivity.isNetworkAvailable(Performance_Activity.this)){
            new Stud_performaceTask().execute();

        }else {
            Toast.makeText(Performance_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

//***********************************************

        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                        ClassID=ClassHashMap.get(i).getM_id();
                        new SectionExecuteTask(ClassHashMap.get(i).getM_id()).execute();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
//*****************************************************
        spin_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try{
//
//                    if(studentList.size() !=0)
//                    {
//                        studentList.clear();
//
//                        recyclerchat.setAdapter(null);
//                        student_chat_adapter.notifyDataSetChanged();
//                    }
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }

                for (int i = 0; i < SectionHashMap.size(); i++)
                {

                    if (SectionHashMap.get(i).getM_name().equals(spin_section.getItemAtPosition(position)))
                    {
                        // new spinnerStudentExecuteTask(SectionHashMap.get(i).getM_id()).execute();
                        SectionID=SectionHashMap.get(i).getM_id();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        //***********************************************

        find_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnInt=1;
                try{

                    if(student_perform_List.size() !=0)
                    {
                        student_perform_List.clear();

                        recyclercPerformance.setAdapter(null);
                        student_perform_adapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (Connectivity.isNetworkAvailable(Performance_Activity.this)){
                    new Stud_performaceTask().execute();

                }else {
                    Toast.makeText(Performance_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
//***************************************************************************
private class spinnerClassExecuteTask extends AsyncTask<String, Integer,String> {

    String output = "";
    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }
    @Override
    protected String doInBackground(String... params) {

        String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_class?school_id="
                + AppPreference.getUserid(Performance_Activity.this);

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
                        ClassHashMap.put(i, new ClassModel(m_id, m_name));

                    }

                    classListAdapter = new ArrayAdapter<String>(Performance_Activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                    classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_class.setAdapter(classListAdapter);


                } else {
                    Toast.makeText(Performance_Activity.this, "no class found", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
    ///*****************************************
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

                        sectionListAdapter = new ArrayAdapter<String>(Performance_Activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                        sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_section.setAdapter(sectionListAdapter);

                    }else {

//                      sectionList.clear();
//                      sectionListAdapter.notifyDataSetChanged();
//                      sectionListAdapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_spinner_item, ChooseSection);
//                      spin_section.setAdapter(sectionListAdapter);


                        Toast.makeText(Performance_Activity.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
//**************************************************************************
    private class Stud_performaceTask extends AsyncTask<String, Integer,String> {

    String output = "";

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        if (btnInt==1){
            sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_chat_user?school_id="+
                    AppPreference.getUserid(Performance_Activity.this)+"&class="+
                    ClassID+"&section="+SectionID;
        }
        else {
            sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_update_performance?school_id="+
                    AppPreference.getUserid(Performance_Activity.this);
        }

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
               // String res=object.getString("responce");

                if (!output.isEmpty()) {

                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String performance_id = jsonObject1.getString("performance_id");
                        String school_id = jsonObject1.getString("school_id");
                        String user_id = jsonObject1.getString("user_id");
                        String January = jsonObject1.getString("January");
                        String February = jsonObject1.getString("February");
                        String March = jsonObject1.getString("March");
                        String April = jsonObject1.getString("April");
                        String May = jsonObject1.getString("May");
                        String June = jsonObject1.getString("June");
                        String July = jsonObject1.getString("July");
                        String August = jsonObject1.getString("August");
                        String September = jsonObject1.getString("September");
                        String October = jsonObject1.getString("October");
                        String November = jsonObject1.getString("November");
                        String December = jsonObject1.getString("December");
                        String firstname = jsonObject1.getString("firstname");
                        String umeta_value = jsonObject1.getString("umeta_value");

                        student_perform_List.add(new Student_Perform_Model(performance_id, user_id,January,February,March,April,May
                        ,June,July,August,September,October,November,December,firstname,umeta_value));
                        student_perform_HashMap.put(i , user_id);
                    }
                    student_perform_adapter = new Student_Perform_Adapter(Performance_Activity.this, student_perform_List);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Performance_Activity.this);
                    recyclercPerformance.setLayoutManager(mLayoutManager);
                    recyclercPerformance.setItemAnimator(new DefaultItemAnimator());
                    recyclercPerformance.setAdapter(student_perform_adapter);


                }else {
                    student_perform_List.clear();
                    student_perform_adapter.notifyDataSetChanged();
                    recyclercPerformance.removeAllViews();
                    recyclercPerformance.setAdapter(student_perform_adapter);

                    Toast.makeText(Performance_Activity.this, "no student found", Toast.LENGTH_SHORT).show();
                }

                super.onPostExecute(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
}
