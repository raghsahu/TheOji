package com.example.admin.theoji;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;

import com.example.admin.theoji.Adapter.StudentAdapter;
import com.example.admin.theoji.Adapter.StudentListAdapter.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.StateModel;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static com.androidquery.util.AQUtility.getContext;
import static com.example.admin.theoji.StudentActivity.studentStringHashMap;

public class Update_Student_Profile_Activity extends AppCompatActivity {

    Spinner spin_state;
    ArrayList<String> ChooseState;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList;

    Spinner rteType;
    Spinner countryType;
    private ArrayAdapter<String> rteTypeAdapter;
    private ArrayList<String> rteTypeList;

    private ArrayAdapter<String> countryAdapter;
    private ArrayList<String> countryList;

    String RTEType;
    String Country;
    Button btn_final_submit;
    RadioButton Male, Female;

//    ArrayList<EditUpdateStudentModel> EditUpdateList;
//    private EditUpdateStudentAdapter EditUpdateStudentAdapter ;

    RadioGroup Radio_group_sex;
    EditText email1, password, student_name, student_dob, student_pre_sch_name, student_aadhar, student_category, student_bank_name,
            student_bank_account;
    Spinner spin_st_class, std_section, std_board_name;


    EditText std_sch_code, std_session, std_admission_no, std_admission_date, std_about;
    EditText student_fathername, student_mother_name, student_city, student_address, student_pincode, student_mobile,
            student_alternate_mobile, student_cast, student_bank_ifsc_code, student_sssmid;

    String School_code, Student_class_type, Student_section, Student_session, Student_admission_no, Student_admission_date,
            Student_board_name, Student_about;
    String Student_Email, Student_Password, Student_Name, Student_pre_school_name, Student_Dob, Student_Aadhar, Student_category,
            Student_cast, Student_Bank_Account, Student_Bank_Name, Student_bank_ifsc_code, Student_sex,
            Student_gardian_name, Student_city, Student_pincode, Student_Address, Student_mobile, Student_Alternate_mobile, Student_state, Student_country, Student_rte_type, Student_sssmid, Student_mother_name;
     String server_url;
    public String SID="";
     String user_type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_profile);


        spin_state = (Spinner) findViewById(R.id.state);
        stateList = new ArrayList<>();
        ChooseState = new ArrayList<>();

        new stateExecuteTask().execute();
        new EditStudentExecuteTask().execute();

        btn_final_submit = (Button) findViewById(R.id.submit_final);

        email1 = (EditText) findViewById(R.id.st_email);
        password = (EditText) findViewById(R.id.st_pw);
        student_name = (EditText) findViewById(R.id.st_name);
        student_dob = (EditText) findViewById(R.id.st_dob);
        student_pre_sch_name = (EditText) findViewById(R.id.st_pre_school_name);
        student_aadhar = (EditText) findViewById(R.id.st_aadhar);
        student_category = (EditText) findViewById(R.id.st_category);
        student_bank_account = (EditText) findViewById(R.id.st_bank_account);
        student_bank_name = (EditText) findViewById(R.id.st_bank_name);
        //  Radio_group_sex=(RadioGroup)firstView.findViewById(R.id.sex);
        Male = (RadioButton) findViewById(R.id.male);
        Female = (RadioButton) findViewById(R.id.female);

        std_sch_code = (EditText) findViewById(R.id.st_school_code);
        std_session = (EditText) findViewById(R.id.st_session);
        std_admission_no = (EditText) findViewById(R.id.st_admission_no);
        std_admission_date = (EditText) findViewById(R.id.st_admission_date);
        std_about = (EditText) findViewById(R.id.st_about);

        spin_st_class = (Spinner) findViewById(R.id.st_classes);
        std_section = (Spinner) findViewById(R.id.st_section);
        std_board_name = (Spinner) findViewById(R.id.st_boardname);

        student_fathername = (EditText) findViewById(R.id.st_father_name);
        student_mother_name = (EditText) findViewById(R.id.st_mother_name);
        student_city = (EditText) findViewById(R.id.st_city);
        student_address = (EditText) findViewById(R.id.st_address);
        student_pincode = (EditText) findViewById(R.id.st_pincode);
        student_mobile = (EditText) findViewById(R.id.st_mobile);
        student_alternate_mobile = (EditText) findViewById(R.id.st_alternate_mobile);
        student_cast = (EditText) findViewById(R.id.st_cast);
        student_bank_ifsc_code = (EditText) findViewById(R.id.st_bank_ifsc_code);
        student_sssmid = (EditText) findViewById(R.id.st_sssmid);

        btn_final_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Student_Email=email1.getText().toString();
               // Student_Password=password.getText().toString();
                Student_Name=student_name.getText().toString();
                Student_pre_school_name=student_pre_sch_name.getText().toString();
                Student_Dob=student_dob.getText().toString();
                Student_Aadhar=student_aadhar.getText().toString();
                Student_category=student_category.getText().toString();
                Student_Bank_Account=student_bank_account.getText().toString();
                Student_Bank_Name=student_bank_name.getText().toString();

                if(Male.isChecked())
                {
                    Student_sex=Male.getText().toString();
                    // Toast.makeText(getContext(), ""+Male.getText().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Student_sex=Female.getText().toString();
                    // Toast.makeText(getContext(), ""+Female.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                // Student_sex=Male.getText().toString();

                Student_gardian_name=student_fathername.getText().toString();
                Student_mother_name=student_mother_name.getText().toString();
                Student_city=student_city.getText().toString();
                Student_Address=student_address.getText().toString();
                Student_pincode=student_pincode.getText().toString();
                Student_mobile=student_mobile.getText().toString();
                Student_Alternate_mobile=student_alternate_mobile.getText().toString();
                Student_state=spin_state.getSelectedItem().toString();
                Student_country=countryType.getSelectedItem().toString();
                Student_cast=student_cast.getText().toString();
                Student_rte_type=rteType.getSelectedItem().toString();
                Student_bank_ifsc_code=student_bank_ifsc_code.getText().toString();
                Student_sssmid=student_sssmid.getText().toString();

                School_code =std_sch_code.getText().toString();
                Student_class_type =spin_st_class.getSelectedItem().toString();
                Student_section =std_section.getSelectedItem().toString();
                Student_session=std_session.getText().toString();
                Student_admission_no=std_admission_no.getText().toString();
                Student_admission_date=std_admission_date.getText().toString();
                Student_board_name=std_board_name.getSelectedItem().toString();
                Student_about=std_about.getText().toString();

                new StudentUpdateExecuteTask().execute();
            }
        });
        //******************************************************************************
        student_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (student_dob.getRight() - student_dob.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                        DatePickerDialog mDatePicker = new DatePickerDialog(Update_Student_Profile_Activity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                                String myFormat = "dd/MM/yyyy"; //Change as you need
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                                student_dob.setText(sdf.format(myCalendar.getTime()));

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
//*******************************************************************************
        std_admission_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (std_admission_date.getRight() - std_admission_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                        DatePickerDialog mDatePicker = new DatePickerDialog(Update_Student_Profile_Activity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                                String myFormat = "dd/MM/yyyy"; //Change as you need
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                                std_admission_date.setText(sdf.format(myCalendar.getTime()));

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


        //***************************************************************


        countryType = (Spinner) findViewById(R.id.country);
        rteType = (Spinner) findViewById(R.id.RTEtype);


//RTE type spinner list
        rteTypeList = new ArrayList<>();
        rteTypeList.add("yes");
        rteTypeList.add("no");

        rteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RTEType = rteTypeAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rteTypeAdapter = new ArrayAdapter<String>(Update_Student_Profile_Activity.this, R.layout.support_simple_spinner_dropdown_item, rteTypeList);
        rteTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rteType.setAdapter(rteTypeAdapter);

// country spinner list
        countryList = new ArrayList<>();
        countryList.add("India");

        countryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country = countryAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryAdapter = new ArrayAdapter<String>(Update_Student_Profile_Activity.this, R.layout.support_simple_spinner_dropdown_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryType.setAdapter(countryAdapter);
//*********************************************************








    }
    class stateExecuteTask extends AsyncTask<String, Integer, String> {

        String output = "";




        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(Update_Student_Profile_Activity.this);


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

                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String sstate_id = jsonObject1.getString("sstate_id");
                        String sstate_name = jsonObject1.getString("sstate_name");


                        stateList.add(new StateModel(sstate_id, sstate_name));
                        ChooseState.add(sstate_name);

                    }

                    stateAdapter = new ArrayAdapter<String>(Update_Student_Profile_Activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_state.setAdapter(stateAdapter);

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        }

    private class EditStudentExecuteTask extends AsyncTask<String, Void, String>{

            String output = "";
            ProgressDialog dialog;
           // private PostListModel postListModel;
            private Object viewHolder;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(Update_Student_Profile_Activity.this);
                dialog.setMessage("Processing");
                dialog.setCancelable(true);
                dialog.show();
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

                try {
//                    int i = pos;
//                    SID =  studentStringHashMap.get(i);

                    server_url = "https://jntrcpl.com/theoji/index.php/Api/get_student_detail?id="
                            +getIntent().getStringExtra("uid");
                            //AppPreference.getUserid(Update_Student_Profile_Activity.this);


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
                            //String[] seperateData = new String[40];
                            for (int i = 0; i < Data_array.length(); i++) {
                                JSONObject c = Data_array.getJSONObject(i);

                                email1.setText(c.getString("email"));
                                student_dob.setText(c.getString("dob"));
                                student_name.setText(c.getString("firstname"));
                                student_mobile.setText(c.getString("mobileno"));
                                student_city.setText(c.getString("city"));
                                student_fathername.setText(c.getString("lastname"));
                                std_about.setText(c.getString("about"));
                               // std_sch_code.setText(c.getString("school_code"));
                                student_address.setText(c.getString("address"));
                                String gender=c.getString("gender");
                                try{
                                    if(!gender.isEmpty())
                                    {
                                       if(gender.equalsIgnoreCase("male"));
                                       Male.isChecked();
                                    }else {
                                        Female.isChecked();
                                    }
                                }catch ( NullPointerException e)
                                { }

                                String user_id = c.getString("user_id");
                                 user_type = c.getString("user_type");
                                String umeta_value = c.getString("umeta_value");

                             String[] seperateData = new String[40];
                                seperateData = umeta_value.split(Pattern.quote(","));
                                String share = seperateData[0];
                                String plike = seperateData[1];
                                String address1 = seperateData[2];
                                String state = seperateData[3];
                               // spin_state.setTextAlignment(seperateData[3]);
                                try{
                                    if(!state.isEmpty())
                                    {
                                        for(int k=0;k<stateAdapter.getCount();k++)
                                        {
                                            if(stateAdapter.getItem(k).equals(state))
                                            {
                                               // Toast.makeText(Update_Student_Profile_Activity.this, "xyz"+state, Toast.LENGTH_SHORT).show();
                                                spin_state.setSelection((int) stateAdapter.getItemId(k));
                                            }else{
                                               // Toast.makeText(Update_Student_Profile_Activity.this, "fgdgf", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }catch ( NullPointerException e)
                                { }

                                String country = seperateData[4];
                                try{
                                    if(!country.isEmpty())
                                    {
                                        for(int k=0;k<countryAdapter.getCount();k++)
                                        {
                                            if(countryAdapter.getItem(k).equals(state))
                                            {
                                               // Toast.makeText(Update_Student_Profile_Activity.this, "xyz"+state, Toast.LENGTH_SHORT).show();
                                                countryType.setSelection((int) countryAdapter.getItemId(k));
                                            }else{
                                                // Toast.makeText(Update_Student_Profile_Activity.this, "fgdgf", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }catch ( NullPointerException e)
                                { }

                                String telno = seperateData[5];
                                student_alternate_mobile.setText(telno);
                                String imagename = seperateData[6];
                                String pin_code = seperateData[7];
                                student_pincode.setText(pin_code);
                                String schoolname = seperateData[8];
                                std_sch_code.setText(schoolname);
                                String roll_no = seperateData[9];
                                String sssm_id = seperateData[10];
                                student_sssmid.setText(sssm_id);
                                String pschoolname = seperateData[11];
                                student_pre_sch_name.setText(pschoolname);
                                String aadhar_no = seperateData[12];
                                student_aadhar.setText(aadhar_no);
                                String bankd = seperateData[13];
                                student_bank_name.setText(bankd);
                                String admission_no = seperateData[14];
                                std_admission_no.setText(admission_no);
                                String bannerimage = seperateData[15];
                                String sclass = seperateData[16];

                                String category = seperateData[17];
                                student_category.setText(category);
                                String cast = seperateData[18];
                                student_cast.setText(cast);
                                String rte = seperateData[19];
                                try{
                                    if(!rte.isEmpty())
                                    {
                                        for(int k=0;k<rteTypeAdapter.getCount();k++)
                                        {
                                            if(rteTypeAdapter.getItem(k).equals(state))
                                            {
                                                Toast.makeText(Update_Student_Profile_Activity.this, "xyz"+state, Toast.LENGTH_SHORT).show();
                                                rteType.setSelection((int) rteTypeAdapter.getItemId(k));
                                            }else{
                                                // Toast.makeText(Update_Student_Profile_Activity.this, "fgdgf", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }catch ( NullPointerException e)
                                { }
                                String bankano = seperateData[20];
                                student_bank_account.setText(bankano);
                                String ifsccode = seperateData[21];
                                student_bank_ifsc_code.setText(ifsccode);
                                String relationship = seperateData[22];
                                String mothername = seperateData[23];
                                student_mother_name.setText(mothername);
                                String section = seperateData[24];

                                String session = seperateData[25];
                                std_session.setText(session);
                                String admission_date = seperateData[26];
                                std_admission_date.setText(admission_date);
                                String board = seperateData[27];


                            }


//                           Picasso.get().load("http://theoji.com/uploads/"+postListModel.getPostimg()).into(viewHolder.);


                        }else {

                            Toast.makeText(Update_Student_Profile_Activity.this, "no details update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  dialog.dismiss();
                    }
                    super.onPostExecute(output);
                }

            }
    }

    private class StudentUpdateExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Update_Student_Profile_Activity.this);
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
                    Toast.makeText(Update_Student_Profile_Activity.this, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject object = new JSONObject(result);
                    String res = object.getString("response");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//                    String email=data.getString("firstname");
//                    String mobile=data.getString("lastname");
//                    String pass=data.getString("email");
//                    String alotclass=data.getString("mobileno");
//                    String address=data.getString("date");



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(Update_Student_Profile_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Update_Student_Profile_Activity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Update_Student_Profile_Activity.this, StudentActivity.class);
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

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/profile_update");

            JSONObject postDataParams = new JSONObject();
            String id= AppPreference.getUserid(Update_Student_Profile_Activity.this);

            postDataParams.put("user_id",id);
            postDataParams.put("user_type", user_type);

            postDataParams.put("email", Student_Email);
           // postDataParams.put("Student_password",Student_Password);
            postDataParams.put("firstname",Student_Name);
            postDataParams.put("dob",Student_Dob);
            postDataParams.put("gender",Student_sex);
            postDataParams.put("pschoolname",Student_pre_school_name);
            postDataParams.put("aadhar_no",Student_Aadhar);
            postDataParams.put("category",Student_category);
            postDataParams.put("bankd",Student_Bank_Name);
            postDataParams.put("bankano",Student_Bank_Account);

            postDataParams.put("lastname",Student_gardian_name);
            postDataParams.put("mothername",Student_mother_name);
            postDataParams.put("city",Student_city);
            postDataParams.put("address",Student_Address);
            postDataParams.put("pin_code",Student_pincode);
            postDataParams.put("mobileno",Student_mobile);
            postDataParams.put("telno",Student_Alternate_mobile);
            postDataParams.put("state",Student_state);
            postDataParams.put("country",Student_country);
            postDataParams.put("cast",Student_cast);
            postDataParams.put("rte",Student_rte_type);
            postDataParams.put("ifsccode",Student_bank_ifsc_code);
            postDataParams.put("sssm_id",Student_sssmid);
            postDataParams.put("school_code",School_code);

            postDataParams.put("sclass",Student_class_type);
            postDataParams.put("section",Student_section);
            postDataParams.put("session",Student_session);
            postDataParams.put("admission_no",Student_admission_no);
            postDataParams.put("admission_date",Student_admission_date);
            postDataParams.put("board",Student_board_name);
            postDataParams.put("about",Student_about);

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

}
