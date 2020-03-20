package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.HomeWorkInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.ArrayList;

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkAdapter.ViewHolder> {


    private Context context;
    private ArrayList<HomeWorkInfo> mList;

    public HomeWorkAdapter(Context context, ArrayList<HomeWorkInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.home_work_adapter,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HomeWorkInfo homeWorkInfo=mList.get(position);

        holder.tv_subject.setText(Html.fromHtml("<h2><u>"+homeWorkInfo.getSubject()+"</u></h2>"));
        holder.tv_topic.setText(homeWorkInfo.getTopic());
        holder.tv_date_of_class.setText(homeWorkInfo.getStartDate() + "  To  " + homeWorkInfo.getEndDate());


    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_subject,tv_topic,tv_date_of_class,tv_complet_date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_subject=(TextView)itemView.findViewById(R.id.tv_subject);
            tv_topic=(TextView)itemView.findViewById(R.id.tv_topic);
            tv_date_of_class=(TextView)itemView.findViewById(R.id.tv_date_of_class);



        }
    }
}
