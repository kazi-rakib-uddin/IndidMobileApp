package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.ClassWorkInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.ArrayList;

public class ClassWorkAdapter extends RecyclerView.Adapter<ClassWorkAdapter.ViewHolder> {


    private Context context;
    private ArrayList<ClassWorkInfo> mList;

    public ClassWorkAdapter(Context context, ArrayList<ClassWorkInfo> list) {
        this.context = context;
        this.mList=list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.class_work_adapter,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClassWorkInfo classWorkInfo=mList.get(position);

        holder.tv_class_name.setText(classWorkInfo.getClassName());
        holder.tv_subject.setText(Html.fromHtml("<h2><u>"+classWorkInfo.getSubject()+"</u></h2>"));
        holder.tv_work.setText(classWorkInfo.getWork());
        holder.tv_date.setText(classWorkInfo.getDate());


    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_class_name,tv_subject,tv_work,tv_date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_class_name=(TextView)itemView.findViewById(R.id.tv_class);
            tv_subject=(TextView)itemView.findViewById(R.id.tv_subject);
            tv_work=(TextView)itemView.findViewById(R.id.tv_work);
            tv_date=(TextView)itemView.findViewById(R.id.tv_date);


        }
    }
}
