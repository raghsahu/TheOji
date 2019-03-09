package com.example.admin.theoji;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.StateModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Registration_activity extends AppCompatActivity {

    Spinner type,state,board;

    ArrayList<String> ChooseState;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList;

    EditText sch_code, full_name, father_name, email_id,mobile_no, passw,city, sch_name, sales_lead;

    TextInputLayout tv_city,tv_sch_code,tv_fullname,tv_fathername,tv_lead,tv_sch_name;
    CardView card_board;
    Button btn_register;
    CheckBox check_box;

    private ArrayAdapter<String> typeAdapter;
    private ArrayList<String> typeList;
    String Type;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // otherside tab soft keyboard closed
        LinearLayout layout = (LinearLayout) findViewById(R.id.login_body);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        sch_code=(EditText)findViewById(R.id.sch_code);
        full_name=(EditText)findViewById(R.id.full_name);
        father_name=(EditText)findViewById(R.id.father_name);
        email_id=(EditText)findViewById(R.id.email);
        passw=(EditText)findViewById(R.id.reg_pw);
        mobile_no=(EditText)findViewById(R.id.mobile);
        city=(EditText)findViewById(R.id.city);
        sch_name=(EditText)findViewById(R.id.sch_name);
        sales_lead=(EditText)findViewById(R.id.sales_lead);

        tv_city=(TextInputLayout)findViewById(R.id.tv_city);
        tv_fullname=(TextInputLayout)findViewById(R.id.tv_fullname);
        tv_fathername=(TextInputLayout)findViewById(R.id.tv_father_name);
        tv_sch_code=(TextInputLayout)findViewById(R.id.tv_code);
        tv_sch_name=(TextInputLayout)findViewById(R.id.tv_sch_name);
        tv_lead=(TextInputLayout)findViewById(R.id.tv_saleslead);
        card_board=(CardView)findViewById(R.id.card_views_board);
        check_box=findViewById(R.id.checkbox);
        btn_register=findViewById(R.id.cont_reg1);

        type = (Spinner)findViewById(R.id.type);
        state=(Spinner)findViewById(R.id.state_type);
        board=(Spinner)findViewById(R.id.sch_boardname);

        stateList = new ArrayList<>();
        ChooseState = new ArrayList<>();
        new state1ExecuteTask().execute();
        typeList = new ArrayList<>();
        typeList.add("Select your Type");
        typeList.add("School");
        typeList.add("Parent");
//********************************************************
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Sch_name=sch_name.getText().toString();
               // Sch_mail=sch_mail.getText().toString();


                if (check_box.isChecked())
                {
                   // new registerExecuteTask().execute();
                }else {
                    Toast.makeText(Registration_activity.this, "please accept terms & conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text=type.getSelectedItem().toString();
                if(text.equalsIgnoreCase("Parent")) {
                    tv_city.setVisibility(View.GONE);
                    tv_sch_name.setVisibility(View.GONE);
                    tv_lead.setVisibility(View.GONE);
                    card_board.setVisibility(View.GONE);
                }else {
                    tv_city.setVisibility(View.VISIBLE);
                    tv_sch_name.setVisibility(View.VISIBLE);
                    tv_lead.setVisibility(View.VISIBLE);
                    card_board.setVisibility(View.VISIBLE);
                }
                if(text.equalsIgnoreCase("School")) {
                    tv_sch_code.setVisibility(View.GONE);
                    tv_fullname.setVisibility(View.GONE);
                   tv_fathername.setVisibility(View.GONE);

                }else {
                    tv_sch_code.setVisibility(View.VISIBLE);
                    tv_fullname.setVisibility(View.VISIBLE);
                    tv_fathername.setVisibility(View.VISIBLE);
                }


                Type = typeAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeAdapter = new ArrayAdapter<String>(Registration_activity.this, R.layout.support_simple_spinner_dropdown_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);


    }

    protected void hideKeyboard(View view) {

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

        class state1ExecuteTask extends AsyncTask<String, Integer, String> {

            String output = "";
            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
                String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(Registration_activity.this);


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

                        stateAdapter = new ArrayAdapter<String>(Registration_activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        state.setAdapter(stateAdapter);

                        super.onPostExecute(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

