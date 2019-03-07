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

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class AddPayFeesActivity extends AppCompatActivity {

    Spinner spin_class;
//    private ArrayAdapter<String> classAdapter;
//    private ArrayList<String> classList;
    String Class;

    Spinner spin_class1;
    String Class1;

    Spinner spin_student;
    ArrayList<String>ChooseStudent;
    private ArrayList<StudentModel> studentList;
    private ArrayAdapter<String> StudentAdapter;
    String Student;


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

//        classList = new ArrayList<>();
////        new spinnerClassExecuteTask().execute();
//        classList.add("Select Class");
//        classList.add("Nursery");
//        classList.add("KG1");
//        classList.add("KG2");
//        classList.add("1");
//        classList.add("2");
//        classList.add("3");
//        classList.add("4");
//        classList.add("5");
//        classList.add("6");
//        classList.add("7");
//        classList.add("8");
//        classList.add("9");
//        classList.add("10");
//        classList.add("11 Mathes");
//        classList.add("11 Bio");
//        classList.add("11 Commerce");
//
//        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Class = classAdapter.getItem(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        classAdapter = new ArrayAdapter<String>(AddPayFeesActivity.this, R.layout.support_simple_spinner_dropdown_item, classList);
//        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin_class.setAdapter(classAdapter);
        //**********************************************
        ChooseStudent = new ArrayList<>();
        studentList = new ArrayList<>();
        new spinnerStudentExecuteTask().execute();

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
                Student=spin_student.getSelectedItem().toString();

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
    public class spinnerStudentExecuteTask extends AsyncTask<String, Integer,String>{

        String output = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/student_list?id="+AppPreference.getUserid(AddPayFeesActivity.this);


            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                   //Toast.makeText(AddPayFeesActivity.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object=new JSONObject(output);

                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String user_id = jsonObject1.getString("user_id");
//                        String username = jsonObject1.getString("password");
//                        String showpass = jsonObject1.getString("showpass");
//                        String st_pass = jsonObject1.getString("stu_password");
                        String firstname = jsonObject1.getString("firstname");
//                        String lastname = jsonObject1.getString("lastname");
//                        String email = jsonObject1.getString("email");
//                        String mobile = jsonObject1.getString("mobileno");
//                        String date = jsonObject1.getString("date");
//                        String user_type = jsonObject1.getString("user_type");
//                        String gender = jsonObject1.getString("gender");
//                        String dob = jsonObject1.getString("dob");
//                        String city = jsonObject1.getString("city");
//                        String address = jsonObject1.getString("address");
//                        String status = jsonObject1.getString("status");
//                        String block_unblock = jsonObject1.getString("block_unblock");
//                        String ref_id = jsonObject1.getString("ref_id");
//                        String profileupdate = jsonObject1.getString("profileupdate");
//                        String about = jsonObject1.getString("about");
//                        String latest_post = jsonObject1.getString("latest_post");
//                        String latest_event = jsonObject1.getString("latest_event");
//                        String school_code = jsonObject1.getString("school_code");
//                        String notice = jsonObject1.getString("notice");
//                        String latest_activities = jsonObject1.getString("latest_activities");
//                        String stutotalfees = jsonObject1.getString("stutotalfees");
//                        String latest_news = jsonObject1.getString("latest_news");
//                        String sturemainfee = jsonObject1.getString("sturemainfee");
//                        String latest_noticboard = jsonObject1.getString("latest_noticboard");
//                        String stulastfee = jsonObject1.getString("stulastfee");
//                        String sales_lead_name = jsonObject1.getString("sales_lead_name");

                        studentList.add(new StudentModel(user_id, firstname));
                        ChooseStudent.add(firstname);

                    }

                    StudentAdapter = new ArrayAdapter<String>(AddPayFeesActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseStudent);
                    StudentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_student.setAdapter(StudentAdapter);

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
}
