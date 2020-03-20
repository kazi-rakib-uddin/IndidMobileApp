package com.visiabletech.indidmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.Adapter.ExamQuestionAdapter;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.Pogo.ExamQuestioninInfo;
import com.visiabletech.indidmobileapp.Pogo.ExaminationInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.visiabletech.indidmobileapp.RoomDatabase.ExamDatabase;
import com.visiabletech.indidmobileapp.RoomDatabase.ExamTable;
import com.visiabletech.indidmobileapp.SharedPreferences.ExamPref;
import com.visiabletech.indidmobileapp.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExamQuestionActivity extends AppCompatActivity {

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    public static ViewPager viewPager;
    public static Button btn_next,btn_prev;
    private ArrayList<ExamTable> arrayList = new ArrayList<>();
    ArrayList<ExamTable> examArrayList = new ArrayList<>();

    String st_exam_id;

    public static ExamDatabase examDatabase;

    private String hold_exam_id,hold_test_status;
    private ExamPref examPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_question);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        viewPager=findViewById(R.id.view_pager);
        btn_next=findViewById(R.id.btn_next);
        btn_prev=findViewById(R.id.btn_prev);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Examination Details");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.back_img);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExamQuestionActivity.this,ExaminationActivity.class));
                finish();
            }
        });



        examDatabase = Room.databaseBuilder(getApplicationContext(),
                ExamDatabase.class, "student_exam").allowMainThreadQueries().fallbackToDestructiveMigration().build();


        hold_exam_id = getIntent().getExtras().getString("exam_id");
        hold_test_status = getIntent().getExtras().getString("test_status");

        Const.exam_id = hold_exam_id;



        examArrayList= (ArrayList<ExamTable>) ExamQuestionActivity.examDatabase.examSetDeo().retriveExamListSet();


        if (examArrayList==null || examArrayList.size()==0)
        {
            //Fetch question
            fetchQuestion();

        }
        else
        {

            ExamQuestionAdapter questionAdapter = new ExamQuestionAdapter(ExamQuestionActivity.this,examArrayList);
            viewPager.setAdapter(questionAdapter);


        }






    }


    private void fetchQuestion()
    {
        requestQueue = Volley.newRequestQueue(ExamQuestionActivity.this);
        stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "question_list",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {


                                JSONObject jsonObject_msg=jsonObject.optJSONObject("message");

                                 st_exam_id = jsonObject_msg.getString("st_exam_id");

                                Const.st_exam_id = st_exam_id;


                                JSONArray jsonArray = jsonObject_msg.getJSONArray("res");

                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 =jsonArray.getJSONObject(i);

                                    String question = jsonObject1.getString("question");
                                    String question_id = jsonObject1.getString("id");


                                    ExamTable examTable = new ExamTable();
                                    examTable.setExam_id(hold_exam_id);
                                    examTable.setQuestion_id(question_id);
                                    examTable.setQuestion(question);
                                    examTable.setStudent_id("1");


                                    /*ExamQuestioninInfo info = new ExamQuestioninInfo(ExamQuestionActivity.this,question_id,
                                            question,st_exam_id,hold_exam_id);*/

                                    arrayList.add(examTable);
                                }

                                examDatabase.examSetDeo().addExamQuestion(arrayList);

                                examArrayList= (ArrayList<ExamTable>) ExamQuestionActivity.examDatabase.examSetDeo().retriveExamListSet();

                                ExamQuestionAdapter examQuestionAdapter = new ExamQuestionAdapter(ExamQuestionActivity.this,examArrayList);

                                viewPager.setAdapter(examQuestionAdapter);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ExamQuestionActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<>();

                myParams.put("student_id","1");
                myParams.put("exam_id",hold_exam_id);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);

    }




    @Override
    public void onBackPressed() {

        startActivity(new Intent(ExamQuestionActivity.this,ExaminationActivity.class));
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        examDatabase = Room.databaseBuilder(getApplicationContext(),
                ExamDatabase.class, "student_exam").allowMainThreadQueries().fallbackToDestructiveMigration().build();


    }
}
