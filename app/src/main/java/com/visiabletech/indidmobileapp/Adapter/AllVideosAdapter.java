package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.visiabletech.indidmobileapp.PlayVideoActivity;
import com.visiabletech.indidmobileapp.Pogo.BulletinInfo;
import com.visiabletech.indidmobileapp.Pogo.VideosInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.YoutubeConfig.YoutubeConfig;

import java.util.ArrayList;

public class AllVideosAdapter extends RecyclerView.Adapter<AllVideosAdapter.ViewHolder> {


    private Context context;
    private ArrayList<VideosInfo> mList;

    public AllVideosAdapter(Context context, ArrayList<VideosInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.all_videos,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final VideosInfo videosInfo=mList.get(position);

        holder.youTubeThumbnailView.initialize(YoutubeConfig.getApiKey(), new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videosInfo.getVideo_url());

            }




            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }

        });



        holder.video_title.setText(videosInfo.getVideo_title());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b=new Bundle();
                b.putString("url",videosInfo.getVideo_url());

               context.startActivity(new Intent(context, PlayVideoActivity.class).putExtras(b));

            }
        });



    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView video_title;
        YouTubeThumbnailView youTubeThumbnailView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            video_title=(TextView)itemView.findViewById(R.id.video_title);
            youTubeThumbnailView=itemView.findViewById(R.id.youtube_thumbnail);

            cardView=itemView.findViewById(R.id.cardView);


        }
    }
}
