package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visiabletech.indidmobileapp.Adapter.ResultDetailsAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.ResultDetailsInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private String holdStdId;
    private User user;
    private ArrayList<ResultDetailsInfo> detailsList;
    private AVLoadingIndicatorView av_loader;
    public static FloatingActionButton btn_download;

    private ImageView iv_exam,iv_study,iv_videos,iv_share;

    private static final String SHORT_URL="http://indid.in/applogin/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_result_details);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Result");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultDetailsActivity.this, ResultActivity.class));
                finish();
            }
        });




        recyclerView=findViewById(R.id.recycler_view);
        btn_download=findViewById(R.id.btn_download_fb);
        av_loader = findViewById(R.id.av_loader);

        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        user = new User(this);
        holdStdId=user.getId();


        fetchResultDetails();


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

                startActivity(new Intent(ResultDetailsActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ResultDetailsActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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


    private void fetchResultDetails()
    {
        requestQueue= Volley.newRequestQueue(ResultDetailsActivity.this);
        av_loader.setVisibility(View.VISIBLE);
        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_result",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                detailsList=new ArrayList<>();

                                JSONObject examInfo=jsonObject.optJSONObject("message");

                                String answar_key = SHORT_URL + examInfo.getString("answer_key");

                                JSONArray jsonArray = examInfo.getJSONArray("exam_dtls");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String subject_name=jsonObject1.getString("subject_name");
                                    String subject_total_marks=jsonObject1.getString("total_marks");
                                    String subject_ontained_marks=jsonObject1.getString("subject_marks");
                                    String subject_percentage=jsonObject1.getString("percentage");


                                    ResultDetailsInfo resultDetailsInfo = new ResultDetailsInfo(ResultDetailsActivity.this,subject_name,
                                            subject_total_marks,subject_ontained_marks,subject_percentage,answar_key);

                                    detailsList.add(resultDetailsInfo);

                                }

                                recyclerView.setAdapter(new ResultDetailsAdapter(ResultDetailsActivity.this,detailsList));

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

                Toast.makeText(ResultDetailsActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> myParams=new HashMap<String, String>();

                myParams.put("id",holdStdId);

                return myParams;

            }


        };

        requestQueue.add(stringRequest);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        av_loader.hide();
    }


}
