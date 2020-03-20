package com.visiabletech.indidmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.SwitchChildInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView attendance_img,notice_img,bulletin_img,time_table_img,syllabus_img;
    private ImageView ptm_img,iv_study;
    private CircleImageView img_profile;
    private TextView tv_name,tv_class,tv_section;

    Typeface typeface;
    private ProgressDialog progressDialog;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    private User user;
    private String holdId,hold_student_role,holdPhone;
    private StringRequest stringRequest, stringRequest_profile,stringRequest_enroll_no;
    private RequestQueue requestQueue,requestQueue_profile,requestQueue_enroll_no;

    //private String SERVER_URL="http://visiabletech.com/snrmemorialtrust/webservices/websvc/switch_user_check";
   // private String SERVER_URL_PROFILE="http://visiabletech.com/snrmemorialtrust/webservices/websvc/student_profile";
    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;

    private ImageView iv_exam,iv_share;

    private TextView entoll_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_modify);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Indid");
        toolbar.setTitleTextColor(Color.WHITE);


        user=new User(this);
        holdId=user.getId();
        hold_student_role=user.getRole();

        holdPhone=user.getMobile();



        notice_img=(ImageView) findViewById(R.id.notice_img);
        bulletin_img=(ImageView)findViewById(R.id.bulletin_img);
        attendance_img=(ImageView)findViewById(R.id.attendance_img);
        time_table_img=(ImageView)findViewById(R.id.time_table_img);
        syllabus_img=(ImageView)findViewById(R.id.syllabus_img);
        img_profile=(CircleImageView) findViewById(R.id.profile_img);
        ptm_img=(ImageView)findViewById(R.id.ptm_img);

        iv_share=(ImageView)findViewById(R.id.share_img);
        iv_study=(ImageView)findViewById(R.id.iv_study);

        entoll_no=findViewById(R.id.enrollment_no);

        tv_name=findViewById(R.id.tv_name);
        tv_class=findViewById(R.id.tv_class);
        tv_section=findViewById(R.id.tv_section);

        iv_exam=(ImageView)findViewById(R.id.iv_exam);


        //Alert Dialog
        alartDialog();



        //Check Internet Connection
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);



        notice_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* startActivity(new Intent(MainActivity.this,NoticeActivity.class));
                finish();*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        bulletin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* startActivity(new Intent(MainActivity.this,BulletinActivity.class));
                finish();*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();


            }
        });


        attendance_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* startActivity(new Intent(MainActivity.this,AttendanceActivity.class));
                finish();*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });


        time_table_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivity(new Intent(MainActivity.this,TimeTableActivity.class));
                finish();*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });

        syllabus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivity(new Intent(MainActivity.this,SyllabusActivity.class));
                finish();*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });


        iv_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivity(new Intent(MainActivity.this,ExaminationActivity.class));
                finish();*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        ptm_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });


        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody="Your Body";
                String shareSub="Your Subject";
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                startActivity(Intent.createChooser(shareIntent,"Share to"));*/

                Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

            }
        });



        //Fetch profile details
        //fetchProfile();

        fetchEntolmentNo();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.switch_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        switch (item.getItemId())
        {
            case R.id.menu_switch_user:

               /* if (hold_student_role.equalsIgnoreCase("s"))
                {
                    switchUser();
                }

                if (hold_student_role.equalsIgnoreCase("d"))
                {

                }*/

                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();

                break;

            case R.id.menu_logout:


                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                user.remove();
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();

                            }
                        }).show();



        }

        return super.onOptionsItemSelected(item);
    }


    private void switchUser()
    {


        requestQueue= Volley.newRequestQueue(MainActivity.this);

        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "switch_user_check",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.optString("status");
                            String message=jsonObject.optString("message");

                            if (status.equalsIgnoreCase("200"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("child_details");

                                switchChildInfoArrayList = new ArrayList<>();

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    //String studentCode = jsonObject1.getString("student_code");
                                    String fName = jsonObject1.getString("First_Name");
                                    String lName = jsonObject1.getString("Last_Name");
                                    String studentId = jsonObject1.getString("Student_id");


                                    SwitchChildInfo switchChildInfo = new SwitchChildInfo(fName, lName, studentId);
                                    switchChildInfoArrayList.add(i, switchChildInfo);


                                }

                                if (switchChildInfoArrayList != null && hold_student_role != null) {
                                    showMultipleChild(hold_student_role, switchChildInfoArrayList);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                }


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();



            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();
                myParams.put("id",holdId);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);


    }




    /**
     * Alert box for choosing multiple child of a particular parent.
     */
    public void showMultipleChild(final String role, final ArrayList<SwitchChildInfo> switchChildInfoArrayList) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        //dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_multiple_child_inside_menu);
        List<String> stringList = new ArrayList<>();  // here is list
        for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
            stringList.add(switchChildInfo.fName + " " + switchChildInfo.lName);
        }
        TextView alertTitleTextView = dialog.findViewById(R.id.alertTitleTextView);
       // alertTitleTextView.setTypeface(typeface);
        final RadioGroup rg = dialog.findViewById(R.id.radioGroup);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
           // rb.setTypeface(typeface);
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                int radioButtonID = rg.getCheckedRadioButtonId();
                RadioButton radioButton = rg.findViewById(radioButtonID);
                String selectChild = radioButton.getText().toString();
                Log.i("Child name: ", selectChild);
                for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
                    String childName = switchChildInfo.fName + " " + switchChildInfo.lName;
                    if (childName.equalsIgnoreCase(selectChild)) {
                        // Constants.PROFILENAME = childName;
                        // Constants.USER_ID = switchChildInfo.studentId;
                        // Constants.USER_ROLE = role;
                        // SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                        // SharedPreferences.Editor edt = pref.edit();
                        // edt.putString("student_id",Constants.USER_ID);
                        // edt.commit();
                        //Toast.makeText(context, ""+Constants.USER_ID, Toast.LENGTH_SHORT).show();
                        //Constants.PhoneNo = phoneNo;


                        String profile_name=childName;
                        String id=switchChildInfo.studentId;
                        user.sharedPreferences.edit().putString("id",id).commit();
                        user.sharedPreferences.edit().putString("username",profile_name).commit();
                        user.sharedPreferences.edit().putString("role",hold_student_role).commit();



                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });


        dialog.show();



    }


    private void fetchProfile()
    {

        requestQueue_profile=Volley.newRequestQueue(MainActivity.this);

        /*progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.show();*/

        stringRequest_profile=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_profile",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            String status=jsonObject.getString("status");
                            String message=jsonObject.getString("message");

                            if (status.equalsIgnoreCase("200"))
                            {

                                JSONArray jsonArray=jsonObject.getJSONArray("student_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String fname=jsonObject1.getString("First_Name");
                                    String lname=jsonObject1.getString("Last_Name");
                                    String student_class=jsonObject1.getString("class_or_year");
                                    String section=jsonObject1.getString("section");
                                    String profile_img=jsonObject1.getString("image_url");


                                    tv_name.setText(fname+" "+lname);
                                    tv_class.setText(student_class);
                                    tv_section.setText(section);


                                    /*if (!profile_img.equals("")){
                                        Picasso.with(getBaseContext())
                                                .load(profile_img)
                                                .placeholder(R.drawable.profile)
                                                .noFade()
                                                .into(img_profile);
                                    }*/

                                }

                                //progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
               // progressDialog.dismiss();

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();

                myParams.put("id",holdId);

                return myParams;
            }
        };

        requestQueue_profile.add(stringRequest_profile);

    }




    private void alartDialog()
    {

        builder= new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.error_dialog,null);

        builder.setView(view);
        dialog=builder.create();
        dialog.setCancelable(false);

    }




    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            isNetworkAvailable(context);

        }
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




    private void fetchEntolmentNo()
    {

        requestQueue_enroll_no=Volley.newRequestQueue(MainActivity.this);
        stringRequest_enroll_no=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "reg_fetch",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                           // Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                JSONArray jsonArray=jsonObject.optJSONArray("message");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                    String enrollment_no=jsonObject1.getString("enrollment_no");

                                    entoll_no.setText(enrollment_no);

                                }




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

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<>();

                myParams.put("phone_no",holdPhone);

                return myParams;
            }
        };

        requestQueue_enroll_no.add(stringRequest_enroll_no);



    }


}
