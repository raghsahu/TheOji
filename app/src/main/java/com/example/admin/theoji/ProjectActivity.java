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

import com.example.admin.theoji.Adapter.ProjectAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ProjectListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectActivity extends AppCompatActivity {

    ImageView viewProjects;
    RecyclerView recyclerproject;
    String server_url;
    ArrayList<ProjectListModel> ProjectList;
    private ProjectAdapter projectAdapter;
    TextView et_post;

   public static HashMap<Integer, String> ProjectHashMap=new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        viewProjects = (ImageView)findViewById(R.id.viewProjects);
        et_post=findViewById(R.id.et_post);

        viewProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this,AddProjects.class);
                startActivity(intent);
                finish();
            }
        });
        et_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this,AddProjects.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerproject = (RecyclerView)findViewById(R.id.recycler_view_project);

        int position=0;
        ProjectList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(ProjectActivity.this)){
            new GetProjectList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public class GetProjectList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private ProjectListModel postListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ProjectActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_school_project?id="+ AppPreference.getUserid(ProjectActivity.this);


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
                          String title = c.getString("post_title");
                            String content = c.getString("post_content");
//                        String posttype = c.getString("post_type");
//                        String ref_id1 = c.getString("ref_id");
//                        String like = c.getString("plike");
                            String name = c.getString("username");
                            String email = c.getString("email");
                            String userimg = c.getString("umeta_value");
                            String postimg = c.getString("pmeta_value");


                            ProjectList.add(i, new ProjectListModel(post_id, name, email, date,title, content, userimg, postimg));
                            ProjectHashMap.put(i,post_id);
                        }


                        projectAdapter = new ProjectAdapter(ProjectActivity.this, ProjectList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProjectActivity.this);
                        recyclerproject.setLayoutManager(mLayoutManager);
                        recyclerproject.setItemAnimator(new DefaultItemAnimator());
                        recyclerproject.setAdapter(projectAdapter);

//                           Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+postListModel.getPostimg()).into(viewHolder.);


                    }else {

                        Toast.makeText(ProjectActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
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

