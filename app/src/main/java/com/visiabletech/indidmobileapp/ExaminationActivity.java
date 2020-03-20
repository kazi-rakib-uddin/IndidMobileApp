package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.ExamListAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.ExamListInfo;
import com.visiabletech.indidmobileapp.Pogo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExaminationActivity extends AppCompatActivity {

    private String SERVER_URL2="http://visiabletech.com/snrmemorialtrust/webservices/websvc/exam_subjects";

    private User user;
    private String holdStdId,holdRole;

    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private ArrayList<ExamListInfo> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Examination");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExaminationActivity.this, MainPageActivity.class));
                finish();
            }
        });


        user=new User(this);
        holdStdId=user.getId();


        recyclerView=findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        RecyclerView.LayoutManager manager=layoutManager;
        recyclerView.setLayoutManager(layoutManager);


        //Fetch Exam List
        fetchExamList();

    }


    private void fetchExamList()
    {
        requestQueue = Volley.newRequestQueue(ExaminationActivity.this);

        progressDialog = new ProgressDialog(ExaminationActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_list",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                                arrayList = new ArrayList<>();

                                JSONArray jsonArray = jsonObject.getJSONArray("message");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String exam_id=jsonObject1.getString("id");
                                    String exam_name=jsonObject1.getString("exam_name");
                                    String exam_subject=jsonObject1.getString("subject_name");
                                    String exam_mins=jsonObject1.getString("tot_time");
                                    String exam_question=jsonObject1.getString("tot_qus");
                                    String test_taken_status=jsonObject1.getString("test_taken_status");

                                    ExamListInfo examListInfo = new ExamListInfo(ExaminationActivity.this,exam_id,exam_name,exam_subject,exam_mins,exam_question,test_taken_status);
                                    arrayList.add(i,examListInfo);

                                }

                                recyclerView.setAdapter(new ExamListAdapter(ExaminationActivity.this,arrayList));

                                progressDialog.dismiss();

                            }
                            else
                            {
                                Toast.makeText(ExaminationActivity.this, "Exam not found", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(ExaminationActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> myParams = new HashMap<>();

                myParams.put("id","1");

                return myParams;
            }
        };

        requestQueue.add(stringRequest);


    }



    @Override
    public void onBackPressed() {

        startActivity(new Intent(ExaminationActivity.this,MainPageActivity.class));
        finish();
    }
}
