package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Add_Chatting;
import com.example.admin.theoji.Add_Performance;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.ModelClass.ChatStudent_Model;
import com.example.admin.theoji.ModelClass.Student_Perform_Model;
import com.example.admin.theoji.Performance_Activity;
import com.example.admin.theoji.Performance_month_act;
import com.example.admin.theoji.R;
import com.example.admin.theoji.TeacherActivity;
import com.squareup.picasso.Picasso;

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

import static com.example.admin.theoji.Performance_Activity.student_perform_HashMap;
import static com.example.admin.theoji.TeacherActivity.TeacherHashMap;

public class Student_Perform_Adapter extends RecyclerView.Adapter<Student_Perform_Adapter.ViewHolder> {


    private static final String TAG = "Student_Perform_Adapter";

    private ArrayList<Student_Perform_Model> Student_Perform_List;
    public Context context;
    String PerformUserID;
    Student_Perform_Model student_perform_model;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView studentName, student_class,student_id;
        ImageView iv_details, iv_delete;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);

            studentName=(TextView)view.findViewById(R.id.perm_name);
            student_class=(TextView)view.findViewById(R.id.perf_class);
            student_id=(TextView)view.findViewById(R.id.perm_id);
            iv_details=view.findViewById(R.id.iv_details);
            iv_delete=view.findViewById(R.id.iv_delete);

           // cardeview = (CardView)view.findViewById(R.id.cardeview);

        }

    }

    public static Context mContext;
    public Student_Perform_Adapter(Context mContext, ArrayList<Student_Perform_Model> student_Perform_List) {
        context = mContext;
        Student_Perform_List = student_Perform_List;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.peformance_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final Student_Perform_Model student_perform_model = Student_Perform_List.get(i);

        viewHolder.studentName.setText(student_perform_model.getFirstname());
        viewHolder.student_id.setText(student_perform_model.getUser_id());
        viewHolder.student_class.setText(student_perform_model.getUmeta_value());


//        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.iv_details.setTag(viewHolder);
        viewHolder.iv_delete.setTag(viewHolder);
        viewHolder.pos = i;


        viewHolder.iv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position=i;
               String PerformUserID=student_perform_HashMap.get(position);
               // Toast.makeText(context, "PerformUserID "+PerformUserID, Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(context, Performance_month_act.class);
                intent.putExtra("Student_perform_model",student_perform_model);
                intent.putExtra("PerformUserID",PerformUserID);
                view.getContext().startActivity(intent);

//                final Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setTitle("Fees Update");
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.performance_month_table);
//
//                TextView jan = (TextView) dialog.findViewById(R.id.january);
//                TextView feb = (TextView) dialog.findViewById(R.id.feb);
//                TextView march = (TextView) dialog.findViewById(R.id.march);
//                TextView april = (TextView) dialog.findViewById(R.id.april);
//                TextView may = (TextView) dialog.findViewById(R.id.may);
//                TextView june = (TextView) dialog.findViewById(R.id.june);
//                TextView july = (TextView) dialog.findViewById(R.id.july);
//                TextView august = (TextView) dialog.findViewById(R.id.august);
//                TextView sep = (TextView) dialog.findViewById(R.id.sep);
//                TextView oct = (TextView) dialog.findViewById(R.id.oct);
//                TextView nov = (TextView) dialog.findViewById(R.id.nov);
//                TextView dec = (TextView) dialog.findViewById(R.id.dec);
//
//                jan.setText(student_perform_model.getJanuary());
//                feb.setText(student_perform_model.getFebruary());
//                march.setText(student_perform_model.getMarch());
//                april.setText(student_perform_model.getApril());
//                may.setText(student_perform_model.getMay());
//                june.setText(student_perform_model.getJune());
//                july.setText(student_perform_model.getJuly());
//                august.setText(student_perform_model.getAugust());
//                sep.setText(student_perform_model.getSeptember());
//                oct.setText(student_perform_model.getOctober());
//                nov.setText(student_perform_model.getNovember());
//                dec.setText(student_perform_model.getDecember());
//
//                //****************************************
//                TextView dialogButton = (TextView) dialog.findViewById(R.id.btn_update_submit);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                   dialog.dismiss();
//                    }
//                });
//
//                dialog.show();


            }
        });


        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                int position=i;
                PerformUserID=student_perform_HashMap.get(position);
                Toast.makeText(context, "PerformUserID "+PerformUserID, Toast.LENGTH_SHORT).show();

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


                        new deleteTask(view.getContext(),PerformUserID).execute();
                        Toast.makeText(context, "PID "+PerformUserID, Toast.LENGTH_SHORT).show();

                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return Student_Perform_List.size();
    }

    private class deleteTask extends AsyncTask<String,Integer,String> {
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

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/delete_performance");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("performance_id",Teacher_ID);

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
                        Intent intent = new Intent(context, Performance_Activity.class);
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
