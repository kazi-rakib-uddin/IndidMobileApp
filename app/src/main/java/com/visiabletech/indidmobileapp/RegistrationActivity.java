package com.visiabletech.indidmobileapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.CustomSpinnerAdapter;
import com.visiabletech.indidmobileapp.Adapter.CustomSpinnerClassAdapter;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerClassItem;
import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerItem;
import com.visiabletech.indidmobileapp.Pogo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView img_profile,otp_icon,phone_icon;
    private EditText et_name,et_phone,et_address,et_otp,et_password;
    private Spinner cls_spinner,board_spinner;
    private TextView tv_dob;
    private RadioGroup rg;
    private RadioButton rb_online,rb_offline;
    private Button btn_register,btn_verify_phone,btn_verify_otp,btn_browes,btn_login;

    private String on_off,holdBoard,holdClass;

    private DatePickerDialog datePickerDialog;

    String[] choice = {"GALLERY","CAMERA"};
    ArrayAdapter<String> adapterimg;
    private static final int galleryCode = 1011;
    private static final int cameraCode = 1012;
    AlertDialog alertDialog;
    private Bitmap bitmap,bitmap2;
    private String mobilePattern;

    ApiInterface apiInterface;

    private User user;

    ArrayList<CustomSpinnerItem> customSpinnerList;
    ArrayList<CustomSpinnerClassItem> customSpinnerClassList;

    private StringRequest stringRequest,stringRequest2;
    private RequestQueue requestQueue,requestQueue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1012);

        adapterimg = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, choice);

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);



        user=new User(this);
        mobilePattern = "[0-9]{10}";


        if (user.getMobile()!="")
        {
            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            finish();
        }


        img_profile=findViewById(R.id.img_profile);
        et_name=findViewById(R.id.et_name);
        cls_spinner=findViewById(R.id.cls_spinner);
        board_spinner=findViewById(R.id.board_spinner);
        et_address=findViewById(R.id.et_address);
        et_phone=findViewById(R.id.et_phone);
        tv_dob=findViewById(R.id.tv_dob);
        et_otp=findViewById(R.id.et_otp);
        et_password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);

        otp_icon=findViewById(R.id.otp_icon);
        phone_icon=findViewById(R.id.phone_icon);

        btn_browes=findViewById(R.id.btn_browes);
        btn_verify_otp=findViewById(R.id.btn_verify_otp);
        btn_verify_phone=findViewById(R.id.btn_verify_phone);
        btn_register=findViewById(R.id.btn_register);

        rg=findViewById(R.id.rg);
        rb_online=findViewById(R.id.rb_online);
        rb_offline=findViewById(R.id.rb_offline);




        et_name.setEnabled(false);
        cls_spinner.setEnabled(false);
        board_spinner.setEnabled(false);
        tv_dob.setEnabled(false);
        btn_browes.setEnabled(false);
        et_address.setEnabled(false);
        rb_online.setEnabled(false);
        rb_offline.setEnabled(false);
        et_password.setEnabled(false);
        btn_register.setEnabled(false);



    btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
         public void onClick(View view) {

         startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            finish();
            }
        });



        btn_browes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("Browse Image :");
                builder.setSingleChoiceItems(adapterimg, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = choice[which];
                        switch (value) {
                            case "GALLERY":
                                Intent galleryIntent =
                                        new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, galleryCode);
                                break;

                            case "CAMERA":
                                Intent cameraIntent =
                                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, cameraCode);
                                break;


                            default:
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


        tv_dob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                datePickerDialog = new DatePickerDialog(RegistrationActivity.this);

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                        tv_dob.setText(day+"/"+(month+1)+"/"+year);


                    }
                });

                datePickerDialog.show();

            }
        });



        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i)
                {
                    case R.id.rb_online:

                        on_off="2";

                        break;

                    case R.id.rb_offline:

                        on_off="1";
                        break;

                }
            }
        });





        et_phone.addTextChangedListener(phoneTextWatcher);

        sendOtp();
        verifyOTP();
        registerDetails();
        fetchClassItems();
        fetchBoardItem();




        //board Spinner Onclick

        board_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CustomSpinnerItem item=(CustomSpinnerItem)adapterView.getSelectedItem();
                holdBoard=item.getSpinnerItemName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        //Class Spinner Onclick

        cls_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CustomSpinnerClassItem item=(CustomSpinnerClassItem)adapterView.getSelectedItem();

                holdClass=item.getSpinnerItemName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==RegistrationActivity.galleryCode && resultCode==RESULT_OK ) {

            Uri selectedimage=data.getData();


            try {

                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedimage);

                img_profile.setImageBitmap(bitmap);

            } catch (Exception ex) {

                ex.printStackTrace();
            }


        }

        else if (requestCode==RegistrationActivity.cameraCode && resultCode==RESULT_OK)
        {

            Uri selectedimage2=data.getData();
            try {

                //bitmap2=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedimage2);


                img_profile.setImageURI(selectedimage2);

            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }


    }



    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream bt=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bt);
        byte[] imgBytes=bt.toByteArray();
        String encodeImage= Base64.encodeToString(imgBytes,Base64.DEFAULT);

        return encodeImage;
    }



    //Phone edittext

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String phoneInput=et_phone.getText().toString();

            if (!phoneInput.isEmpty())
            {
                btn_verify_phone.setVisibility(View.VISIBLE);
                btn_verify_phone.setEnabled(true);
            }
            else
            {
                btn_verify_phone.setVisibility(View.GONE);
                et_otp.setVisibility(View.GONE);
                btn_verify_otp.setVisibility(View.GONE);
                otp_icon.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private void sendOtp()
    {

        btn_verify_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_phone.getText().toString().equals(""))
                {
                    Toast.makeText(RegistrationActivity.this, "Phone No is required", Toast.LENGTH_SHORT).show();
                }
                else if (!et_phone.getText().toString().matches(mobilePattern))
                {
                    Toast.makeText(RegistrationActivity.this, "Invalid Mobile No", Toast.LENGTH_SHORT).show();
                }

                else
                {


                    String phonee_no=et_phone.getText().toString();

                    Call<String> call = apiInterface.sendOTP(phonee_no);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            String res=response.body();

                            try {
                                JSONObject jsonObject = new JSONObject(res);

                                String status=jsonObject.getString("status");

                                if (status.equalsIgnoreCase("200"))
                                {

                                    Toast.makeText(RegistrationActivity.this, "OTP Send", Toast.LENGTH_SHORT).show();

                                    et_otp.setVisibility(View.VISIBLE);
                                    btn_verify_otp.setVisibility(View.VISIBLE);
                                    otp_icon.setVisibility(View.VISIBLE);

                                    btn_verify_phone.setEnabled(false);

                                }

                                else
                                {
                                    Toast.makeText(RegistrationActivity.this, "OTP Not Send Try again", Toast.LENGTH_SHORT).show();

                                    et_otp.setVisibility(View.GONE);
                                    btn_verify_otp.setVisibility(View.GONE);
                                    otp_icon.setVisibility(View.GONE);

                                    btn_verify_phone.setEnabled(true);
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(RegistrationActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            }
        });

    }




    private void verifyOTP()
    {

        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String verify_otp=et_otp.getText().toString();

                if (verify_otp.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Otp is required", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Call<String> call = apiInterface.verifyOTP(et_phone.getText().toString(),verify_otp);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {

                                JSONObject jsonObject = new JSONObject(response.body());

                                String status=jsonObject.getString("status");
                                String message=jsonObject.getString("message");

                                if (status.equalsIgnoreCase("200"))
                                {

                                    Toast.makeText(RegistrationActivity.this, "Verify Successfully", Toast.LENGTH_SHORT).show();

                                    et_phone.setVisibility(View.GONE);
                                    btn_verify_phone.setVisibility(View.GONE);
                                    phone_icon.setVisibility(View.GONE);

                                    et_otp.setVisibility(View.GONE);
                                    btn_verify_otp.setVisibility(View.GONE);
                                    otp_icon.setVisibility(View.GONE);





                                    et_name.setEnabled(true);
                                    cls_spinner.setEnabled(true);
                                    board_spinner.setEnabled(true);
                                    tv_dob.setEnabled(true);
                                    btn_browes.setEnabled(true);
                                    et_address.setEnabled(true);
                                    rb_online.setEnabled(true);
                                    rb_offline.setEnabled(true);
                                    et_password.setEnabled(true);
                                    btn_register.setEnabled(true);




                                }
                                else
                                {
                                    Toast.makeText(RegistrationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(RegistrationActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });

    }



    private void registerDetails()
    {

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String phone=et_phone.getText().toString().trim();
                final String name=et_name.getText().toString().trim();
                final String cls=holdClass;
                final String board=holdBoard;
                final String dob=tv_dob.getText().toString().trim();
                //String img=imageToString(bitmap);
                final String address=et_address.getText().toString();
                final   String mot=on_off;
                final  String pass=et_password.getText().toString();


                if (name.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                }
                else if (phone.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Phone is required", Toast.LENGTH_SHORT).show();

                }
                else if (cls.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Class is required", Toast.LENGTH_SHORT).show();

                }
                else if (board.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Board is required", Toast.LENGTH_SHORT).show();

                }
                else if (dob.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "DOB is required", Toast.LENGTH_SHORT).show();

                }
                else if (address.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "address is required", Toast.LENGTH_SHORT).show();

                }
                else if (rg.getCheckedRadioButtonId()== -1)
                {
                    Toast.makeText(RegistrationActivity.this, "Mode of type is required", Toast.LENGTH_SHORT).show();

                }
                else if (pass.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Password is required", Toast.LENGTH_SHORT).show();

                }
                else {



                    /*Call<String> call=apiInterface.register(phone,name,cls,board,address,mot,dob,pass);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {



                            try {
                                JSONObject jsonObject=new JSONObject(response.body());

                                String status=jsonObject.getString("status");

                                if (status.equalsIgnoreCase("200"))
                                {
                                    Toast.makeText(RegistrationActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                    user.setMobile(phone);
                                    user.setPass(pass);

                                    startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(RegistrationActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Toast.makeText(RegistrationActivity.this, "Try again", Toast.LENGTH_SHORT).show();


                        }
                    });*/





                }


            }
        });





    }



    private void fetchClassItems()
    {


        customSpinnerClassList=new ArrayList<>();

        requestQueue2= Volley.newRequestQueue(RegistrationActivity.this);
        stringRequest2=new StringRequest(Request.Method.POST,Constants.BASE_SERVER + "get_all_class",


                new com.android.volley.Response.Listener<String>() {
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

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String board_name=jsonObject1.getString("class_name");

                                    CustomSpinnerClassItem customSpinnerClassItem=new CustomSpinnerClassItem(RegistrationActivity.this,board_name);
                                    customSpinnerClassList.add(customSpinnerClassItem);
                                }

                                cls_spinner.setAdapter(new CustomSpinnerClassAdapter(RegistrationActivity.this,customSpinnerClassList));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }

        );
        requestQueue2.add(stringRequest2);


    }





    private void fetchBoardItem()
    {


        customSpinnerList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(RegistrationActivity.this);
        stringRequest=new StringRequest(Request.Method.POST,Constants.BASE_SERVER+"get_all_board",


                new com.android.volley.Response.Listener<String>() {
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

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String board_name=jsonObject1.getString("board_name");

                                    CustomSpinnerItem customSpinnerItem=new CustomSpinnerItem(RegistrationActivity.this,board_name);
                                    customSpinnerList.add(customSpinnerItem);
                                }

                                board_spinner.setAdapter(new CustomSpinnerAdapter(RegistrationActivity.this,customSpinnerList));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }

        );
        requestQueue.add(stringRequest);


    }








}
