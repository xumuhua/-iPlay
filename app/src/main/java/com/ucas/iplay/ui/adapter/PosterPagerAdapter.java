package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ChelseaFC on 2015/3/7.
 */
public class PosterPagerAdapter extends PagerAdapter {

    private ImageView [] mImageViews;

    public PosterPagerAdapter(Context context, ImageView[] mImageViews) {
        this.mImageViews = mImageViews;
    }

    @Override
    public int getCount() {
        int adapterLength;
        adapterLength = mImageViews.length > 1 ? mImageViews.length*100:1;
        return adapterLength;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mImageViews.length>3){
            container.removeView(mImageViews[position % mImageViews.length]);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mImageViews[position % mImageViews.length]);
        return mImageViews[position % mImageViews.length];
    }
}
