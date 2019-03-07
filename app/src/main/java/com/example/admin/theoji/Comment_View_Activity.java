package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.RecyclerCommentAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.ModelClass.CommentModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

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



public class Comment_View_Activity extends AppCompatActivity {

    ArrayList<CommentModel> CommentList = new ArrayList<>();
    RecyclerCommentAdapter recyclerCommentAdapter;

    EditText commenttxt;
    String Commenttxt;
    ImageView btnSend;
     String id;

    RecyclerView recyclerView_comment;
    String PID="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);

        commenttxt=(EditText)findViewById(R.id.commenttxt);
        btnSend=(ImageView)findViewById(R.id.btnSend);
        recyclerView_comment = (RecyclerView)findViewById(R.id.comment_view);

        new CommentGetExcuteTask().execute();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Commenttxt = commenttxt.getText().toString();

                if (Connectivity.isNetworkAvailable(Comment_View_Activity.this)){
                    new PostComment().execute();
                }else {
                    Toast.makeText(Comment_View_Activity.this, "No Internet.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //********************************************************************************************
    private class CommentGetExcuteTask extends AsyncTask<String,Integer,String> {


        @Override
        protected void onPostExecute(String result) {

            //      Toast.makeText(context, "delete"+result, Toast.LENGTH_SHORT).show();

            if (result != null) {
                // dialog.dismiss();

                try {
                    JSONObject object = new JSONObject(result);

                    String like_status = object.getString("plike_status");
                    String Comment_count = object.getString("comment_count");
                    String Like_count = object.getString("like_count");

                    JSONArray Data_array = object.getJSONArray("comment");
                    for (int i = 0; i < Data_array.length(); i++) {
                        JSONObject c = Data_array.getJSONObject(i);

                        String comment_id = c.getString("comment_id");
                        String post_id = c.getString("post_id");
                        String cdescription = c.getString("cdescription");
                        String comment_date = c.getString("comment_date");
                        String firstname = c.getString("firstname");
                        String user_id = c.getString("user_id");

                        CommentList.add(i, new CommentModel(post_id,firstname,cdescription,comment_date));
                        //commentStringHashMap.put(i,post_id);
                    }

                    recyclerCommentAdapter = new RecyclerCommentAdapter(Comment_View_Activity.this, CommentList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Comment_View_Activity.this);
                    recyclerView_comment.setLayoutManager(mLayoutManager);
                    recyclerView_comment.setItemAnimator(new DefaultItemAnimator());
                    recyclerView_comment.setAdapter(recyclerCommentAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//       super.onPostExecute(s);
            }
        }
        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/like_check");

                JSONObject postDataParams = new JSONObject();
                id= AppPreference.getUserid(Comment_View_Activity.this);
                PID=getIntent().getStringExtra("postid");


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
//************************************************************
    public class PostComment extends AsyncTask<String, Void, String> {
    ProgressDialog dialog;

    protected void onPreExecute() {
        dialog = new ProgressDialog(Comment_View_Activity.this);
        dialog.show();
    }

    protected String doInBackground(String... arg0) {

        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/comment");

            JSONObject postDataParams = new JSONObject();

            id = AppPreference.getUserid(Comment_View_Activity.this);
            PID = getIntent().getStringExtra("postid");

            postDataParams.put("pid", PID);
            postDataParams.put("id", id);
            postDataParams.put("comment", Commenttxt);

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

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    StringBuffer Ss = sb.append(line);
                    Log.e("Ss", Ss.toString());
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            } else {
                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            dialog.dismiss();
            try {
                JSONObject responce = new JSONObject(result);
                String res = responce.getString("responce");

                if (res.equals("true")) {
                    Toast.makeText(Comment_View_Activity.this, "success", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Comment_View_Activity.this, Comment_View_Activity.class);
//                    startActivity(intent);
//                    finish();
                    reloadAllData();
                


                } else {
                    Toast.makeText(Comment_View_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

    private void reloadAllData() {
       // ArrayList<CommentModel> CommentList = new ArrayList<>();
        // update data in our adapter
      //  recyclerCommentAdapter.notifyDataSetChanged();
        //recyclerCommentAdapter.getData().addAll(CommentList);
        CommentList.clear();
        // fire the event
        recyclerCommentAdapter.notifyDataSetChanged();
        recyclerView_comment.removeAllViews();
        new CommentGetExcuteTask().execute();

    }

}
