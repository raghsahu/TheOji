package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.AnnualFees_List;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.AnnualListModel;
import com.example.admin.theoji.ModelClass.PayfeesListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Student_fees_submit_detals;
import com.example.admin.theoji.TeacherActivity;
import com.example.admin.theoji.Teacher_Edit_Activity;

import org.json.JSONArray;
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

import static com.example.admin.theoji.AnnualFees_List.AnnualFeesHasMap;
import static com.example.admin.theoji.PayFeesActivity.PayFeesHasMap;
import static com.example.admin.theoji.PostActivity.postStringHashMap;

public class AnnualListAdapter  extends RecyclerView.Adapter<AnnualListAdapter.ViewHolder> {

    private static final String TAG = "AnnualListAdapter";
    private ArrayList<AnnualListModel> AnnualListModels;
    public Context context;
    View viewlike;
    String PID = "";
     String server_url;
   EditText edit_fees;
     ProgressDialog dialog;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_class_fee_edit,btn_annual_delete;
        public TextView txt_class,txt_annual_fees, update_date;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_class = (TextView) viewlike.findViewById(R.id.txt_class_sch);
            txt_annual_fees = (TextView) view.findViewById(R.id.txt_annual_fees);
            update_date = (TextView) viewlike.findViewById(R.id.update_date);

            btn_class_fee_edit = (Button) viewlike.findViewById(R.id.btn_class_fee_edit);
            btn_annual_delete = (Button) viewlike.findViewById(R.id.btn_annual_delete);


        }
    }

    public static Context mContext;

    public AnnualListAdapter(Context mContext, ArrayList<AnnualListModel> annualListModels) {
        context = mContext;
        AnnualListModels = annualListModels;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.annual_fees_details_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final AnnualListModel annualListModel = AnnualListModels.get(position);

        viewHolder.txt_class.setText(annualListModel.getSf_school_class());
        viewHolder.txt_annual_fees.setText(annualListModel.getSf_annualfess());
        viewHolder.update_date.setText(annualListModel.getSf_date());

        viewHolder.btn_class_fee_edit.setTag(viewHolder);
        viewHolder.btn_annual_delete.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_class_fee_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i=position;
                PID= String.valueOf(AnnualFeesHasMap.get(i).getSf_id());
                Toast.makeText(context, "id "+PID, Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setTitle("Fees Update");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.edit_annual_fees_dialog);

                edit_fees = (EditText) dialog.findViewById(R.id.edit_fees);

               new Edit_Annual_fees(context,PID).execute();
                //****************************************
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_update_submit);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Connectivity.isNetworkAvailable(context)) {
                            if (!edit_fees.getText().toString().isEmpty()){
                                new Update_Annual_fees(PID,dialog).execute();
                            }else {
                                edit_fees.setError("Please Enter Fees");
                            }




                        }else {
                            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();


            }
        });


        viewHolder.btn_annual_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("The Oji")
                        .setMessage("Are you sure, you want to delete this post");

                dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        exitLauncher();
                    }

                    private void exitLauncher() {
                        int i=position;
                        PID= String.valueOf(AnnualFeesHasMap.get(i).getSf_id());
                       // new AnnualdeleteTask(context,PID).execute();

                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return AnnualListModels.size();
    }
//************************************************************************************
    private class Edit_Annual_fees extends AsyncTask<String, Void, String> {

    String output = "";
    ProgressDialog dialog;
    Context context;
    String PId;

    public Edit_Annual_fees(Context context, String pid) {
        this.context=context;
        this.PId=pid;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Processing");
        dialog.setCancelable(true);
        dialog.show();
        super.onPreExecute();

    }


    @Override
    protected String doInBackground(String... params) {

        try {

            server_url = "https://jntrcpl.com/theoji/index.php/Api/edit_addannualfees?sf_id="
                    +PId;

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sever_url>>>", server_url);
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

                JSONObject dataJsonObject = new JSONObject(output);
                String res = dataJsonObject.getString("responce");

                if (res.equals("true")) {

                    JSONArray Data_array = dataJsonObject.getJSONArray("data");
                    for (int i = 0; i < Data_array.length(); i++) {
                        JSONObject c = Data_array.getJSONObject(i);

                        String sf_annualfess=c.getString("sf_annualfess");

                        edit_fees.setText(sf_annualfess);

                    }


                }else {

                    Toast.makeText(context, "no details update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //  dialog.dismiss();
            }
            super.onPostExecute(output);
        }

    }


    }
//*********************************************
    private class Update_Annual_fees extends AsyncTask<String, Integer, String> {

        String PI_d;
        Dialog dialog_custom;
    public Update_Annual_fees(String pid, Dialog dialog) {
        this.dialog_custom=dialog;
        this.PI_d=pid;
    }

    protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/update_addannualfees");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("sf_id",PI_d);
                postDataParams.put("sf_annualfess",edit_fees.getText().toString());



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
            if (result != null) {
                dialog.dismiss();
                Log.e("result",result);
                try {
                     Toast.makeText(context, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");

                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(context, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        dialog_custom.dismiss();
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, AnnualFees_List.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
