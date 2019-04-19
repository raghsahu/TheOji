package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.Add_Chat_Model;
import com.example.admin.theoji.ModelClass.PayfeesDetailListModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;

public class AddChatAdapter extends RecyclerView.Adapter<AddChatAdapter.ViewHolder> {

    private static final String TAG = "AddChatAdapter";
    private ArrayList<Add_Chat_Model> AddChatList;
    public Context context;
    View viewlike;
    String PayId;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date, txt_name, txt_message;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_name = (TextView) viewlike.findViewById(R.id.chat_name);
            txt_date = (TextView) viewlike.findViewById(R.id.chat_date);
            txt_message = (TextView) viewlike.findViewById(R.id.chat_comment);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
        }
    }

    public static Context mContext;

    public AddChatAdapter(Context mContext, ArrayList<Add_Chat_Model> addchatList) {
        context = mContext;
        AddChatList = addchatList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_out_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Add_Chat_Model add_chat_model = AddChatList.get(position);

        viewHolder.txt_date.setText(add_chat_model.getDatecd());
       // viewHolder.txt_name.setText(add_chat_model.get);
        viewHolder.txt_message.setText(add_chat_model.getMessage());


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return AddChatList.size();

    }
}
