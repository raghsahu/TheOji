package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.PayfeesAdapter;
import com.example.admin.theoji.Adapter.PayfeesDetailAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.PayfeesDetailListModel;
import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Student_fees_submit_detals extends AppCompatActivity {

    ArrayList<PayfeesDetailListModel> PayfeesDetailList=new ArrayList<>();
    private PayfeesDetailAdapter payfeesDetailAdapter;

    public static HashMap<Integer,PayfeesDetailListModel> PayFeesDetailHasMap=new HashMap<>();

     String server_url;
    RecyclerView recyclerpayfeesDetail;
     String PayID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fees_submit_detals);

        recyclerpayfeesDetail=findViewById(R.id.recycler_pay_detailfees);

        PayID= getIntent().getStringExtra("PayId");
        Toast.makeText(this, "pid"+PayID, Toast.LENGTH_SHORT).show();

        if (Connectivity.isNetworkAvailable(Student_fees_submit_detals.this)){
            new GetPayfeesDetails().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetPayfeesDetails extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

//        String PAYID;
//        public GetPayfeesDetails(String payID) {
//            this.PAYID=payID;
//        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Student_fees_submit_detals.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                    server_url = "https://jntrcpl.com/theoji/index.php/Api/onclick_studentfeesdetail?school_id="+
                            AppPreference.getUserid(Student_fees_submit_detals.this)+"&user_id="+PayID;


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

                            String f_id = c.getString("f_id");
                            String f_student_id = c.getString("f_student_id");
                            String f_school_id = c.getString("f_school_id");
                            String f_feespaydate = c.getString("f_feespaydate");
                            String f_student_class = c.getString("f_student_class");
                            String f_status= c.getString("f_status");
                            String f_payfess = c.getString("f_payfess");

                            PayfeesDetailList.add( new PayfeesDetailListModel(f_id, f_student_id,f_school_id, f_feespaydate,
                                    f_student_class,f_status,f_payfess));
                            PayFeesDetailHasMap.put(i, new PayfeesDetailListModel(f_id, f_student_id,f_school_id, f_feespaydate,
                                    f_student_class,f_status,f_payfess));

                            Toast.makeText(Student_fees_submit_detals.this, "px id"+f_id, Toast.LENGTH_SHORT).show();
                        }


                        payfeesDetailAdapter = new PayfeesDetailAdapter(Student_fees_submit_detals.this, PayfeesDetailList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Student_fees_submit_detals.this);
                        recyclerpayfeesDetail.setLayoutManager(mLayoutManager);
                        recyclerpayfeesDetail.setItemAnimator(new DefaultItemAnimator());
                        recyclerpayfeesDetail.setAdapter(payfeesDetailAdapter);

//
                    }else {

                        Toast.makeText(Student_fees_submit_detals.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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
