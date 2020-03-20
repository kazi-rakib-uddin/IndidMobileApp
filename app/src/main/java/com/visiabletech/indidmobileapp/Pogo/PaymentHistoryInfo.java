package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class PaymentHistoryInfo {

    Context context;
    String trans_id,amount,date;


    public PaymentHistoryInfo(Context context, String trans_id, String amount, String date) {
        this.context = context;
        this.trans_id = trans_id;
        this.amount = amount;
        this.date = date;
    }


    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
