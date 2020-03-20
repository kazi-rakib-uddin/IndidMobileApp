package com.visiabletech.indidmobileapp;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mursaat.extendedtextview.AnimatedGradientTextView;
import com.squareup.picasso.Picasso;
import com.visiabletech.indidmobileapp.Adapter.CustomSwipeAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.SlideImageGetSet;
import com.visiabletech.indidmobileapp.Pogo.SwitchChildInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.visiabletech.indidmobileapp.UpdateAppPlayStore.ForceUpdateAsync;
import com.visiabletech.indidmobileapp.Utils.Const;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainPageActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener,
        BaseSliderView.OnSliderClickListener,NavigationView.OnNavigationItemSelectedListener {
    
    private LinearLayout lin_notice,lin_attendance,lin_bulletins,lin_time_table,lin_ptm,lin_syllabus,lin_mt,slider_lin;
    private ImageView iv_exam,iv_study,iv_videos,iv_share;
    private TextView tv_class,tv_badge_count,tv_board,tv_name_drawer,tv_mobile_drawer;
    private AnimatedGradientTextView tv_name;
    private Button btn_register;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private CircleImageView img_profile_drawer;

    private User user;
    private static final String SHORT_URL="http://indid.in/applogin/";
    private String hold_stu_id,hold_student_role,holdPhone,holdMobile,notification_count,role;

    private StringRequest stringRequest, stringRequest_profile,stringRequest_noti,stringRequest_enroll_no,
                          stringRequest_noti_count, stringRequest_slider, stringRequest_popup_image;


    private RequestQueue requestQueue,requestQueue_profile,requestQueue_noti,requestQueue_enroll_no,

                        requestQueue_noti_count,requestQueue_slder, requestQueue_popup_image;

    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;

    private AVLoadingIndicatorView av_loader;


    private SliderLayout sliderLayout;
    private PagerIndicator pagerIndicator;
    ArrayList<SlideImageGetSet> arraySliderList = new ArrayList<>();

    private HashMap<String, String> HashMapForLocalRes;
    private int i;

    private final String image_short_url="http://indid.in/applogin/";

    private PopupWindow window;
    public static Dialog dialog;
    private ImageView iv_cancel_popup,iv_popup_image;


    public static ViewPager viewPager;
    CustomSwipeAdapter adapter;
    LinearLayout sliderDots;
    private int dotCount;
    public static ImageView[]dots;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);


        //Start- code to show pop up first time
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_up_image_dialog);
        iv_cancel_popup =dialog.findViewById(R.id.iv_cancel_popup);
        iv_popup_image=dialog.findViewById(R.id.iv_popup_image);


        viewPager=dialog.findViewById(R.id.pager);
        sliderDots=dialog.findViewById(R.id.slider_dots);


        adapter=new CustomSwipeAdapter(this);

        viewPager.setAdapter(adapter);

        dotCount=adapter.getCount();
        dots=new ImageView[dotCount];

        for (int i=0; i<dotCount; i++)
        {
            dots[i]=new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,

                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0,8,0);

            sliderDots.addView(dots[i],params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i=0; i<dotCount; i++)
                {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);


        checkFirstRun();

        //End- code to show pop up first time

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Indid");
        toolbar.setTitleTextColor(Color.WHITE);


        //Start Drawer
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tv_name_drawer=headerView.findViewById(R.id.tv_name_drawer);
        tv_mobile_drawer=headerView.findViewById(R.id.tv_mobile_drawer);
        img_profile_drawer=headerView.findViewById(R.id.img_profile_drawer);

        //End Drawer


        TextView time_tv=findViewById(R.id.tv_time);
        lin_notice=findViewById(R.id.lin_notice);
        lin_attendance=findViewById(R.id.lin_attendance);
        lin_bulletins=findViewById(R.id.lin_bulltins);
        lin_time_table=findViewById(R.id.lin_time_table);
        lin_ptm=findViewById(R.id.lin_ptm);
        lin_syllabus=findViewById(R.id.lin_syllabus);
        lin_mt=findViewById(R.id.lin_moktest);
        slider_lin=findViewById(R.id.sl_lin);
        av_loader =findViewById(R.id.av_loader);




        btn_register = findViewById(R.id.btn_register);

        tv_class=findViewById(R.id.tv_class);
        tv_board=findViewById(R.id.tv_board);
        tv_name=findViewById(R.id.tv_name);
        
        iv_exam=findViewById(R.id.iv_exam);
        iv_study=findViewById(R.id.iv_study);
        iv_videos=findViewById(R.id.iv_videos);
        iv_share=findViewById(R.id.iv_share);

        sliderLayout=findViewById(R.id.slider);
        pagerIndicator=findViewById(R.id.banner_slider_indicator);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(2000);
        sliderLayout.addOnPageChangeListener(this);

        user=new User(this);
        hold_stu_id=user.getId();
        holdMobile = user.getMobile();


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
           // Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            time_tv.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
           // Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();

            time_tv.setText("Good Afternoon");


        }else if(timeOfDay >= 16 && timeOfDay < 21){
           // Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();

            time_tv.setText("Good Evening");

        }else if(timeOfDay >= 21 && timeOfDay < 24){
           // Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();

            time_tv.setText("Good Night");
        }




        //play store version update
        forceUpdate();




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

                        final String newToken=instanceIdResult.getToken();


                        requestQueue_noti=Volley.newRequestQueue(MainPageActivity.this);
                        stringRequest_noti=new StringRequest(Request.Method.POST, Constants.BASE_SERVER +"svclogin_check",

                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                               // Toast.makeText(MainPageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        ){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>myParams=new HashMap<String, String>();

                                myParams.put("user_token",newToken);
                                myParams.put("username",holdMobile);

                                return myParams;
                            }
                        };

                        requestQueue_noti.add(stringRequest_noti);


                    }
                });

        //End push notification





        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainPageActivity.this, ExecutiveMobileActivity.class));
                finish();
            }
        });



        lin_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 startActivity(new Intent(MainPageActivity.this,NoticeActivity.class));
                finish();

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });


        lin_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* startActivity(new Intent(MainActivity.this,AttendanceActivity.class));
                finish();*/

                Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });


        lin_bulletins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 startActivity(new Intent(MainPageActivity.this,BulletinActivity.class));
                finish();

//                Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });


        lin_time_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainPageActivity.this,StudyActivity.class));
                finish();


            }
        });


        lin_ptm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(MainPageActivity.this, ResultActivity.class));
                finish();
            }
        });


        lin_syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*startActivity(new Intent(MainActivity.this,SyllabusActivity.class));
                finish();*/

                Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });




        lin_mt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainPageActivity.this, BenefitsActivity.class));
                finish();
            }
        });








        iv_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                /*startActivity(new Intent(MainPageActivity.this,StudyActivity.class));
                finish();*/
            }
        });


        iv_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainPageActivity.this,PaymentActivity.class));
                finish();
                //Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });



        iv_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainPageActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                /*startActivity(new Intent(MainPageActivity.this,VideosActivity.class));
                finish();*/
            }
        });



        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                String app_url = " https://play.google.com/store/apps/details?id=com.visiabletech.indidmobileapp&hl=en";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });



        //Fetch Student Profile
        fetchProfile();


        //Image Slider
        //imageSliderURL();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.switch_user,menu);


        final MenuItem menuItem;

        menuItem=menu.findItem(R.id.menu_notification);

        requestQueue_noti_count=Volley.newRequestQueue(MainPageActivity.this);
        stringRequest_noti_count = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_count",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                String noti_count=jsonObject.getString("notification_count");


                                if (noti_count.equals("0"))
                                {
                                    menuItem.setActionView(null);
                                }
                                else
                                {
                                    menuItem.setActionView(R.layout.notification_badge);

                                    View view = menuItem.getActionView();
                                    tv_badge_count = view.findViewById(R.id.tv_badge_count);
                                    tv_badge_count.setText(noti_count);
                                    
                                    
                                    
                                    tv_badge_count.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            startActivity(new Intent(MainPageActivity.this,NotificationActivity.class));
                                            finish();

                                        }
                                    });
                                    

                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainPageActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams=new HashMap<>();
                myParams.put("student_id",hold_stu_id);

                return myParams;
            }
        };

        requestQueue_noti_count.add(stringRequest_noti_count);



        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        switch (item.getItemId())
        {
            case R.id.menu_switch_user:

                /*if (hold_student_role.equalsIgnoreCase("s"))
                {
                    switchUser();
                }

                if (hold_student_role.equalsIgnoreCase("d"))
                {

                }

                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();*/

                break;

            case R.id.menu_logout:


                new AlertDialog.Builder(MainPageActivity.this)
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                user.remove();
                                Const.OPEN_HOME_FIRST_TIME="";
                                startActivity(new Intent(MainPageActivity.this,LoginModifyActivity.class));
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();

                            }
                        }).show();

                break;

            case R.id.menu_mtse:
                startActivity(new Intent(MainPageActivity.this,MTSEActivity.class));
                finish();

                break;





        }

        return super.onOptionsItemSelected(item);
    }





    private void fetchProfile()
    {

        requestQueue_profile= Volley.newRequestQueue(MainPageActivity.this);
        av_loader.setVisibility(View.VISIBLE);
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
                                    String student_class=jsonObject1.getString("class_or_year");
                                    String board=jsonObject1.getString("board");
                                    String profile_img= SHORT_URL + jsonObject1.getString("image_url");
                                    role=jsonObject1.getString("role");


                                    tv_name.setText(fname);
                                    tv_class.setText(student_class);
                                    tv_board.setText(board);

                                    tv_name_drawer.setText(fname);
                                    tv_mobile_drawer.setText(holdMobile);


                                    if (!profile_img.equals("")){
                                        Picasso.with(getBaseContext())
                                                .load(profile_img)
                                                .placeholder(R.drawable.profile_man_img)
                                                .noFade()
                                                .into(img_profile_drawer);
                                    }

                                }

                                if (role.equals("ex"))
                                {
                                    btn_register.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    btn_register.setVisibility(View.GONE);
                                }

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

                Toast.makeText(MainPageActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                 av_loader.hide();

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();

                myParams.put("id",hold_stu_id);

                return myParams;
            }
        };

        requestQueue_profile.add(stringRequest_profile);

    }






    private void switchUser()
    {


        requestQueue= Volley.newRequestQueue(MainPageActivity.this);

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
                                    String student_role = jsonObject1.getString("role");


                                    SwitchChildInfo switchChildInfo = new SwitchChildInfo(fName, lName, studentId);
                                    switchChildInfoArrayList.add(i, switchChildInfo);


                                }

                                if (switchChildInfoArrayList != null && hold_student_role != null) {
                                    showMultipleChild(hold_student_role, switchChildInfoArrayList);
                                }
                                else
                                {
                                    Toast.makeText(MainPageActivity.this, message, Toast.LENGTH_SHORT).show();
                                }


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainPageActivity.this, "Try again", Toast.LENGTH_SHORT).show();



            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();
                myParams.put("id",hold_stu_id);

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



                        Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });


        dialog.show();



    }



    // check version on play store and force update
    public void forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert packageInfo != null;
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion, MainPageActivity.this).execute();
    }



    private void imageSliderURL()
    {
        requestQueue_slder=Volley.newRequestQueue(MainPageActivity.this);
        stringRequest_slider = new StringRequest(Request.Method.POST, Constants.BASE_SERVER+"indid_banner",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                slider_lin.setVisibility(View.VISIBLE);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String img_url=jsonObject1.getString("banner_url");

                                    SlideImageGetSet imageGetSet = new SlideImageGetSet();

                                    imageGetSet.setImg_url(image_short_url+img_url);
                                    arraySliderList.add(imageGetSet);

                                    sliderImage();



                                }

                            }
                            else
                            {
                                slider_lin.setVisibility(View.GONE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainPageActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }

        );

        requestQueue_slder.add(stringRequest_slider);

    }



    private void sliderImage()
    {

        HashMapForLocalRes=new HashMap<>();
        HashMapForLocalRes.put("",arraySliderList.get(i).getImg_url());

        for (String name : HashMapForLocalRes.keySet())
        {
            DefaultSliderView defaultSliderView = new DefaultSliderView(MainPageActivity.this);

            defaultSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
//                    .image(R.drawable.image_home_slider_2)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            /*defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle().putString("extra", name);*/
            sliderLayout.addSlider(defaultSliderView);
            sliderLayout.setCustomIndicator(pagerIndicator);
        }



        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(5000);
        sliderLayout.addOnPageChangeListener(this);

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    @Override
    public void onResume() {
        super.onResume();

        sliderLayout.startAutoCycle();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.edit_profile:
                startActivity(new Intent(MainPageActivity.this,ProfileActivity.class));
                finish();
                break;

            case R.id.contact_us:
                startActivity(new Intent(MainPageActivity.this,ContactUsActivity.class));
                finish();
                break;


            case R.id.payment:
                startActivity(new Intent(MainPageActivity.this,PaymentActivity.class));
                finish();
                break;

            case R.id.payment_history:
                startActivity(new Intent(MainPageActivity.this,PaymentHistoryActivity.class));
                finish();
                break;
        }

        return false;
    }

    //Start- code to show pop up first time

    public void checkFirstRun() {


        if (Const.OPEN_HOME_FIRST_TIME.equals("")){
            Const.OPEN_HOME_FIRST_TIME="false";
            //getPopUpImageApiCall();
            showPopUp();
        }

    }



    private void showPopUp() {

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.70);
        dialog.getWindow().setLayout(width, height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();

        iv_cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);

    }

    private void getPopUpImageApiCall() {

        requestQueue_popup_image=Volley.newRequestQueue(MainPageActivity.this);
        stringRequest_popup_image = new StringRequest(Request.Method.GET, Constants.BASE_SERVER+"indid_popup",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if (status.equalsIgnoreCase("200"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("popup");

                                    for (i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String popup_img_url = jsonObject1.getString("url");
                                        if (!popup_img_url.equals("")) {
                                            Picasso.with(getBaseContext())
                                                    .load(popup_img_url)
                                                    .placeholder(R.drawable.drawer_baner)
                                                    .noFade()
                                                    .into(iv_popup_image);
                                        }
                                        showPopUp();
                                    }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(MainPageActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }

        );
        requestQueue_popup_image.add(stringRequest_popup_image);

    }


    //End- code to show pop up first time





    public class MyTimerTask extends TimerTask
    {

        @Override
        public void run() {

            MainPageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() ==0)
                    {
                        viewPager.setCurrentItem(1);
                    }
                    else if (viewPager.getCurrentItem()==1)
                    {
                        viewPager.setCurrentItem(2);
                    }
                    else if (viewPager.getCurrentItem()==2)
                    {
                        viewPager.setCurrentItem(3);
                    }

                    else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }




}
