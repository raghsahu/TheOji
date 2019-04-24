package com.example.admin.theoji;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.AttendanceAdapter;
import com.example.admin.theoji.Adapter.Attendance_only_Adapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.AttendanceListModel;
import com.example.admin.theoji.ModelClass.Attendance_only_Model;
import com.example.admin.theoji.ModelClass.StudentListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Student_get_Attendence extends AppCompatActivity {

    RecyclerView recycler_only_student;
    String server_url;
    ArrayList<Attendance_only_Model> Attendance_only_List=new ArrayList<>();
    private Attendance_only_Adapter attendance_only_adapter;

    TextView tv_date;
    Button btn_search;
    String Tv_Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_get__attendence);

       recycler_only_student=findViewById(R.id.recycler_attendance_student);
       tv_date=findViewById(R.id.tv_date_search);
       btn_search=findViewById(R.id.find_seach);


        if (Connectivity.isNetworkAvailable(Student_get_Attendence.this)){
            new Only_Student_Attendance_Task().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();

        }
//*******************************************************
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
                        DatePickerDialog mDatePicker = new DatePickerDialog(Student_get_Attendence.this, new DatePickerDialog.OnDateSetListener() {
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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tv_Date= tv_date.getText().toString();

                try {
                    if (Attendance_only_List.size() != 0) {
                        Attendance_only_List.clear();

                        recycler_only_student.setAdapter(null);
                        attendance_only_adapter.notifyDataSetChanged();

                    }
                }catch (Exception e){}

                if (Connectivity.isNetworkAvailable(Student_get_Attendence.this)) {
                    if (!Tv_Date.isEmpty()){
                        new Only_Student_Attendance_Task().execute();
                    }else {
                            Toast.makeText(Student_get_Attendence.this, "Please select date", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    Toast.makeText(Student_get_Attendence.this, "no internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



//**********************************************************
    public class Only_Student_Attendance_Task extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Student_get_Attendence.this);
            dialog.setMessage("Processing");
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_student_attendance?user_id="+
                        AppPreference.getUserid(Student_get_Attendence.this)+"&date"+Tv_Date;

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

                   // JSONObject object = new JSONObject(output);
                    //String res = object.getString("responce");

                    JSONObject dataJsonObject = new JSONObject(output);

//                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("attendance");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            String teacher_name = c.getString("teacher_name");
                            String date = c.getString("date");
                            String status = c.getString("status");

                            Attendance_only_List.add(new Attendance_only_Model(teacher_name,date, status));
                           // AttendanceOnlyHasMap.put(i, new Attendance_only_Model(teacher_name,date, status);

                        }


                        attendance_only_adapter = new Attendance_only_Adapter(Student_get_Attendence.this, Attendance_only_List);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Student_get_Attendence.this);
                        recycler_only_student.setLayoutManager(mLayoutManager);
                        recycler_only_student.setItemAnimator(new DefaultItemAnimator());
                        recycler_only_student.setAdapter(attendance_only_adapter);


                   // }
//                    else {
//
//                        Toast.makeText(Student_get_Attendence.this, "no post update now!", Toast.LENGTH_LONG).show();
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }
}
