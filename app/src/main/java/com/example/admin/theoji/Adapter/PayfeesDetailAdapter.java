package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.PayfeesDetailListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Student_fees_submit_detals;
import com.example.admin.theoji.TeacherActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.example.admin.theoji.Student_fees_submit_detals.PayFeesDetailHasMap;

public class PayfeesDetailAdapter extends RecyclerView.Adapter<PayfeesDetailAdapter.ViewHolder> {

    private static final String TAG = "PayfeesDetailAdapter";
    private ArrayList<PayfeesDetailListModel> PayfeesDetailList;
    public Context context;
    View viewlike;
   String PayId;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_delete;
        public TextView txt_date, txt_pay;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_date = (TextView) viewlike.findViewById(R.id.fees_date);
            txt_pay = (TextView) viewlike.findViewById(R.id.fees_pay);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_delete = (Button) viewlike.findViewById(R.id.pay_delete);


        }
    }

    public static Context mContext;

    public PayfeesDetailAdapter(Context mContext, ArrayList<PayfeesDetailListModel> payfeesDetailList) {
        context = mContext;
        PayfeesDetailList = payfeesDetailList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feespay_details_model, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final PayfeesDetailListModel payfeesDetailListModel = PayfeesDetailList.get(position);

        viewHolder.txt_date.setText(payfeesDetailListModel.getF_feespaydate());
        viewHolder.txt_pay.setText(payfeesDetailListModel.getF_payfess());


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_delete.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i=position;
                PayId= String.valueOf(PayFeesDetailHasMap.get(i).getF_id());

                new PayfeedeleteTask(view.getContext(),PayId).execute();
                Toast.makeText(context, "PayID "+PayId, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return PayfeesDetailList.size();

    }

    private class PayfeedeleteTask extends AsyncTask<String,Integer,String> {
        ProgressDialog dialog;
        Context context;
        String FeesPay_ID;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.show();
        }
        public PayfeedeleteTask(Context context, String payId) {
            this.context=context;
            this.FeesPay_ID=payId;
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/delete_pay_historey");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("f_id",FeesPay_ID);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            }
            catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

            if (result != null) {
                dialog.dismiss();

                try {
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {

                        Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Student_fees_submit_detals.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();


                    } else {
                        Toast.makeText(context, "Some Problem not delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//       super.onPostExecute(s);
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();


    }
}