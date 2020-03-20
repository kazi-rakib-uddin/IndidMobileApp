package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class EBookInfo {

    Context context;

     String pdf_name;
     String pdf_url;


    public EBookInfo(Context context, String pdf_name, String pdf_url) {
        this.context = context;
        this.pdf_name = pdf_name;
        this.pdf_url = pdf_url;
    }


    public Context getContext() {
        return context;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public String getPdf_url() {
        return pdf_url;
    }
}
