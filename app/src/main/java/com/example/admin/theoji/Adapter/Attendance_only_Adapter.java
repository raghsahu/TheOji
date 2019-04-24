package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Add_Chatting;
import com.example.admin.theoji.ModelClass.Attendance_only_Model;
import com.example.admin.theoji.ModelClass.ChatStudent_Model;
import com.example.admin.theoji.R;
import java.util.ArrayList;

public class Attendance_only_Adapter extends RecyclerView.Adapter<Attendance_only_Adapter.ViewHolder> {

    private ArrayList<Attendance_only_Model> Attedance_only_list;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView student_teacher, atend_date,attend_p_a;
       // CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);

            student_teacher=(TextView)view.findViewById(R.id.attend_teacher);
            atend_date=(TextView)view.findViewById(R.id.attend_date);
            attend_p_a=(TextView)view.findViewById(R.id.attend_p_a);

          //  cardeview = (CardView)view.findViewById(R.id.cardeview);

        }

    }

    public static Context mContext;
    public Attendance_only_Adapter(Context mContext, ArrayList<Attendance_only_Model> attend_only_List) {
        context = mContext;
        Attedance_only_list = attend_only_List;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_attendace_model, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final Attendance_only_Model attendance_only_model = Attedance_only_list.get(i);

        viewHolder.student_teacher.setText(attendance_only_model.getTeacher_name());
        viewHolder.attend_p_a.setText(attendance_only_model.getStatus());
        viewHolder.atend_date.setText(attendance_only_model.getDate());

        viewHolder.pos = i;

    }

    @Override
    public int getItemCount() {
        return Attedance_only_list.size();
    }
}