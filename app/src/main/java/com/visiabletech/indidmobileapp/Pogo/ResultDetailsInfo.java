package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class ResultDetailsInfo {

    Context context;
    String subject_name,subject_total_marks,subject_obtained_marks,subject_percentage,answar_key;


    public ResultDetailsInfo(Context context, String subject_name, String subject_total_marks,
                             String subject_obtained_marks, String subject_percentage,String answar_key) {
        this.context = context;
        this.subject_name = subject_name;
        this.subject_total_marks = subject_total_marks;
        this.subject_obtained_marks = subject_obtained_marks;
        this.subject_percentage = subject_percentage;
        this.answar_key = answar_key;
    }


    public Context getContext() {
        return context;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getSubject_total_marks() {
        return subject_total_marks;
    }

    public String getSubject_obtained_marks() {
        return subject_obtained_marks;
    }

    public String getSubject_percentage() {
        return subject_percentage;
    }

    public String getAnswar_key() {
        return answar_key;
    }
}
