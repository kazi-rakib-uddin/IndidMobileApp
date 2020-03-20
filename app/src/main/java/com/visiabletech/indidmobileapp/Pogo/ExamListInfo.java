package com.visiabletech.indidmobileapp.Pogo;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class ExamListInfo {

    Context context;
    String id,exam_name,exam_subject,exam_mins,exam_question,test_taken_status;

    public ExamListInfo(Context context, String id, String exam_name, String exam_subject, String exam_mins, String exam_question,String test_taken_status) {
        this.context = context;
        this.id = id;
        this.exam_name = exam_name;
        this.exam_subject = exam_subject;
        this.exam_mins = exam_mins;
        this.exam_question = exam_question;
        this.test_taken_status = test_taken_status;
    }


    public Context getContext() {
        return context;
    }

    public String getId() {
        return id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public String getExam_subject() {
        return exam_subject;
    }

    public String getExam_mins() {
        return exam_mins;
    }

    public String getExam_question() {
        return exam_question;
    }


    public String getTest_taken_status() {
        return test_taken_status;
    }
}
