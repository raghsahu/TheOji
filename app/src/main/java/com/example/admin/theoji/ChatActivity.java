package com.example.admin.theoji;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.HomeAdapter;
import com.example.admin.theoji.Adapter.StudentListAdapter;
import com.example.admin.theoji.Adapter.Student_Chat_Adapter;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ChatStudent_Model;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.SectionModel;
import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

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

    RecyclerView recyclerchat;
    String server_url;
    String Student;
    private ArrayList<ChatStudent_Model> studentList=new ArrayList<>();
    private Student_Chat_Adapter student_chat_adapter;
    public static HashMap<Integer , String> studentHashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        spin_class=(Spinner)findViewById(R.id.search_class1);
        spin_section = (Spinner)findViewById(R.id.search_section1);
        recyclerchat = (RecyclerView) findViewById(R.id.recycler_chat_stud);

        new spinnerClassExecuteTask().execute();
//***************************************************************
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

                //strSid = stateList.get(position).getState_id();
                try{

                    if(studentList.size() !=0)
                    {
                        studentList.clear();

                        recyclerchat.setAdapter(null);
                        student_chat_adapter.notifyDataSetChanged();
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
                    + AppPreference.getUserid(ChatActivity.this);

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

                        classListAdapter = new ArrayAdapter<String>(ChatActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);


                    } else {
                        Toast.makeText(ChatActivity.this, "no class found", Toast.LENGTH_SHORT).show();
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

                      sectionListAdapter = new ArrayAdapter<String>(ChatActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                      sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      spin_section.setAdapter(sectionListAdapter);

                  }else {

//                      sectionList.clear();
//                      sectionListAdapter.notifyDataSetChanged();
//                      sectionListAdapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_spinner_item, ChooseSection);
//                      spin_section.setAdapter(sectionListAdapter);


                      Toast.makeText(ChatActivity.this, "no section found", Toast.LENGTH_SHORT).show();
                  }
                  super.onPostExecute(output);
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }

      }
  }


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

                            studentList.add(new ChatStudent_Model(user_id, firstname,lastname));
                            studentHashMap.put(i , user_id);
                        }
                        student_chat_adapter = new Student_Chat_Adapter(ChatActivity.this, studentList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                        recyclerchat.setLayoutManager(mLayoutManager);
                        recyclerchat.setItemAnimator(new DefaultItemAnimator());
                        recyclerchat.setAdapter(student_chat_adapter);

                       // reloadAllData();

                    }else {
                        studentList.clear();
                       student_chat_adapter.notifyDataSetChanged();
                        recyclerchat.removeAllViews();
                        recyclerchat.setAdapter(student_chat_adapter);

                        Toast.makeText(ChatActivity.this, "no student found", Toast.LENGTH_SHORT).show();
                    }

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void reloadAllData() {
    }
}
