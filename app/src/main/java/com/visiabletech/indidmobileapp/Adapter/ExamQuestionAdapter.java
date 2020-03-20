package com.visiabletech.indidmobileapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.ExamQuestionActivity;
import com.visiabletech.indidmobileapp.ExaminationActivity;
import com.visiabletech.indidmobileapp.MainPageActivity;
import com.visiabletech.indidmobileapp.Pogo.ExamQuestioninInfo;
import com.visiabletech.indidmobileapp.Pogo.User;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.RoomDatabase.ExamTable;
import com.visiabletech.indidmobileapp.SharedPreferences.ExamPref;
import com.visiabletech.indidmobileapp.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import at.blogc.android.views.ExpandableTextView;

public class ExamQuestionAdapter extends PagerAdapter {

    Context context;
    private ArrayList<ExamTable> quesList;

    private TextView tv_ques_no;
    private ExpandableTextView tv_question;
    private EditText et_answer;
    private Button btn_read_more;
    private LayoutInflater inflater;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private  String answer_text, q_id="",st_exam_id,holdStudentId;
    private String hold_exam_id,hold_test_status;
    private User user;
    private ProgressDialog progressDialog;


    private JSONObject jsonObject;
    private JSONArray jsonArray;


    public ExamQuestionAdapter(Context context, ArrayList<ExamTable> quesList) {
        this.context = context;
        this.quesList = quesList;

        inflater = LayoutInflater.from(context);

        user = new User(context);
        holdStudentId = user.getId();




    }

    @Override
    public int getCount() {
        return quesList.size();
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);


    }


    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_UNCHANGED;

    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {


        quesList= (ArrayList<ExamTable>) ExamQuestionActivity.examDatabase.examSetDeo().retriveExamListSet();


        inflater =(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.content_exam_question,container,false);

        tv_ques_no = view.findViewById(R.id.tv_question_no);
       final ExpandableTextView tv_question = view.findViewById(R.id.tv_expandable_question);
       final EditText et_answer = view.findViewById(R.id.et_answar);
        btn_read_more = view.findViewById(R.id.btn_read_more);


        ExamTable examTable = quesList.get(position);

        int no = position + 1;
        tv_ques_no.setText("Q. "+no);

        tv_question.setText(quesList.get(position).getQuestion());
        et_answer.setText(quesList.get(position).getAnswer());

        hold_exam_id = quesList.get(position).getExam_id();





        et_answer.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {

                answer_text = mEdit.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        //Button Save & Next

        ExamQuestionActivity.btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int question_answer_position=ExamQuestionActivity.viewPager.getCurrentItem();

                String question_id=quesList.get(question_answer_position).getQuestion_id();


                    ExamTable examTable = new ExamTable();
                    examTable.setAnswer(answer_text);
                    examTable.setQuestion_id(question_id);
                    examTable.setQuestion(quesList.get(question_answer_position).getQuestion());
                    examTable.setExam_id(hold_exam_id);
                    examTable.setStudent_id("1");

                    ExamQuestionActivity.examDatabase.examSetDeo().updateExamList(examTable);



                if (ExamQuestionActivity.btn_next.getText().toString().trim().equals("Finish"))
                {


                    ExamTable examTable1 = new ExamTable();
                    examTable1.setAnswer(answer_text);
                    examTable1.setQuestion_id(question_id);
                    examTable1.setQuestion(quesList.get(question_answer_position).getQuestion());
                    examTable1.setExam_id(hold_exam_id);
                    examTable1.setStudent_id("1");

                    ExamQuestionActivity.examDatabase.examSetDeo().updateExamList(examTable);

                    quesList= (ArrayList<ExamTable>) ExamQuestionActivity.examDatabase.examSetDeo().retriveExamListSet();


                    arrayToJson();



                    //context.startActivity(new Intent(context, ExaminationActivity.class));
                }
                else
                {
                    ExamQuestionActivity.viewPager.setCurrentItem(ExamQuestionActivity.viewPager.getCurrentItem() + 1, true);

                }

            }
        });


        //Button Privious

        ExamQuestionActivity.btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExamQuestionActivity.viewPager.setCurrentItem(ExamQuestionActivity.viewPager.getCurrentItem() - 1, true);

            }
        });




        //Viewpager addOnPageChangeListener

        ExamQuestionActivity.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0)
                {
                    ExamQuestionActivity.btn_prev.setVisibility(View.GONE);
                }
                else
                {
                    ExamQuestionActivity.btn_prev.setVisibility(View.VISIBLE);

                }


                if (position + 1 == quesList.size())
                {
                    ExamQuestionActivity.btn_next.setText("Finish");
                }
                else
                {
                    ExamQuestionActivity.btn_next.setText("Next");

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        ((ViewPager) container).addView(view);

        return view;

    }







    private void arrayToJson()
    {
        jsonObject = new JSONObject();
        jsonArray = new JSONArray();

        for (int i=0; i<quesList.size(); i++)
        {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("question_id",quesList.get(i).getQuestion_id());
                jsonObject1.put("answer",quesList.get(i).getAnswer());

                jsonArray.put(jsonObject1);

            }


            catch (JSONException e) {
                e.printStackTrace();
            }


        }


        try {

            jsonObject.put("st_exam_id",Const.st_exam_id);
            jsonObject.put("exam_id",Const.exam_id);

            jsonObject.put("ques_ans_array",jsonArray);

            submitAnswerAPI();

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }



    private void submitAnswerAPI()
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wati..");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.BASE_SERVER + "final_save_ans", jsonObject,


                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));

                            String status = object.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {
                                Toast.makeText(context,"Saved Successfully", Toast.LENGTH_LONG).show();
                                ExamQuestionActivity.examDatabase.examSetDeo().nukeTable();

                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(context, "Answer not saved", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }


        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json; charset=utf-8");

                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,

                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);



    }



}
