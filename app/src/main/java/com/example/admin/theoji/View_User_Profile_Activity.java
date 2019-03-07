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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.AboutUsAdapter;
import com.example.admin.theoji.Adapter.AlbumAdapter;
import com.example.admin.theoji.Adapter.PostAdapter;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.AboutListModel;
import com.example.admin.theoji.ModelClass.AlbumListModel;
import com.example.admin.theoji.ModelClass.PostListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.squareup.picasso.Picasso;

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

import static com.example.admin.theoji.Edit_User_Profile_Activity.user_type;

public class View_User_Profile_Activity extends AppCompatActivity {
    TextView tv_total_std,tv_request_std,tv_conferm_std,tv_sch_code;
    Button btn_edit_profile,btn_add_about,btn_advertise;
    LinearLayout ll_aboutus,ll_sch_album,ll_view_about,llview_title,ll_view_album;
     String school_code;
     ImageView sch_image,banner_image;
     EditText et_title, et_discription;

    RecyclerView about_recycler,Album_recycler;
    String server_url;
    ArrayList<AboutListModel> AboutUsList=new ArrayList<AboutListModel>();
    private AboutUsAdapter aboutUsAdapter;

    ArrayList<AlbumListModel> AlbumList=new ArrayList<AlbumListModel>();
    private AlbumAdapter albumAdapter;
     String Et_title,Et_description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        tv_total_std=(TextView)findViewById(R.id.tv_total_std);
        tv_request_std=(TextView)findViewById(R.id.tv_request_std);
        tv_conferm_std=(TextView)findViewById(R.id.tv_conferm_std);
        tv_sch_code=(TextView)findViewById(R.id.sch_code);

        et_discription=findViewById(R.id.about_description);
        et_title=findViewById(R.id.title_about);

        btn_edit_profile=(Button)findViewById(R.id.edit_profile);
       // btn_add_about=(Button)findViewById(R.id.btn_add_about);
        btn_advertise=(Button)findViewById(R.id.btn_advertise);

        ll_aboutus=(LinearLayout)findViewById(R.id.about_us);
        ll_sch_album=(LinearLayout)findViewById(R.id.sch_album);
        ll_view_about=(LinearLayout)findViewById(R.id.llview_about_us);
        llview_title=(LinearLayout)findViewById(R.id.ll_title_post);
        ll_view_album=(LinearLayout)findViewById(R.id.llview_album);

       // sch_image=(ImageView)findViewById(R.id.school_image);
        banner_image=(ImageView)findViewById(R.id.bannere_image);

        about_recycler = (RecyclerView)findViewById(R.id.about_us_recycler);
        Album_recycler = (RecyclerView)findViewById(R.id.album_recycler);

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_User_Profile_Activity.this,Edit_User_Profile_Activity.class);
                startActivity(intent);
                finish();

            }
            });

        btn_advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Et_title=et_title.getText().toString();
          Et_description=et_discription.getText().toString();

                new PostAboutExcuteTask().execute();

            }
        });

        ll_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ll_view_about.setVisibility(View.VISIBLE);
                ll_view_album.setVisibility(View.GONE);
                llview_title.setVisibility(View.VISIBLE);


            }
        });

        ll_sch_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_view_about.setVisibility(View.GONE);
                llview_title.setVisibility(View.GONE);
                ll_view_album.setVisibility(View.VISIBLE);

            }
        });

        new GetViewProfileExcuteTask().execute();


    }

    private class GetViewProfileExcuteTask extends AsyncTask<String, Void, String> {
            String output = "";
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(View_User_Profile_Activity.this);
                dialog.setMessage("Processing");
                dialog.setCancelable(true);
                dialog.show();
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/profile_view");
                   // server_url = "https://jntrcpl.com/theoji/index.php/Api/profile_view";

//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.e("sever_url>>>>>>>>>", server_url);
//                output = HttpHandler.makeServiceCall(server_url);
//                //   Log.e("getcomment_url", output);
//                System.out.println("getcomment_url" + output);
//                return output;

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id",AppPreference.getUserid(View_User_Profile_Activity.this));
                   // postDataParams.put("user_type",user_type);


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


            @Override
            protected void onPostExecute(String output) {
                if (output == null) {
                    dialog.dismiss();
                } else {
                    try {
                        dialog.dismiss();
                       // Toast.makeText(View_User_Profile_Activity.this, "qq"+output, Toast.LENGTH_SHORT).show();

                        JSONObject dataJsonObject = new JSONObject(output);
                        String res = dataJsonObject.getString("responce");

                        String total = dataJsonObject.getString("total");
                        tv_total_std.setText(total);
                        Toast.makeText(View_User_Profile_Activity.this, "aa"+total, Toast.LENGTH_SHORT).show();
                        String request = dataJsonObject.getString("request");
                        tv_request_std.setText(request);
                        String confirm = dataJsonObject.getString("confirm");
                        tv_conferm_std.setText(confirm);
                        String banner_image = dataJsonObject.getString("banner_image");

                        Picasso.get()
                                .load("https://jntrcpl.com/theoji/uploads/"+banner_image)
                                .into(View_User_Profile_Activity.this.banner_image);

                        if (res.equals("true")) {

                            JSONArray Data_array = dataJsonObject.getJSONArray("data");
                            for (int i = 0; i < Data_array.length(); i++) {
                                JSONObject c = Data_array.getJSONObject(i);

                                String user_id = c.getString("user_id");
                                String firstname = c.getString("firstname");
                                String school_code = c.getString("school_code");

                                tv_sch_code.setText(school_code);
                            }

                           // tv_sch_code.setText(school_code);


                            JSONArray Data_array1 = dataJsonObject.getJSONArray("About_Us");
                            for (int i = 0; i < Data_array1.length(); i++) {
                                JSONObject d = Data_array1.getJSONObject(i);

                                String ad_id = d.getString("ad_id");
                                String ad_description = d.getString("ad_description");
                                String ad_date = d.getString("ad_date");
                                String ad_title = d.getString("ad_title");
                                String user_id = d.getString("user_id");

                                AboutUsList.add(i, new AboutListModel(ad_id,ad_description,ad_date,ad_title, user_id));
                            }
                            aboutUsAdapter = new AboutUsAdapter(View_User_Profile_Activity.this,AboutUsList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(View_User_Profile_Activity.this);
                            about_recycler.setLayoutManager(mLayoutManager);
                            about_recycler.setItemAnimator(new DefaultItemAnimator());
                            about_recycler.setAdapter(aboutUsAdapter);

                            JSONArray Data_array2 = dataJsonObject.getJSONArray("album");
                            for (int i = 0; i < Data_array2.length(); i++) {
                                JSONObject d = Data_array2.getJSONObject(i);

                                String post_id = d.getString("post_id");
                                String post_author = d.getString("post_author");
                                String post_date = d.getString("post_date");
                                String post_title = d.getString("post_title");
                                String post_content = d.getString("post_content");
                                String post_type = d.getString("post_type");
                                String ref_id = d.getString("ref_id");
                                String pmeta_id = d.getString("pmeta_id");
                                String pmeta_key= d.getString("pmeta_key");
                                String pmeta_value = d.getString("pmeta_value");

                                AlbumList.add(i, new AlbumListModel(post_id,post_author,post_date,post_title, post_content,post_type,
                                        ref_id,pmeta_id,pmeta_key,pmeta_value));
                            }
                            albumAdapter = new AlbumAdapter(View_User_Profile_Activity.this,AlbumList);
                            RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(View_User_Profile_Activity.this);
                            Album_recycler.setLayoutManager(sLayoutManager);
                            Album_recycler.setItemAnimator(new DefaultItemAnimator());
                            Album_recycler.setAdapter(albumAdapter);

//                                Picasso.get()
//                                        .load("https://jntrcpl.com/theoji/uploads/"+pmeta_value)
//                                        .into(View_User_Profile_Activity.this.sch_image);
//
//                            }


                        }else {

                            Toast.makeText(View_User_Profile_Activity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  dialog.dismiss();
                    }
                    super.onPostExecute(output);
                }

            }
    }

    private class PostAboutExcuteTask  extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(View_User_Profile_Activity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData(params);

            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                   //  Toast.makeText(View_User_Profile_Activity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(View_User_Profile_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(View_User_Profile_Activity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(View_User_Profile_Activity.this, View_User_Profile_Activity.class);
                        startActivity(intent);
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String PostData(String[] values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/advertise");

            String id= AppPreference.getUserid(View_User_Profile_Activity.this);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("ad_title", Et_title);
            postDataParams.put("ad_description", Et_description);
            postDataParams.put("user_id",id);



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
