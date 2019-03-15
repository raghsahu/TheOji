package com.example.admin.theoji;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.theoji.Shared_prefrence.SessionManager;

public class SplashActivity extends AppCompatActivity {
    SessionManager manager;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
     manager =new SessionManager(SplashActivity.this);

        setContentView(R.layout.activity_splash);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    try{

                        if (manager.isLoggedIn()) {

                            Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    }catch (Exception e) {
                    }
//                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(i);

                    // close this activity
//                    finish();
                }
            }, SPLASH_TIME_OUT);




    }
}
