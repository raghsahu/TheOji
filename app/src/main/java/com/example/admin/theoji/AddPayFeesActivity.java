package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static com.example.admin.theoji.Adapter.StudentListAdapter.multiselected;

public class AddPayFeesActivity extends AppCompatActivity {

    Spinner spin_class,spin_class1;
    ArrayList<String> ChooseClass =new ArrayList<>();
    private ArrayList<ClassModel> classList=new ArrayList<>();
    private ArrayAdapter<String> classListAdapter;

    Spinner spin_section;
    ArrayList<String> ChooseSection =new ArrayList<>();
    private ArrayList<SectionModel> sectionList=new ArrayList<>();
    private ArrayAdapter<String> sectionListAdapter;

    String Class;
    String Class1,Spin_Section;

    String Student;
    Spinner spin_student;
    private ArrayList<StudentModel> studentList=new ArrayList<>();
    private StudentListAdapter studentListAdapter;

    public HashMap<Integer, ClassModel> ClassHashMap = new HashMap<Integer, ClassModel>();
    public HashMap<Integer, SectionModel> SectionHashMap = new HashMap<Integer, SectionModel>();

    LinearLayout layout;
    Button btn_submit, btn_payfees;
    EditText annual_fees, pay_fees;
//    Spinner spin_class_type;

  String Anual_fees,Pay_fees;

    String user_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_add_fees);

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

        annual_fees = (EditText) findViewById(R.id.annualfees);
        pay_fees=(EditText)findViewById(R.id.et_payfees);

        spin_class=(Spinner)findViewById(R.id.classess2);
        spin_class1=(Spinner)findViewById(R.id.classess22);
        spin_student = (Spinner)findViewById(R.id.classess23);
        spin_section = (Spinner)findViewById(R.id.stud_section);

        //**********************************************
        studentList = new ArrayList<>();
       // new spinnerStudentExecuteTask().execute();

        new spinnerClassExecuteTask().execute();

        spin_class1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                    if (ClassHashMap.get(i).getM_name().equals(spin_class1.getItemAtPosition(position)))
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
//*****************************************************
        spin_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //strSid = stateList.get(position).getState_id();
//                try{
//
//                    if(studentList.size() !=0)
//                    {
//                        ChooseStudent.clear();
//
//                        spin_student.setAdapter(null);
//                        studentListAdapter.notifyDataSetChanged();
//
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
                for (int i = 0; i < SectionHashMap.size(); i++)
                {

                    if (SectionHashMap.get(i).getM_name().equals(spin_section.getItemAtPosition(position)))
                    {
                        new spinnerStudentExecuteTask(SectionHashMap.get(i).getM_id()).execute();
                    }
                    // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

//****************************************************************************************

        btn_submit  = (Button)findViewById(R.id.btn_class_addfees);
        btn_payfees=(Button)findViewById(R.id.btn_payfees);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Anual_fees =annual_fees.getText().toString();
           Class=spin_class.getSelectedItem().toString();

                if (Connectivity.isNetworkAvailable(AddPayFeesActivity.this)) {
                    if (validate()) {
                      Anual_fees =annual_fees.getText().toString();
                      Class=spin_class.getSelectedItem().toString();

                        new AddPayFeesExecuteTask().execute();
                    }
                }else {
                    Toast.makeText(AddPayFeesActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    //********************************************************
        btn_payfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pay_fees =pay_fees.getText().toString();
                Class1=spin_class1.getSelectedItem().toString();
                Spin_Section=spin_section.getSelectedItem().toString();
               // Student=spin_student.getSelectedItem().toString();
                StringBuilder commaSepValueBuilder = new StringBuilder();

                //Looping through the list
                for ( int i = 0; i< multiselected.size(); i++){
                    //append the value into the builder
                    commaSepValueBuilder.append(multiselected.get(i));

                    //if the value is not the last element of the list
                    //then append the comma(,) as well
                    if ( i != multiselected.size()-1){
                        commaSepValueBuilder.append(", ");
                    }
                }
                System.out.println(commaSepValueBuilder.toString());
                Student=commaSepValueBuilder.toString();
                Toast.makeText(AddPayFeesActivity.this, ""+Student , Toast.LENGTH_SHORT).show();

                if (Connectivity.isNetworkAvailable(AddPayFeesActivity.this)) {
                    if (validate1()) {
                        Pay_fees =annual_fees.getText().toString();
                        Class1=spin_class1.getSelectedItem().toString();
                        Student=spin_student.getSelectedItem().toString();

                        new PayFeesExecuteTask().execute();
                    }
                }else {
                    Toast.makeText(AddPayFeesActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//********************************************
    class AddPayFeesExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddPayFeesActivity.this);
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
               // Toast.makeText(AddPayFeesActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(AddPayFeesActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddPayFeesActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPayFeesActivity.this, PayFeesActivity.class);
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

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/feesaddbyclass");

            String id= AppPreference.getUserid(AddPayFeesActivity.this);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Class_type", spin_class.getSelectedItem().toString());
            postDataParams.put("Annual_fees", annual_fees.getText().toString());
            postDataParams.put("Select_student_name",spin_student.getSelectedItem().toString());
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
//*******************************************************************

    //*****************************************************************
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

                            studentList.add(new StudentModel(user_id, firstname));
                            // ChooseStudent.add(firstname);
//                            if ( i==0){
//                                studentList.add("select all");
//                            }

                        }

                        studentListAdapter = new StudentListAdapter(AddPayFeesActivity.this, android.R.layout.simple_spinner_item, studentList);
                        //StudentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        // spin_student.setAdapter(StudentAdapter,false, onSelectedListener);
                        spin_student.setAdapter(studentListAdapter);

                    }else {
                        try{

                            // ChooseStudent.clear();
                            studentList.clear();
                            studentListAdapter = new StudentListAdapter(AddPayFeesActivity.this, android.R.layout.simple_spinner_item, studentList);
                            spin_student.setAdapter(studentListAdapter);
                            studentListAdapter.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Toast.makeText(AddPayFeesActivity.this, "no student found", Toast.LENGTH_SHORT).show();
                    }

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //*******************************************************************
    class PayFeesExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddPayFeesActivity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData1(params);

            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    // Toast.makeText(AddPayFeesActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(AddPayFeesActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddPayFeesActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPayFeesActivity.this, PayFeesActivity.class);
                        startActivity(intent);
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String PostData1(String[] values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/feesaddbyclass");

            String id= AppPreference.getUserid(AddPayFeesActivity.this);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Class_type", spin_class1.getSelectedItem().toString());
            postDataParams.put("Annual_fees", pay_fees.getText().toString());
            postDataParams.put("Select_student_name",Student);
            postDataParams.put("section",Spin_Section);
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



    //***************************************************
    public boolean validate() {
        boolean valid = false;

        String  Anual_fees =annual_fees.getText().toString();
        String   Spin_class=spin_class.getSelectedItem().toString();

        if (Spin_class.equals("-select class-")) {
            valid = false;
            Toast.makeText(AddPayFeesActivity.this,"please select class type!",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            valid = true;
            Spin_class = spin_class.getSelectedItem().toString();

        }
        if (Anual_fees.isEmpty()) {
            valid = false;
            annual_fees.setError("Please enter annual fees!");
        } else {
            valid = true;
            annual_fees.setError(null);
        }
        return valid;

    }
//********************************************************
public boolean validate1() {
    boolean valid = false;

    Pay_fees =pay_fees.getText().toString();
    Class1=spin_class1.getSelectedItem().toString();
    Student=spin_student.getSelectedItem().toString();

    if (Class1.equals("-select class-")) {
        valid = false;
        Toast.makeText(AddPayFeesActivity.this,"please select class type!",Toast.LENGTH_SHORT).show();
        return false;
    } else {
        valid = true;
        Class1 = spin_class1.getSelectedItem().toString();
    }
    if (Pay_fees.isEmpty()) {
        valid = false;
        pay_fees.setError("Please enter annual fees!");
        return false;
    } else {
        valid = true;
        pay_fees.setError(null);
    }
    return valid;

}
//************************************************************

    //*************************************************************
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
                    +AppPreference.getUserid(AddPayFeesActivity.this);


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

                        classListAdapter = new ArrayAdapter<String>(AddPayFeesActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);
                        spin_class1.setAdapter(classListAdapter);

                    }else {
                        Toast.makeText(AddPayFeesActivity.this, "no class found", Toast.LENGTH_SHORT).show();
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

                        sectionListAdapter = new ArrayAdapter<String>(AddPayFeesActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                        sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_section.setAdapter(sectionListAdapter);


                        // reloadAllData();

                    }else {
                        Toast.makeText(AddPayFeesActivity.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
