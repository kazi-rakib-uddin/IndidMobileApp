package com.visiabletech.indidmobileapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.PlayVideoActivity;
import com.visiabletech.indidmobileapp.Pogo.VideosInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.ArrayList;

import vimeoextractor.OnVimeoExtractionListener;
import vimeoextractor.VimeoExtractor;
import vimeoextractor.VimeoVideo;


public class AllVimeoVideosAdapter extends RecyclerView.Adapter<AllVimeoVideosAdapter.ViewHolder> {


    private Context context;
    private ArrayList<VideosInfo> mList;
    private String hdStream, vimeo_video_url;

    public AllVimeoVideosAdapter(Context context, ArrayList<VideosInfo> list) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final VideosInfo videosInfo=mList.get(position);

        VimeoExtractor.getInstance().fetchVideoWithURL("https://vimeo.com/76979871", null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                 hdStream = video.getStreams().get("1080p");
                if (hdStream != null) {
//                    playVideo(hdStream);
                    final MediaController mediacontroller = new MediaController(context);
                    mediacontroller.setAnchorView(holder.video_view);
                    holder.video_view.setMediaController(mediacontroller);

                    holder.video_view.setBackgroundColor(Color.TRANSPARENT);
                    Uri video_uri = Uri.parse(hdStream);
                    holder.video_view.setVideoURI(video_uri);
                    holder.video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            holder.video_view.requestFocus();
                            holder.video_view.start();
                        }
                    });

                    holder.video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                            System.out.println("Video Finish");
//                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //Error handling here
            }
        });



        holder.video_title.setText(videosInfo.getVideo_title());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vimeo_video_url="https://vimeo.com/76979871";
                final MediaController mediacontroller = new MediaController(context);
                mediacontroller.setAnchorView(holder.video_view);
                holder.video_view.setVideoURI(Uri.parse(vimeo_video_url));
                holder.video_view.setMediaController(null);
                holder.video_view.requestFocus();
                holder.video_view.start();
                /*Bundle b=new Bundle();
                b.putString("url",videosInfo.getVideo_url());

                context.startActivity(new Intent(context, PlayVideoActivity.class).putExtras(b));*/

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
        CardView cardView;
//        VimeoPlayer vimeoPlayer;
        VideoView video_view;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            video_title=(TextView)itemView.findViewById(R.id.video_title);
            video_view=itemView.findViewById(R.id.video_view);

            cardView=itemView.findViewById(R.id.cardView);


        }
    }

}
