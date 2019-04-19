package com.example.admin.theoji;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.AddChatAdapter;
import com.example.admin.theoji.Adapter.PostAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.ModelClass.Add_Chat_Model;
import com.example.admin.theoji.ModelClass.PostListModel;
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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Add_Chatting extends AppCompatActivity {

    String ChatUserID;
    EditText chat_edit;
    ImageView iv_send;
    String CHAT_EDIT;

    RecyclerView recyclerView_chat_rec;
    String server_url;
    ArrayList<Add_Chat_Model> AddChatList;
    private AddChatAdapter addChatAdapter;
    public static HashMap<Integer , Add_Chat_Model> AddChatHashMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chatting);

        ChatUserID=getIntent().getStringExtra("ChatUserID");

        chat_edit=findViewById(R.id.et_chat_post);
        iv_send=findViewById(R.id.img_chat_send);
        recyclerView_chat_rec=findViewById(R.id.recycler_chat_recev);

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CHAT_EDIT=chat_edit.getText().toString();
                if (!CHAT_EDIT.isEmpty()){

                    if (Connectivity.isNetworkAvailable(Add_Chatting.this)){
                        new ChatSendExecuteTask().execute();

                    }else {
                        Toast.makeText(Add_Chatting.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class ChatSendExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Add_Chatting.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/chat");

                JSONObject postDataParams = new JSONObject();
               String id = AppPreference.getUserid(Add_Chatting.this);

                postDataParams.put("login_id", id);
                postDataParams.put("user_id", ChatUserID);
                postDataParams.put("message", CHAT_EDIT);

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
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());

            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    Toast.makeText(Add_Chatting.this, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");
                    String msg = object.getString("msg");

                    JSONArray Data_array = object.getJSONArray("chat");
                    for (int i = 0; i < Data_array.length(); i++) {
                        JSONObject c = Data_array.getJSONObject(i);

                        String school_chat_id = c.getString("school_chat_id");
                        String to = c.getString("to");
                        String from = c.getString("from");
                        String message = c.getString("message");
                        String datecd = c.getString("datecd");
                        String notice_school = c.getString("notice_school");
                        String Read_status = c.getString("Read_status");

                        AddChatList.add(i, new Add_Chat_Model(school_chat_id, to, from, message, datecd, notice_school,
                                Read_status));
                        AddChatHashMap.put(i , new Add_Chat_Model(school_chat_id, to, from, message, datecd, notice_school,
                                Read_status));

                    }

                    if (res.equals("true")) {

                        Toast.makeText(Add_Chatting.this, "Send Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Forgot_activity.this, ShowAttendenceActivity.class);
//                        startActivity(intent);
//                        finish();
                        addChatAdapter = new AddChatAdapter(Add_Chatting.this, AddChatList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Add_Chatting.this);
                        recyclerView_chat_rec.setLayoutManager(mLayoutManager);
                        recyclerView_chat_rec.setItemAnimator(new DefaultItemAnimator());
                        recyclerView_chat_rec.setAdapter(addChatAdapter);


                    } else {
                        Toast.makeText(Add_Chatting.this, "Please try again", Toast.LENGTH_SHORT).show();

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
