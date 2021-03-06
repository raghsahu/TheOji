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

import com.example.admin.theoji.Adapter.HomeworkAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.HomeworkModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class HomeworkActivity extends AppCompatActivity {
    ImageView viewhomeWork;
    RecyclerView recyclerHomework;
    String server_url;
    ArrayList<HomeworkModel> HomeworkList;
    private HomeworkAdapter homeworkAdapter;
    public static HashMap<Integer , String> HomeworkHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        viewhomeWork = (ImageView)findViewById(R.id.viewhomeWork);

        Toast.makeText(this, "user_id "+AppPreference.getUserid(HomeworkActivity.this), Toast.LENGTH_SHORT).show();

        if (AppPreference.getUser_Type(HomeworkActivity.this).equals("4")) {
            viewhomeWork.setVisibility(View.GONE);
        }

        viewhomeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeworkActivity.this,AddHomeWork.class);
                startActivity(intent);
                finish();
            }
        });


        recyclerHomework = (RecyclerView)findViewById(R.id.recycler_view_homework);

        int position=0;
        HomeworkList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(HomeworkActivity.this)){
            new GetHomeworkEvents().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetHomeworkEvents extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HomeworkActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_post_activity_student?id="+ AppPreference.getUserid(HomeworkActivity.this);

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

                    JSONObject object = new JSONObject(output);


                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String student_alot = jsonObject1.getString("student_alot");
                        JSONObject jsonObject=jsonObject1.getJSONObject("user");


                        String post_id = jsonObject.getString("post_id");
                        String post_author = jsonObject.getString("post_author");
                        String post_date = jsonObject.getString("post_date");
                        String post_title = jsonObject.getString("post_title");
                        String post_content = jsonObject.getString("post_content");
                        String email = jsonObject.getString("email");
                        String post_type = jsonObject.getString("post_type");
                        String umeta_value = jsonObject.getString("umeta_value");
                        String ref_id = jsonObject.getString("ref_id");
                        String plike = jsonObject.getString("plike");
                        String firstname = jsonObject.getString("firstname");
                        String pmeta_value = jsonObject.getString("pmeta_value");


                        String[] seperateData = pmeta_value.split(Pattern.quote(","));
                        String  actvitydate = seperateData[0];
                        String  imagename = seperateData[1];
                       // String participant  = seperateData[2];
                        String  studentclass = seperateData[2];
//


                            HomeworkList.add(i, new HomeworkModel(post_id, post_date, post_title, post_content,email,umeta_value,
                                    studentclass,actvitydate,imagename,firstname,student_alot));

                        HomeworkHashMap.put(i , post_id);
                        }

                        homeworkAdapter = new HomeworkAdapter(HomeworkActivity.this, HomeworkList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeworkActivity.this);
                        recyclerHomework.setLayoutManager(mLayoutManager);
                        recyclerHomework.setItemAnimator(new DefaultItemAnimator());
                        recyclerHomework.setAdapter(homeworkAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }


}


