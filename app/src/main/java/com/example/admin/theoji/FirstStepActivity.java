package com.example.admin.theoji;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.FirstStepModel;

import java.io.Serializable;
import java.util.ArrayList;



public class FirstStepActivity extends Fragment implements View.OnClickListener {


    public ArrayList<FirstStepModel>  FirstStepList = new ArrayList<>();


    Button cont_reg;
    RadioButton Male, Female;
    RadioButton radioButton;
    int pos;
    int pos1;
    RadioGroup Radio_group_sex;
    public EditText email,password,student_name,student_dob,student_pre_sch_name,student_aadhar,student_category,student_bank_name,
    student_bank_account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View firstView = inflater.inflate(R.layout.activity_firststep , container , false);

//        return super.onCreateView(inflater, container, savedInstanceState);

        cont_reg=(Button)firstView.findViewById(R.id.cont_reg);

        email=(EditText)firstView.findViewById(R.id.st_email);
        password=(EditText)firstView.findViewById(R.id.st_pw);
        student_name=(EditText)firstView.findViewById(R.id.st_name);
        student_dob=(EditText)firstView.findViewById(R.id.st_dob);
        student_pre_sch_name=(EditText)firstView.findViewById(R.id.st_pre_school_name);
        student_aadhar=(EditText)firstView.findViewById(R.id.st_aadhar);
        student_category=(EditText)firstView.findViewById(R.id.st_category);
        student_bank_account=(EditText)firstView.findViewById(R.id.st_bank_account);
        student_bank_name=(EditText)firstView.findViewById(R.id.st_bank_name);
        //  Radio_group_sex=(RadioGroup)firstView.findViewById(R.id.sex);
        Male=(RadioButton)firstView.findViewById(R.id.male);
        Female=(RadioButton)firstView.findViewById(R.id.female);


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


//*********************************************************************
        cont_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


//                if(Male.isChecked())
//                {
//                    Toast.makeText(getContext(), ""+Male.getText().toString(), Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getContext(), ""+Female.getText().toString(), Toast.LENGTH_SHORT).show();
//                }


                String Student_Email=email.getText().toString();
                String Student_Password=password.getText().toString();
                String Student_Name=student_name.getText().toString();
                String Student_pre_school_name=student_pre_sch_name.getText().toString();
                String Student_Dob=student_dob.getText().toString();
                String Student_Aadhar=student_aadhar.getText().toString();
                String Student_category=student_category.getText().toString();
                String Student_Bank_Account=student_bank_account.getText().toString();
                String Student_Bank_Name=student_bank_name.getText().toString();
                String Student_sex=Male.getText().toString();



                if(Male.isChecked())
                {
                   // FirstStepList.add(new FirstStepModel(Student_Email,Student_Password,Student_Name,Student_pre_school_name,Student_Dob,
                      //      Student_Aadhar,Student_category,Student_Bank_Account,Student_Bank_Name, Male.getText().toString()));
//******************************************

                    Intent intent = new Intent(getContext(), AddStudentActivity.class);
                     intent.putExtra("key1", Student_Email);
                    intent.putExtra("key2", Student_Password);
                    intent.putExtra("key3", Student_Name);
                    intent.putExtra("key4", Student_pre_school_name);
                    intent.putExtra("key5", Student_Dob);
                    intent.putExtra("key6", Student_Aadhar);
                    intent.putExtra("key7", Student_category);
                    intent.putExtra("key8", Student_Bank_Account);
                    intent.putExtra("key9", Student_Bank_Name);
//                    startActivity(intent);
//***********************************************************
                }else{
                    FirstStepList.add(new FirstStepModel(Student_Email,Student_Password,Student_Name,Student_pre_school_name,Student_Dob,
                            Student_Aadhar,Student_category,Student_Bank_Account,Student_Bank_Name, Female.getText().toString()));

                    Intent intent = new Intent(getContext(), AddStudentActivity.class);
                    intent.putExtra("key", FirstStepList);
                    startActivity(intent);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("FirstList", FirstStepList);
                }



            }
        });

        return firstView;

}

    @Override
    public void onClick(View v) {

    }
}
