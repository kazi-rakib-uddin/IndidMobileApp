package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.HomeWorkAdapter;
import com.visiabletech.indidmobileapp.Pogo.HomeWorkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeWorkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<HomeWorkInfo> homeWorkInfoList;

    private StringRequest stringRequest_home_work;
    private RequestQueue requestQueue_home_work;
    private String SERVER_URL_HOME_WORK="http://visiabletech.com/snrmemorialtrust/webservices/websvc/get_homework_details";

    private String holdId="7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);

      /*  Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Indid");


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager=layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        fetchHomeWorkDetails();*/

    }


    private void fetchHomeWorkDetails()
    {

        requestQueue_home_work= Volley.newRequestQueue(HomeWorkActivity.this);
        stringRequest_home_work=new StringRequest(Request.Method.POST, SERVER_URL_HOME_WORK,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.optString("status");
                            String message=jsonObject.optString("message");

                            if (status.equalsIgnoreCase("200"))
                            {

                                homeWorkInfoList=new ArrayList<>();
                                JSONArray jsonArray=jsonObject.getJSONArray("home_work_details");

                                for (int i=0; i<jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 =jsonArray.getJSONObject(i);

                                    String id=jsonObject1.getString("id");
                                    String subject=jsonObject1.getString("subject");
                                    String topic=jsonObject1.getString("topic");
                                    String class_name=jsonObject1.getString("class_name");
                                    String date_of_class = jsonObject1.getString("date_of_class");
                                    String complete_date = jsonObject1.getString("complete_date");

                                    HomeWorkInfo homeWorkInfo = new HomeWorkInfo(HomeWorkActivity.this,id,class_name,subject,topic,date_of_class,complete_date);
                                    homeWorkInfoList.add(i,homeWorkInfo);

                                }

                                recyclerView.setAdapter(new HomeWorkAdapter(getBaseContext(),homeWorkInfoList));
                            }


                        }catch (Exception e)
                        {

                            Toast.makeText(HomeWorkActivity.this, "Try again", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(HomeWorkActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

        requestQueue_home_work.add(stringRequest_home_work);

    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(HomeWorkActivity.this,MainActivity.class));
        finish();
    }
}
