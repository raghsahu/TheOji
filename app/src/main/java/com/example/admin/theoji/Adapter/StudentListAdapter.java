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

   // private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

   public static ArrayList<String> multiselected = new ArrayList<String>();
     boolean isChecked1;

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
                   // Toast.makeText(mContext, ""+getPosition, Toast.LENGTH_SHORT).show();

                    if (!isFromView) {
                        studentList.get(position).setSelected(isChecked);
                    }
//*****************************************************************
//                    if (getPosition==0) {
//                        try {
//                            isChecked1 = studentList.get(0).isSelected();
//
//                            if (isChecked1 == true) {
//                                Toast.makeText(mContext, "all select", Toast.LENGTH_SHORT).show();
//
//                                for (int i = 0; i < studentList.size(); i++) {
//                                    studentList.get(i).setSelected(true);
//
//                                    multiselected.add(studentList.get(i).getUser_id());
//                                    Toast.makeText(mContext, "all+ " + multiselected, Toast.LENGTH_SHORT).show();
//                                }
//                                notifyDataSetChanged();
//
//                            }
//                            //*****************************************************************
//                            else if (isChecked1 == false) {
//                                for (int i = 0; i < studentList.size(); i++) {
//                                    studentList.get(i).setSelected(false);
//
//                                    multiselected.remove(studentList.get(i).getUser_id());
//                                    Toast.makeText(mContext, "remo all- " + multiselected, Toast.LENGTH_SHORT).show();
//                                }
//                                notifyDataSetChanged();
//                            }
//
//                        } catch (Exception e) {
//                            Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
////**********************************************************
//                    else {
//
//                        try{
//                            boolean isChecked2 = !(studentList.get(0).isSelected());
//
//                            if(isChecked2==true)
//                            {
//                                if (holder.mCheckBox.isChecked()) {
//                                    multiselected.add(studentList.get(position).getUser_id());
//                                    Toast.makeText(mContext, "multi+ "+multiselected, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            //***************************************************************
//                            else if(isChecked2==false)
//                            {
//                           // if (multiselected.contains(studentList.get(position).getUser_id())){
//
//                                multiselected.remove(studentList.get(position).getUser_id());
//                                Toast.makeText(mContext, "Removechecked+ "+multiselected, Toast.LENGTH_SHORT).show();
//                                }
//                           // }
//                        }catch (Exception e){
//                            Toast.makeText(mContext, ""+e, Toast.LENGTH_SHORT).show();
//                        }
                   // }
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
