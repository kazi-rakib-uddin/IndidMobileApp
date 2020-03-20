package com.visiabletech.indidmobileapp;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    private Spinner cls_spinner,board_spinner;
    private TextView tv_dob;
    private Button btn_register;
    private EditText et_name,et_pincode,et_area,et_address,et_city;
    private RadioGroup rg;
    private RadioButton rb_online,rb_offline;
    private CheckBox ch_box;

    private String holdChecked="0",user_token;
    private ProgressDialog progressDialog;
    private User user;

    private String hold_online_offline,hold_mobile,hold_class,hold_board;
    private ApiInterface apiInterface;

    ArrayList<CustomSpinnerItem> customSpinnerList;
    ArrayList<CustomSpinnerClassItem> customSpinnerClassList;

    private DatePickerDialog datePickerDialog;

    private StringRequest stringRequest_class,stringRequest_board;
    private RequestQueue requestQueue_class,requestQueue_board;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


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

        //fetch Class name and boadrs name
        fetchClassName();
        fetchBoardName();

        Const.OPEN_HOME_FIRST_TIME="";




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

                        //User Register
                        registerUser(user_token);



                    }
                });


        //End push notification





        //Alert Dialog
        alartDialog();

        //Check Internet Connection
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);







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
            @Override
            public void onClick(View view) {


                datePickerDialog = new DatePickerDialog(RegisterActivity.this);

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

        requestQueue_class= Volley.newRequestQueue(RegisterActivity.this);
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
                                    CustomSpinnerClassItem customSpinnerClassItem=new CustomSpinnerClassItem(RegisterActivity.this,class_name);
                                    customSpinnerClassList.add(customSpinnerClassItem);

                                }

                                cls_spinner.setAdapter(new CustomSpinnerClassAdapter(RegisterActivity.this,customSpinnerClassList));

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

        requestQueue_board=Volley.newRequestQueue(RegisterActivity.this);
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

                                    CustomSpinnerItem customSpinnerItem=new CustomSpinnerItem(RegisterActivity.this,board_name);
                                    customSpinnerList.add(customSpinnerItem);
                                }

                                board_spinner.setAdapter(new CustomSpinnerAdapter(RegisterActivity.this,customSpinnerList));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();

            }
        }

        );

        requestQueue_board.add(stringRequest_board);

    }




    private void registerUser(final String user_token)
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
                    Toast.makeText(RegisterActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                }
                else if (cls.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Class is required", Toast.LENGTH_SHORT).show();
                }
                else if (board.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Board is required", Toast.LENGTH_SHORT).show();
                }
                else if (dob.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "DOB is required", Toast.LENGTH_SHORT).show();
                }
                else if (pin_code.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Pin code is required", Toast.LENGTH_SHORT).show();
                }
                else if (area.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Area is required", Toast.LENGTH_SHORT).show();
                }
                else if (city.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "City is required", Toast.LENGTH_SHORT).show();
                }
                else if (address.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Address is required", Toast.LENGTH_SHORT).show();
                }
                else if (rg.getCheckedRadioButtonId()== -1)
                {
                    Toast.makeText(RegisterActivity.this, "Mode of type is required", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    progressDialog=new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.show();

                    Call<String> call=apiInterface.register(hold_mobile,name,cls,board,dob,pin_code,area,city,address,hold_online_offline,holdChecked,user_token);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body());

                                String status=jsonObject.getString("status");

                                if (status.equalsIgnoreCase("200"))
                                {


                                    JSONArray jsonArray=jsonObject.getJSONArray("message");

                                    for (int i=0; i<jsonArray.length(); i++)
                                    {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        String student_id=jsonObject1.getString("Student_id");

                                        user.setId(student_id);

                                        Toast.makeText(RegisterActivity.this, student_id, Toast.LENGTH_SHORT).show();

                                    }

                                    //Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                                    user.setMobile(hold_mobile);

                                    startActivity(new Intent(RegisterActivity.this,MainPageActivity.class));
                                    finish();

                                    progressDialog.dismiss();

                                }

                                else
                                {
                                    Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }


                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });


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

        builder= new AlertDialog.Builder(RegisterActivity.this);
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
