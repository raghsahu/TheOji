package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.theoji.ModelClass.AboutListModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.ViewHolder> {

    private static final String TAG = "AboutUsAdapter";

    private ArrayList<AboutListModel> AboutUsList;
    public Context context;
    View viewlike;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1, txt2, txt3;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt1 = (TextView) viewlike.findViewById(R.id.about_title);
            txt2 = (TextView) viewlike.findViewById(R.id.tv_date_about);
            txt3 = (TextView) viewlike.findViewById(R.id.tv__about_description);

            cardeview = (CardView)viewlike.findViewById(R.id.cardeview);

        }
    }

    public static Context mContext;
    public AboutUsAdapter(Context mContext, ArrayList<AboutListModel> aboutusList) {
        context = mContext;
        AboutUsList = aboutusList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.aboutus_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final AboutListModel aboutListModel = AboutUsList.get(position);

        viewHolder.txt1.setText(aboutListModel.getAd_title());
        viewHolder.txt2.setText(aboutListModel.getAd_date());
        viewHolder.txt3.setText(aboutListModel.getAd_description());

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = position;

    }


    @Override
    public int getItemCount() {
        return AboutUsList.size();
    }
}
