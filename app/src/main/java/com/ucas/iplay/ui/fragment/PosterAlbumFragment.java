package com.ucas.iplay.ui.fragment;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ucas.iplay.R;
import com.ucas.iplay.ui.adapter.PosterPagerAdapter;
import com.ucas.iplay.ui.base.BaseFragment;

/**
 * Created by Origa on 2015/3/1.
 */
public class PosterAlbumFragment extends BaseFragment {

    private View mPosterAlbumView;
    private ViewPager mViewPager;
    private PosterPagerAdapter mPagerAdapter;

    private ImageView mBackView;
    private ImageView [] mImageViews;

    private Activity mActivity;
    private PosterAlbumCallback mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poster_album,container,false);
        mPosterAlbumView = view;

        mViewPager = (ViewPager) mPosterAlbumView.findViewById(R.id.vp_poster_album_poster);
        mBackView = (ImageView) mPosterAlbumView.findViewById(R.id.iv_poster_album_back);

        mPagerAdapter = new PosterPagerAdapter(this.getActivity(),mImageViews);
        mViewPager.setAdapter(mPagerAdapter);
        if(mImageViews.length > 1){
            mViewPager.setCurrentItem(mImageViews.length*50);
        }

        setListener();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        releaseFragmentStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseFragmentStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        try {
            mCallback = (PosterAlbumCallback) activity;
        } catch (ClassCastException e){
            throw new ClassCastException("Activity must implement PosterAlbumCallback");
        }
    }

    public void setImageViews(ImageView [] imageViews){
        mImageViews = imageViews;
    }

    public void freshViewPager(){
        mPagerAdapter = new PosterPagerAdapter(this.getActivity(),mImageViews);
        mViewPager.setAdapter(mPagerAdapter);
        if(mImageViews.length > 1){
            mViewPager.setCurrentItem(mImageViews.length*50);
        }
        mViewPager.invalidate();
    }

    public void updateImage(int imageID, int position){
        mImageViews[position%mImageViews.length].setImageResource(imageID);
        mImageViews[position%mImageViews.length].invalidate();
    }

    private void setListener(){
        if(mImageViews == null) return;
        for (int i=0;i<mImageViews.length;i++) {
            mImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPosterAlbumQuit();
                }
            });
        }
    }

    public void releaseFragmentStack(){
        for (int i =0;i<getFragmentManager().getBackStackEntryCount();i++){
            getFragmentManager().popBackStack();
        }
    }
    /*  onPosterAlbumQuit
*       相册界面的退出接口*/
    private void onPosterAlbumQuit() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    public interface PosterAlbumCallback{
    }


}
