package com.visiabletech.indidmobileapp.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.visiabletech.indidmobileapp.ExamQuestionActivity;
import com.visiabletech.indidmobileapp.PlayVideoActivity;
import com.visiabletech.indidmobileapp.Pogo.ExamListInfo;
import com.visiabletech.indidmobileapp.Pogo.VideosInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.YoutubeConfig.YoutubeConfig;

import java.util.ArrayList;

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.ViewHolder> {


    private Context context;
    private ArrayList<ExamListInfo> mList;
    private String test_taken_status;

    public ExamListAdapter(Context context, ArrayList<ExamListInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_exam_list,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ExamListInfo examListInfo=mList.get(position);

        holder.tv_exam_name.setText(examListInfo.getExam_name());
        holder.tv_exam_subject.setText(examListInfo.getExam_subject());
        holder.tv_exam_mins.setText(examListInfo.getExam_mins()+" mins");
        holder.tv_exam_tot_question.setText(examListInfo.getExam_question()+" Question");

        test_taken_status = examListInfo.getTest_taken_status();
        final String examId=examListInfo.getId();


        /*if (test_taken_status.equals("1"))
        {
            holder.btn_start_test.setVisibility(View.INVISIBLE);
            holder.btn_view_solution.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btn_start_test.setVisibility(View.VISIBLE);
            holder.btn_view_solution.setVisibility(View.INVISIBLE);
        }*/


        holder.btn_start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString("exam_id",examId);
                b.putString("test_status",test_taken_status);
               context.startActivity(new Intent(context, ExamQuestionActivity.class).putExtras(b));
                ((Activity)context).finish();

            }
        });


    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_exam_name,tv_exam_subject,tv_exam_mins,tv_exam_tot_question;
        CardView cardView;
        Button btn_start_test,btn_view_solution;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_exam_name=(TextView)itemView.findViewById(R.id.tv_exam_name);
            tv_exam_subject=(TextView)itemView.findViewById(R.id.tv_exam_subject);
            tv_exam_mins=(TextView)itemView.findViewById(R.id.tv_exam_mins);
            tv_exam_tot_question=(TextView)itemView.findViewById(R.id.tv_exam_question);

            btn_start_test=itemView.findViewById(R.id.btn_start_test);
            btn_view_solution=itemView.findViewById(R.id.btn_view_solution);

            cardView=itemView.findViewById(R.id.cardView);


        }
    }
}
