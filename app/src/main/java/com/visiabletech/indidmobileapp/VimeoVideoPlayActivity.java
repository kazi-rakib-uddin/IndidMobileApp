package com.visiabletech.indidmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Picture;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;

import java.util.ArrayList;
import java.util.HashMap;

public class VimeoVideoPlayActivity extends AppCompatActivity {

    private static final String VIDEO = "https://vimeo.com/76979871";
    private static final String VIDEO_HASH_KEY = "9f380b1a11";

//Add a constant for the video file//

    private VideoView videoView;
    private VimeoPlayerView vimeoPlayer;
    private int currentPosition = 0;
    private int REQUEST_CODE = 1234;
    private PlayerState playerState;
    ArrayList<Picture> h;
    VimeoClient vimeoClient;
    private ImageView imageView;

    String key="9f380b1a11";
    String url="https://indid.in";

    String VIDEO_URL=url+"/"+key;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_of_vimeo_videos);


        ActivityCompat.requestPermissions(VimeoVideoPlayActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);



        vimeoPlayer = findViewById(R.id.vimeoPlayer);
        imageView=findViewById(R.id.img);
        getLifecycle().addObserver(vimeoPlayer);
//        vimeoPlayer.initialize(76979871);
//        vimeoPlayer.initialize(10679287);
        vimeoPlayer.initialize(351757325, "9f380b1a11", "https://indid.in");
        vimeoPlayer.setFullscreenVisibility(true);

        vimeoPlayer.setFullscreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(VimeoPlayerActivity.createIntent(VimeoVideoPlayActivity.this,
                        VimeoPlayerActivity.REQUEST_ORIENTATION_LANDSCAPE, vimeoPlayer), REQUEST_CODE);
            }
        });


        /*String vv ="https://www.youtube.com/watch?v=pan_9tKOEQ4";
        Bitmap bitmap;
        bitmap = ImageUtil.retriveVideoFrameFromVideo(vv);
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, false);
            imageView.setImageBitmap(bitmap);
        }*/



        /*int id =367334645;
        String vv ="https://www.youtube.com/watch?v=pan_9tKOEQ4";
        ContentResolver crThumb = getContentResolver();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;

        Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(vv, MediaStore.Images.Thumbnails.MICRO_KIND);

        //Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        imageView.setImageBitmap(bitmap);*/


       /* vimeoClient.fetchNetworkContent("https://player.vimeo.com/video/351757325/config", new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {
                // It's good practice to always make sure that the values the API sends us aren't null
                if (videoList != null && videoList.data != null) {
                    Toast.makeText(VimeoVideoPlayActivity.this, "Success", Toast.LENGTH_SHORT).show();                }
            }

            @Override
            public void failure(VimeoError error) {
                String errorMessage = error.getDeveloperMessage();
                Toast.makeText(VimeoVideoPlayActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });*/




       /* vimeoClient.fetchNetworkContent("https://vimeo.com/164864490", new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {

                if (videoList!=null && videoList.data!=null)
                {
                    ArrayList<Video> videoArrayList=videoList.data;
                    Video video = videoArrayList.get(0);
                    ArrayList<Picture> pictureArrayList=video.pictures.sizes;

                    //imageView.setImageBitmap( (Bitmap) pictureArrayList.get(0));
                    Glide.with(VimeoVideoPlayActivity.this).load(pictureArrayList.get(0)).into(imageView);
                }
                else
                {
                    Toast.makeText(VimeoVideoPlayActivity.this, "error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void failure(VimeoError error) {

            }
        });*/




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            float playAt=data.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f);
            vimeoPlayer.seekTo(playAt);
            playerState = PlayerState.valueOf(data.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE));
        if (playerState.equals(PlayerState.PLAYING))
            vimeoPlayer.play();
        else if (playerState.equals(PlayerState.PAUSED))
            vimeoPlayer.pause();
        }
    }




    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


}