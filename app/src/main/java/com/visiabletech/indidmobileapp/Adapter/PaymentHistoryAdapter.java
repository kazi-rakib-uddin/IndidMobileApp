package com.visiabletech.indidmobileapp.Adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.BulletinInfo;
import com.visiabletech.indidmobileapp.Pogo.PaymentHistoryInfo;
import com.visiabletech.indidmobileapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {


    private Context context;
    private ArrayList<PaymentHistoryInfo> mList;

    public PaymentHistoryAdapter(Context context, ArrayList<PaymentHistoryInfo> list) {
        this.context = context;
        this.mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_payment_history,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PaymentHistoryInfo historyInfo=mList.get(position);

        String strDate=parseDateToddMMyyyy(historyInfo.getDate());

        holder.tv_trans_id.setText(historyInfo.getTrans_id());
        holder.tv_amount.setText(historyInfo.getAmount());
        holder.tv_date.setText(strDate);



    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_trans_id, tv_amount, tv_date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_trans_id = itemView.findViewById(R.id.tv_transaction_id);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_date = itemView.findViewById(R.id.tv_date);


        }
    }
}
