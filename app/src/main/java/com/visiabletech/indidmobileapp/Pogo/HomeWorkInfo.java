package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class HomeWorkInfo {

    String className,id,subject,topic,startDate,endDate;

    Context context;

    public HomeWorkInfo(Context context, String id, String className, String subject, String topic, String startDate, String endDate) {
        this.className = className;
        this.id = id;
        this.subject = subject;
        this.topic = topic;
        this.startDate = startDate;
        this.endDate = endDate;
        this.context=context;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {

        this.className = className;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
