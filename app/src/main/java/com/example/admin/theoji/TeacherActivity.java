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

import com.example.admin.theoji.Adapter.TeacherAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.TeacherListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {
   ImageView viewTeacher, add_attendence;
    RecyclerView recyclerteacher;
    String server_url;
    ArrayList<TeacherListModel> TeacherList;
    private TeacherAdapter teacherAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        viewTeacher = (ImageView)findViewById(R.id.viewTeacher);
        add_attendence=(ImageView)findViewById(R.id.add_attendence);

        viewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherActivity.this,AddTeacher.class);
                startActivity(intent);
                finish();
            }
        });

        add_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherActivity.this,AttendenceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerteacher = (RecyclerView)findViewById(R.id.recycler_view_teacher);

        int position=0;
        TeacherList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(TeacherActivity.this)){
            new GetTeacherList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetTeacherList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private TeacherListModel teacherListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TeacherActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_teacher?id="+ AppPreference.getUserid(TeacherActivity.this);


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

                            String user_id = c.getString("user_id");
                            String email = c.getString("email");
                            String firstname = c.getString("firstname");
                            String address = c.getString("address");
                        String mobileno = c.getString("mobileno");
                        String umeta_value = c.getString("umeta_value");

                            TeacherList.add(0, new TeacherListModel(user_id, email, firstname, address,mobileno, umeta_value));
//                AppPreference.setPostid(PostActivity.this,post_id);
                        }


                        teacherAdapter = new TeacherAdapter(TeacherActivity.this, TeacherList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TeacherActivity.this);
                        recyclerteacher.setLayoutManager(mLayoutManager);
                        recyclerteacher.setItemAnimator(new DefaultItemAnimator());
                        recyclerteacher.setAdapter(teacherAdapter);

//
                    }else {

                        Toast.makeText(TeacherActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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

