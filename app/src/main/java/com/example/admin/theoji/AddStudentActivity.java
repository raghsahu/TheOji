package com.example.admin.theoji;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.FirstStepModel;
import com.example.admin.theoji.ModelClass.SecondStepModel;
import com.example.admin.theoji.ModelClass.SectionModel;
import com.example.admin.theoji.ModelClass.StateModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.Utils.ProjectUtils;

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

public class AddStudentActivity extends AppCompatActivity{
    Button btn_final_submit;

    ArrayList<String> ChooseClass =new ArrayList<>();
    private ArrayList<ClassModel> classList=new ArrayList<>();
    private ArrayAdapter<String> classListAdapter;

    Spinner  std_board_name;
    ArrayList<String> ChooseBoard =new ArrayList<>();
    //private ArrayList<BoardModel> BoardList=new ArrayList<>();
    private ArrayAdapter<String> BoardListAdapter;

    ArrayList<String> ChooseSection =new ArrayList<>();
    private ArrayList<SectionModel> sectionList=new ArrayList<>();
    private ArrayAdapter<String> sectionListAdapter;


    Spinner rteType;
    Spinner country;
    private ArrayAdapter<String> rteTypeAdapter;
    private ArrayList<String> rteTypeList;

    private ArrayAdapter<String> countryAdapter;
    private ArrayList<String> countryList;

    Spinner spin_state;
    ArrayList<String>ChooseState;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList;

    String RTEType;
    String Country;

    RadioButton Male, Female;

    RadioGroup Radio_group_sex;
     EditText email,password,student_name,student_dob,student_pre_sch_name,student_aadhar,student_category,student_bank_name,
            student_bank_account;
    Spinner spin_st_class, std_section;

    EditText std_sch_code,std_session,std_admission_no,std_admission_date,std_about  ;
    EditText student_fathername,student_mother_name,student_city,student_address,student_pincode,student_mobile,
            student_alternate_mobile,student_cast, student_bank_ifsc_code, student_sssmid;

    String School_code, Student_class_type, Student_section,Student_session,Student_admission_no,Student_admission_date,
            Student_board_name,Student_about;
    String Student_Email,Student_Password,Student_Name,Student_pre_school_name,Student_Dob,Student_Aadhar,Student_category,
    Student_cast,Student_Bank_Account,Student_Bank_Name,Student_bank_ifsc_code,Student_sex,
    Student_gardian_name,Student_city, Student_pincode,Student_Address,Student_mobile,Student_Alternate_mobile,Student_state,Student_country
            ,Student_rte_type,Student_sssmid,Student_mother_name;

    private String beforeText, currentText;
    private boolean noAction, addStroke, dontAddChar, deleteStroke;


    public HashMap<Integer, ClassModel> ClassHashMap = new HashMap<Integer, ClassModel>();
    public HashMap<Integer, SectionModel> SectionHashMap = new HashMap<Integer, SectionModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        btn_final_submit=(Button)findViewById(R.id.submit_final);

        email=(EditText)findViewById(R.id.st_email);
        password=(EditText)findViewById(R.id.st_pw);
        student_name=(EditText)findViewById(R.id.st_name);
        student_dob=(EditText)findViewById(R.id.st_dob);
        student_pre_sch_name=(EditText)findViewById(R.id.st_pre_school_name);
        student_aadhar=(EditText)findViewById(R.id.st_aadhar);
        student_category=(EditText)findViewById(R.id.st_category);
        student_bank_account=(EditText)findViewById(R.id.st_bank_account);
        student_bank_name=(EditText)findViewById(R.id.st_bank_name);
        //  Radio_group_sex=(RadioGroup)firstView.findViewById(R.id.sex);
        Male=(RadioButton)findViewById(R.id.male);
        Female=(RadioButton)findViewById(R.id.female);

        std_sch_code=(EditText)findViewById(R.id.st_school_code);
        std_session=(EditText)findViewById(R.id.st_session);
        std_admission_no=(EditText)findViewById(R.id.st_admission_no);
        std_admission_date=(EditText)findViewById(R.id.st_admission_date);
        std_about=(EditText)findViewById(R.id.st_about);

        spin_st_class = (Spinner)findViewById(R.id.st_classes);
        std_section = (Spinner)findViewById(R.id.st_section);
        std_board_name = (Spinner)findViewById(R.id.st_boardname);

        student_fathername=(EditText)findViewById(R.id.st_father_name);
        student_mother_name=(EditText)findViewById(R.id.st_mother_name);
        student_city=(EditText)findViewById(R.id.st_city);
        student_address=(EditText)findViewById(R.id.st_city);
        student_pincode=(EditText)findViewById(R.id.st_pincode);
        student_mobile=(EditText)findViewById(R.id.st_mobile);
        student_alternate_mobile=(EditText)findViewById(R.id.st_alternate_mobile);
        student_cast=(EditText)findViewById(R.id.st_cast);
        student_bank_ifsc_code=(EditText)findViewById(R.id.st_bank_ifsc_code);
        student_sssmid=(EditText)findViewById(R.id.st_sssmid);
//************************************************
        student_aadhar.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
                if (addStroke) {
                    Log.i("TextWatcherImplement", "afterTextChanged String == " + s + " beforeText == " + beforeText + " currentText == " + currentText);
                    noAction = true;
                    addStroke = false;
                    student_aadhar.setText(currentText + "-");
                } else if (dontAddChar) {
                    dontAddChar = false;
                    noAction = true;
                    student_aadhar.setText(beforeText);
                } else if (deleteStroke) {
                    deleteStroke = false;
                    noAction = true;
                    currentText = currentText.substring(0, currentText.length() - 1);
                    student_aadhar.setText(currentText);
                } else {
                    noAction = false;
                    student_aadhar.setSelection(student_aadhar.getText().length()); // set cursor at the end of the line.
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                Log.i("TextWatcherImplement", "beforeTextChanged start==" + String.valueOf(start) + " count==" + String.valueOf(count) + " after==" + String.valueOf(after));
                if (start >= 14)
                    beforeText = s.toString();

            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
                Log.i("TextWatcherImplement", "onTextChanged start==" + String.valueOf(start) + " before==" + String.valueOf(before) + " count==" + String.valueOf(count) + " noAction ==" + String.valueOf(noAction));
                if ( (before < count) && !noAction ) {
                    if ( (start == 3) || (start == 8)  ) {
                        currentText = s.toString();
                        addStroke = true;
                    } else if (start >= 14) {
                        currentText = s.toString();
                        dontAddChar = true;
                    }
                } else {
                    if ( (start == 4) ||  (start == 9)  ) { //(start == 5) || (start == 10) || (start == 15)
                        currentText = s.toString();
                        deleteStroke = true;
                    }
                }
            }
        });

//******************************************************************************************

        if (Connectivity.isNetworkAvailable(AddStudentActivity.this)){
            new spinnerClassExecuteTask().execute();

        }else {
            Toast.makeText(AddStudentActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        if (Connectivity.isNetworkAvailable(AddStudentActivity.this)){
            new spinnerBoardExecuteTask().execute();

        }else {
            Toast.makeText(AddStudentActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        spin_st_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //strSid = stateList.get(position).getState_id();
                try{
                    if(sectionList.size() !=0)
                    {
                        ChooseSection.clear();

                        std_section.setAdapter(null);
                        sectionListAdapter.notifyDataSetChanged();

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < ClassHashMap.size(); i++)
                {

                    if (ClassHashMap.get(i).getM_name().equals(spin_st_class.getItemAtPosition(position)))
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
        //**********************************************************

        student_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (student_dob.getRight() - student_dob.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                        DatePickerDialog mDatePicker = new DatePickerDialog(AddStudentActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (std_admission_date.getRight() - std_admission_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                        DatePickerDialog mDatePicker = new DatePickerDialog(AddStudentActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        country = (Spinner)findViewById(R.id.country);
        rteType = (Spinner)findViewById(R.id.RTEtype);
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

        rteTypeAdapter = new ArrayAdapter<String>(AddStudentActivity.this, R.layout.support_simple_spinner_dropdown_item, rteTypeList);
        rteTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rteType.setAdapter(rteTypeAdapter);

// country spinner list
        countryList = new ArrayList<>();
        countryList.add("India");

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country = countryAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryAdapter = new ArrayAdapter<String>(AddStudentActivity.this, R.layout.support_simple_spinner_dropdown_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(countryAdapter);
//*********************************************************
        //state spinner list
        spin_state = (Spinner)findViewById(R.id.state);
        stateList = new ArrayList<>();
//        stateList.add("Select State");
        ChooseState = new ArrayList<>();
        new stateExecuteTask().execute();

//        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                State =stateAdapter.getItem(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        stateAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, stateList);
//        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        state.setAdapter(stateAdapter);
//***********************************************************************


        Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Female.setChecked(false);
            }
        });
        Female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Male.setChecked(false);

            }
        });

        btn_final_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Student_Email=email.getText().toString();
               Student_Password=password.getText().toString();
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

                Student_gardian_name=student_fathername.getText().toString();
              Student_mother_name=student_mother_name.getText().toString();
             Student_city=student_city.getText().toString();
               Student_Address=student_address.getText().toString();
                Student_pincode=student_pincode.getText().toString();
                 Student_mobile=student_mobile.getText().toString();
                Student_Alternate_mobile=student_alternate_mobile.getText().toString();
                 Student_state=spin_state.getSelectedItem().toString();
              // Student_country=country.getSelectedItem().toString();
                Student_cast=student_cast.getText().toString();
                Student_rte_type=rteType.getSelectedItem().toString();
                 Student_bank_ifsc_code=student_bank_ifsc_code.getText().toString();
                Student_sssmid=student_sssmid.getText().toString();

                School_code =std_sch_code.getText().toString();
                Student_class_type =spin_st_class.getSelectedItem().toString();

                try {
                    if (sectionList.size()!=0){
                        Student_section =std_section.getSelectedItem().toString();
                    }
                }catch (Exception e){
                    Student_section =null;
                }


                Student_session=std_session.getText().toString();
               // Student_admission_no=std_admission_no.getText().toString();
                Student_admission_date=std_admission_date.getText().toString();
                Student_board_name=std_board_name.getSelectedItem().toString();
                Student_about=std_about.getText().toString();

                if (Connectivity.isNetworkAvailable(AddStudentActivity.this)){

                    if (!Student_Email.isEmpty() && !Student_Password.isEmpty()&&!Student_Name.isEmpty()&&!Student_pre_school_name.isEmpty()
                    && !Student_Dob.isEmpty()&&!Student_Aadhar.isEmpty()&&!Student_category.isEmpty()&& !Student_cast.isEmpty()
                    &&!Student_Bank_Account.isEmpty()&&!Student_Bank_Name.isEmpty()&&!Student_bank_ifsc_code.isEmpty()
                    &&!Student_sex.isEmpty()&&!Student_gardian_name.isEmpty() &&!Student_city.isEmpty() && !Student_pincode.isEmpty()
                    &&!Student_Address.isEmpty()&&!Student_mobile.isEmpty() &&!Student_Alternate_mobile.isEmpty()&&!Student_state.isEmpty()
                     &&!Student_rte_type.isEmpty() &&!Student_sssmid.isEmpty()&& !Student_mother_name.isEmpty()
                    && !School_code.isEmpty() && !Student_class_type.isEmpty() && !Student_section.isEmpty()&&!Student_session.isEmpty()
                    &&Student_admission_date.isEmpty() && !Student_board_name.isEmpty() &&!Student_about.isEmpty())

                    {
                        new StudentCreatExecuteTask().execute();
                    }else {
                        Toast.makeText(AddStudentActivity.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddStudentActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //**************************************************************************************
    public class stateExecuteTask extends AsyncTask<String, Integer,String> {

        String output = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(AddStudentActivity.this);


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
                    JSONObject object=new JSONObject(output);
                    String res=object.getString("responce");

                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String sstate_id = jsonObject1.getString("sstate_id");
                        String sstate_name = jsonObject1.getString("sstate_name");


                        stateList.add(new StateModel(sstate_id,sstate_name ));
                        ChooseState.add(sstate_name);

                    }

                    stateAdapter = new ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_state.setAdapter(stateAdapter);

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //****************************************************************
    class StudentCreatExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(AddStudentActivity.this);
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
                    Toast.makeText(AddStudentActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AddStudentActivity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddStudentActivity.this, "success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddStudentActivity.this, StudentActivity.class);
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

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/create_student");

            JSONObject postDataParams = new JSONObject();
            String id= AppPreference.getUserid(AddStudentActivity.this);

            postDataParams.put("id",id);

            postDataParams.put("Student_email", Student_Email);
            postDataParams.put("Student_password",Student_Password);
            postDataParams.put("Student_name",Student_Name);
            postDataParams.put("Student_DOB",Student_Dob);
            postDataParams.put("Student_gender",Student_sex);
            postDataParams.put("Student_previous_school_name",Student_pre_school_name);
            postDataParams.put("Student_aadhar_no",Student_Aadhar);
            postDataParams.put("Student_category",Student_category);
            postDataParams.put("Student_bank_name",Student_Bank_Name);
            postDataParams.put("Student_bank_account",Student_Bank_Account);

            postDataParams.put("Student_gardian_name",Student_gardian_name);
            postDataParams.put("Student_mother_name",Student_mother_name);
            postDataParams.put("Student_city",Student_city);
            postDataParams.put("Student_Address",Student_Address);
            postDataParams.put("Student_pincode",Student_pincode);
            postDataParams.put("Student_mobile",Student_mobile);
            postDataParams.put("Student_Alternate_mobile",Student_Alternate_mobile);
            postDataParams.put("Student_state",Student_state);
           // postDataParams.put("Student_country",Student_country);
            postDataParams.put("Student_cast",Student_cast);
            postDataParams.put("Student_rte_type",Student_rte_type);
            postDataParams.put("Student_bank_ifsc_code",Student_bank_ifsc_code);
            postDataParams.put("Student_sssmid",Student_sssmid);

            postDataParams.put("School_code",School_code);
            postDataParams.put("Student_class_type",Student_class_type);
            postDataParams.put("Student_section",Student_section);
            postDataParams.put("Student_session",Student_session);
          //  postDataParams.put("Student_admission_no",Student_admission_no);
            postDataParams.put("Student_admission_date",Student_admission_date);
            postDataParams.put("Student_board_name",Student_board_name);
            postDataParams.put("Student_about",Student_about);

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

   //***************************************************************
   private class spinnerClassExecuteTask extends AsyncTask<String, Integer,String> {

       String output = "";


       @Override
       protected void onPreExecute() {

           super.onPreExecute();

       }

       @Override
       protected String doInBackground(String... params) {

           String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_class?school_id="
                   +AppPreference.getUserid(AddStudentActivity.this);


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

                       classListAdapter = new ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                       classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                       spin_st_class.setAdapter(classListAdapter);

                   }else {
                       Toast.makeText(AddStudentActivity.this, "no class found", Toast.LENGTH_SHORT).show();
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

                        sectionListAdapter = new ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseSection);
                        sectionListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        std_section.setAdapter(sectionListAdapter);


                        // reloadAllData();

                    }else {
                        Toast.makeText(AddStudentActivity.this, "no section found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class spinnerBoardExecuteTask extends AsyncTask<String,Integer,String> {

        String output = "";


        @Override
        protected String doInBackground(String... params) {
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_board";

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

                           // BoardList.add(new BoardModel(m_id, m_name));
                           // SectionHashMap.put(i, new SectionModel(m_id, m_name));
                            ChooseBoard.add(m_name);

                        }

                        BoardListAdapter = new ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseBoard);
                        BoardListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        std_board_name.setAdapter(BoardListAdapter);


                        // reloadAllData();

                    } else {
                        Toast.makeText(AddStudentActivity.this, "No Board found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
