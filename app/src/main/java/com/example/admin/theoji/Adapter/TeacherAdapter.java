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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Add_Chatting;
import com.example.admin.theoji.ModelClass.TeacherListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
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

import static com.androidquery.util.AQUtility.getContext;
import static com.example.admin.theoji.TeacherActivity.TeacherHashMap;

public class TeacherAdapter  extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private static final String TAG = "TeacherAdapter";
    private ArrayList<TeacherListModel> TeacherList;
    public Context context;
    View viewlike;
     String id;
    String TeacherID;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn_teacher_delet;
        public TextView txt1, txt2, txt3,txt4, txt_nm;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt1 = (TextView) viewlike.findViewById(R.id.txt_email);
            txt2 = (TextView) viewlike.findViewById(R.id.txt_first_name);
            txt3 = (TextView) viewlike.findViewById(R.id.mobile);
           txt4 = (TextView) view.findViewById(R.id.txt_address);
            txt_nm = (TextView) viewlike.findViewById(R.id.txt_class);
            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn_teacher_delet = (Button) viewlike.findViewById(R.id.delete);


        }
    }

    public static Context mContext;

    public TeacherAdapter(Context mContext, ArrayList<TeacherListModel> teacher_list) {
        context = mContext;
        TeacherList = teacher_list;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final TeacherListModel teacherListModel = TeacherList.get(position);

        viewHolder.txt1.setText(teacherListModel.getEmail());
        viewHolder.txt2.setText(teacherListModel.getFirstname());
        viewHolder.txt3.setText(teacherListModel.getMobileno());
        viewHolder.txt4.setText(teacherListModel.getAddress());
        viewHolder.txt_nm.setText(teacherListModel.getUmeta_value());

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn_teacher_delet.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.btn_teacher_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=position;
                TeacherID = TeacherHashMap.get(i).getUser_id();

              new deleteTask(view.getContext(),TeacherID).execute();
                Toast.makeText(context, "tID"+TeacherID, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return TeacherList.size();
    }

    //******************************************************
    public class deleteTask extends AsyncTask<String,Integer,String> {
        ProgressDialog dialog;
        Context context;
        String Teacher_ID;

        protected void onPreExecute() {
          dialog = new ProgressDialog(context);
           dialog.show();
        }

        public deleteTask(Context context, String teacher_Id) {
            this.context=context;
            this.Teacher_ID=teacher_Id;
        }
        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/delete_teacher");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("id",Teacher_ID);

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
                        Intent intent = new Intent(context, TeacherActivity.class);
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