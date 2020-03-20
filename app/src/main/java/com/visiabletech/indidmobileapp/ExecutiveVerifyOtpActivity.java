package com.visiabletech.indidmobileapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.goodiebag.pinview.Pinview;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExecutiveVerifyOtpActivity extends AppCompatActivity {

    private Pinview pinview;
    private Button btn_verify;
    private TextView tv_mobile,tv_edit_mobile,tv_resend_otp,tv_count_down;

    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogVerify;

    private String holdMobile;
    CountDownTimer countDownTimer;
    private long timerLeftInMilliSecond =120000; //2 mins

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executive_verify_otp);

        btn_verify=findViewById(R.id.btn_verify);
        pinview=findViewById(R.id.pin_view_otp);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_edit_mobile=findViewById(R.id.tv_edit_mobile);
        tv_resend_otp=findViewById(R.id.tv_resend_otp);
        tv_count_down=findViewById(R.id.tv_count_down);


        holdMobile=getIntent().getExtras().getString("mobile_no");

        tv_mobile.setText(holdMobile);

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);

        //Count Down Timer
        countDownTimerOTP();

        //Resend OTP
        resendOtp();

        //Verifi OTP
        verifyOTP();


        tv_edit_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ExecutiveVerifyOtpActivity.this, ExecutiveMobileActivity.class));
                finish();
            }
        });

    }



    private void resendOtp()
    {
        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile_number=tv_mobile.getText().toString();

                progressDialog=new ProgressDialog(ExecutiveVerifyOtpActivity.this);
                progressDialog.setMessage("Please Wait..");
                progressDialog.show();

                Call<String> call=apiInterface.sendOTP(mobile_number);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.body());

                            String status=jsonObject.getString("status");


                            if (status.equalsIgnoreCase("200"))
                            {

                                Toast.makeText(ExecutiveVerifyOtpActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();

                                countDownTimerOTP();

                                progressDialog.dismiss();



                            }
                            else
                            {
                                Toast.makeText(ExecutiveVerifyOtpActivity.this, "OTP Not send try again", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        progressDialog.dismiss();

                    }
                });


            }
        });


    }



    private void verifyOTP()
    {

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialogVerify=new ProgressDialog(ExecutiveVerifyOtpActivity.this);
                progressDialogVerify.setMessage("Please Wait..");
                progressDialogVerify.show();

                Call<String> call=apiInterface.verifyOTP(holdMobile,pinview.getValue());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(ExecutiveVerifyOtpActivity.this, "Verify Successfully", Toast.LENGTH_SHORT).show();
                                progressDialogVerify.dismiss();

                                Bundle b=new Bundle();
                                b.putString("phone_no",holdMobile);

                                startActivity(new Intent(ExecutiveVerifyOtpActivity.this, ExecutiveRegisterActivity.class).putExtras(b));
                                finish();

                            }

                            else
                            {
                                Toast.makeText(ExecutiveVerifyOtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                progressDialogVerify.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialogVerify.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        Toast.makeText(ExecutiveVerifyOtpActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                        progressDialogVerify.dismiss();

                    }
                });

            }
        });


    }



    private void countDownTimerOTP()
    {

        countDownTimer=new CountDownTimer(timerLeftInMilliSecond,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tv_count_down.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                tv_resend_otp.setTextColor(getResources().getColor(R.color.colorGray));

            }

            @Override
            public void onFinish() {

                tv_resend_otp.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_resend_otp.setEnabled(true);
                tv_count_down.setText("");

            }
        }.start();

    }



    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                final String otps=message.replace("Hi, this is your OTP","");


                pinview.setValue(otps.trim());


            }
        }
    };



    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }



}
