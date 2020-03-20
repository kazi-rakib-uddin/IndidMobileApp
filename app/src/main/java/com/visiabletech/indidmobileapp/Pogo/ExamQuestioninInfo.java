package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class ExamQuestioninInfo {

    private Context context;
    private String question_id,question,st_exam_id,exam_id;

    public ExamQuestioninInfo(Context context, String question_id, String question,
                              String st_exam_id,String exam_id)
    {
        this.context = context;
        this.question_id = question_id;
        this.question = question;
        this.st_exam_id = st_exam_id;
        this.exam_id = exam_id;

    }

    public Context getContext() {
        return context;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getSt_exam_id() {
        return st_exam_id;
    }

    public String getExam_id() {
        return exam_id;
    }


}
