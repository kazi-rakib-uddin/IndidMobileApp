package com.visiabletech.indidmobileapp.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.ColorGenerator.ColorGenerator;
import com.visiabletech.indidmobileapp.ColorGenerator.TextDrawable;
import com.visiabletech.indidmobileapp.Pogo.StudyInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.StudyChapterActivity;

import java.util.ArrayList;

public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.ViewHolder> {


    private Context context;
    private ArrayList<StudyInfo> mList;

    private String subject_id;

    public StudyAdapter(Context context, ArrayList<StudyInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_study,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        StudyInfo studyInfo=mList.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(position);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(studyInfo.getSubject_name().charAt(0)), color);
        holder.study_image.setImageDrawable(drawable);


        holder.tv_study_subject.setText(studyInfo.getSubject_name());
        holder.tv_chapter.setText(studyInfo.getSubject_topic()+" Chapters");
        holder.tv_ebook.setText(studyInfo.getTotal_ebook()+" E-book");
        holder.tv_videos.setText(studyInfo.getTotal_videos()+" Videos");




        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString("subject_id",mList.get(position).getSubject_id());
                context.startActivity(new Intent(context, StudyChapterActivity.class).putExtras(b));
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
        TextView tv_study_subject,tv_chapter,tv_ebook,tv_videos;
        ImageView study_image;
        CardView cardView;
        LinearLayout expandable_lin;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_study_subject = itemView.findViewById(R.id.study_subject);
            tv_chapter = itemView.findViewById(R.id.tv_chapter);
            tv_ebook = itemView.findViewById(R.id.tv_ebook);
            tv_videos = itemView.findViewById(R.id.tv_videos);
            study_image = itemView.findViewById(R.id.study_image);
            cardView = itemView.findViewById(R.id.card_view);




        }
    }
}
