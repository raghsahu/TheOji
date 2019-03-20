package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.ClassSectionAdapter;
import com.example.admin.theoji.Adapter.TeacherAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.ClassSectionListModel;
import com.example.admin.theoji.ModelClass.TeacherListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowClassActivity extends AppCompatActivity {
    ImageView addClass;

    RecyclerView recyclerschool_class;
    String server_url;
    ArrayList<ClassSectionListModel> ClassSectionList=new ArrayList<>();
    private ClassSectionAdapter classSectionAdapter;

    public static HashMap<Integer , ClassSectionListModel> classSectionHashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);

    addClass=findViewById(R.id.addclass);
        recyclerschool_class = (RecyclerView)findViewById(R.id.recycler_class_list);

    addClass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(ShowClassActivity.this,AddClassActivity.class);
            startActivity(intent);
            finish();
        }
    });


        if (Connectivity.isNetworkAvailable(ShowClassActivity.this)){
            new GetClassSectionList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private class GetClassSectionList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private TeacherListModel teacherListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ShowClassActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_class_section?school_id="+ AppPreference.getUserid(ShowClassActivity.this);


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

                            String class_id = c.getString("class_id");
                            String m_name = c.getString("m_name");
                            String section = c.getString("section");


                            ClassSectionList.add(i, new ClassSectionListModel(class_id,m_name,section));
                            classSectionHashMap.put(i,new ClassSectionListModel(class_id,m_name,section));
                        }


                        classSectionAdapter = new ClassSectionAdapter(ShowClassActivity.this, ClassSectionList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowClassActivity.this);
                        recyclerschool_class.setLayoutManager(mLayoutManager);
                        recyclerschool_class.setItemAnimator(new DefaultItemAnimator());
                        recyclerschool_class.setAdapter(classSectionAdapter);

//
                    }else {

                        Toast.makeText(ShowClassActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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
