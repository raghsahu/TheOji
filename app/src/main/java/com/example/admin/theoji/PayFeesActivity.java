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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.PayfeesAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.ModelClass.SectionModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PayFeesActivity  extends AppCompatActivity {
    ImageView viewpayfees;
    RecyclerView recyclerpayfees;
    String server_url;
    ArrayList<PayfeesListModel> PayfeesList;
    private PayfeesAdapter payfeesAdapter;


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

   public static HashMap<Integer,PayfeesListModel>PayFeesHasMap=new HashMap<>();
     String ClassID;
    Button find_seach,annual_fees;
    int btnInt=0;
     String SectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);

        viewpayfees = (ImageView)findViewById(R.id.viewpayfees);
        spin_class=(Spinner)findViewById(R.id.search_class1);
        spin_section = (Spinner)findViewById(R.id.search_section1);
        find_seach=findViewById(R.id.find_seach);
        annual_fees=findViewById(R.id.annual_fees);

        if (AppPreference.getUser_Type(PayFeesActivity.this).equals("4")) {
            viewpayfees.setVisibility(View.GONE);
        }

        viewpayfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayFeesActivity.this,AddPayFeesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerpayfees = (RecyclerView)findViewById(R.id.recycler_view);

        int position=0;
        PayfeesList = new ArrayList<>();
        //******************************filter student************************

        if (Connectivity.isNetworkAvailable(PayFeesActivity.this)){
            new GetPayfeesList().execute();
            new spinnerClassExecuteTask().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        //**************************selected item********************

        annual_fees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(PayFeesActivity.this,AnnualFees_List.class);
            startActivity(intent);
            }
        });

        //********************************************************************
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
                        ClassID=ClassHashMap.get(i).getM_id();
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
//                    if(PayfeesList.size() !=0)
//                    {
//                        PayfeesList.clear();
//
//                        recyclerpayfees.setAdapter(null);
//                        payfeesAdapter.notifyDataSetChanged();
//                    }
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }

                for (int i = 0; i < SectionHashMap.size(); i++)
                {

                    if (SectionHashMap.get(i).getM_name().equals(spin_section.getItemAtPosition(position)))
                    {
                       // new sExecuteTask(SectionHashMap.get(i).getM_id()).execute();
                       // if (Connectivity.isNetworkAvailable(PayFeesActivity.this)){
                           //new GetPayfeesList().execute();
                           SectionID=SectionHashMap.get(i).getM_id();
//                        }else {
//                            Toast.makeText(PayFeesActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
//****************************************
       find_seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnInt=1;
                try{

                    if(PayfeesList.size() !=0)
                    {
                        PayfeesList.clear();

                        recyclerpayfees.setAdapter(null);
                        payfeesAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
           if (Connectivity.isNetworkAvailable(PayFeesActivity.this)){
            new GetPayfeesList().execute();
        }else {
            Toast.makeText(PayFeesActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

            }
        });

//**********************************************

    }

    class GetPayfeesList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private PayfeesListModel payfeesListModel;
        private Object viewHolder;

//        String M_id;
//        public GetPayfeesList(String m_id) {
//            this.M_id=m_id;
//        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(PayFeesActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                if (btnInt==1){
                    server_url = "https://jntrcpl.com/theoji/index.php/Api/studentfeesdetail?id="+
                            AppPreference.getUserid(PayFeesActivity.this)+"&class="+
                            ClassID+"&section="+SectionID;
                }
                else {
                    server_url = "https://jntrcpl.com/theoji/index.php/Api/studentfeesdetail?id="+
                            AppPreference.getUserid(PayFeesActivity.this);
                }

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

                    JSONObject responce = new JSONObject(output);
                    String res = responce.getString("responce");
                    JSONObject dataJsonObject = new JSONObject(output);

                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("data");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            String f_id = c.getString("user_id");
                            String admission_no = c.getString("admission_no");
                            String firstname = c.getString("firstname");
                            String sf_annualfess = c.getString("sf_annualfess");
                            String total_pay = c.getString("total_pay");
                            String due_payment = c.getString("due_payment");
                            String st_class = c.getString("class");
                            String last_pay_date1 = c.getString("last_pay_date");

                            String[] seperateData = last_pay_date1.split(Pattern.quote(","));
                            String  last_pay_date = seperateData[0];

                            PayfeesList.add(i, new PayfeesListModel(f_id, admission_no,firstname, sf_annualfess,
                                   total_pay,due_payment,st_class,last_pay_date));
                            PayFeesHasMap.put(i, new PayfeesListModel(f_id, admission_no,firstname, sf_annualfess,
                                    total_pay,due_payment,st_class,last_pay_date));
                        }


                        payfeesAdapter = new PayfeesAdapter(PayFeesActivity.this, PayfeesList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PayFeesActivity.this);
                        recyclerpayfees.setLayoutManager(mLayoutManager);
                        recyclerpayfees.setItemAnimator(new DefaultItemAnimator());
                        recyclerpayfees.setAdapter(payfeesAdapter);

//
                    }else {

                        Toast.makeText(PayFeesActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

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
                    + AppPreference.getUserid(PayFeesActivity.this);

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

                        classListAdapter = new ArrayAdapter<String>(PayFeesActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);


                    } else {
                        Toast.makeText(PayFeesActivity.this, "no class found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

                        sectionListAdapter = new ArrayAdapter<String>(PayFeesActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                        sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_section.setAdapter(sectionListAdapter);

                    }else {

//                      sectionList.clear();
//                      sectionListAdapter.notifyDataSetChanged();
//                      sectionListAdapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_spinner_item, ChooseSection);
//                      spin_section.setAdapter(sectionListAdapter);


                        Toast.makeText(PayFeesActivity.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

