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
import android.widget.Toast;

import com.example.admin.theoji.Adapter.HomeAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.HomeListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

//    ImageView addPost;
    RecyclerView recyclerpost;
    String server_url;
    ArrayList<HomeListModel> HomeList= new ArrayList<>();
    private HomeAdapter homeAdapter;
    public static HashMap<Integer , String> postStringHashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerpost = (RecyclerView)findViewById(R.id.recycler_post);

        int position=0;

        if (Connectivity.isNetworkAvailable(HomeActivity.this)){
            new GetPostList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetPostList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private HomeListModel postListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HomeActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/home_posts?id="+ AppPreference.getUserid(HomeActivity.this);


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

                            String post_id = c.getString("post_id");
//                        String author = c.getString("post_author");
                            String date = c.getString("post_date");
//                            String title = c.getString("post_title");
                            String content = c.getString("post_content");
//                        String posttype = c.getString("post_type");
//                        String ref_id1 = c.getString("ref_id");
//                        String like = c.getString("plike");
                            String name = c.getString("username");
                            String firstname = c.getString("firstname");
                            String email = c.getString("email");
                            String userimg = c.getString("umeta_value");
                            String postimg = c.getString("pmeta_value");


                            HomeList.add(i, new HomeListModel(post_id, name, email, date, content, userimg, postimg,firstname));
                            postStringHashMap.put(i , post_id);
//                AppPreference.setPostid(PostActivity.this,post_id);
                        }


                        homeAdapter = new HomeAdapter(HomeActivity.this, HomeList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
                        recyclerpost.setLayoutManager(mLayoutManager);
                        recyclerpost.setItemAnimator(new DefaultItemAnimator());
                        recyclerpost.setAdapter(homeAdapter);

//                           Picasso.get().load("http://theoji.com/uploads/"+postListModel.getPostimg()).into(viewHolder.);


                    }else {

                        Toast.makeText(HomeActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }

}
