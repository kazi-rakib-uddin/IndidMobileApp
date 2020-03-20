package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class StudyInfo {

    Context context;
    String subject_id,subject_name,subject_topic,total_ebook,total_videos;
    private boolean expanded;


    public StudyInfo(Context context, String subject_id, String subject_name, String subject_topic, String total_ebook, String total_videos) {
        this.context = context;
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.subject_topic = subject_topic;
        this.total_ebook = total_ebook;
        this.total_videos = total_videos;

        this.expanded = false;
    }


    public Context getContext() {
        return context;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getSubject_topic() {
        return subject_topic;
    }

    public String getTotal_ebook() {
        return total_ebook;
    }

    public String getTotal_videos() {
        return total_videos;
    }


    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
