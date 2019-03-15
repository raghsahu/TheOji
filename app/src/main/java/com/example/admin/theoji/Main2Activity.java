package com.example.admin.theoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.HomeAdapter;
import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.ModelClass.HomeListModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.example.admin.theoji.Shared_prefrence.SessionManager;
import com.example.admin.theoji.Utils.CustomAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerpost;
    String server_url;
    ArrayList<HomeListModel> HomeList= new ArrayList<>();
    private HomeAdapter homeAdapter;
    public static HashMap<Integer , String> postStringHashMap = new HashMap<>();

    TextView Nav_text_name,Nav_text_email;
    CircleImageView Profile_img;

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerpost = (RecyclerView)findViewById(R.id.recycler_post);
        int position=0;
        if (Connectivity.isNetworkAvailable(Main2Activity.this)){
            new GetPostList().execute();
        }else {
           // Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
        }

        manager = new SessionManager(Main2Activity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Nav_text_name=findViewById(R.id.nav_hedder_name);
        Nav_text_email=findViewById(R.id.nav_hedder_email);
        Profile_img=findViewById(R.id.profile_image);

        Nav_text_name.setText(AppPreference.getFirstname(Main2Activity.this));
        Nav_text_email.setText(AppPreference.getEmail(Main2Activity.this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.more_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                if (Connectivity.isNetworkAvailable(Main2Activity.this)) {
                    manager.logoutUser();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();

                } else {
                    Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
                }
                return true;

            case R.id.edit_profile:
                if (Connectivity.isNetworkAvailable(Main2Activity.this)) {
                    startActivity(new Intent(this, Edit_User_Profile_Activity.class));
                    //finish();

                } else {
                    Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
                }
                return true;

            case R.id.view_profile:
                if (Connectivity.isNetworkAvailable(Main2Activity.this)) {
                    startActivity(new Intent(this, View_User_Profile_Activity.class));
                    // finish();

                } else {
                    Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
                }

                return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        } else if (id == R.id.nav_post) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,PostActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        } else if (id == R.id.nav_buy_news) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,NewsActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        } else if (id == R.id.nav_homework) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,HomeworkActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        } else if (id == R.id.nav_project) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,ProjectActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        } else if (id == R.id.nav_student) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,StudentActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        }else if (id == R.id.nav_teacher) {

            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,TeacherActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

    } else if (id == R.id.nav_fees_details) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,PayFeesActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

    } else if (id == R.id.nav_library) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,LibraryActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

    } else if (id == R.id.nav_attendance) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,ShowAttendenceActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

    } else if (id == R.id.nav_class) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,ShowClassActivity.class);
                startActivity(intent);
                //finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

    }else if (id == R.id.nav_share) {
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
//                Intent intent = new Intent(Main2Activity.this,LoginActivity.class);
//                startActivity(intent);
//                finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        }else if (id == R.id.nav_logout) {
            manager.logoutUser();
            if (Connectivity.isNetworkAvailable(Main2Activity.this)){
                Intent intent = new Intent(Main2Activity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                CustomAlert.alertDialogShow(getApplicationContext(),"Please Check Internet!");
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class GetPostList extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;
        private HomeListModel postListModel;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Main2Activity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/theoji/index.php/Api/home_posts?id="+ AppPreference.getUserid(Main2Activity.this);


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
//                            String title = c.getString("post_title");
                            String content = c.getString("post_content");
//                        String posttype = c.getString("post_type");
//                        String ref_id1 = c.getString("ref_id");
//                        String like = c.getString("plike");
                            String name = c.getString("username");
                            String firstname = c.getString("firstname");
                            String email = c.getString("email");
                            String userimg = c.getString("umeta_value");
                            String postimg = c.getString("pmeta_value");


                            HomeList.add(i, new HomeListModel(post_id, name, email, date, content, userimg, postimg,firstname));
                            postStringHashMap.put(i , post_id);
//                AppPreference.setPostid(PostActivity.this,post_id);
                        }


                        homeAdapter = new HomeAdapter(Main2Activity.this, HomeList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main2Activity.this);
                        recyclerpost.setLayoutManager(mLayoutManager);
                        recyclerpost.setItemAnimator(new DefaultItemAnimator());
                        recyclerpost.setAdapter(homeAdapter);

//                           Picasso.get().load("https://jntrcpl.com/theoji/uploads/"+postListModel.getPostimg()).into(viewHolder.);


                    }else {

                        Toast.makeText(Main2Activity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
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
