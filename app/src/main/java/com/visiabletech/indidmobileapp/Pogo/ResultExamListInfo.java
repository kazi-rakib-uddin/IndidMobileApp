package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class ResultExamListInfo {

    Context context;
    String exam_name,exam_date;


    public ResultExamListInfo(Context context, String exam_name, String exam_date) {
        this.context = context;
        this.exam_name = exam_name;
        this.exam_date = exam_date;
    }

    public Context getContext() {
        return context;
    }

    public String getExam_name() {
        return exam_name;
    }

    public String getExam_date() {
        return exam_date;
    }
}
