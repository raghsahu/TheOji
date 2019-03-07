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

import com.example.admin.theoji.Adapter.PayfeesAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PayFeesActivity  extends AppCompatActivity {
    ImageView viewpayfees;
    RecyclerView recyclerpayfees;
    String server_url;
    ArrayList<PayfeesListModel> PayfeesList;
    private PayfeesAdapter payfeesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);

        viewpayfees = (ImageView)findViewById(R.id.viewpayfees);

        viewpayfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayFeesActivity.this,AddPayFeesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerpayfees = (RecyclerView)findViewById(R.id.recycler_view);

        int position=0;
        PayfeesList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(PayFeesActivity.this)){
            new GetPayfeesList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetPayfeesList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private PayfeesListModel payfeesListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(PayFeesActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/studentfeesdetail?id="+ AppPreference.getUserid(PayFeesActivity.this);


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

                            String f_student_id = c.getString("f_student_id");
                            String f_payfess = c.getString("f_payfess");
                            String f_feespaydate = c.getString("f_feespaydate");
                            String umeta_value = c.getString("umeta_value");
                            String firstname = c.getString("firstname");
                            String sf_annualfess = c.getString("sf_annualfess");

                           PayfeesList.add(0, new PayfeesListModel(f_student_id, f_payfess, f_feespaydate, umeta_value,firstname, sf_annualfess));
//                AppPreference.setPostid(PostActivity.this,post_id);
                        }


                        payfeesAdapter = new PayfeesAdapter(PayFeesActivity.this, PayfeesList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PayFeesActivity.this);
                        recyclerpayfees.setLayoutManager(mLayoutManager);
                        recyclerpayfees.setItemAnimator(new DefaultItemAnimator());
                        recyclerpayfees.setAdapter(payfeesAdapter);

//
                    }else {

                        Toast.makeText(PayFeesActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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

