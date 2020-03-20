package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visiabletech.indidmobileapp.Pogo.SpecialClassInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.List;

public class SpecialClassAdapter extends BaseAdapter {

    private Context context;
    private  List<SpecialClassInfo> specialClassInfoList;
    LayoutInflater mInflater;
    LayoutInflater inflater;

    public SpecialClassAdapter(Context context, List<SpecialClassInfo> specialClassInfoList, LayoutInflater inflater) {
        this.context = context;
        this.specialClassInfoList = specialClassInfoList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return specialClassInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return specialClassInfoList.get(i);
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
        itemView = inflater.inflate(R.layout.special_class_adapter, parent, false);
        TextView textView = itemView.findViewById(R.id.textView);

        final SpecialClassInfo specialClassInfo = specialClassInfoList.get(i);
        textView.setText("\u25A0 "+specialClassInfo.subject6 +" "+specialClassInfo.time);
        //textView.setTypeface(typeface);


        String subject6 = specialClassInfo.subject6.substring(0,1);

        if (subject6.equalsIgnoreCase("A")){
            textView.setTextColor(context.getResources().getColor(R.color.colorA));

        }else if (subject6.equalsIgnoreCase("B")){
            textView.setTextColor(context.getResources().getColor(R.color.colorB));

        }else if (subject6.equalsIgnoreCase("C")){
            textView.setTextColor(context.getResources().getColor(R.color.colorC));

        }else if (subject6.equalsIgnoreCase("D")){
            textView.setTextColor(context.getResources().getColor(R.color.colorD));

        }else if (subject6.equalsIgnoreCase("E")){
            textView.setTextColor(context.getResources().getColor(R.color.colorE));

        }else if (subject6.equalsIgnoreCase("F")){
            textView.setTextColor(context.getResources().getColor(R.color.colorF));

        }else if (subject6.equalsIgnoreCase("G")){
            textView.setTextColor(context.getResources().getColor(R.color.colorG));

        }else if (subject6.equalsIgnoreCase("H")){
            textView.setTextColor(context.getResources().getColor(R.color.colorH));

        }else if (subject6.equalsIgnoreCase("I")){
            textView.setTextColor(context.getResources().getColor(R.color.colorI));

        }else if (subject6.equalsIgnoreCase("J")){
            textView.setTextColor(context.getResources().getColor(R.color.colorJ));

        }else if (subject6.equalsIgnoreCase("K")){
            textView.setTextColor(context.getResources().getColor(R.color.colorK));

        }else if (subject6.equalsIgnoreCase("L")){
            textView.setTextColor(context.getResources().getColor(R.color.colorL));

        }else if (subject6.equalsIgnoreCase("M")){
            textView.setTextColor(context.getResources().getColor(R.color.colorM));

        }else if (subject6.equalsIgnoreCase("N")){
            textView.setTextColor(context.getResources().getColor(R.color.colorN));

        }else if (subject6.equalsIgnoreCase("O")){
            textView.setTextColor(context.getResources().getColor(R.color.colorO));

        }else if (subject6.equalsIgnoreCase("P")){
            textView.setTextColor(context.getResources().getColor(R.color.colorP));

        }else if (subject6.equalsIgnoreCase("Q")){
            textView.setTextColor(context.getResources().getColor(R.color.colorQ));

        }else if (subject6.equalsIgnoreCase("R")){
            textView.setTextColor(context.getResources().getColor(R.color.colorR));

        }else if (subject6.equalsIgnoreCase("S")){
            textView.setTextColor(context.getResources().getColor(R.color.colorS));

        }else if (subject6.equalsIgnoreCase("T")){
            textView.setTextColor(context.getResources().getColor(R.color.colorT));

        }else if (subject6.equalsIgnoreCase("U")){
            textView.setTextColor(context.getResources().getColor(R.color.colorU));

        }else if (subject6.equalsIgnoreCase("V")){
            textView.setTextColor(context.getResources().getColor(R.color.colorV));

        }else if (subject6.equalsIgnoreCase("W")){
            textView.setTextColor(context.getResources().getColor(R.color.colorW));

        }else if (subject6.equalsIgnoreCase("X")){
            textView.setTextColor(context.getResources().getColor(R.color.colorX));

        }else if (subject6.equalsIgnoreCase("Y")){
            textView.setTextColor(context.getResources().getColor(R.color.colorY));

        }else if (subject6.equalsIgnoreCase("Z")){
            textView.setTextColor(context.getResources().getColor(R.color.colorZ));

        }


        return itemView;
    }
}
