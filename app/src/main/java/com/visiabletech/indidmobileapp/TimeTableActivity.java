package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.SpecialClassAdapter;
import com.visiabletech.indidmobileapp.Adapter.TimeTableAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.SpecialClassInfo;
import com.visiabletech.indidmobileapp.Pogo.TimeTableInfo;
import com.visiabletech.indidmobileapp.Pogo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeTableActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<TimeTableInfo> timekArrayList;
   // private ArrayList<String> timeTableArrayList;
    private ListView special_listView;
    private TextView special_title_textView;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private String holdId;
    private User user;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private TextView networkStatus;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    private ProgressDialog progressDialog;

    //private String SERVER_URL="http://visiabletech.com/snrmemorialtrust/webservices/websvc/get_timetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Time Table");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TimeTableActivity.this,MainActivity.class));
                finish();
            }
        });

        user=new User(this);
        holdId=user.getId();

        listView = findViewById(R.id.listView);
        special_listView = findViewById(R.id.special_listView);
        special_title_textView = findViewById(R.id.special_title_textView);
        //special_title_textView.setTypeface(typeface);
        special_title_textView.setVisibility(View.INVISIBLE);




            timeTable();



        //Alert Dialog
        alartDialog();



        //Check Internet Connection
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);


    }



    public void timeTable()
    {


        //fetch student time table from server
        // -----------------------------------------

        requestQueue= Volley.newRequestQueue(TimeTableActivity.this);

        progressDialog=new ProgressDialog(TimeTableActivity.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_timetable",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.optString("status");
                            String message=jsonObject.optString("message");


                            if (status.equalsIgnoreCase("200"))
                            {
                                String day_status=jsonObject.optString("day_status");

                                if (day_status.equalsIgnoreCase("true"))
                                {

                                    timekArrayList=new ArrayList<>();
                                    JSONArray jsonArray=jsonObject.getJSONArray("timetable_details");

                                    for (int i=0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                        String time = jsonObject1.getString("time");
                                        //  String sub_time = time.substring(0,5);  now disable
                                        String subject1 = jsonObject1.getString("subject1");
                                        String subject2 = jsonObject1.getString("subject2");
                                        String subject3 = jsonObject1.getString("subject3");
                                        String subject4 = jsonObject1.getString("subject4");
                                        String subject5 = jsonObject1.getString("subject5");
                                        String subject6 = jsonObject1.getString("subject6");

                                        TimeTableInfo timeInfo = new TimeTableInfo(time, subject1, subject2, subject3, subject4, subject5, subject6);
                                        timekArrayList.add(i, timeInfo);

                                    }

                                    listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));

                                    special_title_textView.setVisibility(View.VISIBLE);

                                    ArrayList<SpecialClassInfo> specialClassInfoArrayList = new ArrayList<>();
                                    for (int i = 0; i < timekArrayList.size(); i++) {
                                        SpecialClassInfo specialClassInfo = new SpecialClassInfo(timekArrayList.get(i).time, timekArrayList.get(i).subject6);
                                        specialClassInfoArrayList.add(specialClassInfo);
                                    }

                                    special_listView.setAdapter(new SpecialClassAdapter(TimeTableActivity.this, specialClassInfoArrayList, getLayoutInflater()));

                                    progressDialog.dismiss();


                                }
                                else if (day_status.equalsIgnoreCase("false"))
                                {

                                    timekArrayList = new ArrayList<>();
                                    JSONArray jsonArrayList = jsonObject.getJSONArray("timetable_details");

                                    for (int i = 0; i < jsonArrayList.length(); i++) {
                                        JSONObject jsonObject2 = jsonArrayList.getJSONObject(i);
                                        String time = jsonObject2.getString("time");
                                        //  String sub_time = time.substring(0,5); now disable
                                        String subject1 = jsonObject2.getString("subject1");
                                        String subject2 = jsonObject2.getString("subject2");
                                        String subject3 = jsonObject2.getString("subject3");
                                        String subject4 = jsonObject2.getString("subject4");
                                        String subject5 = jsonObject2.getString("subject5");
                                        TimeTableInfo timeInfo = new TimeTableInfo(time, subject1, subject2, subject3, subject4, subject5, "");
                                        timekArrayList.add(i, timeInfo);
                                    }

                                    listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));
                                    special_title_textView.setVisibility(View.INVISIBLE);
                                    special_listView.setVisibility(View.INVISIBLE);

                                    progressDialog.dismiss();

                                }



                            }
                            else
                            {

                                Toast.makeText(TimeTableActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            }

                        } catch (JSONException e) {
                            Toast.makeText(TimeTableActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(TimeTableActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();
                myParams.put("id",holdId);
                return myParams;
            }
        };

        requestQueue.add(stringRequest);


    }




    private void alartDialog()
    {

        builder= new AlertDialog.Builder(TimeTableActivity.this);
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

        startActivity(new Intent(TimeTableActivity.this,MainActivity.class));
        finish();
    }
}
