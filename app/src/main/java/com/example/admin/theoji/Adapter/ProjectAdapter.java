package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.MotionEvent;
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
import com.example.admin.theoji.ModelClass.CommentModel;
import com.example.admin.theoji.ModelClass.ProjectListModel;
import com.example.admin.theoji.PostActivity;
import com.example.admin.theoji.ProjectActivity;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.squareup.picasso.Picasso;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.admin.theoji.ProjectActivity.ProjectHashMap;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private static final String TAG = "ProjectAdapter";
    private ArrayList<ProjectListModel> ProjectList;
    String Count;
    public Context context;
    View viewlike;
    String PID="";
    String id;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img2,img_close;
        public CircleImageView img_person;
        public ImageView btn1, btn2, btn3, dis_like;
        public TextView txt1, txt2, txt3,txt_nm,txt_title;
        TextView Click_all;
        public  TextView count1,count2;
        public LinearLayout et_comment;
        public  RecyclerView recyclerView_comment;
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
           txt_title = (TextView) view.findViewById(R.id.title);
            recyclerView_comment=(RecyclerView)viewlike.findViewById(R.id.comment_view);

            txt_nm = (TextView) viewlike.findViewById(R.id.description);
            count1 = (TextView) viewlike.findViewById(R.id.badge_count1);
            count2 = (TextView) viewlike.findViewById(R.id.badge_count2);
            Click_all = (TextView) viewlike.findViewById(R.id.commentall);

            btn1 = (ImageView) viewlike.findViewById(R.id.btn1);
            btn2 = (ImageView) viewlike.findViewById(R.id.btn2);
            btn3 = (ImageView) viewlike.findViewById(R.id.btn3);
            dis_like=(ImageView)viewlike.findViewById(R.id.dis_btn);
            img_close=(ImageView)viewlike.findViewById(R.id.image_close);

            et_comment = (LinearLayout) viewlike.findViewById(R.id.ll_comment);
            cardeview = (CardView)viewlike.findViewById(R.id.cardeview);

        }
    }

    public static Context mContext;
    public ProjectAdapter(Context mContext, ArrayList<ProjectListModel> project_list) {
        context = mContext;
        ProjectList = project_list;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ProjectListModel projectListModel = ProjectList.get(position);

        viewHolder.txt1.setText(projectListModel.getName());
        viewHolder.txt2.setText(projectListModel.getEmail());
        viewHolder.txt3.setText(projectListModel.getDate());
        viewHolder.txt_title.setText(projectListModel.getTitle());
        viewHolder.txt_nm.setText(projectListModel.getContent());
//        Picasso.get().load("https://jntrcpl.com/theoji/uploads/apex_school.jpg").into(viewHolder.img2);
        viewHolder.img_person.setImageResource(R.drawable.person);
        Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+projectListModel.getUserimg()).into(viewHolder.img_person);

        viewHolder.img2.setImageResource(R.drawable.img);
        Picasso.get()
                .load("https://jntrcpl.com/theoji/uploads/"+projectListModel.getPostimg())
                .placeholder(R.drawable.img)
                .into(viewHolder.img2);



        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn1.setTag(viewHolder);
        viewHolder.btn2.setTag(viewHolder);
        viewHolder.btn3.setTag(viewHolder);
        viewHolder.dis_like.setTag(viewHolder);
        viewHolder.img2.setTag(viewHolder);
        viewHolder.pos = position;


        viewHolder.img2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "iii "+projectListModel.getPostimg(), Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder alertadd = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
                ImageView dialogImage = new ImageView(context);
                final Dialog d = alertadd.setView(new View(context)).create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(d.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                if (projectListModel.getPostimg().length()!=0)
                {
                    Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+projectListModel.getPostimg())
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

                int i=position;
                PID = ProjectHashMap.get(i);
                new GetLikeCount(view, PID).execute();
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

        viewHolder.Click_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = position;
                PID =  ProjectHashMap.get(i);

                Intent intent = new Intent(context, Comment_View_Activity.class);
                intent.putExtra("postid",PID);
                context.startActivity(intent);

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
                        // String  PID = PostListModel.getPost_id();
                        int i = position;
                        PID =  ProjectHashMap.get(i);

                        new DeleteExecuteTask(context,PID).execute();

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


    }



    @Override
    public int getItemCount() {
        return ProjectList.size();
    }

    public class GetLikeCount extends AsyncTask<String, Void, String> {
        String output = "";

        private Object viewHolder;
        private String server_url;
        View v;


        public GetLikeCount(View view, String PID) {
            this.v = view;

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
                        +"&pid="+PID;


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

                    TextView count1=viewlike.findViewById(R.id.badge_count1);
                    count1.setText(like);
                    Toast.makeText(v.getContext(),like,Toast.LENGTH_LONG).show();



                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(output);
        }

    }

    //*************************************delete************************************************
    public class DeleteExecuteTask extends AsyncTask<String,Integer,String> {
        Context context;
        String PID1;

        public DeleteExecuteTask(Context context,String pid) {
            this.context=context;
            this.PID1=pid;

        }

        protected void onPreExecute() {
      //  dialog = new ProgressDialog(getContext());
       // dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/deletepost");

                JSONObject postDataParams = new JSONObject();
                id= AppPreference.getUserid(context);

                postDataParams.put("id",id);
                postDataParams.put("pid",PID1);


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
          //  Toast.makeText(context, "delete "+result, Toast.LENGTH_SHORT).show();
            if (result != null) {
            //dialog.dismiss();

                try {
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {

                        Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ProjectActivity.class);
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




}
