package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visiabletech.indidmobileapp.Pogo.TimeTableInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.List;

public class TimeTableAdapter extends BaseAdapter {

    private Context context;
    private final List<TimeTableInfo> timeTaInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;


    public TimeTableAdapter(Context context, List<TimeTableInfo> timeTaInfoList, LayoutInflater inflater) {
        this.context = context;
        this.timeTaInfoList = timeTaInfoList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return timeTaInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return timeTaInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView;
        assert inflater != null;
        itemView = inflater.inflate(R.layout.time_table_adapter, parent, false);
        TextView textView_time = itemView.findViewById(R.id.textView_time);
        TextView textView_monday_sub = itemView.findViewById(R.id.textView_monday_sub);
        TextView textView_tuesday_sub = itemView.findViewById(R.id.textView_tuesday_sub);
        TextView textView_wednesday_sub = itemView.findViewById(R.id.textView_wednesday_sub);
        TextView textView_thursday_sub = itemView.findViewById(R.id.textView_thursday_sub);
        TextView textView_friday_sub = itemView.findViewById(R.id.textView_friday_sub);
        TextView textView_saturday_sub = itemView.findViewById(R.id.textView_saturday_sub);


        final TimeTableInfo tTableInfo = timeTaInfoList.get(i);

        textView_monday_sub.setText(tTableInfo.subject1);
        textView_tuesday_sub.setText(tTableInfo.subject2);
        textView_wednesday_sub.setText(tTableInfo.subject3);
        textView_thursday_sub.setText(tTableInfo.subject4);
        textView_friday_sub.setText(tTableInfo.subject5);
        textView_saturday_sub.setText(tTableInfo.subject6);
        textView_time.setText(tTableInfo.time);



        String subject = textView_monday_sub.getText().toString();
        if (subject.startsWith("A")){
            itemView.setBackgroundResource(R.color.colorA);
        }else if (subject.startsWith("B")){
            itemView.setBackgroundResource(R.color.colorB);
        }else if (subject.startsWith("C")){
            itemView.setBackgroundResource(R.color.colorC);
        }else if (subject.startsWith("D")){
            itemView.setBackgroundResource(R.color.colorD);
        }else if (subject.startsWith("E")){
            itemView.setBackgroundResource(R.color.colorE);
        }else if (subject.startsWith("F")){
            itemView.setBackgroundResource(R.color.colorF);
        }else if (subject.startsWith("G")){
            itemView.setBackgroundResource(R.color.colorG);
        }else if (subject.startsWith("H")){
            itemView.setBackgroundResource(R.color.colorH);
        }else if (subject.startsWith("I")){
            itemView.setBackgroundResource(R.color.colorI);
        }else if (subject.startsWith("J")){
            itemView.setBackgroundResource(R.color.colorJ);
        }else if (subject.startsWith("K")){
            itemView.setBackgroundResource(R.color.colorK);
        }else if (subject.startsWith("L")){
            itemView.setBackgroundResource(R.color.colorL);
        }else if (subject.startsWith("M")){
            itemView.setBackgroundResource(R.color.colorM);
        }else if (subject.startsWith("N")){
            itemView.setBackgroundResource(R.color.colorN);
        }else if (subject.startsWith("O")){
            itemView.setBackgroundResource(R.color.colorO);
        }else if (subject.startsWith("P")){
            itemView.setBackgroundResource(R.color.colorP);
        }else if (subject.startsWith("Q")){
            itemView.setBackgroundResource(R.color.colorQ);
        }else if (subject.startsWith("R")){
            itemView.setBackgroundResource(R.color.colorR);
        }else if (subject.startsWith("S")){
            itemView.setBackgroundResource(R.color.colorS);
        }else if (subject.startsWith("T")){
            itemView.setBackgroundResource(R.color.colorT);
        }else if (subject.startsWith("U")){
            itemView.setBackgroundResource(R.color.colorU);
        }else if (subject.startsWith("V")){
            itemView.setBackgroundResource(R.color.colorV);
        }else if (subject.startsWith("W")){
            itemView.setBackgroundResource(R.color.colorW);
        }else if (subject.startsWith("X")){
            itemView.setBackgroundResource(R.color.colorX);
        }else if (subject.startsWith("Y")){
            itemView.setBackgroundResource(R.color.colorY);
        }else if (subject.startsWith("Z")){
            itemView.setBackgroundResource(R.color.colorZ);
        }


        return itemView;
    }
}
