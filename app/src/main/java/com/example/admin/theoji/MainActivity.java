package com.example.admin.theoji;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.admin.theoji.Connection.Connectivity;
import com.example.admin.theoji.Shared_prefrence.SessionManager;

public class MainActivity extends AppCompatActivity {
    CardView home,post,news,homework,student,projects,teacher,fees_details,library,attendence;
    Toolbar toolbar;

    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ActionBar myactionbar=getSupportActionBar();
//        if (myactionbar!=null){
//            myactionbar.setDisplayHomeAsUpEnabled(true);
//        }

        if (Connectivity.isNetworkAvailable(MainActivity.this)){

        }else {
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }


        home = (CardView)findViewById(R.id.home);
        post = (CardView)findViewById(R.id.post);
        news = (CardView)findViewById(R.id.news);
        homework = (CardView)findViewById(R.id.activites);
        student = (CardView)findViewById(R.id.student);
        projects = (CardView)findViewById(R.id.projects);
        teacher = (CardView)findViewById(R.id.teacher);
        fees_details = (CardView)findViewById(R.id.feesdetails);
        library = (CardView)findViewById(R.id.library);
        attendence = (CardView)findViewById(R.id.attendence);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    //finish();
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,PostActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,HomeworkActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,StudentActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,ProjectActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,TeacherActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fees_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,PayFeesActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,LibraryActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    Intent intent = new Intent(MainActivity.this,ShowAttendenceActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.more_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        manager = new SessionManager(MainActivity.this);

        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    manager.logoutUser();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();

                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
                return true;

            case  R.id.edit_profile:
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    startActivity(new Intent(this, Edit_User_Profile_Activity.class));
                     //finish();

                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.view_profile:
                if (Connectivity.isNetworkAvailable(MainActivity.this)){
                    startActivity(new Intent(this, View_User_Profile_Activity.class));
                    // finish();

                }else {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
