package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class CustomSpinnerClassItem {

    private Context context;
    private String spinnerItemName;

    public CustomSpinnerClassItem(Context context, String spinnerItemName) {
        this.context = context;
        this.spinnerItemName = spinnerItemName;
    }

    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public void setSpinnerItemName(String spinnerItemName) {
        this.spinnerItemName = spinnerItemName;
    }
}
