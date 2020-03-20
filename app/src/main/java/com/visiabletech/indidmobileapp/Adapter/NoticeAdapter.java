package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.NoticeInfo;
import com.visiabletech.indidmobileapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {


    private Context context;
    private ArrayList<NoticeInfo> mList;

    public NoticeAdapter(Context context, ArrayList<NoticeInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.notice_adapter,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NoticeInfo noticeInfo=mList.get(position);

        String strDate=parseDateToddMMyyyy(noticeInfo.getNoticeDate());

//        holder.tv_notice_subject.setText(Html.fromHtml("<h2>" + noticeInfo.getNoticeSubject() + "</h2>" + noticeInfo.getNoticeDate() + "<br>" + noticeInfo.getNotice()));
        holder.tv_notice_subject.setText(Html.fromHtml(noticeInfo.getNoticeSubject()));

        holder.tv_notice_header.setText(Html.fromHtml("<u>" +noticeInfo.getNotice() + "</u>"));
//        holder.tv_notice_date.setText(Html.fromHtml(noticeInfo.getNoticeDate()));
        holder.tv_notice_date.setText(strDate);

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_notice_subject,tv_notice_header,tv_notice_date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_notice_subject=(TextView)itemView.findViewById(R.id.tv_notice_subject);
            tv_notice_header=itemView.findViewById(R.id.tv_notice_header);
            tv_notice_date=itemView.findViewById(R.id.tv_notice_date);



        }
    }
}
