package com.example.admin.theoji.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.theoji.Connection.HttpHandler;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.androidquery.util.AQUtility.getContext;

public class NewsEventsAdapter extends RecyclerView.Adapter<NewsEventsAdapter.ViewHolder> {

    private static final String TAG = "NewsEventsAdapter";
    private ArrayList<NewsEventModel> NewsEventsList;
    public Context context;
    String resId = "";
    String id;
    String PID;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView newsName, newsEmail, newsTittle, newsContent, newsDate;
        public ImageView  newsImg, btn1, btn2, btn3;
        public CircleImageView profileImg;
        public LinearLayout et_comment;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);

            newsName = (TextView) view.findViewById(R.id.txtname);
            newsEmail = (TextView) view.findViewById(R.id.txt2);
            newsContent = (TextView) view.findViewById(R.id.news_description);
//            newsTime = (TextView)view.findViewById(R.id.time);
            newsTittle = (TextView) view.findViewById(R.id.title_news);
            newsDate = (TextView) view.findViewById(R.id.date);
            profileImg = view.findViewById(R.id.img_person);
            newsImg = (ImageView) view.findViewById(R.id.iv_news);
            btn1 = (ImageView) view.findViewById(R.id.btn1);
            btn2 = (ImageView) view.findViewById(R.id.btn2);
            btn3 = (ImageView) view.findViewById(R.id.btn3);
            et_comment = (LinearLayout) view.findViewById(R.id.ll_comment);
            cardeview = (CardView) view.findViewById(R.id.cardeview);


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

        viewHolder.btn1.setTag(viewHolder);
        viewHolder.btn2.setTag(viewHolder);


        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetLikeCount(view).execute();

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
    public class GetLikeCount extends AsyncTask<String, Void, String> {
        String output = "";

        private Object viewHolder;
        private String server_url;
        View v;
        public GetLikeCount(View view) {
            this.v = view;

        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/plike?id="+AppPreference.getUserid(v.getContext())
                        +"&pid="+AppPreference.getPOSTID(v.getContext());


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

//                    TextView clike= v.findViewById(R.id.badge_count1);
//                  clike.setText(like);

                    Toast.makeText(v.getContext(),like,Toast.LENGTH_LONG).show();



                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(output);
        }

    }
//*********************************************************************
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

//        Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

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
            PID = NewsEventModel.getPost_id1();

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