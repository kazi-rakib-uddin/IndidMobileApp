package com.visiabletech.indidmobileapp.Adapter;


import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visiabletech.indidmobileapp.Pogo.BulletinInfo;
import com.visiabletech.indidmobileapp.Pogo.ResultDetailsInfo;
import com.visiabletech.indidmobileapp.R;
import com.visiabletech.indidmobileapp.ResultActivity;
import com.visiabletech.indidmobileapp.ResultDetailsActivity;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultDetailsAdapter extends RecyclerView.Adapter<ResultDetailsAdapter.ViewHolder> {


    private Context context;
    private ArrayList<ResultDetailsInfo> mList;

    private long queueId;
    private DownloadManager dm;
    private String hold_answar_key="";
    private BroadcastReceiver receiver;


    public ResultDetailsAdapter(Context context, ArrayList<ResultDetailsInfo> list) {
        this.context = context;
        this.mList=list;

        broadcastReciver();
        context.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.content_result_details,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ViewHolder viewHolder=(ViewHolder)holder;



        int rowPos=viewHolder.getAdapterPosition();

        if (rowPos==0)
        {
            // Header Cells. Main Headings appear here
            holder.subject_name.setBackgroundResource(R.drawable.table_header_cell_bg);
            holder.subject_total_marks.setBackgroundResource(R.drawable.table_header_cell_bg);
            holder.subject_obtained_marks.setBackgroundResource(R.drawable.table_header_cell_bg);
            holder.subject_percentage.setBackgroundResource(R.drawable.table_header_cell_bg);

            holder.subject_name.setText("Subject Name");
            holder.subject_total_marks.setText("Total Marks");
            holder.subject_obtained_marks.setText("Marks Obtained");
            holder.subject_percentage.setText("Precentage");

        }
        else
        {

            ResultDetailsInfo detailsInfo=mList.get(rowPos-1);

            holder.subject_name.setText(detailsInfo.getSubject_name());
            holder.subject_total_marks.setText(detailsInfo.getSubject_total_marks());
            holder.subject_obtained_marks.setText(detailsInfo.getSubject_obtained_marks());
            holder.subject_percentage.setText(detailsInfo.getSubject_percentage()+"%");

            hold_answar_key = detailsInfo.getAnswar_key();

        }



        //Download Answar key pdf file

        ResultDetailsActivity.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hold_answar_key.isEmpty()) {

                    dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(hold_answar_key));
                    queueId = dm.enqueue(request);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    Toast.makeText(context, "Download Start..", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(context, "Empty File", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }



    @Override
    public int getItemCount() {

        return mList.size()+1; // one more to add header row
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView subject_name,subject_total_marks,subject_obtained_marks,subject_percentage;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            subject_name=itemView.findViewById(R.id.tv_subject_name);
            subject_total_marks=itemView.findViewById(R.id.tv_subject_total_marks);
            subject_obtained_marks=itemView.findViewById(R.id.tv_subject_obtained_marks);
            subject_percentage=itemView.findViewById(R.id.tv_subject_percentage);


        }


    }

    private void broadcastReciver()
    {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action))
                {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(queueId);

                    Cursor c = dm.query(query);

                    if (c.moveToFirst())
                    {
                        int column_index = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(column_index))
                        {
                            Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();

                        }

                    }
                }

            }
        };
    }


}
