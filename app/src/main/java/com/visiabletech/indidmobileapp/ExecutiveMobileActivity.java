package com.visiabletech.indidmobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExecutiveMobileActivity extends AppCompatActivity {


    private EditText et_mobile;
    private Button btn_next;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private ApiInterface apiInterface;

    private String mobilePattern,mobile_number;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executive_mobile);


        et_mobile = findViewById(R.id.et_mobile);
        btn_next = findViewById(R.id.btn_next);

        mobilePattern = "[0-9]{10}";

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);

        et_mobile.addTextChangedListener(phoneTextWatcher);


        //Send OTP from Mobile number
        sendOtp();

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
                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(ExecutiveMobileActivity.this,R.drawable.button_round_bg));

                btn_next.setEnabled(true);
            }
            else
            {
                btn_next.setBackgroundDrawable(ContextCompat.getDrawable(ExecutiveMobileActivity.this,R.drawable.button_round_disable_bg));

                btn_next.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    private void sendOtp()
    {

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobile_number = et_mobile.getText().toString();


                if (mobile_number.isEmpty())
                {
                    Toast.makeText(ExecutiveMobileActivity.this, "Mobile Number is required", Toast.LENGTH_SHORT).show();
                }

                else if (!mobile_number.matches(mobilePattern))
                {
                    Toast.makeText(ExecutiveMobileActivity.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    progressDialog=new ProgressDialog(ExecutiveMobileActivity.this);
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

                                    Toast.makeText(ExecutiveMobileActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();

                                    Bundle b=new Bundle();
                                    b.putString("mobile_no",mobile_number);

                                    startActivity(new Intent(ExecutiveMobileActivity.this, ExecutiveVerifyOtpActivity.class).putExtras(b));
                                    finish();

                                    progressDialog.dismiss();

                                }
                                else
                                {
                                    Toast.makeText(ExecutiveMobileActivity.this, "Student already register", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }


                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(ExecutiveMobileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });

                }


            }
        });


    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(ExecutiveMobileActivity.this,MainPageActivity.class));
        finish();
    }


}
