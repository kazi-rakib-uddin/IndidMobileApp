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
import com.visiabletech.indidmobileapp.Adapter.BulletinAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.BulletinInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BulletinActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<BulletinInfo> bulletinInfoList;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    //private String SERVER_URL="http://visiabletech.com/snrmemorialtrust/webservices/websvc/get_bulletin";

    private String holdId;
    private User user;


    private AVLoadingIndicatorView av_loader;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private TextView networkStatus;
    private ImageView iv_exam,iv_study,iv_videos,iv_share;

    AlertDialog dialog;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bulletins");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BulletinActivity.this,MainPageActivity.class));
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


        fetchBulletinsDetails();



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

                startActivity(new Intent(BulletinActivity.this,MainPageActivity.class));
                finish();
            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BulletinActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(BulletinActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

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

    private void fetchBulletinsDetails()
    {

        requestQueue= Volley.newRequestQueue(BulletinActivity.this);

        av_loader.setVisibility(View.VISIBLE);

        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_bulletin",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status= jsonObject.getString("status");
                            String message= jsonObject.getString("message");

                            if (status.equalsIgnoreCase("200"))
                            {
                                bulletinInfoList=new ArrayList<>();

                                JSONArray jsonArray=jsonObject.getJSONArray("bulletin_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String title = jsonObject1.getString("title");
                                    String body = jsonObject1.getString("body");
                                    String date = jsonObject1.getString("date");
                                    String post_by = jsonObject1.getString("post_by");

                                    BulletinInfo bulletinInfo = new BulletinInfo(BulletinActivity.this,title,body,date,post_by);

                                    bulletinInfoList.add(i,bulletinInfo);



                                }
                                av_loader.hide();


                                recyclerView.setAdapter(new BulletinAdapter(BulletinActivity.this,bulletinInfoList));
                                av_loader.hide();


                            }
                            else
                            {
                                Toast.makeText(BulletinActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }


                        }catch (Exception e)
                        {
                            Toast.makeText(BulletinActivity.this, "Not available", Toast.LENGTH_SHORT).show();
                            av_loader.hide();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(BulletinActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();

            }
        }


        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<String, String>();

                myParams.put("id",holdId);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);


    }





    private void alartDialog()
    {

        builder= new AlertDialog.Builder(BulletinActivity.this);
        View view=getLayoutInflater().inflate(R.layout.error_dialog,null);

        builder.setView(view);
        dialog=builder.create();
        dialog.setCancelable(false);

    }




    public class NetworkChangeReceiver extends BroadcastReceiver {

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

        startActivity(new Intent(BulletinActivity.this,MainPageActivity.class));
        finish();
    }
}
