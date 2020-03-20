package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText et_mobile,et_password;
    private Button btn_login,btn_register;

    private String fname,lname,hold_user_name,id,role;
    private RadioGroup rg;

    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;

   private StringRequest stringRequest;
    private RequestQueue requestQueue;

    //private String SERVER_URL="http://visiabletech.com/snrmemorialtrust/webservices/websvc/svclogin";

    private User user;
    private ProgressDialog progressDialog;

    Typeface typeface;

    private ConnectivityManager cm;
    private NetworkInfo networkInfo;
    private String mobilePattern;



    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;



    AlertDialog dialog;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Alert Dialog
        alartDialog();



        //Check Internet Connection
        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);


        et_mobile=findViewById(R.id.et_mobile);
        et_password=findViewById(R.id.et_password);

        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);

        user=new User(this);
        mobilePattern = "[0-9]{10}";

//        typeface= ResourcesCompat.getFont(this,R.font.cabin_medium);


        if (user.getMobile()!="")
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        else
        {

        }



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginDetails();


            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });

    }


    private void loginDetails() {

        final String userName = et_mobile.getText().toString().trim();


        if (et_mobile.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "Mobile is required", Toast.LENGTH_SHORT).show();
        } else if (!et_mobile.getText().toString().trim().matches(mobilePattern)) {
            Toast.makeText(LoginActivity.this, "Please Enter Valid Mobile No", Toast.LENGTH_SHORT).show();
        } else if (et_password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
        } else {


            requestQueue = Volley.newRequestQueue(LoginActivity.this);

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER+ "svclogin",


                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                if (status.equalsIgnoreCase("200")) {


                                    JSONObject info = jsonObject.optJSONObject("info");

                                    fname = info.optString("firstname");
                                    lname = info.optString("lastname");
                                    id = info.optString("id");
                                    role = info.optString("role");




                                    if (role.equalsIgnoreCase("s")) {


                                    /*
                                       For student login, if childType 'm' means parent having multiple child
                                       else childType 's' means parent having single child.
                                        */
                                        String childType = info.getString("childType");


                                        if (childType.equalsIgnoreCase("m")) {


                                            JSONArray areaListJsonArray = info.getJSONArray("student_code");
                                            switchChildInfoArrayList = new ArrayList<>();
                                            for (int i = 0; i < areaListJsonArray.length(); i++) {
                                                JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                                                //String studentCode = cuisineJsonObject.getString("student_code");
                                                String fName = cuisineJsonObject.getString("First_Name");
                                                String lName = cuisineJsonObject.getString("Last_Name");
                                                String studentId = cuisineJsonObject.getString("Student_id");
                                                SwitchChildInfo switchChildInfo = new SwitchChildInfo(fName, lName, studentId);
                                                switchChildInfoArrayList.add(i, switchChildInfo);

                                            }


                                            if (switchChildInfoArrayList != null) {
                                                //Log.e("Before select ", Constants.PROFILENAME);
                                                showMultipleChild(role, switchChildInfoArrayList, userName);
                                            }


                                        } else if (childType.equalsIgnoreCase("s")) {

                                            hold_user_name = fname + " " + lname;


                                            Toast.makeText(LoginActivity.this, "Welcome To Indid", Toast.LENGTH_SHORT).show();

                                            user.setMobile(et_mobile.getText().toString());
                                            user.setUsername(hold_user_name);
                                            user.setId(id);
                                            user.setRole(role);


                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();

                                            progressDialog.dismiss();

                                            //progressDialog.dismiss();


                                        } else if (childType.equalsIgnoreCase("d")) {

                                            hold_user_name = fname + " " + lname;


                                            Toast.makeText(LoginActivity.this, "Welcome To Indid", Toast.LENGTH_SHORT).show();

                                            user.setMobile(et_mobile.getText().toString());
                                            user.setUsername(hold_user_name);
                                            user.setId(id);
                                            user.setRole(role);


                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();

                                            progressDialog.dismiss();
                                        }


                                    }


                                } else {
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(LoginActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            ) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> myParams = new HashMap<String, String>();
                    myParams.put("username", et_mobile.getText().toString());
                    myParams.put("password", et_password.getText().toString());

                    return myParams;
                }
            };

            requestQueue.add(stringRequest);

        }
    }




    /**
     * Alert box for multiple child of a particular parent.
     */
    public void showMultipleChild(final String role, final ArrayList<SwitchChildInfo> switchChildInfoArrayList, final String phoneNo) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_multiple_child);
        List<String> stringList = new ArrayList<>();  // here is list
        for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
            stringList.add(switchChildInfo.fName + " " + switchChildInfo.lName);
        }
        TextView alertTitleTextView = dialog.findViewById(R.id.alertTitleTextView);
        //alertTitleTextView.setTypeface(typeface);
        rg = dialog.findViewById(R.id.radioGroup);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setTypeface(typeface);
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                int radioButtonID = rg.getCheckedRadioButtonId();
                RadioButton radioButton = rg.findViewById(radioButtonID);
                String selectChild = radioButton.getText().toString().trim();
                //Log.e("Select from radio ", selectChild);
                for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
                    String childName = switchChildInfo.fName + " " + switchChildInfo.lName;
                    if (childName.equalsIgnoreCase(selectChild)) {

                        user.setUsername(childName);
                        user.setMobile(et_mobile.getText().toString());
                        user.setRole(role);
                        user.setId(switchChildInfo.studentId);


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });
        dialog.show();
        progressDialog.dismiss();
    }






    private void alartDialog()
    {

         builder= new AlertDialog.Builder(LoginActivity.this);
        View view=getLayoutInflater().inflate(R.layout.error_dialog,null);

        builder.setView(view);
        dialog=builder.create();
        dialog.setCancelable(false);

    }




    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
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



}
