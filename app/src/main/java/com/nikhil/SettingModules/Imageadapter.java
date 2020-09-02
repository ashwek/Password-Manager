package com.nikhil.SettingModules;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nikhil.EnPass.R;

public class Imageadapter extends PagerAdapter {
    private Context mContext;
    private int[] mImageids = new int[]{R.drawable.htu1_login_page, R.drawable.htu2_selection_screen, R.drawable.htu3_adddetailscreen, R.drawable.htu4_dashboard_withdata,R.drawable.htu5_retrivedetails,
    R.drawable.htu6_updatebtn,R.drawable.htu7_updatescreen,R.drawable.htu8_deletedatabtn};

    Imageadapter (Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return mImageids.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mImageids[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}
