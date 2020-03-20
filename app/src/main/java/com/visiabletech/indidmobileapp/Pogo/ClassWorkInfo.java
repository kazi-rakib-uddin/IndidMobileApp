package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class ClassWorkInfo {

    String className,subject,work,date,id;
    Context context;

    public ClassWorkInfo(Context context, String id, String className, String subject, String work, String date) {

        this.context=context;

        this.className = className;
        this.subject = subject;
        this.work = work;
        this.date = date;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


}
