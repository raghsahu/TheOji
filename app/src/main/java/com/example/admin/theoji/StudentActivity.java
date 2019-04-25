package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.StudentAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.SectionModel;
import com.example.admin.theoji.ModelClass.StudentListModel;
import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class StudentActivity extends AppCompatActivity {
    ImageView viewStudent;
    RecyclerView recyclerstudent;
    String server_url;
    ArrayList<StudentListModel> StudentList= new ArrayList<>();;
    private StudentAdapter studentAdapter;


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

    public static HashMap<Integer , StudentListModel> studentStringHashMap = new HashMap<>();
    Button find_stud;
     String ClassID;
     String SectionID;
     int btnInt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        viewStudent = (ImageView)findViewById(R.id.viewstudent);
        spin_class=(Spinner)findViewById(R.id.search_class1);
        spin_section = (Spinner)findViewById(R.id.search_section1);
        find_stud =findViewById(R.id.find_seach);

        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this,AddStudentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerstudent = (RecyclerView)findViewById(R.id.recycler_view_student);

        if (Connectivity.isNetworkAvailable(StudentActivity.this)){
            new spinnerClassExecuteTask().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }


        if (Connectivity.isNetworkAvailable(StudentActivity.this)){
            new GetStudentList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    //*******************************************************************************

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
//***********************************************************
        find_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnInt=1;
                try{

                    if(StudentList.size() !=0)
                    {
                        StudentList.clear();

                        recyclerstudent.setAdapter(null);
                        studentAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (Connectivity.isNetworkAvailable(StudentActivity.this)){
                    new GetStudentList().execute();

                }else {
                    Toast.makeText(StudentActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
//*********************************************************************************
    class GetStudentList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private StudentListModel studentListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(StudentActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/student_list?id="+ AppPreference.getUserid(StudentActivity.this);


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

                    JSONObject object = new JSONObject(output);
                   String res = object.getString("responce");

                    JSONObject dataJsonObject = new JSONObject(output);

                   if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("data");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            String user_id = c.getString("user_id");
                            String email = c.getString("email");
                            String firstname = c.getString("firstname");
                            String address = c.getString("address");
                            String mobileno = c.getString("mobileno");
                            String city = c.getString("city");
                            String status = c.getString("status");

                           String st_class = c.getString("umeta_value");
                           // String section = c.getString("umeta_value");

//                            String[] seperateData = st_class.split(Pattern.quote(","));
//                            for (int j = 0; j < seperateData.length; j++) {
//                                Log.e("Your st class Value-> ", seperateData[j]);
//                            }


                            StudentList.add(i, new StudentListModel(user_id, email, firstname, address,mobileno, city,st_class,status));
                           studentStringHashMap.put(i, new StudentListModel(user_id, email, firstname, address,mobileno, city,st_class,status));
                          //  Toast.makeText(StudentActivity.this, "stI "+studentStringHashMap.get(i).getUser_id(), Toast.LENGTH_SHORT).show();
                        }

                        studentAdapter = new StudentAdapter(StudentActivity.this, StudentList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StudentActivity.this);
                        recyclerstudent.setLayoutManager(mLayoutManager);
                        recyclerstudent.setItemAnimator(new DefaultItemAnimator());
                        recyclerstudent.setAdapter(studentAdapter);


                    }else {

                        Toast.makeText(StudentActivity.this, "no post update now, please check after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }

//***************************************************
    private class spinnerClassExecuteTask extends AsyncTask<String, Integer,String> {

        String output = "";
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {

            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_class?school_id="
                    + AppPreference.getUserid(StudentActivity.this);

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

                        classListAdapter = new ArrayAdapter<String>(StudentActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);


                    } else {
                        Toast.makeText(StudentActivity.this, "no class found", Toast.LENGTH_SHORT).show();
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

                        sectionListAdapter = new ArrayAdapter<String>(StudentActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                        sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_section.setAdapter(sectionListAdapter);

                    }else {

//                      sectionList.clear();
//                      sectionListAdapter.notifyDataSetChanged();
//                      sectionListAdapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_spinner_item, ChooseSection);
//                      spin_section.setAdapter(sectionListAdapter);


                        Toast.makeText(StudentActivity.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

