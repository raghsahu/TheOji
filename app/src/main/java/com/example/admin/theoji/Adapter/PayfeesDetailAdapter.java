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

import com.example.admin.theoji.ModelClass.PayfeesDetailListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Student_fees_submit_detals;

import java.util.ArrayList;

public class PayfeesDetailAdapter extends RecyclerView.Adapter<PayfeesDetailAdapter.ViewHolder> {

    private static final String TAG = "PayfeesDetailAdapter";
    private ArrayList<PayfeesDetailListModel> PayfeesDetailList;
    public Context context;
    View viewlike;
    String PID = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_delete;
        public TextView txt_date, txt_pay;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_date = (TextView) viewlike.findViewById(R.id.fees_date);
            txt_pay = (TextView) viewlike.findViewById(R.id.fees_pay);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_delete = (Button) viewlike.findViewById(R.id.pay_delete);


        }
    }

    public static Context mContext;

    public PayfeesDetailAdapter(Context mContext, ArrayList<PayfeesDetailListModel> payfeesDetailList) {
        context = mContext;
        PayfeesDetailList = payfeesDetailList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feespay_details_model, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final PayfeesDetailListModel payfeesDetailListModel = PayfeesDetailList.get(position);

        viewHolder.txt_date.setText(payfeesDetailListModel.getF_feespaydate());
        viewHolder.txt_pay.setText(payfeesDetailListModel.getF_payfess());


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_delete.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int i=position;
//                PID= String.valueOf(PayFeesHasMap.get(i).getF_id());
//
//                Intent intent = new Intent(context, Student_fees_submit_detals.class);
//                intent.putExtra("PayId",PID);
//                context.startActivity(intent);
//                Toast.makeText(context, "id"+PID, Toast.LENGTH_SHORT).show();
                // mContext.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return PayfeesDetailList.size();

    }

}