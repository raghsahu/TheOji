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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.AttendanceAdapter;
import com.example.admin.theoji.Adapter.StudentAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.AttendanceListModel;
import com.example.admin.theoji.ModelClass.ClassModel;
import com.example.admin.theoji.ModelClass.StudentListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.Utils.CustomAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ShowAttendenceActivity extends AppCompatActivity {
    ImageView add_attendence;
    RecyclerView recyclerstudentattendance;
    String server_url;
    ArrayList<AttendanceListModel> AttendanceList;
    private AttendanceAdapter attendanceAdapter;

    Spinner spin_class;
    ArrayList<String> ChooseClass =new ArrayList<>();
    private ArrayList<ClassModel> classList=new ArrayList<>();
    private ArrayAdapter<String> classListAdapter;

    TextView tv_date;
    EditText tv_date1;
    Button btn_seach_attend;
   String Tv_Date,Select_Class;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendence);

            add_attendence=(ImageView)findViewById(R.id.add_attendence);
            tv_date=(TextView) findViewById(R.id.tv_date_search);
           // tv_date1=(EditText) findViewById(R.id.tv_date_search);
            spin_class=findViewById(R.id.search_class1);
            btn_seach_attend=findViewById(R.id.find_seach);


            //***********2=school**********4=parent***********
        if (AppPreference.getUser_Type(ShowAttendenceActivity.this).equals("2")){
            add_attendence.setVisibility(View.GONE);
        } if (AppPreference.getUser_Type(ShowAttendenceActivity.this).equals("4")) {
            add_attendence.setVisibility(View.GONE);
        }

             add_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowAttendenceActivity.this,AttendenceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerstudentattendance = (RecyclerView)findViewById(R.id.recycler_attendance);

        AttendanceList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(ShowAttendenceActivity.this)){
            new GetAttendanceList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();

        }

        new spinnerClassExecuteTask().execute();
        //****************************************

        tv_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (tv_date.getRight() - tv_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                        DatePickerDialog mDatePicker = new DatePickerDialog(ShowAttendenceActivity.this, new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Calendar myCalendar = Calendar.getInstance();
                                myCalendar.set(Calendar.YEAR, selectedyear);
                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                                String myFormat = "yyyy/MM/dd"; //Change as you need
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                                tv_date.setText(sdf.format(myCalendar.getTime()));

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
//***************************************************************************
        btn_seach_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Tv_Date= tv_date.getText().toString();
               Select_Class=spin_class.getSelectedItem().toString();

                try {
                    if (AttendanceList.size() != 0) {
                        AttendanceList.clear();

                        recyclerstudentattendance.setAdapter(null);
                        attendanceAdapter.notifyDataSetChanged();

                    }
                }catch (Exception e){}

                if (Connectivity.isNetworkAvailable(ShowAttendenceActivity.this)) {
                    if (!Tv_Date.isEmpty() && !Select_Class.equals("select")){
                        new GetAttendanceList().execute();
                    }else {
                        if (Tv_Date.isEmpty()){
                            Toast.makeText(ShowAttendenceActivity.this, "please select date", Toast.LENGTH_SHORT).show();
                        }if (Select_Class.equals("select")){
                            Toast.makeText(ShowAttendenceActivity.this, "please select class", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(ShowAttendenceActivity.this, "no internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class GetAttendanceList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private StudentListModel studentListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ShowAttendenceActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_attendance?id="+
                        AppPreference.getUserid(ShowAttendenceActivity.this)+"&date="+Tv_Date+"&class="+Select_Class;

//*************+"&date="+Tv_Date+"&class="+Select_Class
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

                            String date = c.getString("date");
                            String attendance_id = c.getString("attendance_id");
                            String section_name = c.getString("section_name");
                            String class_name = c.getString("class_name");
                            String remark = c.getString("remark");
                            String teacher_name = c.getString("teacher_name");
                            String student_all = c.getString("student_all");
                            String present = c.getString("present");
                            String absent = c.getString("absent");

                            AttendanceList.add(i, new AttendanceListModel(date, attendance_id, section_name, class_name,remark,
                                    teacher_name,student_all,present,absent));

                        }


                        attendanceAdapter = new AttendanceAdapter(ShowAttendenceActivity.this, AttendanceList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowAttendenceActivity.this);
                        recyclerstudentattendance.setLayoutManager(mLayoutManager);
                        recyclerstudentattendance.setItemAnimator(new DefaultItemAnimator());
                        recyclerstudentattendance.setAdapter(attendanceAdapter);


                    }else {

                        Toast.makeText(ShowAttendenceActivity.this, "no post update now!", Toast.LENGTH_LONG).show();
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
                    +AppPreference.getUserid(ShowAttendenceActivity.this);


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

                            if ( i==0){
                               // classList.add(new ClassModel("0","Select class"));
                                ChooseClass.add("select");
                            }

                            classList.add(new ClassModel(m_id, m_name));
                            ChooseClass.add(m_name);
                           // ClassHashMap.put(i, new ClassModel(m_id,m_name));

                        }

                        classListAdapter = new ArrayAdapter<String>(ShowAttendenceActivity.this, android.R.layout.simple_spinner_dropdown_item, ChooseClass);
                        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_class.setAdapter(classListAdapter);

                    }else {
                        Toast.makeText(ShowAttendenceActivity.this, "no class found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
