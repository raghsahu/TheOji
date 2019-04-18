package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.theoji.ModelClass.P_A_Model;
import com.example.admin.theoji.ModelClass.PayfeesDetailListModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;

public class P_A_Adapter extends RecyclerView.Adapter<P_A_Adapter.ViewHolder> {

    private static final String TAG = "P_A_Adapter";
    private ArrayList<P_A_Model> PresentList;
    public Context context;
    View viewlike;
    String PID = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

       // public Button btn_delete;
        public TextView txt_date, txt_name,txt_class,txt_section;
      //  CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_date = (TextView) viewlike.findViewById(R.id.pa_date);
            txt_name = (TextView) viewlike.findViewById(R.id.pa_name);
            txt_class = (TextView) viewlike.findViewById(R.id.pa_class);
            txt_section = (TextView) viewlike.findViewById(R.id.pa_section);

           // cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
           // btn_delete = (Button) viewlike.findViewById(R.id.pay_delete);


        }
    }

    public static Context mContext;

    public P_A_Adapter(Context mContext, ArrayList<P_A_Model> presentList) {
        context = mContext;
        PresentList = presentList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.p_a_model_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final P_A_Model p_a_model = PresentList.get(position);

        viewHolder.txt_date.setText(p_a_model.getDate());
        viewHolder.txt_name.setText(p_a_model.getFirstname());
        viewHolder.txt_class.setText(p_a_model.getPa_class());
        viewHolder.txt_section.setText(p_a_model.getSection());

       // viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = position;


    }

    @Override
    public int getItemCount() {
        return PresentList.size();

    }
}
