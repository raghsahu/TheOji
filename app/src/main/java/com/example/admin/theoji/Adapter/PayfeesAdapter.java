package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.AddPayFeesActivity;
import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.PayFeesActivity;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Student_fees_submit_detals;

import java.util.ArrayList;

import static com.example.admin.theoji.PayFeesActivity.PayFeesHasMap;

public class PayfeesAdapter   extends RecyclerView.Adapter<PayfeesAdapter.ViewHolder> {

    private static final String TAG = "PayfeesAdapter";
    private ArrayList<PayfeesListModel> PayfeesList;
    public Context context;
    View viewlike;
    String PID = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

       public Button btn_details;
        public TextView txt_id, txt_name, txt_class,txt_date, txt_totalfee,txt_totalPay, duePay;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_id = (TextView) viewlike.findViewById(R.id.txt_user_id);
            txt_name = (TextView) viewlike.findViewById(R.id.txt_first_name);
            txt_class = (TextView) viewlike.findViewById(R.id.st_class);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_totalfee = (TextView) viewlike.findViewById(R.id.txt_totalfees);
            txt_totalPay = (TextView) viewlike.findViewById(R.id.txt_lastfees);
            duePay = (TextView) viewlike.findViewById(R.id.txt_due);
            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
           btn_details = (Button) viewlike.findViewById(R.id.st_detail);


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

        viewHolder.txt_id.setText(payfeesListModel.getF_id());
        viewHolder.txt_name.setText(payfeesListModel.getFirstname());
        viewHolder.txt_date.setText(payfeesListModel.getLast_pay_date());
        viewHolder.txt_class.setText(payfeesListModel.getSt_class());
        viewHolder.txt_totalfee.setText(payfeesListModel.getSf_annualfess());
        viewHolder.txt_totalPay.setText(payfeesListModel.getTotal_pay());
        viewHolder.duePay.setText(payfeesListModel.getDue_payment());

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_details.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i=position;
                PID= String.valueOf(PayFeesHasMap.get(i).getF_id());

                Intent intent = new Intent(context, Student_fees_submit_detals.class);
                intent.putExtra("PayId",PID);
                context.startActivity(intent);
                Toast.makeText(context, "id"+PID, Toast.LENGTH_SHORT).show();
               // mContext.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return PayfeesList.size();
    }

}
