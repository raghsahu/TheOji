package com.example.admin.theoji;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.SecondStepModel;
import com.example.admin.theoji.ModelClass.StateModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeconStepActivity extends Fragment {

    ArrayList<SecondStepModel> SecondStepList = new ArrayList<>();

    Spinner rteType;
    Spinner country;
    Spinner state;
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
    String State;

    Button cont_reg;
    public EditText student_fathername,student_mother_name,student_city,student_address,student_pincode,student_mobile,
    student_alternate_mobile,student_cast, student_bank_ifsc_code, student_sssmid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View secondView = inflater.inflate(R.layout.activity_secondstep , container , false);

        cont_reg=(Button)secondView.findViewById(R.id.cont_reg1);

        student_fathername=(EditText)secondView.findViewById(R.id.st_father_name);
        student_mother_name=(EditText)secondView.findViewById(R.id.st_mother_name);
        student_city=(EditText)secondView.findViewById(R.id.st_city);
        student_address=(EditText)secondView.findViewById(R.id.st_city);
        student_pincode=(EditText)secondView.findViewById(R.id.st_pincode);
        student_mobile=(EditText)secondView.findViewById(R.id.st_mobile);
        student_alternate_mobile=(EditText)secondView.findViewById(R.id.st_alternate_mobile);
        student_cast=(EditText)secondView.findViewById(R.id.st_cast);
        student_bank_ifsc_code=(EditText)secondView.findViewById(R.id.st_bank_ifsc_code);
        student_sssmid=(EditText)secondView.findViewById(R.id.st_sssmid);



        country = (Spinner)secondView.findViewById(R.id.country);
        rteType = (Spinner)secondView.findViewById(R.id.RTEtype);
        state = (Spinner)secondView.findViewById(R.id.state);

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

        rteTypeAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, rteTypeList);
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

        countryAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(countryAdapter);
//*********************************************************
        //state spinner list
        spin_state = (Spinner)secondView.findViewById(R.id.state);
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

        cont_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Student_gardian_name=student_fathername.getText().toString();
                String Student_mother_name=student_mother_name.getText().toString();
                String Student_city=student_city.getText().toString();
                String Student_Address=student_address.getText().toString();
                String Student_pincode=student_pincode.getText().toString();
                String Student_mobile=student_mobile.getText().toString();
                String Student_Alternate_mobile=student_alternate_mobile.getText().toString();
                String Student_state=state.getSelectedItem().toString();
                String Student_country=country.getSelectedItem().toString();
                String Student_cast=student_cast.getText().toString();
                String Student_rte_type=rteType.getSelectedItem().toString();
                String Student_bank_ifsc_code=student_bank_ifsc_code.getText().toString();
                String Student_sssmid=student_sssmid.getText().toString();

                SecondStepList.add(new SecondStepModel(Student_gardian_name,Student_mother_name,Student_city,Student_Address,Student_pincode,
                        Student_mobile,Student_Alternate_mobile,Student_state,Student_country,Student_cast,
                        Student_rte_type,Student_bank_ifsc_code,Student_sssmid));

            }
        });


        return secondView;
    }

    private class stateExecuteTask extends AsyncTask<String, Integer,String> {

        String output = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
            String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(getContext());


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

                    stateAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ChooseState);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_state.setAdapter(stateAdapter);

                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    }
