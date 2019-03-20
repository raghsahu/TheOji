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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.ClassSectionListModel;
import com.example.admin.theoji.ModelClass.SectionListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.ShowClassActivity;
import com.example.admin.theoji.Update_Class_Section_Activity;

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

import static com.example.admin.theoji.Update_Class_Section_Activity.sectionHashMap;


public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private static final String TAG = "SectionAdapter";
    private ArrayList<SectionListModel> SectionList;
    public Context context;
    View viewlike;
    String Section_ID = "";
    String id;
    String Edit_Section;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_update,btn_del;
      public EditText edit_section;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
          edit_section=viewlike.findViewById(R.id.txt_section_edit);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_del = (Button) viewlike.findViewById(R.id.class_delete);
            btn_update = (Button) viewlike.findViewById(R.id.section_update);


        }
    }

    public static Context mContext;

    public SectionAdapter(Context mContext, ArrayList<SectionListModel> sectionList) {
        context = mContext;
       SectionList = sectionList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.section_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SectionAdapter.ViewHolder viewHolder, final int position) {
        final SectionListModel sectionListModel = SectionList.get(position);

        viewHolder.edit_section.setText(sectionListModel.getM_name());

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_update.setTag(viewHolder);
        viewHolder.btn_del.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Edit_Section=viewHolder.edit_section.getText().toString();
                int i=position;
                    Section_ID=sectionHashMap.get(i).getM_id();
                    Toast.makeText(context, "s_Id"+Section_ID, Toast.LENGTH_SHORT).show();

                new SectionUpdateTask(view.getContext(),Section_ID).execute();
            }

        });

        viewHolder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // PID = ClassSectionListModel.get(position).getUser_id();
//                new deleteTask(view.getContext(),PID).execute();

            }
        });

    }

    @Override
    public int getItemCount() {
        return SectionList.size();
    }

    //******************************************************
    public class deleteTask extends AsyncTask<String,Integer,String> {
        Context context;
        public deleteTask(Context context, String pid) {
            this.context=context;

        }

        //    ProgressDialog dialog;
//
        protected void onPreExecute() {
//       dialog = new ProgressDialog(getContext());
//       dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData1(params);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            // Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

            if (result != null) {
                // dialog.dismiss();

                try {
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {

                        Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(context, "Some Problem post not delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//       super.onPostExecute(s);
            }
        }
    }
    public String PostData1(String[] values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/deletepost");

            JSONObject postDataParams = new JSONObject();
            id= AppPreference.getUserid(context);
            //PID = ClassSectionListModel.get;

            // postDataParams.put("pid",PID);
            postDataParams.put("id",id);

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

    private class SectionUpdateTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

      Context Context1;
        String Section_ID;
        public SectionUpdateTask(Context context, String section_id) {
            this.Context1=context;
            this.Section_ID=section_id;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(Context1);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/update_class_section");

                JSONObject postDataParams = new JSONObject();
                // String id= AppPreference.getUserid(Update_Class_Section_Activity.this);

                postDataParams.put("section",Edit_Section);
                postDataParams.put("section_id",Section_ID);

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

                try {
                    Toast.makeText(Context1, "result is" + result, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {
                        Toast.makeText(Context1, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Context1, ShowClassActivity.class);
                       Context1.startActivity(intent);
                        ((Activity)context).finish();

                    } else {
                        Toast.makeText(Context1, "unsuccess", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
