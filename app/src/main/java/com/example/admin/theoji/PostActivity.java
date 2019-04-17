package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.PostAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.NewsEventModel;
import com.example.admin.theoji.ModelClass.PostListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    ImageView addPost;
    RecyclerView recyclerpost;
    String server_url;
    ArrayList<PostListModel> PostList;
    private PostAdapter postAdapter;
    public static HashMap<Integer , String> postStringHashMap = new HashMap<>();
    CardView add_post_icon;
    TextView et_post;
    LinearLayout ll_next_post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        addPost = (ImageView)findViewById(R.id.viewPost);
        add_post_icon=(CardView) findViewById(R.id.add_post_icon);
        ll_next_post=(LinearLayout)findViewById(R.id.le11);
        et_post=findViewById(R.id.et_post);
       // et_post.setEnabled(false);


        if (AppPreference.getUser_Type(PostActivity.this).equals("4")) {
            addPost.setVisibility(View.GONE);
        }

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this,AddPostActivity.class);
                startActivity(intent);
                finish();
            }
        });

        et_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this,AddPostActivity.class);
                startActivity(intent);
                 finish();
                // Toast.makeText(PostActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
            }
        });
        //--------------------------------------------------------------------------

        recyclerpost = (RecyclerView)findViewById(R.id.recycler_post);

        int position=0;
        PostList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(PostActivity.this)){
            new GetPostList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetPostList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private PostListModel postListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(PostActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_posts?id="+AppPreference.getUserid(PostActivity.this);


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


                            PostList.add(i, new PostListModel(post_id, name, email, date, content, userimg, postimg,firstname));
                            postStringHashMap.put(i , post_id);
                        }


                        postAdapter = new PostAdapter(PostActivity.this, PostList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PostActivity.this);
                        recyclerpost.setLayoutManager(mLayoutManager);
                        recyclerpost.setItemAnimator(new DefaultItemAnimator());
                        recyclerpost.setAdapter(postAdapter);

//                           Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+postListModel.getPostimg()).into(viewHolder.);


                    }else {

                        Toast.makeText(PostActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(output);
            }

        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
