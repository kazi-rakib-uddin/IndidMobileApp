package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.MockTestInfo;
import com.visiabletech.indidmobileapp.Pogo.NoticeInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.ArrayList;

public class MockTestAdapter extends RecyclerView.Adapter<MockTestAdapter.ViewHolder> {


    private Context context;
    private ArrayList<MockTestInfo> mList;

    public MockTestAdapter(Context context, ArrayList<MockTestInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_mock_test,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MockTestInfo mockTestInfo=mList.get(position);

        int no = position + 1;

        holder.tv_number.setText(String.valueOf(no)+" )");

        holder.tv_mock_test.setText(mockTestInfo.getMockTest());


        String subject = holder.tv_mock_test.getText().toString();

        if (position % 2==0)
        {
            holder.cardView.setBackgroundResource(R.color.skyColor);



        }
        else
        {
            holder.cardView.setBackgroundResource(R.color.sky);

        }
        /*if (subject.startsWith("A")){
            holder.cardView.setBackgroundResource(R.color.colorA);
        }else if (subject.startsWith("B")){
            holder.cardView.setBackgroundResource(R.color.colorB);
        }else if (subject.startsWith("C")){
            holder.cardView.setBackgroundResource(R.color.colorC);
        }else if (subject.startsWith("D")){
            holder.cardView.setBackgroundResource(R.color.colorD);
        }else if (subject.startsWith("E")){
            holder.cardView.setBackgroundResource(R.color.colorE);
        }else if (subject.startsWith("F")){
            holder.cardView.setBackgroundResource(R.color.colorF);
        }else if (subject.startsWith("G")){
            holder.cardView.setBackgroundResource(R.color.colorG);
        }else if (subject.startsWith("H")){
            holder.cardView.setBackgroundResource(R.color.colorH);
        }else if (subject.startsWith("I")){
            holder.cardView.setBackgroundResource(R.color.colorI);
        }else if (subject.startsWith("J")){
            holder.cardView.setBackgroundResource(R.color.colorJ);
        }else if (subject.startsWith("K")){
            holder.cardView.setBackgroundResource(R.color.colorK);
        }else if (subject.startsWith("L")){
            holder.cardView.setBackgroundResource(R.color.colorL);
        }else if (subject.startsWith("M")){
            holder.cardView.setBackgroundResource(R.color.colorM);
        }else if (subject.startsWith("N")){
            holder.cardView.setBackgroundResource(R.color.colorN);
        }else if (subject.startsWith("O")){
            holder.cardView.setBackgroundResource(R.color.colorO);
        }else if (subject.startsWith("P")){
            holder.cardView.setBackgroundResource(R.color.colorP);
        }else if (subject.startsWith("Q")){
            holder.cardView.setBackgroundResource(R.color.colorQ);
        }else if (subject.startsWith("R")){
            holder.cardView.setBackgroundResource(R.color.colorR);
        }else if (subject.startsWith("S")){
            holder.cardView.setBackgroundResource(R.color.colorS);
        }else if (subject.startsWith("T")){
            holder.cardView.setBackgroundResource(R.color.colorT);
        }else if (subject.startsWith("U")){
            holder.cardView.setBackgroundResource(R.color.colorU);
        }else if (subject.startsWith("V")){
            holder.cardView.setBackgroundResource(R.color.colorV);
        }else if (subject.startsWith("W")){
            holder.cardView.setBackgroundResource(R.color.colorW);
        }else if (subject.startsWith("X")){
            holder.cardView.setBackgroundResource(R.color.colorX);
        }else if (subject.startsWith("Y")){
            holder.cardView.setBackgroundResource(R.color.colorY);
        }else if (subject.startsWith("Z")){
            holder.cardView.setBackgroundResource(R.color.colorZ);
        }*/


    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_mock_test,tv_number;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_mock_test=(TextView)itemView.findViewById(R.id.tv_mock_test);
            tv_number=(TextView)itemView.findViewById(R.id.tv_number);
            cardView=(CardView)itemView.findViewById(R.id.cardView);



        }
    }
}
