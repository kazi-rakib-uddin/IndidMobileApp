package com.visiabletech.indidmobileapp.Pogo;

import android.content.Context;

public class VideosInfo {

    Context context;

     String video_title;
     String video_url;

    public VideosInfo(Context context, String video_title, String video_url) {
        this.context = context;
        this.video_title = video_title;
        this.video_url = video_url;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
