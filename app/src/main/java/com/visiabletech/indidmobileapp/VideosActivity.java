package com.visiabletech.indidmobileapp;

import androidx.annotation.NonNull;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.visiabletech.indidmobileapp.Adapter.AllVideosAdapter;
import com.visiabletech.indidmobileapp.Adapter.BulletinAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.visiabletech.indidmobileapp.Pogo.VideosInfo;
import com.visiabletech.indidmobileapp.YoutubeConfig.YoutubeConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideosActivity extends YouTubeBaseActivity  {


    private RecyclerView recyclerView;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private ArrayList<VideosInfo> videosInfoList;

    private ImageView iv_exam,iv_study,iv_videos,iv_share;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    private String holdUserId;
    private User user;
    
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar=findViewById(R.id.toolbar);
        //getSupportActionBar().setTitle("Attendance");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideosActivity.this,MainPageActivity.class));
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


        user=new User(this);
        holdUserId=user.getId();


        //Fetch All videos
        fetchAllVideos();


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

                startActivity(new Intent(VideosActivity.this,MainPageActivity.class));
                finish();
            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(VideosActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(VideosActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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



    private void fetchAllVideos()
    {

        requestQueue= Volley.newRequestQueue(VideosActivity.this);
        
        progressDialog=new ProgressDialog(VideosActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        
        stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "video_list",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                videosInfoList=new ArrayList<>();

                                JSONArray jsonArray = jsonObject.getJSONArray("message");

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String video_title=jsonObject1.getString("video_title");
                                    String video_url=jsonObject1.getString("video_url");

                                    String shortUrl=video_url.replace("https://www.youtube.com/watch?v=","");


                                    VideosInfo info=new VideosInfo(VideosActivity.this,video_title,shortUrl);

                                    videosInfoList.add(i,info);

                                }


                                recyclerView.setAdapter(new AllVideosAdapter(VideosActivity.this,videosInfoList));
//                                startActivity(new Intent(VideosActivity.this, VimeoVideoPlayActivity.class));
                                progressDialog.dismiss();

                            }
                            else 
                            {
                                Toast.makeText(VideosActivity.this, "Video not available", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(VideosActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<String, String>();

                myParams.put("id",holdUserId);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);


    }





    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            isNetworkAvailable(context);

        }
    }





    private void alartDialog()
    {

        builder= new AlertDialog.Builder(VideosActivity.this);
        View view=getLayoutInflater().inflate(R.layout.error_dialog,null);

        builder.setView(view);
        dialog=builder.create();
        dialog.setCancelable(false);

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

        startActivity(new Intent(VideosActivity.this,MainPageActivity.class));
        finish();
    }
}
