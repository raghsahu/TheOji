package com.example.admin.theoji;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.StudentListAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static com.androidquery.util.AQUtility.getContext;

public class AttendenceActivity extends AppCompatActivity {

    Spinner spin_student;
    //ArrayList<String> ChooseStudent =new ArrayList<>();
    private ArrayList<StudentModel> studentList=new ArrayList<>();
    private StudentListAdapter studentListAdapter;


    Spinner spin_class;
    ArrayList<String> ChooseClass =new ArrayList<>();
    private ArrayList<ClassModel> classList=new ArrayList<>();
    private ArrayAdapter<String> classListAdapter;

    Spinner spin_section;
    ArrayList<String> ChooseSection =new ArrayList<>();
    private ArrayList<SectionModel> sectionList=new ArrayList<>();
    private ArrayAdapter<String> sectionListAdapter;

    EditText attend_date,attend_remark;
    Spinner p_a;
    Button btn_send_attend;

    String Spin_Student,Spin_Class,Spin_Section,Attend_date,Spin_P_A,Attend_Remark;

    public HashMap<Integer, ClassModel> ClassHashMap = new HashMap<Integer, ClassModel>();
    public HashMap<Integer, SectionModel> SectionHashMap = new HashMap<Integer, SectionModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        spin_student = (Spinner)findViewById(R.id.class_students);
        spin_class=(Spinner)findViewById(R.id.spin_class);
        attend_date=(EditText)findViewById(R.id.attendence_date);
        attend_remark=(EditText)findViewById(R.id.attendence_remark);
        p_a=findViewById(R.id.students_p_a);
        spin_section = (Spinner)findViewById(R.id.stud_section);
        btn_send_attend=(Button) findViewById(R.id.btn_attend);
        //******************************************************************
        btn_send_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spin_Student=spin_student.getSelectedItem().toString();
                Spin_Class=spin_class.getSelectedItem().toString();
                Spin_P_A=p_a.getSelectedItem().toString();
                Spin_Section=spin_section.getSelectedItem().toString();
                Attend_date=attend_date.getText().toString();
                Attend_Remark=attend_remark.getText().toString();

                new AttendanceExecuteTask().execute();

            }
        });

        //*******************************************************************
        new spinnerClassExecuteTask().execute();

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

//***********************************************************
       // new spinnerStudentExecuteTask().execute();

        attend_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (attend_date.getRight() - attend_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                        DatePickerDialog mDatePicker = new DatePickerDialog(AttendenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                                String myFormat = "dd/MM/yyyy"; //Change as you need
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                                attend_date.setText(sdf.format(myCalendar.getTime()));

                                mDay[0] = selectedday;
                                mMonth[0] = selectedmonth;
                                mYear[0] = selectedyear;
                            }
                        }, mYear[0], mMonth[0], mDay[0]);
                        //mDatePicker.setTitle("Select date");
                        mDatePicker.show();


                        return true;
                    }
                }
                return false;
            }
        });

        //********************************************************************
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

                            studentList.add(new StudentModel(user_id, firstname));
                           // ChooseStudent.add(firstname);
//                            if ( i==0){
//                                studentList.add("select all");
//                            }

                        }

                        studentListAdapter = new StudentListAdapter(AttendenceActivity.this, android.R.layout.simple_spinner_item, studentList);
                        //StudentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        // spin_student.setAdapter(StudentAdapter,false, onSelectedListener);
                        spin_student.setAdapter(studentListAdapter);

                    }else {
                        try{

                               // ChooseStudent.clear();
                                studentList.clear();
                            studentListAdapter = new StudentListAdapter(AttendenceActivity.this, android.R.layout.simple_spinner_item, studentList);
                                spin_student.setAdapter(studentListAdapter);
                                studentListAdapter.notifyDataSetChanged();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                            Toast.makeText(AttendenceActivity.this, "no student found", Toast.LENGTH_SHORT).show();
                        }

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//*******************************************************************
    private class spinnerClassExecuteTask extends AsyncTask<String, Integer,String> {

        String output = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_class?school_id="
                    +AppPreference.getUserid(AttendenceActivity.this);


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

                    classListAdapter = new ArrayAdapter<String>(AttendenceActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                    classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_class.setAdapter(classListAdapter);

                    }else {
                        Toast.makeText(AttendenceActivity.this, "no class found", Toast.LENGTH_SHORT).show();
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

                        sectionListAdapter = new ArrayAdapter<String>(AttendenceActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                       sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_section.setAdapter(sectionListAdapter);


                        // reloadAllData();

                    }else {
                        Toast.makeText(AttendenceActivity.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
//*****************************************************************
    private class AttendanceExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AttendenceActivity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

//            String res = PostData(params);
//
//            return res;
            try {


                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/student_attendance");

                JSONObject postDataParams = new JSONObject();
                String id = AppPreference.getUserid(AttendenceActivity.this);

                postDataParams.put("id", id);

                postDataParams.put("school_id", "AD2565");
                postDataParams.put("teacher_id", id);
                postDataParams.put("class", Spin_Class);
                postDataParams.put("attendance", Spin_Student);
                postDataParams.put("type", Spin_P_A);
                postDataParams.put("remark", Attend_Remark);
                postDataParams.put("date", Attend_date);
                postDataParams.put("section", Spin_Section);


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
                    Toast.makeText(AttendenceActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject responce = new JSONObject(result);
                    String res = responce.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//                    String email=data.getString("firstname");
//                    String mobile=data.getString("lastname");
//                    String pass=data.getString("email");
//                    String alotclass=data.getString("mobileno");
//                    String address=data.getString("date");



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(AttendenceActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AttendenceActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AttendenceActivity.this, ShowAttendenceActivity.class);
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
