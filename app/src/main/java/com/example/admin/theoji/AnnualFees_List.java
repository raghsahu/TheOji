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

import com.example.admin.theoji.Adapter.AnnualListAdapter;
import com.example.admin.theoji.Adapter.PayfeesAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.AnnualListModel;
import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class AnnualFees_List extends AppCompatActivity {


    RecyclerView recycler_view_annuallist;
    String server_url;
    ArrayList<AnnualListModel> annualListModel=new ArrayList<>();
    private AnnualListAdapter annualListAdapter;

    public static HashMap<Integer,AnnualListModel>AnnualFeesHasMap=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_fees__list);


        recycler_view_annuallist=findViewById(R.id.recycler_view_annuallist);
        if (Connectivity.isNetworkAvailable(AnnualFees_List.this)){
            new AnnualfeesList().execute();

        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }


    }

    private class AnnualfeesList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AnnualFees_List.this);
            dialog.setMessage("Processing");
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                    server_url = "https://jntrcpl.com/theoji/index.php/Api/get_addannualfees?id="+
                            AppPreference.getUserid(AnnualFees_List.this);


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
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

                            String sf_id = c.getString("sf_id");
                            String sf_school_id = c.getString("sf_school_id");
                            String sf_school_class = c.getString("sf_school_class");
                            String sf_annualfess = c.getString("sf_annualfess");
                            String sf_date = c.getString("sf_date");
                            String sf_status = c.getString("sf_status");

//                            String[] seperateData = last_pay_date1.split(Pattern.quote(","));
//                            String  last_pay_date = seperateData[0];

                            annualListModel.add(i, new AnnualListModel(sf_id, sf_school_id,sf_school_class, sf_annualfess,
                                    sf_date,sf_status));
                            AnnualFeesHasMap.put(i, new AnnualListModel(sf_id, sf_school_id,sf_school_class, sf_annualfess,
                                    sf_date,sf_status));
                        }


                        annualListAdapter = new AnnualListAdapter(AnnualFees_List.this, annualListModel);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AnnualFees_List.this);
                        recycler_view_annuallist.setLayoutManager(mLayoutManager);
                        recycler_view_annuallist.setItemAnimator(new DefaultItemAnimator());
                        recycler_view_annuallist.setAdapter(annualListAdapter);

//
                    }else {

                        Toast.makeText(AnnualFees_List.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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
