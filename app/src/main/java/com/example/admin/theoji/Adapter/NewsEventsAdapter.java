package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Comment_View_Activity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.CommentModel;
import com.example.admin.theoji.ModelClass.NewsEventModel;
import com.example.admin.theoji.NewsActivity;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.admin.theoji.NewsActivity.NewsHashMap;

public class NewsEventsAdapter extends RecyclerView.Adapter<NewsEventsAdapter.ViewHolder> {

    private static final String TAG = "NewsEventsAdapter";
    private ArrayList<NewsEventModel> NewsEventsList;
    public Context context;
    View viewlike;
    String id;
    String PID;
    ArrayList<CommentModel> CommentList = new ArrayList<>();
    public static HashMap<Integer , String> CountCheckHashMap = new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView newsName, newsEmail, newsTittle, newsContent, newsDate,Click_all;
        public ImageView  newsImg, btn1, btn2, btn3,img_close,dis_like;
        public CircleImageView profileImg;
        public LinearLayout et_comment;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;

            newsName = (TextView) view.findViewById(R.id.txtname);
            newsEmail = (TextView) view.findViewById(R.id.txt2);
            newsContent = (TextView) view.findViewById(R.id.news_description);
            Click_all = (TextView) view.findViewById(R.id.commentall);
            newsTittle = (TextView) view.findViewById(R.id.title_news);
            newsDate = (TextView) view.findViewById(R.id.date);
            profileImg = view.findViewById(R.id.img_person);
            newsImg = (ImageView) view.findViewById(R.id.iv_news);
            btn1 = (ImageView) view.findViewById(R.id.btn1);
            btn2 = (ImageView) view.findViewById(R.id.btn2);
            btn3 = (ImageView) view.findViewById(R.id.btn3);
            img_close = (ImageView) view.findViewById(R.id.image_close);
            et_comment = (LinearLayout) view.findViewById(R.id.ll_comment);
            cardeview = (CardView) view.findViewById(R.id.cardeview);
            dis_like=(ImageView)view.findViewById(R.id.dis_btn);


        }

    }

    public static Context mContext;

    public NewsEventsAdapter(Context mContext, ArrayList<NewsEventModel> news_list) {
        context = mContext;
        NewsEventsList = news_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_post_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final NewsEventModel newsEventModel = NewsEventsList.get(position);

//        Log.d( "position::: " , String.valueOf(NewsEventsList.size()));

        viewHolder.newsEmail.setText(newsEventModel.getEmail());
        viewHolder.newsTittle.setText(newsEventModel.getTitle());
        viewHolder.newsContent.setText(newsEventModel.getContent());
        viewHolder.newsName.setText(newsEventModel.getName());
        viewHolder.newsDate.setText(newsEventModel.getDate());

        viewHolder.profileImg.setImageResource(R.drawable.person);
        Picasso.get().load("https://jntrcpl.com/theoji/uploads/" + newsEventModel.getUserimg()).into(viewHolder.profileImg);

        viewHolder.newsImg.setImageResource(R.drawable.img);
        Picasso.get().load("https://jntrcpl.com/theoji/uploads/" + newsEventModel.getPostimg())
                .placeholder(R.drawable.img)
                .into(viewHolder.newsImg);


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = position;
        viewHolder.newsImg.setTag(viewHolder);
        viewHolder.img_close.setTag(viewHolder);
        viewHolder.dis_like.setTag(viewHolder);
        viewHolder.btn1.setTag(viewHolder);
        viewHolder.btn2.setTag(viewHolder);
        viewHolder.Click_all.setTag(viewHolder);


        viewHolder.newsImg.setOnClickListener(new View.OnClickListener() {
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

                if (newsEventModel.getPostimg().length()!=0)
                {
                    Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+newsEventModel.getPostimg())
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

        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = position;
                PID =  NewsHashMap.get(i);

                new LikeUnlikeExcuteTask(view , PID).execute();

                viewHolder.dis_like.setVisibility(View.VISIBLE);
                viewHolder.btn1.setVisibility(View.INVISIBLE);

            }
        });
        viewHolder.dis_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btn1.setVisibility(View.VISIBLE);
                viewHolder.dis_like.setVisibility(View.INVISIBLE);
            }
        });

        viewHolder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.et_comment.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("The Oji")
                        .setMessage("Are you sure, you want to delete this post");

                dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        exitLauncher();
                    }

                    private void exitLauncher() {
                        int i = position;
                        PID =  NewsHashMap.get(i);
                        new deleteTask(context,PID).execute();

                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewHolder.et_comment.setVisibility(view.GONE);

            }
        });

        viewHolder.Click_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = position;
                PID =  NewsHashMap.get(i);


                Intent intent = new Intent(context, Comment_View_Activity.class);
                intent.putExtra("postid",PID);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return NewsEventsList.size();
    }

    private String convertTime(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }
    //*************************************************




//*********************************************************************

//*********************************************************************
public class deleteTask extends AsyncTask<String,Integer,String>{

    Context context;
    String PID1;
    public deleteTask(Context context, String pid) {
        this.PID1=pid;
        this.context=context;

    }

    //    ProgressDialog dialog;
//
    protected void onPreExecute() {
//       dialog = new ProgressDialog(getContext());
//       dialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/deletepost");

            JSONObject postDataParams = new JSONObject();
            id= AppPreference.getUserid(context);

            postDataParams.put("pid",PID1);
            postDataParams.put("id",id);

            Log.e("postDataParams", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds*/);
            conn.setConnectTimeout(15000  /*milliseconds*/);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line);
                }
                r.close();
                return result.toString();

            } else {
                return new String("false : " + responseCode);
            }
        }
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {

//        Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

        if (result != null) {
            // dialog.dismiss();

            try {
                JSONObject object = new JSONObject(result);
                String res = object.getString("responce");

                if (res.equals("true")) {

                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, NewsActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();


                } else {
                    Toast.makeText(context, "Some Problem post not delete", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    private class LikeUnlikeExcuteTask extends AsyncTask<String, Void, String> {
        String output = "";

        String PIDg;
        private String server_url;
        View v;


        public LikeUnlikeExcuteTask(View view, String PID) {
            this.v = view;
            this.PIDg=PID;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // https://jntrcpl.com/theoji/index.php/Api/plike?id=
                server_url = "https://jntrcpl.com/theoji/index.php/Api/plike?id="+AppPreference.getUserid(v.getContext())
                        +"&pid="+PIDg;


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            //   Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }


        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
//                dialog.dismiss();
            } else {
                try {

                    JSONObject jsonObject = new JSONObject(output);
                    String like = jsonObject.getString("tlike");

//                    TextView count1=viewlike.findViewById(R.id.badge_count1);
//                    count1.setText(like);
//                    Toast.makeText(v.getContext(),like,Toast.LENGTH_LONG).show();

                    new CountStatusExcuteTask(v.getContext(),PIDg ).execute();

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(output);
        }

    }


    public class CountStatusExcuteTask extends AsyncTask<String,Integer,String>{
        Context context1;
        String PIDg2;

        public CountStatusExcuteTask(Context context, String PIDg) {
            this.context1=context;
            this.PIDg2=PIDg;
        }


        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/like_check");

                JSONObject postDataParams = new JSONObject();
                String id= AppPreference.getUserid(context);

                postDataParams.put("pid",PIDg2);
                postDataParams.put("id",id);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            }
            catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                try {
                    JSONObject object = new JSONObject(result);

                    String like_status = object.getString("plike_status");
                    String Comment_count = object.getString("comment_count");
                    String Like_count = object.getString("like_count");

                    JSONArray Data_array = object.getJSONArray("comment");
                    for (int i = 0; i < Data_array.length(); i++) {
                        JSONObject c = Data_array.getJSONObject(i);

                        String comment_id = c.getString("comment_id");
                        String post_id = c.getString("post_id");
                        String cdescription = c.getString("cdescription");
                        String comment_date = c.getString("comment_date");
                        String firstname = c.getString("firstname");
                        String user_id = c.getString("user_id");

                        CommentList.add(i, new CommentModel(post_id,firstname,cdescription,comment_date));
                        CountCheckHashMap.put(i,post_id);
                    }

                    TextView count1=viewlike.findViewById(R.id.badge_count1);
                    TextView count2=viewlike.findViewById(R.id.badge_count2);

                    if (Like_count.equals("false")) {
//                            Toast.makeText(context, "no like", Toast.LENGTH_SHORT).show();
                        count1.setText(0);
                    } else {
                        count1.setText(Like_count);
                        // Toast.makeText(context,"like"+Like_count,Toast.LENGTH_SHORT).show();
                    }


                    if (Comment_count.equals("false")) {
                        count2.setText(String.valueOf(0));
                        // Toast.makeText(context, "no comment", Toast.LENGTH_SHORT).show();

                    } else {
                        count2.setText(Comment_count);
                        // Toast.makeText(context,"C-like"+Comment_count,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//       super.onPostExecute(s);
            }
        }
    }
}