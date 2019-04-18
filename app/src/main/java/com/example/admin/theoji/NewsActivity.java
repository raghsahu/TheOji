package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.NewsEventsAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.NewsEventModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends AppCompatActivity {

    ImageView addNews;
    RecyclerView recyclerNews;
    String server_url;
    ArrayList<NewsEventModel> NewsEventList;
    private NewsEventsAdapter newseventsadapter;
    TextView add_post_icon;

    public static HashMap<Integer, String> NewsHashMap=new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        addNews = (ImageView)findViewById(R.id.viewNews);
        if (AppPreference.getUser_Type(NewsActivity.this).equals("4")) {
            addNews.setVisibility(View.GONE);
        }

        add_post_icon=findViewById(R.id.et_post);
//        add_post_icon.setEnabled(false);
        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this,AddNewsEvents.class);
                startActivity(intent);
                finish();
            }
        });
        add_post_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this,AddNewsEvents.class);
                startActivity(intent);
                finish();
            }
        });
        //--------------------------------------------------------------------------

        recyclerNews = (RecyclerView)findViewById(R.id.recycler_view_news);

        int position=0;
        NewsEventList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(NewsActivity.this)){
            new GetNewsEvents().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetNewsEvents extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(NewsActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_news_update?id="+AppPreference.getUserid(NewsActivity.this);


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
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();

                    JSONObject responce = new JSONObject(output);
                    String res = responce.getString("responce");
                    JSONObject dataJsonObject = new JSONObject(output);

                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("data");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                       String post_id1 = c.getString("post_id");
//                        String author = c.getString("post_author");
                            String date = c.getString("post_date");
                            String title = c.getString("post_title");
                            String content = c.getString("post_content");
//                        String posttype = c.getString("post_type");
//                        String ref_id1 = c.getString("ref_id");
//                        String like = c.getString("plike");
                            String name = c.getString("username");
                            String email = c.getString("email");
                            String userimg = c.getString("umeta_value");
                            String postimg = c.getString("pmeta_value");


                            NewsEventList.add(i, new NewsEventModel(post_id1,name, email, title, date, content, userimg, postimg));
                            NewsHashMap.put(i ,post_id1);
                        }

                        newseventsadapter = new NewsEventsAdapter(NewsActivity.this, NewsEventList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewsActivity.this);
                        recyclerNews.setLayoutManager(mLayoutManager);
                        recyclerNews.setItemAnimator(new DefaultItemAnimator());
                        recyclerNews.setAdapter(newseventsadapter);

                    }else {

                        Toast.makeText(NewsActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(output);
            }

        }

    }


}
