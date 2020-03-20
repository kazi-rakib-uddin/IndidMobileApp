package com.visiabletech.indidmobileapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.goodiebag.pinview.Pinview;
import com.mukesh.OtpView;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity{

    private Pinview pinview;
    private Button btn_verify;
    private TextView tv_mobile,tv_edit_mobile,tv_resend_otp,tv_count_down;
    private EditText et_otp;
    private OtpView otpView;

    private ApiInterface apiInterface;
    private AVLoadingIndicatorView av_loader;


    private String holdMobile;
    CountDownTimer countDownTimer;
    private long timerLeftInMilliSecond =120000; //2 mins


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        ActivityCompat.requestPermissions(VerifyOTPActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, 200);


        btn_verify=findViewById(R.id.btn_verify);
        pinview=findViewById(R.id.pin_view_otp);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_edit_mobile=findViewById(R.id.tv_edit_mobile);
        tv_resend_otp=findViewById(R.id.tv_resend_otp);
        tv_count_down=findViewById(R.id.tv_count_down);
        et_otp=findViewById(R.id.et_otp);
        av_loader = findViewById(R.id.av_loader);





        holdMobile=getIntent().getExtras().getString("mobile_no");

        tv_mobile.setText(holdMobile);


        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);




        //Resend OTP
        resendOtp();

        //Verifi OTP
        verifyOTP();

        //Count Down Timer
        countDownTimerOTP();

        UIUtil.hideKeyboard(this);



        tv_edit_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(VerifyOTPActivity.this,MobileNumberActivity.class));
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

                Call<String> call=apiInterface.sendOTP(mobile_number);

                av_loader.setVisibility(View.VISIBLE);
                av_loader.show();

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.body());

                            String status=jsonObject.getString("status");


                            if (status.equalsIgnoreCase("200"))
                            {

                                Toast.makeText(VerifyOTPActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();

                                countDownTimerOTP();

                                av_loader.hide();



                            }
                            else
                            {
                                Toast.makeText(VerifyOTPActivity.this, "OTP Not send try again", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            av_loader.hide();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        av_loader.hide();

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


                Call<String> call=apiInterface.verifyOTP(holdMobile,pinview.getValue());
                av_loader.setVisibility(View.VISIBLE);
                av_loader.show();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(VerifyOTPActivity.this, "Verify Successfully", Toast.LENGTH_SHORT).show();
                                av_loader.hide();

                                Bundle b=new Bundle();
                                b.putString("phone_no",holdMobile);

                                startActivity(new Intent(VerifyOTPActivity.this,RegisterActivity.class).putExtras(b));
                                finish();

                            }

                            else
                            {
                                Toast.makeText(VerifyOTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            av_loader.hide();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        Toast.makeText(VerifyOTPActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                        av_loader.hide();

                    }
                });

            }
        });
    }




    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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

                //et_otp.setText(otps);



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
