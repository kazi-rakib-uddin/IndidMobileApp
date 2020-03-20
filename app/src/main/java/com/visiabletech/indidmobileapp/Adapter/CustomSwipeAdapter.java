package com.visiabletech.indidmobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.visiabletech.indidmobileapp.MainPageActivity;
import com.visiabletech.indidmobileapp.R;

public class CustomSwipeAdapter extends PagerAdapter {

    private int[] IMAGE={R.drawable.photo,R.drawable.photo2,R.drawable.photo3,R.drawable.photo4};
    private String[] CONTENT ={"LEARNING UPDATED VIDEO LECTURES FROM EXPERT ",
                                "ONLINE TEST SERIES",
                                "E-BOOK",
            "STUDENT PERFORMANCE REPORTS"};



    private String[] SUB_CONTENT ={"Video classes where you can learn for free with short videos & live classes",
                                    "Students can test their ability by giving time to time test online",
                                    "E-content which is designed by our expert faculty",
                                    "Get precise reports that help you improve your performance, ranking, speed & accuracy"};


    private Context context;
    private LayoutInflater layoutInflater;
    Button btn_gotit;


    public CustomSwipeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return IMAGE.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=layoutInflater.inflate(R.layout.swipe_layout,container,false);

        ImageView imageView=view.findViewById(R.id.img_view);
        TextView tv_content = view.findViewById(R.id.tv_content);
        TextView tv_sub_content=view.findViewById(R.id.tv_sub_content);
        btn_gotit = view.findViewById(R.id.btn_gotit);

        imageView.setImageResource(IMAGE[position]);

        tv_content.setText(CONTENT[position]);
        tv_sub_content.setText(SUB_CONTENT[position]);

        btn_gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainPageActivity.dialog.dismiss();
            }
        });


        MainPageActivity.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position + 1 == MainPageActivity.dots.length)
                {
                    btn_gotit.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {


        container.removeView((LinearLayout)object);

    }
}
