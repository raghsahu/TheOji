package com.example.admin.theoji;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ShowClassActivity extends AppCompatActivity {
    ImageView addClass;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);

    addClass=findViewById(R.id.addclass);

    addClass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(ShowClassActivity.this,AddClassActivity.class);
            startActivity(intent);
        }
    });

    }
}
