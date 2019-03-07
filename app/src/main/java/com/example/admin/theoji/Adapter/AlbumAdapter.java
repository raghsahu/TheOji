package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.theoji.ModelClass.AboutListModel;
import com.example.admin.theoji.ModelClass.AlbumListModel;
import com.example.admin.theoji.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private static final String TAG = "AlbumAdapter";

    private ArrayList<AlbumListModel> AlbumList;
    public Context context;
    View viewlike;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sch_album_view;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
          sch_album_view=(ImageView)viewlike.findViewById(R.id.img_sch_album);

            cardeview = (CardView)viewlike.findViewById(R.id.cardeview);

        }
    }

    public static Context mContext;
    public AlbumAdapter(Context mContext, ArrayList<AlbumListModel> albumlist) {
        context = mContext;
        AlbumList = albumlist;


    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);

        return new AlbumAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumAdapter.ViewHolder viewHolder, final int position) {
        final AlbumListModel albumListModel = AlbumList.get(position);

        viewHolder.sch_album_view.setImageResource(R.drawable.img);
        Picasso.get()
                .load("https://jntrcpl.com/theoji/uploads/"+albumListModel.getPmeta_value())
                .into(viewHolder.sch_album_view);


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = position;

    }


    @Override
    public int getItemCount() {
        return AlbumList.size();
    }

}
