package com.example.admin.theoji.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.admin.theoji.ModelClass.StudentListModel;
import com.example.admin.theoji.ModelClass.TeacherListModel;
import com.example.admin.theoji.R;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.StudentActivity;
import com.example.admin.theoji.Update_Student_Profile_Activity;

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
import static com.example.admin.theoji.StudentActivity.studentStringHashMap;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private static final String TAG = "StudentAdapter";
    private ArrayList<StudentListModel> StudentList;
    public Context context;
    View viewlike;
    String PID = "";
    String id;
    String SID= "";
    public  int pos;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn1,btn2,btn3;
        public TextView txt1, txt2, txt3,txt4, txt_nm,txt_class;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;
            txt1 = (TextView) viewlike.findViewById(R.id.txt_email);
            txt2 = (TextView) viewlike.findViewById(R.id.txt_first_name);
            txt3 = (TextView) viewlike.findViewById(R.id.mobile);
            txt4 = (TextView) view.findViewById(R.id.txt_address);
            txt_nm = (TextView) viewlike.findViewById(R.id.txt_city);
            txt_class = (TextView) viewlike.findViewById(R.id.txt_class);
            //txt_nm = (TextView) viewlike.findViewById(R.id.st_city);

            cardeview=(CardView)viewlike.findViewById(R.id.cardeview);
            btn1 = (Button) viewlike.findViewById(R.id.st_approve);
            btn2 = (Button) viewlike.findViewById(R.id.st_edit);
            btn3 = (Button) viewlike.findViewById(R.id.st_delete);


        }
    }

    public static Context mContext;

    public StudentAdapter(Context mContext, ArrayList<StudentListModel> studentLis) {
        context = mContext;
        StudentList = studentLis;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final StudentListModel studentListModel = StudentList.get(position);

        viewHolder.txt1.setText(studentListModel.getEmail());
        viewHolder.txt2.setText(studentListModel.getFirstname());
        viewHolder.txt3.setText(studentListModel.getMobileno());
        viewHolder.txt4.setText(studentListModel.getAddress());
        viewHolder.txt_nm.setText(studentListModel.getCity());
        viewHolder.txt_class.setText(studentListModel.getSt_class());

        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.btn1.setTag(viewHolder);
        viewHolder.btn2.setTag(viewHolder);
        viewHolder.btn3.setTag(viewHolder);
        viewHolder.pos = position;

       // viewHolder.btn1.setTag(1);
       viewHolder.btn1.setText("Approve");

        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewHolder.btn1.getText() == "Approve"){


                 pos = position;
                SID =  studentStringHashMap.get(pos);

               new approveTask(view.getContext(),SID , viewHolder.btn1.getTag()).execute();
                }
                else {
                    Toast.makeText(context, "already approve", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = position;
                SID =  studentStringHashMap.get(i);
               // new editStudentTask(view.getContext(),PID).execute();
                Intent intent = new Intent(context, Update_Student_Profile_Activity.class);
                intent.putExtra("uid",SID);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

        viewHolder.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("The Oji")
                        .setMessage("Are you sure, you want to delete this student");

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
                        int i = position;
                        SID =  studentStringHashMap.get(i);
                        new deleteTask(context,SID).execute();

                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return StudentList.size();
    }

    //******************************************************
    public class deleteTask extends AsyncTask<String,Integer,String> {
        Context context;
        String SID1;
        public deleteTask(Context context, String SID) {
            this.SID1=SID;
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

            String res = PostData1(SID1);

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
                        Intent intent = new Intent(context, StudentActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();


                    } else {
                        Toast.makeText(context, "Some Problem student not delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//       super.onPostExecute(s);
            }
        }

    }
    public String PostData1(String values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/delete_student");

            JSONObject postDataParams = new JSONObject();
            id= AppPreference.getUserid(context);

            postDataParams.put("id",values);
            //postDataParams.put("id",id);

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
//********************************************************************************************
    private class approveTask extends AsyncTask<String,Integer,String> {
        Context context;
        String SID2;
        Object tag1;
        public approveTask(Context context, String SID, Object tag) {
            this.SID2=SID;
            this.context=context;
            this.tag1=tag;

        }
        //    ProgressDialog dialog;
        protected void onPreExecute() {
//       dialog = new ProgressDialog(getContext());
//       dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData2(SID2);

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
                        Toast.makeText(context, "approve success", Toast.LENGTH_SHORT).show();

                      Button  btn1 = (Button) viewlike.findViewById(R.id.st_approve);
                       // final int status =(Integer) viewlike.getTag();
                       // if(status == 1) {
                            btn1.setText("Approved");
                            btn1.setBackgroundColor(Color.GREEN);
                           // btn1.setBackgroundColor("#7CFC00");
                           // viewlike.setTag(0);
//                        } else {
//                            btn1.setText("Approve");
//                            viewlike.setTag(1);
//                        }



                    } else {
                        Toast.makeText(context, "Some Problem not approve", Toast.LENGTH_SHORT).show();
                        Button  btn1 = (Button) viewlike.findViewById(R.id.st_approve);
                        btn1.setText("Approve");btn1.setBackgroundColor(Color.RED);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//       super.onPostExecute(s);
            }
        }
    }
    public String PostData2(String values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/confirm_student");

            JSONObject postDataParams = new JSONObject();
            id= AppPreference.getUserid(context);

            postDataParams.put("id",values);
            //postDataParams.put("id",id);

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
//*********************************************************

}
