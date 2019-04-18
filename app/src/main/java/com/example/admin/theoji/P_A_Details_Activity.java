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

import com.example.admin.theoji.Adapter.P_A_Adapter;
import com.example.admin.theoji.Adapter.PayfeesDetailAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.P_A_Model;
import com.example.admin.theoji.ModelClass.PayfeesDetailListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class P_A_Details_Activity extends AppCompatActivity {


    ArrayList<P_A_Model> P_A_List=new ArrayList<>();
    P_A_Adapter p_a_adapter;

    public static HashMap<Integer,P_A_Model> P_A_HasMap=new HashMap<>();

    String server_url;
    RecyclerView recycler_attende_p_a;
    String AttendenceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p__a__details);

        recycler_attende_p_a=findViewById(R.id.recycler_attende_p_a);

        AttendenceID= getIntent().getStringExtra("AttendanceID");
        Toast.makeText(this, "Aid"+AttendenceID, Toast.LENGTH_SHORT).show();

        if (Connectivity.isNetworkAvailable(P_A_Details_Activity.this)){
            new P_AttendenceDetails().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class P_AttendenceDetails extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

//        String PAYID;
//        public GetPayfeesDetails(String payID) {
//            this.PAYID=payID;
//        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(P_A_Details_Activity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                server_url = "https://jntrcpl.com/theoji/index.php/Api/present_list?attendance_id="+AttendenceID;


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
                    String date = responce.getString("date");
                    String teacher = responce.getString("teacher");
                    JSONObject dataJsonObject = new JSONObject(output);

                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("data");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONArray c = Data_array.getJSONArray(i);
                         for(int k=0;k<c.length();k++)
                         {

                             JSONObject object = c.getJSONObject(k);
                             String pa_class = object.getString("class");
                             String firstname = object.getString("firstname");
                             String section = object.getString("section");

                             P_A_List.add( new P_A_Model(date,teacher,firstname,pa_class, section));
                             P_A_HasMap.put(i, new P_A_Model(date,teacher,firstname,pa_class, section));
                         }


                        }


                        p_a_adapter = new P_A_Adapter(P_A_Details_Activity.this, P_A_List);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(P_A_Details_Activity.this);
                        recycler_attende_p_a.setLayoutManager(mLayoutManager);
                        recycler_attende_p_a.setItemAnimator(new DefaultItemAnimator());
                        recycler_attende_p_a.setAdapter(p_a_adapter);

//
                    }else {

                        Toast.makeText(P_A_Details_Activity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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
