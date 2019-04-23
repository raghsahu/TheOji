package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.StudentAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.StudentListModel;
import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class StudentActivity extends AppCompatActivity {
    ImageView viewStudent;
    RecyclerView recyclerstudent;
    String server_url;
    ArrayList<StudentListModel> StudentList;
    private StudentAdapter studentAdapter;

    public static HashMap<Integer , StudentListModel> studentStringHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        viewStudent = (ImageView)findViewById(R.id.viewstudent);

        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this,AddStudentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerstudent = (RecyclerView)findViewById(R.id.recycler_view_student);

        int position=0;
       StudentList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(StudentActivity.this)){
            new GetStudentList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetStudentList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private StudentListModel studentListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(StudentActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/student_list?id="+ AppPreference.getUserid(StudentActivity.this);


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
                   String res = object.getString("responce");

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
                            String city = c.getString("city");
                            String status = c.getString("status");

                           String st_class = c.getString("umeta_value");
                           // String section = c.getString("umeta_value");

//                            String[] seperateData = st_class.split(Pattern.quote(","));
//                            for (int j = 0; j < seperateData.length; j++) {
//                                Log.e("Your st class Value-> ", seperateData[j]);
//                            }


                            StudentList.add(new StudentListModel(user_id, email, firstname, address,mobileno, city,st_class,status));
                           studentStringHashMap.put(i, new StudentListModel(user_id, email, firstname, address,mobileno, city,st_class,status));
                           //  AppPreference.setPostid(PostActivity.this,post_id);
                        }


                        studentAdapter = new StudentAdapter(StudentActivity.this, StudentList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StudentActivity.this);
                        recyclerstudent.setLayoutManager(mLayoutManager);
                        recyclerstudent.setItemAnimator(new DefaultItemAnimator());
                        recyclerstudent.setAdapter(studentAdapter);


                    }else {

                        Toast.makeText(StudentActivity.this, "no post update now, please check after some time!", Toast.LENGTH_LONG).show();
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

