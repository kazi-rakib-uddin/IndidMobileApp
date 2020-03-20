package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AssignmentActivity extends AppCompatActivity {

    private LinearLayout lin_class_work,lin_home_work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Time Table");


        lin_class_work=(LinearLayout)findViewById(R.id.lin_class_work);
        lin_home_work=(LinearLayout)findViewById(R.id.lin_home_work);


        lin_class_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AssignmentActivity.this,ClassWorkActivity.class));
                finish();
            }
        });



        lin_home_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AssignmentActivity.this,HomeWorkActivity.class));
                finish();
            }
        });


    }
}
