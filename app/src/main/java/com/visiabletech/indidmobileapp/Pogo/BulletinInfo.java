package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class BulletinInfo {

    Context context;
    String title,date,body,postBy;

    public BulletinInfo(Context context, String title, String body, String date, String postBy) {
        this.context = context;
        this.title = title;
        this.body = body;
        this.date = date;
        this.postBy = postBy;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostBy() {
        return postBy;
    }

    public void setPostBy(String postBy) {
        this.postBy = postBy;
    }

}
