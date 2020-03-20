package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.visiabletech.indidmobileapp.Adapter.NotificationAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.NotificationInfo;
import com.visiabletech.indidmobileapp.Pogo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private ArrayList<NotificationInfo> infoArrayList;

    private User user;
    private String holdStuId;

    private ImageView iv_exam,iv_study,iv_videos,iv_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationActivity.this,MainPageActivity.class));
                finish();
            }
        });

        user=new User(this);
        holdStuId=user.getId();

        recyclerView=findViewById(R.id.recycler_view);

        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager=layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        fetchNotificationList();


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

                startActivity(new Intent(NotificationActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(NotificationActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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

    private void fetchNotificationList()
    {
        requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_details",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                infoArrayList=new ArrayList<>();

                                JSONArray jsonArray = jsonObject.getJSONArray("notification_count");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String id=jsonObject1.getString("notice_id");
                                    String title=jsonObject1.getString("notification_title");
                                    String activity=jsonObject1.getString("notification_activity");

                                    NotificationInfo info = new NotificationInfo(NotificationActivity.this,title,id,activity);
                                    infoArrayList.add(i,info);

                                }

                                recyclerView.setAdapter(new NotificationAdapter(NotificationActivity.this,infoArrayList));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(NotificationActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams= new HashMap<>();

                myParams.put("student_id",holdStuId);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(NotificationActivity.this,MainPageActivity.class));
        finish();
    }
}
