package com.visiabletech.indidmobileapp.Adapter;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visiabletech.indidmobileapp.Pogo.ExamListInfo;
import com.visiabletech.indidmobileapp.R;

import java.util.ArrayList;

/**
 * Created by hp on 5/22/2017.
 */
public class StudentExamAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ExamListInfo> examInfoArrayList;

    public StudentExamAdapter(Context context, ArrayList<ExamListInfo> examInfoArrayList, LayoutInflater layoutInflater){
        this.context = context;
        this.examInfoArrayList = examInfoArrayList;
    }
    @Override
    public int getCount() {
        return examInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return examInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.exam_adapter, parent, false);
        TextView textView = view.findViewById(R.id.textView);
        ExamListInfo examInfo = examInfoArrayList.get(position);
        //textView.setText(examInfo.name);
        return view;
    }
}
