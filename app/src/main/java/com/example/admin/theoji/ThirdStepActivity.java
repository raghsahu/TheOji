package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.FirstStepModel;
import com.example.admin.theoji.ModelClass.SecondStepModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

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

public class ThirdStepActivity extends Fragment {

    ArrayList<FirstStepModel> FirstStepList;
    ArrayList<SecondStepModel> SecondStepList;

    FirstStepModel firstStepModel;
    SecondStepModel secondStepModel;

    Spinner spin_st_class, std_section, std_board_name;
    Button SubmitDetails;

    EditText std_sch_code,std_session,std_admission_no,std_admission_date,std_about  ;
    String School_code, Student_class_type, Student_section,Student_session,Student_admission_no,Student_admission_date,
            Student_board_name,Student_about;
    String student_email,student_password,student_name;

    String student_gardian_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thirdView = inflater.inflate(R.layout.activity_thirdstep , container , false);

        SubmitDetails=(Button)thirdView.findViewById(R.id.submit_all_details);

        std_sch_code=(EditText)thirdView.findViewById(R.id.st_school_code);
        std_session=(EditText)thirdView.findViewById(R.id.st_session);
        std_admission_no=(EditText)thirdView.findViewById(R.id.st_admission_no);
        std_admission_date=(EditText)thirdView.findViewById(R.id.st_admission_date);
        std_about=(EditText)thirdView.findViewById(R.id.st_about);

        spin_st_class = (Spinner)thirdView.findViewById(R.id.st_classes);
        std_section = (Spinner)thirdView.findViewById(R.id.st_section);
        std_board_name = (Spinner)thirdView.findViewById(R.id.st_boardname);



        SubmitDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                School_code =std_sch_code.getText().toString();
                Student_class_type =spin_st_class.getSelectedItem().toString();
                Student_section =std_section.getSelectedItem().toString();
                Student_session=std_session.getText().toString();
                Student_admission_no=std_admission_no.getText().toString();
                Student_admission_date=std_admission_date.getText().toString();
               Student_board_name=std_board_name.getSelectedItem().toString();
                Student_about=std_about.getText().toString();

                //ArrayList<FirstStepModel> FirstStepList = new ArrayList<FirstStepModel>();
               // FirstStepList = (ArrayList<FirstStepModel>)getActivity().getIntent().getSerializableExtra("FirstList");

               // ArrayList<FirstStepModel> FirstStepList = (ArrayList<FirstStepModel>)getArguments().getSerializable("FirstList");


              // student_email = FirstStepList.get(0).getStudent_email();
              //   student_password=FirstStepList.get(0).getStudent_password();
              //  student_name=FirstStepList.get(0).getStudent_name();
//                String student_pre_school_name=firstStepModel.getStudent_pre_school_name();
//                 String student_dob=firstStepModel.getStudent_dob();
//                 String student_aadhar=firstStepModel.getStudent_aadhar();
//                 String student_category=firstStepModel.getStudent_category();
//                 String student_bank_account=firstStepModel.getStudent_bank_account();
//                 String student_bank_name=firstStepModel.getStudent_bank_name();
//                String student_sex=firstStepModel.getStudent_sex();
//
            //   student_gardian_name=SecondStepList.get(0).getStudent_gardian_name();
//                 String student_mother_name=secondStepModel.getStudent_mother_name();
//                String student_city=secondStepModel.getStudent_city();
//                String student_address=secondStepModel.getStudent_address();
//                String student_pincode=secondStepModel.getStudent_pincode();
//                 String student_mobile=secondStepModel.getStudent_mobile();
//                String student_alternate_mobile=secondStepModel.getStudent_alternate_mobile();
//                String student_state=secondStepModel.getStudent_state();
//                 String student_country=secondStepModel.getStudent_country();
//                String student_cast=secondStepModel.getStudent_cast();
//               String student_rte_type=secondStepModel.getStudent_rte_type();
//               String student_bank_ifsc_code=secondStepModel.getStudent_bank_ifsc_code();
//               String student_sssmid=secondStepModel.getStudent_sssmid();

               // new StudentCreatExecuteTask().execute();

            }
        });



        return thirdView;

    }


        class StudentCreatExecuteTask extends AsyncTask<String, Integer, String> {
            ProgressDialog dialog;

            protected void onPreExecute() {
                dialog = new ProgressDialog(getContext());
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
              Toast.makeText(getContext(), "result is" + result, Toast.LENGTH_SHORT).show();

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



//                        if (!res.equalsIgnoreCase("true")) {
//
//                            Toast.makeText(getContext(), "unsuccess", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getContext(), StudentActivity.class);
//                            startActivity(intent);
////                            finish();
//
//                        }


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
                String id= AppPreference.getUserid(getContext());

                postDataParams.put("id",id);

                postDataParams.put("Student_email",student_email);
                postDataParams.put("Student_password",student_password);
                postDataParams.put("Student_name",student_name);
                postDataParams.put("Student_DOB",firstStepModel.getStudent_dob());
                postDataParams.put("Student_gender",firstStepModel.getStudent_sex());
                postDataParams.put("Student_previous_school_name",firstStepModel.getStudent_pre_school_name());
                postDataParams.put("Student_aadhar_no",firstStepModel.getStudent_aadhar());
                postDataParams.put("Student_category",firstStepModel.getStudent_category());
                postDataParams.put("Student_bank_name",firstStepModel.getStudent_bank_name());
                postDataParams.put("Student_bank_account",firstStepModel.getStudent_bank_account());

                postDataParams.put("Student_gardian_name",secondStepModel.getStudent_gardian_name());
                postDataParams.put("Student_mother_name",secondStepModel.getStudent_mother_name());
                postDataParams.put("Student_city",secondStepModel.getStudent_city());
                postDataParams.put("Student_Address",secondStepModel.getStudent_address());
                postDataParams.put("Student_pincode",secondStepModel.getStudent_pincode());
                postDataParams.put("Student_mobile",secondStepModel.getStudent_mobile());
                postDataParams.put("Student_Alternate_mobile",secondStepModel.getStudent_alternate_mobile());
                postDataParams.put("Student_state",secondStepModel.getStudent_state());
                postDataParams.put("Student_country",secondStepModel.getStudent_country());
                postDataParams.put("student_cast",secondStepModel.getStudent_cast());
                postDataParams.put("Student_rte_type",secondStepModel.getStudent_rte_type());
                postDataParams.put("Student_bank_ifsc_code",secondStepModel.getStudent_bank_ifsc_code());
                postDataParams.put("Student_sssmid",secondStepModel.getStudent_sssmid());

                postDataParams.put("School_code",School_code);
                postDataParams.put("Student_class_type",Student_class_type);
                postDataParams.put("Student_section",Student_section);
                postDataParams.put("Student_session",Student_session);
                postDataParams.put("Student_admission_no",Student_admission_no);
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

    }
