package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ucas.iplay.R;
import com.ucas.iplay.ui.fragment.UserInfoFragment;
import com.ucas.iplay.ui.fragment.UserPostFragment;

/**
 * Created by ivanchou on 4/11/15.
 */
public class UserInfoPagerAdapter extends FragmentPagerAdapter {
    private static final int COUNT = 2;

    private static final int USER_INFO = 0;
    private static final int USER_POST = 1;

    private String[] mTitles;


    public UserInfoPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mTitles = new String[]{
                context.getResources().getString(R.string.viewpager_item_info),
                context.getResources().getString(R.string.viewpager_item_post)
        };
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case USER_INFO:
                return UserInfoFragment.newInstance(USER_INFO, mTitles[USER_INFO]);
            case USER_POST:
                return UserPostFragment.newInstance(USER_POST, mTitles[USER_POST]);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}

