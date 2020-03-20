package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.visiabletech.indidmobileapp.Utils.Const;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView tv_name,tv_class,tv_board,tv_pin_code,tv_city,tv_address;
    private EditText et_amount;
    private Button btn_pay,btn_skip;

    private StringRequest stringRequest,stringRequest_payment;
    private RequestQueue requestQueue,requestQueue_payment;

    private AVLoadingIndicatorView av_loader;

    private String hold_student_id,hold_mobile;
    private int pAmount,random;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment");
        toolbar.setTitleTextColor(Color.WHITE);


        tv_name = findViewById(R.id.tv_name);
        tv_class = findViewById(R.id.tv_class);
        tv_board = findViewById(R.id.tv_board);
        tv_pin_code = findViewById(R.id.tv_pin_code);
        tv_city = findViewById(R.id.tv_city);
        tv_address = findViewById(R.id.tv_address);

        btn_pay = findViewById(R.id.btn_pay);
        btn_skip = findViewById(R.id.btn_skip);

        et_amount = findViewById(R.id.et_amount);
        av_loader = findViewById(R.id.av_loader);


        Checkout.preload(getApplicationContext());

        final int min =100000000;
        final int max =999999999;
        random = new Random().nextInt((max - min) + 1) + min;


        user = new User(this);

        hold_student_id = Const.student_id;
        hold_mobile = Const.mobile_no;



        if (hold_student_id.equals(""))
        {
            hold_student_id = user.getId();
            hold_mobile = user.getMobile();
        }
        else
        {

            hold_student_id = Const.student_id;

        }

        //student details
        fetchStudentDetails(hold_student_id);

        et_amount.addTextChangedListener(amountTextWatcher);


        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PaymentActivity.this,MainPageActivity.class));
                finish();
            }
        });


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pAmount= Integer.parseInt(et_amount.getText().toString().trim());

                startPayment();

            }
        });


    }


    private void fetchStudentDetails(final String student_id)
    {
        requestQueue = Volley.newRequestQueue(PaymentActivity.this);

        av_loader.setVisibility(View.VISIBLE);
        av_loader.show();

        stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_profile",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("student_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String fname=jsonObject1.getString("First_Name");
                                    String student_class=jsonObject1.getString("class_or_year");
                                    String board=jsonObject1.getString("board");
                                    String pin_code=jsonObject1.getString("pin_Code");
                                    String city=jsonObject1.getString("city");
                                    String address=jsonObject1.getString("permanent_address");


                                    tv_name.setText(fname);
                                    tv_class.setText(student_class);
                                    tv_board.setText(board);
                                    tv_pin_code.setText(pin_code);
                                    tv_city.setText(city);
                                    tv_address.setText(address);


                                }

                                av_loader.hide();

                            }
                            else
                            {
                                av_loader.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            av_loader.hide();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PaymentActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> myParams=new HashMap<>();

                myParams.put("id",student_id);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);

    }




    public void startPayment(){

        pAmount= Integer.parseInt(et_amount.getText().toString());
        Checkout checkout=new Checkout();
        // checkout.setImage(R.drawable.nola);

        final Activity activity=this;

        try{
            JSONObject options=new JSONObject();
            options.put("description",random);
            options.put("currency","INR");
            options.put("amount",pAmount*100);


            JSONObject preFill = new JSONObject();
            preFill.put("email", "info@visiabletech.com");
            preFill.put("contact", hold_mobile);

            options.put("prefill", preFill);


            checkout.open(activity,options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void userPayment()
    {

        requestQueue_payment = Volley.newRequestQueue(PaymentActivity.this);

        av_loader.setVisibility(View.VISIBLE);
        av_loader.show();

        stringRequest_payment = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "payment_save",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(PaymentActivity.this, "Payment Successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(PaymentActivity.this,MainPageActivity.class));
                                finish();

                                av_loader.hide();
                            }
                            else
                            {
                                Toast.makeText(PaymentActivity.this, "Payment already done", Toast.LENGTH_SHORT).show();

                                av_loader.hide();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            av_loader.hide();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> myParams = new HashMap<>();

                myParams.put("student_id",hold_student_id);
                myParams.put("amount",String.valueOf(pAmount));
                myParams.put("transaction_id",String.valueOf(random));

                return myParams;
            }
        };

        requestQueue_payment.add(stringRequest_payment);

    }


    @Override
    public void onPaymentSuccess(String s) {

        userPayment();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Payment failed and Try again", Toast.LENGTH_SHORT).show();

    }



    //Amount edittext

    private TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String amount=et_amount.getText().toString();

            if (amount.length()>=1)
            {
                btn_pay.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this,R.drawable.button_round_bg));

                btn_pay.setEnabled(true);
            }
            else
            {
                btn_pay.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this,R.drawable.button_round_disable_bg));

                btn_pay.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentActivity.this,MainPageActivity.class));
        finish();
    }
}
