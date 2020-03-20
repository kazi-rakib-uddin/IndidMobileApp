package com.visiabletech.indidmobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.visiabletech.indidmobileapp.Pogo.CustomSpinnerClassItem;
import com.visiabletech.indidmobileapp.R;

import java.util.ArrayList;

public class CustomSpinnerClassAdapter extends ArrayAdapter<CustomSpinnerClassItem> {
    public CustomSpinnerClassAdapter(@NonNull Context context, ArrayList<CustomSpinnerClassItem> customSpinnerClassItems) {
        super(context, 0, customSpinnerClassItems);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
        {

            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout,parent,false);
        }

        CustomSpinnerClassItem item=getItem(position);

        TextView spinnerTV= convertView.findViewById(R.id.tv_spinner_layout);

        if (item !=null) {
            spinnerTV.setText(item.getSpinnerItemName());
        }

        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView==null)
        {

            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);
        }

        CustomSpinnerClassItem item=getItem(position);

        TextView dropDawnTV= convertView.findViewById(R.id.tv_drop_dawn);

        if (item !=null) {
            dropDawnTV.setText(item.getSpinnerItemName());
        }

        return convertView;
    }
}
