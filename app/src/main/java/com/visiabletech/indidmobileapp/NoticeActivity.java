package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.NoticeAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.NoticeInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<NoticeInfo> noticeInfoList;

    private StringRequest stringRequest_notice;
    private RequestQueue requestQueue_notice;

    private ImageView iv_exam,iv_study,iv_videos,iv_share;


    private String holdId;
    private User user;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private TextView networkStatus;
    private AVLoadingIndicatorView av_loader;

    AlertDialog dialog;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notice");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoticeActivity.this,MainPageActivity.class));
                finish();
            }
        });


        user=new User(this);
        holdId=user.getId();


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        av_loader = findViewById(R.id.av_loader);

        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager=layoutManager;
        recyclerView.setLayoutManager(layoutManager);


        //Notice Details
        fetchNoticeDetails();

        //Alert Dialog
        alartDialog();



        //Check Internet Connection
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);



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

                startActivity(new Intent(NoticeActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(NoticeActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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


    private void fetchNoticeDetails()
    {
        requestQueue_notice= Volley.newRequestQueue(NoticeActivity.this);
        av_loader.setVisibility(View.VISIBLE);
        stringRequest_notice=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_notice",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");
                            String message= jsonObject.optString("message");

                            if (status.equalsIgnoreCase("200"))
                            {

                                noticeInfoList=new ArrayList<>();

                                JSONArray jsonArray = jsonObject.getJSONArray("notice_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                   /* String notice_subject=jsonObject1.getString("notice_subject");
                                    String notice_date = jsonObject1.getString("notice_date");
                                    String notice = jsonObject1.getString("notice");*/


                                    String notice_subject=jsonObject1.getString("notice_blog");
                                    String notice_date = jsonObject1.getString("notice_date");
                                    String notice = jsonObject1.getString("notice");


                                    NoticeInfo noticeInfo = new NoticeInfo(NoticeActivity.this,notice_subject,notice_date,notice);
                                    noticeInfoList.add(noticeInfo);

                                }

                                recyclerView.setAdapter(new NoticeAdapter(NoticeActivity.this,noticeInfoList));
                                av_loader.hide();


                            }
                            else
                            {
                                Toast.makeText(NoticeActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }


                        }
                        catch (Exception x)
                        {

                            av_loader.hide();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(NoticeActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();


            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> myParams=new HashMap<String, String>();

                myParams.put("id",holdId);

                return myParams;
            }
        };

        requestQueue_notice.add(stringRequest_notice);

    }




    private void alartDialog()
    {

        builder= new AlertDialog.Builder(NoticeActivity.this);
        View view=getLayoutInflater().inflate(R.layout.error_dialog,null);

        builder.setView(view);
        dialog=builder.create();
        dialog.setCancelable(false);

    }




    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            isNetworkAvailable(context);

        }
    }



    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if(!isConnected){

                            //networkStatus.setText("Now you are connected to Internet!");
                            dialog.dismiss();

                            isConnected = true;
                            //do your processing here ---
                            //if you need to post any data to the server or get status
                            //update from the server
                        }
                        return true;
                    }
                }
            }
        }

        //networkStatus.setText("You are not connected to Internet!");
        dialog.show();
        isConnected = false;
        return false;
    }




    @Override
    public void onBackPressed() {

        startActivity(new Intent(NoticeActivity.this,MainPageActivity.class));
        finish();
    }
}
