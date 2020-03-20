package com.visiabletech.indidmobileapp;



import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.MockTestAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.MockTestInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BenefitsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private ArrayList<MockTestInfo> mockTestInfoList;

    private ProgressDialog progressDialog;

    private ImageView iv_exam,iv_study,iv_videos,iv_share;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefits);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Benefits");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BenefitsActivity.this,MainPageActivity.class));
                finish();
            }
        });

        recyclerView=findViewById(R.id.recyclerView);

        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager=layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        fetchMockTest();




        iv_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(BenefitsActivity.this,MainPageActivity.class));
                finish();
            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BenefitsActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(BenefitsActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                /*startActivity(new Intent(MainPageActivity.this,VideosActivity.class));
                finish();*/
            }
        });



        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                String app_url = " https://play.google.com/store/apps/details?id=com.visiabletech.indidmobileapp&hl=en";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });



    }


    private void fetchMockTest()
    {

        requestQueue= Volley.newRequestQueue(BenefitsActivity.this);

        /*progressDialog=new ProgressDialog(BenefitsActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();*/

        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "features_fetch",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                mockTestInfoList=new ArrayList<>();

                                JSONArray jsonArray = jsonObject.getJSONArray("message");

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String mocktest=jsonObject1.getString("content");


                                    MockTestInfo mockTestInfo = new MockTestInfo(BenefitsActivity.this,mocktest);
                                    mockTestInfoList.add(mockTestInfo);

                                }

                                recyclerView.setAdapter(new MockTestAdapter(BenefitsActivity.this,mockTestInfoList));

                                //progressDialog.dismiss();


                            }

                        }
                        catch (Exception e)
                        {
                            //Toast.makeText(BenefitsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(BenefitsActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }
        }

        );

        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(BenefitsActivity.this,MainPageActivity.class));
        finish();
    }
}
