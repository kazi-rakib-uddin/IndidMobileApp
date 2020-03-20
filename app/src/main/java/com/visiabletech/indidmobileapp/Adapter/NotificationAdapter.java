package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.EBookActivity;
import com.visiabletech.indidmobileapp.MainPageActivity;
import com.visiabletech.indidmobileapp.NoticeActivity;
import com.visiabletech.indidmobileapp.PlayVideoActivity;
import com.visiabletech.indidmobileapp.Pogo.NotificationInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.VideosActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private Context context;
    private ArrayList<NotificationInfo> mList;
    private String check_activity;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private String holdNoticeId;

    public NotificationAdapter(Context context, ArrayList<NotificationInfo> list) {
        this.context = context;
        this.mList=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_notification_list,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final NotificationInfo notificationInfo=mList.get(position);

        holder.tv_title_noti.setText(notificationInfo.getNotificationTitle());



        check_activity=notificationInfo.getActivity();

        if (check_activity.equals("NOTICEACTIVITY"))
        {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holdNoticeId=notificationInfo.getNotificationId();
                    notificationStatus(holdNoticeId);

                    context.startActivity(new Intent(context,NoticeActivity.class));


                }
            });
        }
        else if (check_activity.equals("VIDEOACTIVITY"))
        {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holdNoticeId=notificationInfo.getNotificationId();
                    notificationStatus(holdNoticeId);

                    context.startActivity(new Intent(context,VideosActivity.class));
                }
            });
        }
        else if (check_activity.equals("BOOKACTIVITY"))
        {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holdNoticeId=notificationInfo.getNotificationId();
                    notificationStatus(holdNoticeId);

                    context.startActivity(new Intent(context, EBookActivity.class));
                }
            });

        }




    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title_noti;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_title_noti=(TextView)itemView.findViewById(R.id.tv_title_noti);

            cardView=itemView.findViewById(R.id.cardView);


        }
    }


    private void notificationStatus(final String holdNoticeId)
    {
        requestQueue= Volley.newRequestQueue(context);
        stringRequest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_status",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status=jsonObject.getString("status");

                            if (status.equalsIgnoreCase("200"))
                            {

                               // Toast.makeText(context, "Notification updated", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<>();

                myParams.put("notice_id",holdNoticeId);

                return myParams;
            }
        };

        requestQueue.add(stringRequest);
    }



}
