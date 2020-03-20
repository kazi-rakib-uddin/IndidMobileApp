package com.visiabletech.indidmobileapp.FirebaseMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.visiabletech.indidmobileapp.EBookActivity;
import com.visiabletech.indidmobileapp.MainPageActivity;
import com.visiabletech.indidmobileapp.NoticeActivity;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.VideosActivity;

import java.util.List;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String msg_body,msg_title,click_action;
    int noti_count;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        msg_body= remoteMessage.getNotification().getBody();
        msg_title= remoteMessage.getNotification().getTitle();
        click_action=remoteMessage.getNotification().getClickAction();
        noti_count=Integer.parseInt(remoteMessage.getData().get("count"));

        //Notification badge count app icon
        if(remoteMessage.getData().size() > 0)
        {
            setBadge(getApplicationContext(),noti_count);
        }
        // End Notification badge count app icon

        showNotification(remoteMessage.getData().get("message"),msg_title,msg_body,click_action);

    }


    private void showNotification(String message,String msg_title,String msg_body,String click_action)
    {

        Intent intent;

        if (click_action.equals("NOTICEACTIVITY"))
        {
            intent=new Intent(this, NoticeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        }
        else if (click_action.equals("VIDEOACTIVITY"))
        {
            intent=new Intent(this, VideosActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if (click_action.equals("BOOKACTIVITY"))
        {
            intent=new Intent(this, EBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        else
        {
            intent=new Intent(this, MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,"Mynoti")
                .setAutoCancel(true)
                .setContentTitle(msg_title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg_body))
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);


        //NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,builder.build());

    }




    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            //Log.e("classname","null");
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }




    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }




}
