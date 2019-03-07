package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.theoji.ModelClass.CommentModel;
import com.example.admin.theoji.R;

import java.util.ArrayList;

 public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder> {


    private static final String TAG = "RecyclerCommentAdapter";

    private ArrayList<CommentModel>CommentList;
    public Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comment_view, comment_name, comment_date;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);

            comment_view=(TextView)view.findViewById(R.id.tv_comment);
            comment_name=(TextView)view.findViewById(R.id.comment_name);
            comment_date=(TextView)view.findViewById(R.id.tv_date_comment);
            cardeview = (CardView)view.findViewById(R.id.cardeview);

        }

    }

    public static Context mContext;
    public RecyclerCommentAdapter(Context mContext, ArrayList<CommentModel> commentList) {
        context = mContext;
        CommentList = commentList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final CommentModel commentModel = CommentList.get(i);

        viewHolder.comment_view.setText(commentModel.getCdescription());
        viewHolder.comment_name.setText(commentModel.getFirstname());
        viewHolder.comment_date.setText(commentModel.getComment_date());



        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = i;

    }

    @Override
    public int getItemCount() {
        return CommentList.size();
    }
}
