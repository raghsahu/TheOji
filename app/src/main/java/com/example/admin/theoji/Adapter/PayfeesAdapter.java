package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;

public class PayfeesAdapter   extends RecyclerView.Adapter<PayfeesAdapter.ViewHolder> {

    private static final String TAG = "PayfeesAdapter";
    private ArrayList<PayfeesListModel> PayfeesList;
    public Context context;
    View viewlike;
    String PID = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

//        public Button btn1;
        public TextView txt1, txt2, txt3,txt4, txt_nm,txt_ll;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt1 = (TextView) viewlike.findViewById(R.id.txt_user_id);
            txt2 = (TextView) viewlike.findViewById(R.id.txt_first_name);
            txt3 = (TextView) viewlike.findViewById(R.id.st_class);
            txt4 = (TextView) view.findViewById(R.id.txt_date);
            txt_nm = (TextView) viewlike.findViewById(R.id.txt_totalfees);
            txt_ll = (TextView) viewlike.findViewById(R.id.txt_lastfees);
            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
//            btn1 = (Button) viewlike.findViewById(R.id.delete);


        }
    }

    public static Context mContext;

    public PayfeesAdapter(Context mContext, ArrayList<PayfeesListModel> payfeesList) {
        context = mContext;
        PayfeesList = payfeesList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_fees_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final PayfeesListModel payfeesListModel = PayfeesList.get(position);

        viewHolder.txt1.setText(payfeesListModel.getF_student_id());
        viewHolder.txt2.setText(payfeesListModel.getFirstname());
        viewHolder.txt3.setText(payfeesListModel.getF_feespaydate());
        viewHolder.txt4.setText(payfeesListModel.getUmeta_value());
        viewHolder.txt_nm.setText(payfeesListModel.getSf_annualfess());
        viewHolder.txt_ll.setText(payfeesListModel.getF_payfess());

        viewHolder.cardeview.setTag(viewHolder);
//        viewHolder.btn1.setTag(viewHolder);
        viewHolder.pos = position;

//        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PID = TeacherList.get(position).getUser_id();
//
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return PayfeesList.size();
    }

}
