package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.AbsentActivity;
import com.example.admin.theoji.ModelClass.AttendanceListModel;
import com.example.admin.theoji.P_A_Details_Activity;
import com.example.admin.theoji.R;

import java.util.ArrayList;

import static com.example.admin.theoji.ShowAttendenceActivity.AttendanceHasMap;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private static final String TAG = "AttendanceAdapter";
    private ArrayList<AttendanceListModel> AttendanceList;
    public Context context;
    View viewlike;
    String PID = "";
    String id;
    String SID= "";
    public  int pos;
    String AttendanceID;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_present, btn_absent;
        public TextView txt_date, txt_classe, txt_section,txt_teacher, txt_preesent,txt_absent,txt_remark;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_date = (TextView) viewlike.findViewById(R.id.txt_date);
            txt_classe= (TextView) viewlike.findViewById(R.id.txt_class_sch);
            txt_section = (TextView) viewlike.findViewById(R.id.section);
            txt_teacher = (TextView) view.findViewById(R.id.txt_teacher);
            txt_preesent = (TextView) viewlike.findViewById(R.id.txt_present);
            txt_absent= (TextView) viewlike.findViewById(R.id.txt_absent);
            txt_remark = (TextView) viewlike.findViewById(R.id.txt_remark);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_present = (Button) viewlike.findViewById(R.id.present_btn);
            btn_absent = (Button) viewlike.findViewById(R.id.absent_btm);

        }
    }

    public static Context mContext;

    public AttendanceAdapter(Context mContext, ArrayList<AttendanceListModel> attendanceList) {
        context = mContext;
        AttendanceList = attendanceList;


    }

    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final AttendanceListModel attendanceListModel = AttendanceList.get(position);

        viewHolder.txt_date.setText(attendanceListModel.getDate());
        viewHolder.txt_classe.setText(attendanceListModel.getClass_name());
        viewHolder.txt_section.setText(attendanceListModel.getSection_name());
        viewHolder.txt_teacher.setText(attendanceListModel.getTeacher_name());
        viewHolder.txt_preesent.setText(attendanceListModel.getPresent());
        viewHolder.txt_absent.setText(attendanceListModel.getAbsent());
        viewHolder.txt_remark.setText(attendanceListModel.getRemark());

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_present.setTag(viewHolder);
        viewHolder.btn_absent.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             int i=position;
                AttendanceID= (AttendanceHasMap.get(i).getAttendance_id());

                Intent intent = new Intent(context, P_A_Details_Activity.class);
                intent.putExtra("AttendanceID",AttendanceID);
                context.startActivity(intent);
                Toast.makeText(context, "PrId"+AttendanceID, Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.btn_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=position;
                AttendanceID= (AttendanceHasMap.get(i).getAttendance_id());

                Intent intent = new Intent(context, AbsentActivity.class);
                intent.putExtra("AttendanceID",AttendanceID);
                context.startActivity(intent);
                Toast.makeText(context, "AbId"+AttendanceID, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return AttendanceList.size();
    }

    //******************************************************
}
