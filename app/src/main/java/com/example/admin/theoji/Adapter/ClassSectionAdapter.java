package com.example.admin.theoji.Adapter;

import android.app.Activity;
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

import com.example.admin.theoji.ModelClass.ClassSectionListModel;
import com.example.admin.theoji.ModelClass.TeacherListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.example.admin.theoji.ShowClassActivity.classSectionHashMap;

public class ClassSectionAdapter extends RecyclerView.Adapter<ClassSectionAdapter.ViewHolder> {

    private static final String TAG = "ClassSectionAdapter";
    private ArrayList<ClassSectionListModel> ClassSectionList;
    public Context context;
    View viewlike;
    String Class_ID = "";
    String id;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_edit,btn_del;
        public TextView txt_class_sch, txt_section_sch;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt_class_sch = (TextView) viewlike.findViewById(R.id.txt_class_sch);
            txt_section_sch = (TextView) viewlike.findViewById(R.id.txt_section_sch);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_del = (Button) viewlike.findViewById(R.id.class_delete);
            btn_edit = (Button) viewlike.findViewById(R.id.class_edit);


        }
    }

    public static Context mContext;

    public ClassSectionAdapter(Context mContext, ArrayList<ClassSectionListModel> classsectionList) {
        context = mContext;
        ClassSectionList = classsectionList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ClassSectionListModel classSectionListModel = ClassSectionList.get(position);

        viewHolder.txt_class_sch.setText(classSectionListModel.getM_name());

        if (classSectionListModel.getSection()=="null"){
            viewHolder.txt_section_sch.setText("");
        }else {
            viewHolder.txt_section_sch.setText(classSectionListModel.getSection());
        }



        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_edit.setTag(viewHolder);
        viewHolder.btn_del.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = position;
                        Class_ID=classSectionHashMap.get(i).getClass_id();
                        Toast.makeText(context, "c_Id"+Class_ID, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, Update_Class_Section_Activity.class);
                intent.putExtra("c_id",Class_ID);
                context.startActivity(intent);
                ((Activity)context).finish();
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
        return ClassSectionList.size();
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
}