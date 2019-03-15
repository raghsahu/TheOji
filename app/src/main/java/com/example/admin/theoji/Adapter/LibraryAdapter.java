package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.CommentModel;
import com.example.admin.theoji.ModelClass.LibraryListModel;
import com.example.admin.theoji.ModelClass.NewsEventModel;
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

import static com.androidquery.util.AQUtility.getContext;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private static final String TAG = "LibraryAdapter";
    private ArrayList<LibraryListModel> LibraryList;
    String Count;
    public Context context;
    View viewlike;
    String PID="";

    ArrayList<CommentModel> commentList = new ArrayList<>();
    RecyclerView recyclerView_comment;

    RecyclerCommentAdapter recyclerCommentAdapter;
     String id;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_person,img2;
        public ImageView btn1, btn2, btn3, dis_like;
        public TextView txt1, txt2, txt3,txt_nm,txt_title;
        public  TextView count1,count2;
        public EditText etcomment;
        public LinearLayout et_comment;
        public  RecyclerView recyclerView_comment;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            img_person = (ImageView)viewlike.findViewById(R.id.img_person);
            img2 = (ImageView)viewlike.findViewById(R.id.img2);
            txt1 = (TextView) viewlike.findViewById(R.id.txt1);
            txt2 = (TextView) viewlike.findViewById(R.id.txt2);
            txt3 = (TextView) viewlike.findViewById(R.id.txt3);
            txt_title = (TextView) view.findViewById(R.id.title);
            etcomment=(EditText)viewlike.findViewById(R.id.comment_post);
            recyclerView_comment=(RecyclerView)viewlike.findViewById(R.id.comment_view);

            txt_nm = (TextView) viewlike.findViewById(R.id.description);
            count1 = (TextView) viewlike.findViewById(R.id.badge_count1);
            count2 = (TextView) viewlike.findViewById(R.id.badge_count2);

            btn1 = (ImageView) viewlike.findViewById(R.id.btn1);
            btn2 = (ImageView) viewlike.findViewById(R.id.btn2);
            btn3 = (ImageView) viewlike.findViewById(R.id.btn3);
            dis_like=(ImageView)viewlike.findViewById(R.id.dis_btn);

//            lin_vew = (LinearLayout)view.findViewById(R.id.lin_vew);
            et_comment = (LinearLayout) viewlike.findViewById(R.id.ll_comment);
            cardeview = (CardView)viewlike.findViewById(R.id.cardeview);

//            commentList = new ArrayList<>();


        }
    }

    public static Context mContext;
    public LibraryAdapter(Context mContext, ArrayList<LibraryListModel> libraryList) {
        context = mContext;
        LibraryList =libraryList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.library_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final LibraryListModel libraryListModel = LibraryList.get(position);

        viewHolder.txt1.setText(libraryListModel.getName());
        viewHolder.txt2.setText(libraryListModel.getEmail());
        viewHolder.txt3.setText(libraryListModel.getDate());
        viewHolder.txt_title.setText(libraryListModel.getTitle());
        viewHolder.txt_nm.setText(libraryListModel.getContent());
//        Picasso.get().load("https://jntrcpl.com/theoji/uploads/apex_school.jpg").into(viewHolder.img2);
        viewHolder.img_person.setImageResource(R.drawable.person);
        Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+libraryListModel.getUserimg()).into(viewHolder.img_person);

        viewHolder.img2.setImageResource(R.drawable.img);
        Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+libraryListModel.getPostimg())
                .placeholder(R.drawable.img)
                .into(viewHolder.img2);



        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn1.setTag(viewHolder);
        viewHolder.btn2.setTag(viewHolder);
        viewHolder.btn3.setTag(viewHolder);
        viewHolder.dis_like.setTag(viewHolder);
        viewHolder.etcomment.setTag(viewHolder);
        viewHolder.pos = position;

        commentList = new ArrayList<>();

        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PID = LibraryList.get(position).getPost_id();
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

        viewHolder.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteTask(view.getContext(),PID).execute();
            }
        });




        viewHolder.etcomment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (viewHolder.etcomment.getRight() - viewHolder.etcomment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

//                        additemlistview();

                        EditText etcomment=viewlike.findViewById(R.id.comment_post);
                        recyclerView_comment=(RecyclerView)viewlike.findViewById(R.id.comment_view);


                        String comment = etcomment.getText().toString();
//                        commentList.add(new CommentModel(comment) );
//
//                        RecyclerCommentAdapter recyclerCommentAdapter = new RecyclerCommentAdapter(getContext(), commentList);
//
//                        recyclerView_comment.setAdapter(recyclerCommentAdapter);

//                        new CommentExecuteTask().execute();

                        return true;
                    }
                }
                return false;
            }
        });


    }



    @Override
    public int getItemCount() {
        return LibraryList.size();
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

    //*************************************comment**********************
    public class CommentExecuteTask extends AsyncTask<String,Integer,String> {
//    ProgressDialog dialog;

        protected void onPreExecute() {
//        dialog = new ProgressDialog(getContext());
//        dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData(params);

            return res;


        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
//            dialog.dismiss();

                try {
                    JSONObject responce = new JSONObject(result);
                    String res = responce.getString("responce");
//
//                JSONObject data= new JSONObject(result).getJSONObject("data");
//                user_id=data.getString("user_id");
//                String username1=data.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//        super.onPostExecute(s);
            }
        }
    }
    public String PostData(String[] values) {
        try {

            URL url = new URL("http://*******************");

            JSONObject postDataParams = new JSONObject();
//            postDataParams.put("Comment",et_comment.getText().toString());

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

//********************************************************
  public class deleteTask extends AsyncTask<String,Integer,String>{
    Context context;
    public deleteTask(Context context, String pid) {
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

        String res = PostData1(params);

        return res;
    }

    @Override
    protected void onPostExecute(String result) {

        //Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

        if (result != null) {
            // dialog.dismiss();

            try {
                JSONObject object = new JSONObject(result);
                String res = object.getString("responce");

                if (res.equals("true")) {

                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(context, "Some Problem post not delete", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//       super.onPostExecute(s);
        }
    }
}
    public String PostData1(String[] values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/deletepost");

            JSONObject postDataParams = new JSONObject();
            id= AppPreference.getUserid(context);
            PID = LibraryListModel.getPost_id();

            postDataParams.put("pid",PID);
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

}
