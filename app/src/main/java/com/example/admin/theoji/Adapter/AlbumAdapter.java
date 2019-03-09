package com.example.admin.theoji.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        viewHolder.sch_album_view.setTag(viewHolder);
        viewHolder.pos = position;




        viewHolder.sch_album_view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertadd = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);

                ImageView dialogImage = new ImageView(context);
                final Dialog d = alertadd.setView(new View(context)).create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                if (albumListModel.getPmeta_value().length()!=0)
                {
                    Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+albumListModel.getPmeta_value())
                            .into(dialogImage);
                    d.show();
                    d.getWindow().setAttributes(lp);
                    d.setContentView(dialogImage);
                }else {
                    Toast.makeText(context, "no image found", Toast.LENGTH_SHORT).show();

                }
                ((AlertDialog) d).setButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d.dismiss();
                    }
                });
            }
        });


    }


    @Override
    public int getItemCount() {
        return AlbumList.size();
    }

}
