package com.visiabletech.indidmobileapp.Adapter;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.visiabletech.indidmobileapp.PlayVideoActivity;
import com.visiabletech.indidmobileapp.Pogo.EBookInfo;
import com.visiabletech.indidmobileapp.Pogo.VideosInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.YoutubeConfig.YoutubeConfig;

import java.io.File;
import java.util.ArrayList;

public class EBookAdapter extends RecyclerView.Adapter<EBookAdapter.ViewHolder> {


    private Context context;
    private ArrayList<EBookInfo> mList;

    public EBookAdapter(Context context, ArrayList<EBookInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.all_ebook,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final EBookInfo eBookInfo=mList.get(position);

        holder.pdf_name.setText(eBookInfo.getPdf_name());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eBookInfo.getPdf_url()));
                context.startActivity(browserIntent);


            }
        });

    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView pdf_name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            pdf_name=itemView.findViewById(R.id.pdf_name);

            cardView=itemView.findViewById(R.id.card_view);


        }
    }
}
