package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends ArrayAdapter<StudentModel> {

        private Context mContext;
        ArrayList<StudentModel> studentList;
        private StudentListAdapter studentListAdapter;
        private boolean isFromView = false;
      public static String present_students;

    //ArrayList<MultiSelectedItem> selected = new ArrayList<MultiSelectedItem>();

        public StudentListAdapter(Context context, int resource, List<StudentModel> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.studentList = (ArrayList<StudentModel>) objects;
            this.studentListAdapter = this;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(final int position, View convertView,
                                  ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(mContext);
                convertView = layoutInflator.inflate(R.layout.studentlist_item, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView.findViewById(R.id.text);
               // holder.mTextView1 = (TextView) convertView.findViewById(R.id.text1);
                holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(studentList.get(position).getFirstname());

            // To check weather checked event fire from getview() or user input
            isFromView = true;
            holder.mCheckBox.setChecked(studentList.get(position).isSelected());

            //******************************************
            isFromView = false;

            if ((position == 0)) {
                holder.mCheckBox.setVisibility(View.VISIBLE);
            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }

                holder.mCheckBox.setTag(position);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();

                    if (!isFromView) {
                        studentList.get(position).setSelected(isChecked);
                    }
//*****************************************************************
                    Toast.makeText(mContext, "select+"+studentList.get(position).getFirstname(), Toast.LENGTH_SHORT).show();

//                    ArrayList<String> selectedStdName= new ArrayList<>();
//
//                    selectedStdName.add(studentList.get(position).getFirstname());  // add

                   // StringBuilder stu_name = new StringBuilder();
                    try {

                        if (!present_students.isEmpty()){

                            if (holder.mCheckBox.isChecked()) {
                                present_students = present_students.concat(studentList.get(position).getFirstname().concat(","));
                            }
//                            else {
//                                present_students=present_students.replace();
//                            }
                        }
                    }catch (Exception e)
                    {
                      present_students = studentList.get(position).getFirstname().concat(",");
                      e.printStackTrace();                    }

//                    for (int i = 0; i < selectedStdName.size(); i++) {
//
//                        stu_name .append(selectedStdName.get(i));
//
//                        if (i == selectedStdName.size() - 1) {
//
//                           stu_name = stu_name .append(",");
//                        }
//
//                    }
                    // to create std_name string

                    //stu_name.toString();

                    Toast.makeText(mContext, "checked+ "+present_students, Toast.LENGTH_SHORT).show();


//**********************************************************
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView mTextView;
           // private TextView mTextView1;
            private CheckBox mCheckBox;
        }


}
