package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.ResultExamListAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.ResultExamListInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {


   private RecyclerView recyclerView;
   private StringRequest stringRequest;
   private RequestQueue requestQueue;
   private ArrayList<ResultExamListInfo> examList;

   private String holdStdId;
   private User user;
    private AVLoadingIndicatorView av_loader;
    private ImageView iv_exam,iv_study,iv_videos,iv_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Result");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        recyclerView=findViewById(R.id.recycler_view);
        av_loader = findViewById(R.id.av_loader);

        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this,MainPageActivity.class));
                finish();
            }
        });


        user=new User(this);
        holdStdId=user.getId();


        fetchExamDetails();


        iv_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
                finish();
            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ResultActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ResultActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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

    private void fetchExamDetails()
    {

        requestQueue= Volley.newRequestQueue(ResultActivity.this);

        av_loader.setVisibility(View.VISIBLE);

        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_result",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject =new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                examList=new ArrayList<>();

                                JSONObject examInfo=jsonObject.optJSONObject("message");

                                String exam_name=examInfo.getString("exam_name");
                                String exam_date=examInfo.getString("exam_date");

                                ResultExamListInfo examListInfo = new ResultExamListInfo(ResultActivity.this,exam_name,exam_date);
                                examList.add(examListInfo);

                                recyclerView.setAdapter(new ResultExamListAdapter(ResultActivity.this,examList));
                                av_loader.hide();

                            }
                            
                            else 
                            {
                                Toast.makeText(ResultActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            av_loader.hide();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ResultActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<String, String>();

                myParams.put("id",holdStdId);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(ResultActivity.this,MainPageActivity.class));
        finish();
    }
}
