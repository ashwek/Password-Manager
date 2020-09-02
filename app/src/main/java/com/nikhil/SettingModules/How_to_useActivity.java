package com.nikhil.SettingModules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nikhil.EnPass.R;

public class How_to_useActivity extends AppCompatActivity {
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private ImageView nextbtn;
    private ImageView prevbtn;
    int i;
    private int mCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        mDotLayout = findViewById(R.id.mDotLayout);
        prevbtn = findViewById(R.id.prevbtn);
        nextbtn = findViewById(R.id.nextbtn);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        Imageadapter imageadapter = new Imageadapter(this);
        viewPager.setAdapter(imageadapter);
        viewPager.addOnPageChangeListener(viewlistener);
        addDotsIndicator(0);

        nextbtn.setOnClickListener(v -> viewPager.setCurrentItem(mCurrentPage + 1));

        prevbtn.setOnClickListener(v -> viewPager.setCurrentItem(mCurrentPage - 1));
    }


    public void addDotsIndicator(int position){
        mDots = new TextView[8];
        mDotLayout.removeAllViews();
        for(int i =0; i<mDots.length;i++){
            mDots[i] =new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.checkdarktblack));
            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage = position;

            if(position == 0){
                nextbtn.setEnabled(true);
                prevbtn.setEnabled(false);
                prevbtn.setVisibility(View.INVISIBLE);
                nextbtn.setVisibility(View.VISIBLE);
            }
            else if(position == mDots.length-1){
                nextbtn.setEnabled(false);
                prevbtn.setEnabled(true);
                nextbtn.setVisibility(View.INVISIBLE);
                prevbtn.setVisibility(View.VISIBLE);
            }
            else{
                nextbtn.setEnabled(true);
                prevbtn.setEnabled(true);
                prevbtn.setVisibility(View.VISIBLE);
                prevbtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
