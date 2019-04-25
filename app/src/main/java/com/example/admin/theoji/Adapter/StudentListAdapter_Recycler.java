package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.PostListModel;
import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter_Recycler extends RecyclerView.Adapter<StudentListAdapter_Recycler.ViewHolder> {

    View viewlike;
    public Context context;
    ArrayList<StudentModel> studentList;
    private StudentListAdapter_Recycler studentListAdapter_recycler;
    private boolean isFromView = false;
    public static String present_students = "";

    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public static ArrayList<String> multiselected_stud = new ArrayList<String>();
    boolean isChecked1;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public int pos;
        TextView mTextView;
       CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;

           mTextView = (TextView)viewlike.findViewById(R.id.text);
          mCheckBox = (CheckBox) viewlike.findViewById(R.id.checkbox);

        }
    }

    public static Context mContext;
    public StudentListAdapter_Recycler(Context mContext, ArrayList<StudentModel> stud_list) {
        context = mContext;
        studentList = stud_list;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.studentlist_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final StudentModel studentModel = studentList.get(position);

        viewHolder.mTextView.setText(studentModel.getFirstname());
        viewHolder.mCheckBox.setChecked(studentModel.isSelected());

        viewHolder.mCheckBox.setTag(position);
        viewHolder.pos = position;


        viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                // Toast.makeText(context, ""+getPosition, Toast.LENGTH_SHORT).show();

//*****************************************************************
                if (getPosition==0) {
                    try {
                        isChecked1 = studentList.get(0).isSelected();

                        if (isChecked1 == true) {
                            Toast.makeText(context, "all select", Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < studentList.size(); i++) {
                                studentList.get(i).setSelected(true);

                                multiselected_stud.add(studentList.get(i).getUser_id());
                                Toast.makeText(context, "all+ " + multiselected_stud, Toast.LENGTH_SHORT).show();
                            }
                            notifyDataSetChanged();

                        }
                        //*****************************************************************
                        else if (isChecked1 == false) {
                            for (int i = 0; i < studentList.size(); i++) {
                                studentList.get(i).setSelected(false);

                                multiselected_stud.remove(studentList.get(i).getUser_id());
                                Toast.makeText(context, "remo all- " + multiselected_stud, Toast.LENGTH_SHORT).show();
                            }
                            notifyDataSetChanged();
                        }

                    } catch (Exception e) {
//                        Toast.makeText(mContext, "" + e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
//**********************************************************
                else {

                    try{
                        boolean isChecked2 = !(studentList.get(0).isSelected());

                        if(isChecked2==true)
                        {
                            if (viewHolder.mCheckBox.isChecked()) {
                                multiselected_stud.add(studentList.get(position).getUser_id());
                                Toast.makeText(context, "multi+ "+multiselected_stud, Toast.LENGTH_SHORT).show();
                            }
                        }
                        //***************************************************************
                        else if(isChecked2==false)
                        {
                            // if (multiselected.contains(studentList.get(position).getUser_id())){

                            multiselected_stud.remove(studentList.get(position).getUser_id());
                            Toast.makeText(context, "Removechecked+ "+multiselected_stud, Toast.LENGTH_SHORT).show();
                        }
                        // }
                    }catch (Exception e){
                        Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }

    }
