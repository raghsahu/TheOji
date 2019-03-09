package com.example.admin.theoji;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ShowAttendenceActivity extends AppCompatActivity {
    ImageView add_attendence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendence);

            add_attendence=(ImageView)findViewById(R.id.add_attendence);

             add_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowAttendenceActivity.this,AttendenceActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
