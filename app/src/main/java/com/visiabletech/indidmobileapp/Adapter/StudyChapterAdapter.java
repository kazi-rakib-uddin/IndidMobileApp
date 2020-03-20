package com.visiabletech.indidmobileapp.Adapter;


import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visiabletech.indidmobileapp.BaseURLs.Constants;
import com.visiabletech.indidmobileapp.ColorGenerator.ColorGenerator;
import com.visiabletech.indidmobileapp.ColorGenerator.TextDrawable;
import com.visiabletech.indidmobileapp.Pogo.StudyChapterInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.VideosActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudyChapterAdapter extends RecyclerView.Adapter<StudyChapterAdapter.ViewHolder> implements View.OnClickListener {


    private Context context;
    private ArrayList<StudyChapterInfo> mList;
    private int lastExpandedPosition = -1;
    private String hold_subject_id,hold_chapter_id;
    private StringRequest stringRequest_videos;
    private RequestQueue requestQueue_videos;

    public StudyChapterAdapter(Context context, ArrayList<StudyChapterInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_study_syllabus,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        //Expandable
        viewHolder.expandable_lin.setOnClickListener(StudyChapterAdapter.this);
        viewHolder.expandable_lin.setTag(viewHolder);
        //Exd Expandable

        return viewHolder;

    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        StudyChapterInfo syllabusInfo = mList.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        final int color = generator.getColor(position);

        final int pos = position+1;

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(pos), color);
        holder.iv_syllabus.setImageDrawable(drawable);


        holder.tv_chapter_name.setText(syllabusInfo.getChapter_name());

        hold_subject_id = mList.get(position).getSubject_id();
        hold_chapter_id = mList.get(position).getChapter_id();

        boolean isExpandes =mList.get(position).isExpanded();


        if (isExpandes)
        {
            holder.expandable_rel.setVisibility(View.VISIBLE);
            holder.iv_arrow_up.setVisibility(View.VISIBLE);
            holder.iv_arrow_down.setVisibility(View.INVISIBLE);

        }
        else
        {
            holder.expandable_rel.setVisibility(View.GONE);
            holder.iv_arrow_up.setVisibility(View.INVISIBLE);
            holder.iv_arrow_down.setVisibility(View.VISIBLE);

        }

        //holder.expandable_rel.setVisibility(isExpandes ? View.VISIBLE : View.GONE);


        holder.lin_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putString("subject_id",hold_subject_id);
                b.putString("chapter_id",hold_chapter_id);

                context.startActivity(new Intent(context, VideosActivity.class));
                ((Activity)context).finish();

            }
        });



    }



    @Override
    public int getItemCount() {

        return mList.size();
    }


    //Expandable Layout OnClick
    @Override
    public void onClick(View v) {

        ViewHolder holder = (ViewHolder) v.getTag();

        if (mList.get(holder.getPosition()).isExpanded())
        {
            mList.get(holder.getPosition()).setExpanded(false);
            notifyDataSetChanged();
        }
        else
        {
            for (int i=0; i<mList.size(); i++)
            {
                if (mList.get(i).isExpanded())
                {
                    mList.get(i).setExpanded(false);
                }
            }

            mList.get(holder.getPosition()).setExpanded(true);
            notifyDataSetChanged();
        }

    }

    //End Expandable OnClick

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_chapter_name;
        ImageView iv_syllabus,iv_arrow_down,iv_arrow_up;
        CardView cardView;
        LinearLayout expandable_lin,lin_video,lin_ebook,lin_exam;
        RelativeLayout expandable_rel;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_chapter_name = itemView.findViewById(R.id.tv_chapter_name);
            iv_syllabus = itemView.findViewById(R.id.iv_syllabus);
            cardView = itemView.findViewById(R.id.card_view);
            expandable_lin = itemView.findViewById(R.id.expandable_lin);
            expandable_rel = itemView.findViewById(R.id.expandable_rel);
            iv_arrow_down = itemView.findViewById(R.id.arrow_down);
            iv_arrow_up = itemView.findViewById(R.id.arrow_up);
            lin_video = itemView.findViewById(R.id.lin_video);
            lin_ebook = itemView.findViewById(R.id.lin_ebook);
            lin_exam = itemView.findViewById(R.id.lin_exam);



        }


    }

    private void allVideos()
    {
        requestQueue_videos = Volley.newRequestQueue(context);
        stringRequest_videos = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "study_video",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



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
                Map<String,String> myParams = new HashMap<>();
                myParams.put("subject_id",hold_subject_id);
                myParams.put("chapter_id","");

                return myParams;
            }
        };

        requestQueue_videos.add(stringRequest_videos);

    }


}
