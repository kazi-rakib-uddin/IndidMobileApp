package com.visiabletech.indidmobileapp.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.visiabletech.indidmobileapp.PlayVideoActivity;
import com.visiabletech.indidmobileapp.Pogo.ResultExamListInfo;
import com.visiabletech.indidmobileapp.Pogo.VideosInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.ResultDetailsActivity;
import com.visiabletech.indidmobileapp.YoutubeConfig.YoutubeConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultExamListAdapter extends RecyclerView.Adapter<ResultExamListAdapter.ViewHolder> {


    private Context context;
    private ArrayList<ResultExamListInfo> mList;

    public ResultExamListAdapter(Context context, ArrayList<ResultExamListInfo> list) {
        this.context = context;
        this.mList=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_result_list,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ResultExamListInfo examListInfo=mList.get(position);

        String strDate=parseDateToddMMyyyy(examListInfo.getExam_date());

        holder.exam_name.setText(examListInfo.getExam_name());
        //holder.exam_date.setText(strDate);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, ResultDetailsActivity.class));
                ((Activity)context).finish();





            }
        });



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
        TextView exam_name,exam_date;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            cardView=itemView.findViewById(R.id.cardView);
            exam_name=itemView.findViewById(R.id.tv_exam_name);
           //exam_date=itemView.findViewById(R.id.tv_exam_date);


        }
    }
}
