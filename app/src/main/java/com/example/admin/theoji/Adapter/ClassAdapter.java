package com.example.admin.theoji.Adapter;

import android.content.Context;
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

import com.example.admin.theoji.ModelClass.ClassListModel;
import com.example.admin.theoji.ModelClass.TeacherListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

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

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private static final String TAG = "ClassAdapter";
    private ArrayList<ClassListModel> ClassList;
    public Context context;
    View viewlike;
    String PID = "";
    String id;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_del,btn_edit;
        public TextView txt1, txt2, txt3,txt4, txt_nm;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt1 = (TextView) viewlike.findViewById(R.id.txt_class_sch);
            txt2 = (TextView) viewlike.findViewById(R.id.txt_section);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_del = (Button) viewlike.findViewById(R.id.class_delete);
            btn_edit = (Button) viewlike.findViewById(R.id.class_edit);


        }
    }

    public static Context mContext;

    public ClassAdapter(Context mContext, ArrayList<ClassListModel> class_list) {
        context = mContext;
        ClassList = class_list;


    }

    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_item, parent, false);

        return new ClassAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ClassAdapter.ViewHolder viewHolder, final int position) {
        final ClassListModel classListModel = ClassList.get(position);

       // viewHolder.txt1.setText(classListModel.getEmail());
       // viewHolder.txt2.setText(classListModel.getFirstname());


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_edit.setTag(viewHolder);
        viewHolder.btn_del.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // PID = TeacherList.get(position).getUser_id();
//                new deleteTask(view.getContext(),PID).execute();

            }
        });
        viewHolder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // PID = TeacherList.get(position).getUser_id();
//                new deleteTask(view.getContext(),PID).execute();

            }
        });

    }

    @Override
    public int getItemCount() {
        return ClassList.size();
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
            //PID = TeacherListModel.ge;

            postDataParams.put("pid",PID);
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
}

