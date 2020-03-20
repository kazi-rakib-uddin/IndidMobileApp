package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class NoticeInfo {

    Context context;

    String noticeSubject,notice,noticeDate;


    public NoticeInfo(Context context, String notice_subject, String notice_date, String notice) {
        this.context = context;
        this.noticeSubject = notice_subject;
        this.notice = notice;
        this.noticeDate = notice_date;
    }

    public String getNoticeSubject() {
        return noticeSubject;
    }

    public void setNoticeSubject(String notice_subject) {
        this.noticeSubject = notice_subject;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String notice_date) {
        this.noticeDate = notice_date;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
