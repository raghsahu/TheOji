package com.example.admin.theoji;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.admin.theoji.Adapter.StudentListAdapter;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AttendenceActivity extends AppCompatActivity {

    Spinner spin_student;
    ArrayList<String> ChooseStudent =new ArrayList<>();
    private ArrayList<StudentModel> studentList=new ArrayList<>();
    private StudentListAdapter studentListAdapter;

    Spinner spin_class;

    String Spin_Student;
    String Spin_class;

    EditText attend_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        spin_student = (Spinner)findViewById(R.id.class_students);
        spin_class=(Spinner)findViewById(R.id.spin_class);
        attend_date=(EditText)findViewById(R.id.attendence_date);

//        spin_class.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//            });


        new spinnerStudentExecuteTask().execute();

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


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/class_by_student?class="
                    + Spin_class+"&ref_id="
                    +AppPreference.getRefid(AttendenceActivity.this);


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

                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String user_id = jsonObject1.getString("user_id");
                        // String password = jsonObject1.getString("password");
                        // String showpass = jsonObject1.getString("showpass");
                        // String st_pass = jsonObject1.getString("stu_password");
                        String firstname = jsonObject1.getString("firstname");
                        // String lastname = jsonObject1.getString("lastname");
                        String email = jsonObject1.getString("email");
                        String mobile = jsonObject1.getString("mobileno");
                        //  String date = jsonObject1.getString("date");
                        //  String user_type = jsonObject1.getString("user_type");
                        // String gender = jsonObject1.getString("gender");
                        // String dob = jsonObject1.getString("dob");
                        String city = jsonObject1.getString("city");
                        String address = jsonObject1.getString("address");
                        // String status = jsonObject1.getString("status");
                        // String block_unblock = jsonObject1.getString("block_unblock");
                        // String ref_id = jsonObject1.getString("ref_id");
                        // String profileupdate = jsonObject1.getString("profileupdate");
                        // String about = jsonObject1.getString("about");
                        // String latest_post = jsonObject1.getString("latest_post");
                        // String latest_event = jsonObject1.getString("latest_event");
                        // String school_code = jsonObject1.getString("school_code");
                        //  String notice = jsonObject1.getString("notice");
                        //  String latest_activities = jsonObject1.getString("latest_activities");
                        //  String stutotalfees = jsonObject1.getString("stutotalfees");
                        //  String latest_news = jsonObject1.getString("latest_news");
                        // String sturemainfee = jsonObject1.getString("sturemainfee");
                        // String latest_noticboard = jsonObject1.getString("latest_noticboard");
                        // String stulastfee = jsonObject1.getString("stulastfee");
                        // String sales_lead_name = jsonObject1.getString("sales_lead_name");

                        studentList.add(new StudentModel(user_id, firstname));
                        ChooseStudent.add(firstname);

                    }

                    studentListAdapter = new StudentListAdapter(AttendenceActivity.this, android.R.layout.simple_spinner_item, studentList);
                    //StudentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    // spin_student.setAdapter(StudentAdapter,false, onSelectedListener);
                    spin_student.setAdapter(studentListAdapter);

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
