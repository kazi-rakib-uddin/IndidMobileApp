package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MTSEActivity extends AppCompatActivity {

    private TextView tv_time,tv_name,tv_class,tv_board;
    private StringRequest stringRequest_profile,stringRequest_about_mtse;
    private RequestQueue requestQueue_profile,requestQueue_about_mtse;
    private AVLoadingIndicatorView av_loader;

    private String hold_stu_id;
    private User user;

    private ImageView iv_exam,iv_study,iv_videos,iv_share;
    private LinearLayout lin_benefits_mtse,lin_about_mtse,lin_register_mtse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtse);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MTSE");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MTSEActivity.this,MainPageActivity.class));
                finish();
            }
        });


        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        tv_class = findViewById(R.id.tv_class);
        tv_board = findViewById(R.id.tv_board);
        av_loader = findViewById(R.id.av_loader);

        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);

        lin_benefits_mtse = findViewById(R.id.lin_benefits_mtse);
        lin_about_mtse = findViewById(R.id.lin_about_mtse);
        lin_register_mtse = findViewById(R.id.lin_register_mtse);


        user = new User(this);
        hold_stu_id = user.getId();



        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            // Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            tv_time.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            // Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();

            tv_time.setText("Good Afternoon");


        }else if(timeOfDay >= 16 && timeOfDay < 21){
            // Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();

            tv_time.setText("Good Evening");

        }else if(timeOfDay >= 21 && timeOfDay < 24){
            // Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();

            tv_time.setText("Good Night");
        }


        //Fetch Profile
        fetchProfile();




        //Bottom Navigation OnClick

        iv_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MTSEActivity.this,MainPageActivity.class));
                finish();
            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MTSEActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MTSEActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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




        lin_benefits_mtse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MTSEActivity.this,BenefitsMTSEActivity.class));
                finish();

            }
        });


        lin_about_mtse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchAboutMtse();
            }
        });


        lin_register_mtse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }



    private void fetchProfile()
    {

        requestQueue_profile= Volley.newRequestQueue(MTSEActivity.this);
        av_loader.setVisibility(View.VISIBLE);
        av_loader.show();
        stringRequest_profile=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_profile",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");
                            String message=jsonObject.getString("message");

                            if (status.equalsIgnoreCase("200"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("student_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String fname=jsonObject1.getString("First_Name");
                                    String student_class=jsonObject1.getString("class_or_year");
                                    String board=jsonObject1.getString("board");



                                    tv_name.setText(fname);
                                    tv_class.setText(student_class);
                                    tv_board.setText(board);




                                }



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

                Toast.makeText(MTSEActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();

                myParams.put("id",hold_stu_id);

                return myParams;
            }
        };

        requestQueue_profile.add(stringRequest_profile);

    }


    private void fetchAboutMtse()
    {
        requestQueue_about_mtse = Volley.newRequestQueue(MTSEActivity.this);
        av_loader.setVisibility(View.VISIBLE);
        av_loader.show();
        stringRequest_about_mtse = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "about_mtse",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                String pdf = jsonObject.getString("about_mtse");
                                av_loader.hide();

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
                                startActivity(browserIntent);
                            }
                            else
                            {
                                Toast.makeText(MTSEActivity.this, "Not found", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(MTSEActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        );

        requestQueue_about_mtse.add(stringRequest_about_mtse);



    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(MTSEActivity.this,MainPageActivity.class));
        finish();
    }
}
