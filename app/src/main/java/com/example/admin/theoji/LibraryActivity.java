package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.LibraryAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.LibraryListModel;
import com.example.admin.theoji.ModelClass.ProjectListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {
    ImageView viewlibrary;
    RecyclerView recyclerlibrary;
    String server_url;
    ArrayList<LibraryListModel> LibraryList;
    private LibraryAdapter libraryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        viewlibrary = (ImageView)findViewById(R.id.viewlibrary);

        viewlibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibraryActivity.this,AddLibraryActivity.class);
                startActivity(intent);

            }
        });

        recyclerlibrary = (RecyclerView)findViewById(R.id.recycler_view_library);

        int position=0;
      LibraryList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(LibraryActivity.this)){
            new GetLibraryList().execute();
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class GetLibraryList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private ProjectListModel postListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LibraryActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_library?id="+ AppPreference.getUserid(LibraryActivity.this);


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            //   Log.e("getcomment_url", output);
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

                    JSONObject responce = new JSONObject(output);
                    String res = responce.getString("responce");
                    JSONObject dataJsonObject = new JSONObject(output);

                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("data");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            String post_id = c.getString("post_id");
//                        String author = c.getString("post_author");
                            String date = c.getString("post_date");
                            String title = c.getString("post_title");
                            String content = c.getString("post_content");
//                        String posttype = c.getString("post_type");
//                        String ref_id1 = c.getString("ref_id");
//                        String like = c.getString("plike");
                            String name = c.getString("username");
                            String email = c.getString("email");
                            String userimg = c.getString("umeta_value");
                            String postimg1 = c.getString("pmeta_value");
                              if (postimg1.startsWith("|"))
                              {
                                  String postimg = postimg1.substring(1 , postimg1.length());
//                                  LibraryList.add(0,new LibraryListModel());
                                  LibraryList.add(0, new LibraryListModel(post_id, name, email, date,title, content, userimg,postimg));
                              }else{
                                  LibraryList.add(0, new LibraryListModel(post_id, name, email, date,title, content, userimg,postimg1));
                              }



//                AppPreference.setPostid(PostActivity.this,post_id);
                        }


                        libraryAdapter = new LibraryAdapter(LibraryActivity.this, LibraryList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LibraryActivity.this);
                        recyclerlibrary.setLayoutManager(mLayoutManager);
                        recyclerlibrary.setItemAnimator(new DefaultItemAnimator());
                        recyclerlibrary.setAdapter(libraryAdapter);

//                           Picasso.get().load("http://theoji.com/uploads/"+postListModel.getPostimg()).into(viewHolder.);


                    }else {

                        Toast.makeText(LibraryActivity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }

}

