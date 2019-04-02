package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Comment_View_Activity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.HomeActivity;
import com.example.admin.theoji.Main2Activity;
import com.example.admin.theoji.ModelClass.CommentModel;
import com.example.admin.theoji.ModelClass.HomeListModel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.androidquery.util.AQUtility.getContext;
import static com.example.admin.theoji.Main2Activity.HomeHashMap;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private static final String TAG = "HomeAdapter";

    private ArrayList<HomeListModel> HomeList;
    String Count;
    public Context context;
    View viewlike;
    String PID="";

    private ArrayList<String> _imagePaths;

    ArrayList<CommentModel>CommentList = new ArrayList<>();
    // RecyclerView recyclerView_comment;
    //RecyclerCommentAdapter recyclerCommentAdapter;
    String id;
    String Comment;
    public static HashMap<Integer , String> CountCheckHashMap = new HashMap<>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img2;
        public CircleImageView img_person;
        public ImageView btn1, btn2, btn3, dis_like,send_comment,img_close;
        public TextView txt1, txt2, txt3,txt_nm;
        public  TextView count1,count2;
        public EditText etcomment;
        public LinearLayout et_comment;
        TextView Click_all;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            img_person = viewlike.findViewById(R.id.img_person);
            img2 = (ImageView)viewlike.findViewById(R.id.img2);
            txt1 = (TextView) viewlike.findViewById(R.id.txt1);
            txt2 = (TextView) viewlike.findViewById(R.id.txt2);
            txt3 = (TextView) viewlike.findViewById(R.id.txt3);
            Click_all = (TextView) view.findViewById(R.id.commentall);
            etcomment=(EditText)viewlike.findViewById(R.id.commenttxt);

            txt_nm = (TextView) viewlike.findViewById(R.id.txt_nm);
            count1 = (TextView) viewlike.findViewById(R.id.badge_count1);
            count2 = (TextView) viewlike.findViewById(R.id.badge_count2);

            btn1 = (ImageView) viewlike.findViewById(R.id.btn1);
            btn2 = (ImageView) viewlike.findViewById(R.id.btn2);
            btn3 = (ImageView) viewlike.findViewById(R.id.btn3);
            dis_like=(ImageView)viewlike.findViewById(R.id.dis_btn);
            img_close=(ImageView) viewlike.findViewById(R.id.image_close);
           // send_comment=(ImageView)viewlike.findViewById(R.id.btnSend);

//            lin_vew = (LinearLayout)view.findViewById(R.id.lin_vew);
            et_comment = (LinearLayout) viewlike.findViewById(R.id.ll_comment);
            // recyclerView_comment = (RecyclerView) viewlike.findViewById(R.id.comment_view);
            cardeview = (CardView)viewlike.findViewById(R.id.cardeview);

//            commentList = new ArrayList<>();


        }
    }

    public static Context mContext;
    public HomeAdapter(Context mContext, ArrayList<HomeListModel> home_list) {
        context = mContext;
        HomeList = home_list;


    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);

        return new HomeAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder viewHolder, final int position) {
        final HomeListModel homeListModel = HomeList.get(position);

        viewHolder.txt1.setText(homeListModel.getFirstname());
        viewHolder.txt2.setText(homeListModel.getEmail());
        viewHolder.txt3.setText(homeListModel.getDate());
        viewHolder.txt_nm.setText(homeListModel.getContent());
//        Picasso.get().load("https://jntrcpl.com/theoji/uploads/apex_school.jpg").into(viewHolder.img2);
        viewHolder.img_person.setImageResource(R.drawable.person);
        Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+homeListModel.getUserimg()).into(viewHolder.img_person);

        viewHolder.img2.setImageResource(R.drawable.img);
        Picasso.get()
                .load("https://jntrcpl.com/theoji/uploads/"+homeListModel.getPostimg())
                .placeholder(R.drawable.img)
                .into(viewHolder.img2);



        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn1.setTag(viewHolder);
        viewHolder.btn2.setTag(viewHolder);
        viewHolder.btn3.setTag(viewHolder);
        viewHolder.dis_like.setTag(viewHolder);
        viewHolder.img_close.setTag(viewHolder);
        viewHolder.img2.setTag(viewHolder);
        viewHolder.Click_all.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertadd = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
                ImageView dialogImage = new ImageView(context);
                final Dialog d = alertadd.setView(new View(context)).create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                if (homeListModel.getPostimg().length()!=0)
                {
                    Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+homeListModel.getPostimg())
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
                PID =  HomeHashMap.get(i).getPost_id();

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



        viewHolder.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("The Oji")
                        .setMessage("Are you sure, you want to delete this post");

                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        exitLauncher();
                    }

                    private void exitLauncher() {
                        int i = position;
                        PID =  HomeHashMap.get(i).getPost_id();
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

        viewHolder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.et_comment.setVisibility(View.VISIBLE);

                int i = position;
                PID =  HomeHashMap.get(i).getPost_id();

            }
        });

        viewHolder.Click_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = position;
             String PIDHome =  HomeHashMap.get(i).getPost_id();

                Intent intent = new Intent(context, Comment_View_Activity.class);
                intent.putExtra("postid",PIDHome);
                context.startActivity(intent);

            }
        });


    }



    @Override
    public int getItemCount() {
        return HomeList.size();
    }

    //****************************************************************
    public class LikeUnlikeExcuteTask extends AsyncTask<String, Void, String> {
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
                server_url = "https://jntrcpl.com/theoji/index.php/Api/plike?id="+ AppPreference.getUserid(v.getContext())
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

                    new HomeAdapter.CountStatusExcuteTask(getContext(),PIDg ).execute();

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(output);
        }

    }

    //*************************************comment**********************

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

    //***************************************************************************
    public class deleteTask extends AsyncTask<String,Integer,String>{
        Context context;
        String PID1;
        public deleteTask(Context context, String PID) {
            this.PID1=PID;
            this.context=context;
        }
        //    ProgressDialog dialog;
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
            //      Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();
            if (result != null) {
                // dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {

                        Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Main2Activity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();

                    } else {
                        Toast.makeText(context, "Some Problem post not delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.onPostExecute(result);
            }
        }
    }

//***********************************************************************************************

    public class CountStatusExcuteTask extends AsyncTask<String,Integer,String>{
        Context context1;
        String PIDg2;

        public CountStatusExcuteTask(Context context, String PIDg) {
            this.context1=context;
            this.PIDg2=PIDg;
        }


        @Override
        protected String doInBackground(String... params) {

//                String res = PostData2(PIDg2);
//
//                return res;
            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/like_check");

                JSONObject postDataParams = new JSONObject();
                id= AppPreference.getUserid(context1);

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

            //      Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

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
