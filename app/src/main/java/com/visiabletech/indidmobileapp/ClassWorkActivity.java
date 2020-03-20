package com.visiabletech.indidmobileapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.ClassWorkAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.ClassWorkInfo;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassWorkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ClassWorkInfo> classWorkInfoList;

    private StringRequest stringRequest_class_work;
    private RequestQueue requestQueue_class_work;

   // private String SERVER_URL_CLASS_WORK="http://visiabletech.com/snrmemorialtrust/webservices/websvc/get_classwork_details";

    private String holdId="7";

    private int PERMISSION_CODE=1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_work);



       /* Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Indid");

        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager=layoutManager;
        recyclerView.setLayoutManager(layoutManager);


        checkPermission();
        fetchClassWorkDetails();*/




    }


    private void fetchClassWorkDetails()
    {

        requestQueue_class_work= Volley.newRequestQueue(ClassWorkActivity.this);
        stringRequest_class_work=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_classwork_details",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);


                            String status=jsonObject.optString("status");
                            String message=jsonObject.optString("message");

                            if (status.equalsIgnoreCase("200"))
                            {
                                classWorkInfoList=new ArrayList<>();
                                JSONArray jsonArray=jsonObject.getJSONArray("class_work_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String id=jsonObject1.getString("id");
                                    String class_name=jsonObject1.getString("class_name");
                                    String subject=jsonObject1.getString("subject");
                                    String topic = jsonObject1.getString("topic");
                                    String date=jsonObject1.getString("date_of_class");

                                    ClassWorkInfo classWorkInfo = new ClassWorkInfo(ClassWorkActivity.this,id,class_name,subject,topic,date);
                                    classWorkInfoList.add(i,classWorkInfo);

                                }

                                recyclerView.setAdapter(new ClassWorkAdapter(getBaseContext(),classWorkInfoList));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ClassWorkActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> myParams=new HashMap<String, String>();

                myParams.put("id",holdId);

                return myParams;
            }
        };

        requestQueue_class_work.add(stringRequest_class_work);

    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(ClassWorkActivity.this,MainActivity.class));
        finish();
    }


    public void checkPermission()
    {

        PermissionListener permissionListener=new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                //Toast.makeText(ClassWorkActivity.this, "granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                //Toast.makeText(ClassWorkActivity.this, "Not granted", Toast.LENGTH_SHORT).show();


            }
        };


        TedPermission.with(ClassWorkActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE)
                .check();
    }


}
