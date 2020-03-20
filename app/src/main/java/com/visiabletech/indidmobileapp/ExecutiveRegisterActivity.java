package com.visiabletech.indidmobileapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.FtsOptions;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.visiabletech.indidmobileapp.Adapter.CustomSpinnerAdapter;
import com.visiabletech.indidmobileapp.Adapter.CustomSpinnerClassAdapter;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerClassItem;
import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerItem;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.visiabletech.indidmobileapp.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;

public class ExecutiveRegisterActivity extends AppCompatActivity {


    private Spinner cls_spinner,board_spinner;
    private TextView tv_dob;
    private Button btn_register;
    private EditText et_name,et_pincode,et_area,et_address,et_city;
    private RadioGroup rg;
    private RadioButton rb_online,rb_offline;
    private CheckBox ch_box;
    private int pAmount,random;
    private String holdChecked="0",executive_id;
    private ProgressDialog progressDialog,progressDialog_payment;
    private User user;

    private String hold_online_offline,hold_mobile,hold_class,hold_board;
    private ApiInterface apiInterface;

    ArrayList<CustomSpinnerItem> customSpinnerList;
    ArrayList<CustomSpinnerClassItem> customSpinnerClassList;

    private DatePickerDialog datePickerDialog;

    private StringRequest stringRequest_class,stringRequest_board,stringRequest_payment;
    private RequestQueue requestQueue_class,requestQueue_board,requestQueue_payment;

    private AttendanceActivity.NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executive_register);


        btn_register=findViewById(R.id.btn_register);


        cls_spinner=findViewById(R.id.class_spinner);
        tv_dob=findViewById(R.id.dob);
        board_spinner=findViewById(R.id.board_spinner);

        et_name=findViewById(R.id.et_name);
        et_pincode=findViewById(R.id.et_pin_code);
        et_area=findViewById(R.id.et_area);
        et_city=findViewById(R.id.et_city);
        et_address=findViewById(R.id.et_address);
        rg=findViewById(R.id.rg);
        ch_box=findViewById(R.id.ch_box);


        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        hold_mobile=getIntent().getExtras().getString("phone_no");

        user=new User(this);
        executive_id = user.getId();



        //fetch Class name and boadrs name
        fetchClassName();
        fetchBoardName();


        //Executive Register
        registerUser();




        //Board Spinner OnClick
        board_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CustomSpinnerItem item=(CustomSpinnerItem)adapterView.getSelectedItem();

                hold_board=item.getSpinnerItemName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Class Spinner OnClick
        cls_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CustomSpinnerClassItem item=(CustomSpinnerClassItem)adapterView.getSelectedItem();

                hold_class=item.getSpinnerItemName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //Checkbox onClick
        ch_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked)
                {
                    holdChecked="1";

                }
                else
                {
                    holdChecked="0";
                }

            }
        });





        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                switch (i)
                {
                    case R.id.rb_online:

                        hold_online_offline="2";

                        break;

                    case R.id.rb_offline:

                        hold_online_offline="1";
                        break;

                }

            }
        });




        tv_dob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                datePickerDialog = new DatePickerDialog(ExecutiveRegisterActivity.this);

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                        tv_dob.setText(day+"-"+(month+1)+"-"+year);


                    }
                });

                datePickerDialog.show();

            }
        });





    }




    private void fetchClassName()
    {

        customSpinnerClassList=new ArrayList<>();

        requestQueue_class= Volley.newRequestQueue(ExecutiveRegisterActivity.this);
        stringRequest_class=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_all_class",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");


                            if (status.equalsIgnoreCase("200"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("classes");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String class_name=jsonObject1.getString("class_or_year");
                                    CustomSpinnerClassItem customSpinnerClassItem=new CustomSpinnerClassItem(ExecutiveRegisterActivity.this,class_name);
                                    customSpinnerClassList.add(customSpinnerClassItem);

                                }

                                cls_spinner.setAdapter(new CustomSpinnerClassAdapter(ExecutiveRegisterActivity.this,customSpinnerClassList));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );

        requestQueue_class.add(stringRequest_class);



    }



    private void fetchBoardName()
    {

        customSpinnerList=new ArrayList<>();

        requestQueue_board=Volley.newRequestQueue(ExecutiveRegisterActivity.this);
        stringRequest_board=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_all_board",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                JSONArray jsonArray = jsonObject.getJSONArray("classes");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String board_name=jsonObject1.getString("board_name");

                                    CustomSpinnerItem customSpinnerItem=new CustomSpinnerItem(ExecutiveRegisterActivity.this,board_name);
                                    customSpinnerList.add(customSpinnerItem);
                                }

                                board_spinner.setAdapter(new CustomSpinnerAdapter(ExecutiveRegisterActivity.this,customSpinnerList));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ExecutiveRegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();

            }
        }

        );

        requestQueue_board.add(stringRequest_board);

    }




    private void registerUser()
    {

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=et_name.getText().toString();
                String cls=hold_class;
                String board=hold_board;
                String dob=tv_dob.getText().toString();
                String pin_code=et_pincode.getText().toString();
                String area=et_area.getText().toString();
                String city=et_city.getText().toString();
                String address=et_address.getText().toString();



                if (name.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                }
                else if (cls.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Class is required", Toast.LENGTH_SHORT).show();
                }
                else if (board.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Board is required", Toast.LENGTH_SHORT).show();
                }
                else if (dob.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "DOB is required", Toast.LENGTH_SHORT).show();
                }
                else if (pin_code.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Pin code is required", Toast.LENGTH_SHORT).show();
                }
                else if (area.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Area is required", Toast.LENGTH_SHORT).show();
                }
                else if (city.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "City is required", Toast.LENGTH_SHORT).show();
                }
                else if (address.isEmpty())
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
                }
                else if (rg.getCheckedRadioButtonId()== -1)
                {
                    Toast.makeText(ExecutiveRegisterActivity.this, "Mode of type is required", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    progressDialog=new ProgressDialog(ExecutiveRegisterActivity.this);
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.show();

                    Call<String> call=apiInterface.executiveRegister(hold_mobile,name,cls,board,dob,pin_code,area,city,address,hold_online_offline,holdChecked,executive_id);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body());

                                String status=jsonObject.getString("status");

                                if (status.equalsIgnoreCase("200"))
                                {

                                    JSONArray jsonArray = jsonObject.getJSONArray("message");

                                    for (int i =0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        String student_id = jsonObject1.getString("Student_id");

                                        Const.student_id = student_id;
                                        Const.mobile_no = hold_mobile;

                                    }

                                    Toast.makeText(ExecutiveRegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(ExecutiveRegisterActivity.this,PaymentActivity.class));
                                    finish();

                                    progressDialog.dismiss();

                                }

                                else
                                {
                                    Toast.makeText(ExecutiveRegisterActivity.this, "Register not Successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }


                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(ExecutiveRegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });


                }


            }
        });



    }




}
