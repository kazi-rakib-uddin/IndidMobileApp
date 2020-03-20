package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class NotificationInfo {

    Context context;
    String notificationTitle,notificationId,activity;

    public NotificationInfo(Context context, String notificationTitle, String notificationId, String activity) {
        this.context = context;
        this.notificationTitle = notificationTitle;
        this.notificationId = notificationId;
        this.activity = activity;
    }


    public Context getContext() {
        return context;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getActivity() {
        return activity;
    }
}
