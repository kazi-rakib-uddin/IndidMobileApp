package com.visiabletech.indidmobileapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberActivity extends AppCompatActivity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    private int RESOLVE_HINT=2;
    private EditText et_mobile;
    private GoogleApiClient googleApiClient;
    private Button btn_next;

    String mobile_number;

    private ApiInterface apiInterface;

    private String mobilePattern;

    private AVLoadingIndicatorView av_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        btn_next=findViewById(R.id.btn_next);
        et_mobile=findViewById(R.id.et_mobile);
        av_loader = findViewById(R.id.av_loader);


        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);

        mobilePattern = "[0-9]{10}";

        //set google api client for hint request
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this,this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        //Get Mobile number from phone
        getHintPhoneNumber();


        //Send OTP from mobile number
        sendOtp();



        et_mobile.addTextChangedListener(phoneTextWatcher);




    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    public void getHintPhoneNumber() {

        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);

        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result if we want hint number
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                    et_mobile.setText(credential.getId());
                }

            }
        }

    }


    private void sendOtp()
    {

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 mobile_number=et_mobile.getText().toString();


                if (mobile_number.startsWith("+"))
                {
                    String m=mobile_number.substring(3);

                    mobile_number=m;

                }



                if (mobile_number.isEmpty())
                {
                    Toast.makeText(MobileNumberActivity.this, "Mobile Number is required", Toast.LENGTH_SHORT).show();
                }

                else if (!mobile_number.matches(mobilePattern))
                {
                    Toast.makeText(MobileNumberActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    av_loader.setVisibility(View.VISIBLE);

                    Call<String> call=apiInterface.sendOTP(mobile_number);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {

                                JSONObject jsonObject=new JSONObject(response.body());

                                String status=jsonObject.getString("status");

                                if (status.equalsIgnoreCase("200"))
                                {

                                    Toast.makeText(MobileNumberActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();

                                    Bundle b=new Bundle();
                                    b.putString("mobile_no",mobile_number);

                                    startActivity(new Intent(MobileNumberActivity.this,VerifyOTPActivity.class).putExtras(b));
                                    finish();

                                    av_loader.hide();

                                }
                                else
                                {
                                    Toast.makeText(MobileNumberActivity.this, "Student already register", Toast.LENGTH_SHORT).show();
                                    av_loader.hide();
                                }


                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                av_loader.hide();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(MobileNumberActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            av_loader.hide();

                        }
                    });

                }


            }
        });



    }



    //Phone edittext

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String phoneInput=et_mobile.getText().toString();

            if (phoneInput.length()>=4)
            {
                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(MobileNumberActivity.this,R.drawable.button_round_bg));

                btn_next.setEnabled(true);
            }
            else
            {
                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(MobileNumberActivity.this,R.drawable.button_round_disable_bg));

                btn_next.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };





}
