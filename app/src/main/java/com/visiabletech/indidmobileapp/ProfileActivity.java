package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.visiabletech.indidmobileapp.Adapter.CustomSpinnerAdapter;
import com.visiabletech.indidmobileapp.Adapter.CustomSpinnerClassAdapter;
import com.visiabletech.indidmobileapp.ApiClient.ApiClient;
import com.visiabletech.indidmobileapp.ApiInterface.ApiInterface;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerClassItem;
import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerItem;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {

    private Spinner cls_spinner,board_spinner;

    private TextView tv_dob,tv_name,tv_mobile_no;
    private Button btn_save_profile;
    private EditText et_name,et_pincode,et_area,et_address,et_city,et_email;
    private RadioGroup rg;
    private RadioButton rb_online,rb_offline;
    private CheckBox ch_box;
    private CircleImageView img_profile;

    String[] choice = {"CAMERA","GALLERY"};
    ArrayAdapter<String> adapterimg;
    private static final int galleryCode = 1011;
    private static final int cameraCode = 1012;
    private Bitmap bitmap;

    private String holdChecked="0";
    private AVLoadingIndicatorView av_loader;
    private User user;

    private static final String SHORT_URL="http://indid.in/applogin/";


    private String hold_online_offline,hold_mobile,hold_class,hold_board,holdStdId;

    private DatePickerDialog datePickerDialog;
    private ApiInterface apiInterface;
    ArrayList<CustomSpinnerItem> customSpinnerList;
    ArrayList<CustomSpinnerClassItem> customSpinnerClassList;

    private StringRequest stringRequest_class,stringRequest_board,stringRequest_profile_details,
            stringRequest_profile_update,stringRequest_upload_profileImg;
    private RequestQueue requestQueue_class,requestQueue_board,requestQueue_profile_details,
            requestQueue_profile_update,requestQueue_upload_profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        ActivityCompat.requestPermissions(ProfileActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1012);

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);

        cls_spinner=findViewById(R.id.class_spinner);
        tv_dob=findViewById(R.id.dob);
        board_spinner=findViewById(R.id.board_spinner);

        btn_save_profile = findViewById(R.id.btn_save_profile);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_pincode=findViewById(R.id.et_pin_code);
        et_area=findViewById(R.id.et_area);
        et_city=findViewById(R.id.et_city);
        et_address=findViewById(R.id.et_address);
        rg=findViewById(R.id.rg);
        ch_box=findViewById(R.id.ch_box);
        img_profile=findViewById(R.id.img_profile);
        tv_name=findViewById(R.id.tv_name);
        tv_mobile_no=findViewById(R.id.tv_mobile_no);

        rb_offline = findViewById(R.id.rb_offline);
        rb_online = findViewById(R.id.rb_online);

        av_loader = findViewById(R.id.av_loader);


        user = new User(this);
        hold_mobile = user.getMobile();
        holdStdId =user.getId();

        tv_mobile_no.setText(hold_mobile);

        //fetch Class name and boadrs name
        fetchClassName();
        fetchBoardName();

        fetchProfileDetails();

        adapterimg = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, choice);



        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();

            }
        });



        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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

                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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




        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                datePickerDialog = new DatePickerDialog(ProfileActivity.this);

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                        tv_dob.setText(day+"-"+(month+1)+"-"+year);


                    }
                });

                datePickerDialog.show();

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




    }




    private void fetchClassName()
    {

        customSpinnerClassList=new ArrayList<>();

        requestQueue_class= Volley.newRequestQueue(ProfileActivity.this);
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
                                    CustomSpinnerClassItem customSpinnerClassItem=new CustomSpinnerClassItem(ProfileActivity.this,class_name);
                                    customSpinnerClassList.add(customSpinnerClassItem);

                                }

                                cls_spinner.setAdapter(new CustomSpinnerClassAdapter(ProfileActivity.this,customSpinnerClassList));

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

        requestQueue_board=Volley.newRequestQueue(ProfileActivity.this);
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

                                    CustomSpinnerItem customSpinnerItem=new CustomSpinnerItem(ProfileActivity.this,board_name);
                                    customSpinnerList.add(customSpinnerItem);
                                }

                                board_spinner.setAdapter(new CustomSpinnerAdapter(ProfileActivity.this,customSpinnerList));


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ProfileActivity.this, "Try again", Toast.LENGTH_SHORT).show();

            }
        }

        );

        requestQueue_board.add(stringRequest_board);

    }




    private void updateProfile()
    {

        final String name=et_name.getText().toString();
        final String email=et_email.getText().toString();
        final String cls=hold_class;
        final String board=hold_board;
        final String dob=tv_dob.getText().toString();
        final String pin_code=et_pincode.getText().toString();
       final String area=et_area.getText().toString();
       final String city=et_city.getText().toString();
       final String address=et_address.getText().toString();


        requestQueue_profile_update=Volley.newRequestQueue(ProfileActivity.this);

        av_loader.setVisibility(View.VISIBLE);
        av_loader.show();

        stringRequest_profile_update=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_reg_update",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(ProfileActivity.this, "Profile Saved", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }
                            else
                            {
                                Toast.makeText(ProfileActivity.this, "not saved", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(ProfileActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams=new HashMap<>();

                myParams.put("student_id",holdStdId);
                myParams.put("name",et_name.getText().toString());
                myParams.put("email",et_email.getText().toString());
                //myParams.put("class",hold_class);
               // myParams.put("board",hold_board);
                myParams.put("dob",tv_dob.getText().toString());
                myParams.put("pincode",et_pincode.getText().toString());
                myParams.put("area",et_area.getText().toString());
                myParams.put("city",et_city.getText().toString());
                myParams.put("address",et_address.getText().toString());
                myParams.put("mode_of_test",hold_online_offline);
                myParams.put("workshop",holdChecked);

                return myParams;
            }
        };

        requestQueue_profile_update.add(stringRequest_profile_update);




    }




    private void fetchProfileDetails()
    {

        requestQueue_profile_details = Volley.newRequestQueue(ProfileActivity.this);

        av_loader.setVisibility(View.VISIBLE);
        av_loader.show();

        stringRequest_profile_details = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_profile",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("student_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String name=jsonObject1.getString("First_Name");
                                    String email=jsonObject1.getString("email");
                                    //String st_class=jsonObject1.getString("class_or_year");
                                    //String board=jsonObject1.getString("board");
                                    String pin_Code=jsonObject1.getString("pin_Code");
                                    String dob=jsonObject1.getString("date_of_birth");
                                    String city=jsonObject1.getString("city");
                                    String area=jsonObject1.getString("area");
                                    String address=jsonObject1.getString("permanent_address");
                                    String mot=jsonObject1.getString("mode_of_test");
                                    String workshop=jsonObject1.getString("workshop");
                                    String image_url=SHORT_URL + jsonObject1.getString("image_url");


                                    Picasso.with(ProfileActivity.this).load(image_url)
                                            .error(R.drawable.profile_man_img)
                                            .placeholder(R.drawable.profile_man_img)
                                            .into(img_profile);


                                    if (name.matches("null"))
                                    {
                                        et_name.setText("");
                                        tv_name.setText("");
                                    }
                                    else
                                    {
                                        et_name.setText(name);
                                        tv_name.setText(name);

                                    }

                                     if (email.matches("null"))
                                    {
                                        et_email.setText("");
                                    }
                                     else
                                     {
                                         et_email.setText(email);
                                     }
                                     if (pin_Code.matches("null"))
                                    {
                                        et_pincode.setText("");
                                    }

                                     else
                                     {
                                         et_pincode.setText(pin_Code);
                                     }

                                     if (dob.matches("null"))
                                    {
                                        tv_dob.setText("");
                                    }

                                     else
                                     {
                                         tv_dob.setText(dob);
                                     }

                                     if (city.matches("null"))
                                    {
                                        et_city.setText("");
                                    }
                                     else
                                     {
                                         et_city.setText(city);
                                     }
                                     if (area.matches("null"))
                                    {
                                        et_area.setText("");
                                    }
                                     else
                                     {
                                         et_area.setText(area);
                                     }
                                     if (address.matches("null"))
                                    {
                                        et_address.setText("");
                                    }

                                    else
                                    {
                                        et_address.setText(address);
                                    }


                                    if (mot.equals("1"))
                                    {
                                        rb_offline.setChecked(true);

                                        hold_online_offline="1";
                                    }
                                    else
                                    {

                                        rb_online.setChecked(true);

                                        hold_online_offline="2";
                                    }


                                    if (workshop.equals("1"))
                                    {
                                        ch_box.setChecked(true);
                                        holdChecked="1";
                                    }
                                    else
                                    {
                                        ch_box.setChecked(false);
                                        holdChecked="0";
                                    }





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

                Toast.makeText(ProfileActivity.this, "Try again", Toast.LENGTH_SHORT).show();

                av_loader.hide();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<>();
                myParams.put("id",holdStdId);

                return myParams;
            }
        };

        requestQueue_profile_details.add(stringRequest_profile_details);



    }



    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream bt=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bt);
        byte[] imgBytes=bt.toByteArray();
        String encodeImage= Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return encodeImage;
    }


    private void updateProfileImage()
    {

        final String image=imageToString(bitmap);

        requestQueue_upload_profileImg = Volley.newRequestQueue(ProfileActivity.this);

       av_loader.setVisibility(View.VISIBLE);
       av_loader.show();

        stringRequest_upload_profileImg = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "profile_img_update",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");


                            if (status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(ProfileActivity.this, "Save Profile Image", Toast.LENGTH_SHORT).show();
                                av_loader.hide();
                            }
                            else
                            {
                                Toast.makeText(ProfileActivity.this, "Image Not Saved", Toast.LENGTH_SHORT).show();

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

                Toast.makeText(ProfileActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                av_loader.hide();
            }
        }

        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<>();

                myParams.put("student_id",holdStdId);
                myParams.put("image",image);

                return myParams;
            }
        };



         stringRequest_upload_profileImg.setRetryPolicy(new RetryPolicy() {
        @Override
        public int getCurrentTimeout() {
            return 50000;
        }

        @Override
        public int getCurrentRetryCount() {
            return 50000;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {

        }
    });


         requestQueue_upload_profileImg.add(stringRequest_upload_profileImg);


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ProfileActivity.galleryCode && resultCode==RESULT_OK ) {

            Uri selectedimage=data.getData();


            try {

                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedimage);

                img_profile.setImageBitmap(bitmap);


            } catch (Exception ex) {

                ex.printStackTrace();
            }

            updateProfileImage();


        }



        if (requestCode == cameraCode && resultCode == RESULT_OK) {


            bitmap = (Bitmap) data.getExtras().get("data");
            img_profile.setImageBitmap(bitmap);

            updateProfileImage();

        }




    }






    @Override
    public void onBackPressed() {

        startActivity(new Intent(ProfileActivity.this,MainPageActivity.class));
        finish();
    }
}
