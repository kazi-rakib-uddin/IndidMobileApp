package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.BulletinInfo;
import com.visiabletech.indidmobileapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.ViewHolder> {


    private Context context;
    private ArrayList<BulletinInfo> mList;

    public BulletinAdapter(Context context, ArrayList<BulletinInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.bulletin_adapter,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BulletinInfo bulletinInfo=mList.get(position);

        String strDate=parseDateToddMMyyyy(bulletinInfo.getDate());

        holder.tv_bulletins_title.setText(Html.fromHtml(bulletinInfo.getTitle()));
        holder.tv_bulletins_date.setText(strDate);
        holder.tv_bulletins_body.setText(Html.fromHtml(bulletinInfo.getBody()));
        holder.tv_bulletins_post_by.setText( "Posted by: " + Html.fromHtml(bulletinInfo.getPostBy()));

        /*holder.tv_bulletins.setText(Html.fromHtml("<h3><u>"+bulletinInfo.getNoticeSubject()+"</u></h3>" +
                "<p>"+bulletinInfo.getDate()+"<br>"+bulletinInfo.getNotice()+"</p><p>"+bulletinInfo.getPostBy()+"</p>"));*/



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
        TextView tv_bulletins_title, tv_bulletins_date, tv_bulletins_body, tv_bulletins_post_by;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_bulletins_title=(TextView)itemView.findViewById(R.id.tv_bulletins_title);
            tv_bulletins_date=(TextView)itemView.findViewById(R.id.tv_bulletins_date);
            tv_bulletins_body=(TextView)itemView.findViewById(R.id.tv_bulletins_body);
            tv_bulletins_post_by=(TextView)itemView.findViewById(R.id.tv_bulletins_post_by);


        }
    }
}
