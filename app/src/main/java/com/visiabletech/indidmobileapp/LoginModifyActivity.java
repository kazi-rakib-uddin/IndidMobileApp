package com.visiabletech.indidmobileapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginModifyActivity extends AppCompatActivity {

    private Button btn_login,btn_register;
    private EditText et_mobile_no,et_password;

    private StringRequest stringRequest,stringRequest_payment;
    private RequestQueue requestQueue,requestQueue_payment;

    private User user;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    private int pAmount,random;

    private ProgressDialog progressDialog,progressDialog_payment;

    private String fname,lname,hold_user_name,id,role;
    private String user_token;
    SharedPreferences show_pop_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        ActivityCompat.requestPermissions(LoginModifyActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, 200);


        user=new User(this);

        if (user.getMobile()!="")
        {
            startActivity(new Intent(LoginModifyActivity.this,MainPageActivity.class));
            finish();
        }


         btn_login=findViewById(R.id.btn_login);
         btn_register=findViewById(R.id.btn_register);
         et_mobile_no=findViewById(R.id.et_number);
         et_password=findViewById(R.id.et_password);



        //Alert Dialog network connection
        alartDialog();

        //Check Internet Connection
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);



         btn_register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 startActivity(new Intent(LoginModifyActivity.this,IntroPageActivity.class));
                 finish();
             }
         });



        //Push notification Firebase
        FirebaseMessaging.getInstance().subscribeToTopic("Indid");
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("Mynoti","Mynoti", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                         user_token=instanceIdResult.getToken();

                         userLogin(user_token);



                    }
                });


        //End push notification




    }


    private void userLogin(final String user_token)
    {


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_mobile_no.getText().toString().equals(""))
                {
                    Toast.makeText(LoginModifyActivity.this, "Mobile number is required", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    requestQueue= Volley.newRequestQueue(LoginModifyActivity.this);

                    progressDialog=new ProgressDialog(LoginModifyActivity.this);
                    progressDialog.setMessage("Please wait..");
                    progressDialog.show();

                    stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "svclogin",


                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        String status = jsonObject.getString("status");
                                        String message = jsonObject.getString("message");


                                        if (status.equalsIgnoreCase("200"))
                                        {

                                            Toast.makeText(LoginModifyActivity.this, message, Toast.LENGTH_SHORT).show();
                                            JSONObject info = jsonObject.optJSONObject("info");


                                            id=info.getString("id");

                                            user.setId(id);
                                            user.setMobile(et_mobile_no.getText().toString());

                                            progressDialog.dismiss();

                                            show_pop_up=getSharedPreferences("show_pop_up", Context.MODE_PRIVATE);
                                            show_pop_up.edit().putBoolean("isFirstRun",true).commit();

                                            startActivity(new Intent(LoginModifyActivity.this,MainPageActivity.class));
                                            finish();



                                        }

                                        else
                                        {
                                            Toast.makeText(LoginModifyActivity.this, message, Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(LoginModifyActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                    )
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> myParams = new HashMap<String, String>();

                            myParams.put("username",et_mobile_no.getText().toString());
                            myParams.put("user_token",user_token);
                            myParams.put("password","123456");

                            return myParams;
                        }
                    };

                    requestQueue.add(stringRequest);


                }



            }
        });

    }








    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            isNetworkAvailable(context);

        }
    }



    private void alartDialog()
    {

        builder= new AlertDialog.Builder(LoginModifyActivity.this);
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






}
