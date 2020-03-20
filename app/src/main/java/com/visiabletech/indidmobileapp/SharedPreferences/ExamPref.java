package com.visiabletech.indidmobileapp.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ExamPref {



    private String exam_id,st_exam_id,username;

    private Context context;

    public SharedPreferences sharedPreferences;


    public ExamPref(Context context) {

        this.context=context;

        sharedPreferences=context.getSharedPreferences("exam_id_details", Context.MODE_PRIVATE);

    }




    public String getExam_id() {

        exam_id = sharedPreferences.getString("exam_id","");
        return exam_id;
    }

    public void setExam_id(String exam_id) {

        this.exam_id = exam_id;
        sharedPreferences.edit().putString("exam_id",exam_id).commit();

    }

    public String getSt_exam_id() {

        st_exam_id = sharedPreferences.getString("st_exam_id","");
        return st_exam_id;
    }

    public void setSt_exam_id(String st_exam_id) {

        this.st_exam_id = st_exam_id;
        sharedPreferences.edit().putString("st_exam_id",st_exam_id).commit();
    }

    public  void  remove(){


        sharedPreferences.edit().clear().commit();

    }

}
