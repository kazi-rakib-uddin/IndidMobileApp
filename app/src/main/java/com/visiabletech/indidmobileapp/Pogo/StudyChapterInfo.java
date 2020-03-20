package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class StudyChapterInfo {

    Context context;
    String chapter_id,chapter_name,subject_id;
    private boolean expanded;


    public StudyChapterInfo(Context context, String chapter_id, String chapter_name,String subject_id) {
        this.context = context;
        this.chapter_id = chapter_id;
        this.chapter_name = chapter_name;
        this.subject_id = subject_id;

        this.expanded = false;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getChapter_id() {
        return chapter_id;
    }



    public String getChapter_name() {
        return chapter_name;
    }


    public String getSubject_id() {
        return subject_id;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


}
