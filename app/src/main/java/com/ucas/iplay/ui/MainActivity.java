package com.ucas.iplay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;

import com.ucas.iplay.R;
import com.ucas.iplay.ui.base.BaseActivity;
import com.ucas.iplay.ui.fragment.JointedFragment;
import com.ucas.iplay.ui.fragment.NavigationDrawerFragment;
import com.ucas.iplay.ui.fragment.NavigationDrawerFragment.NavigationDrawerCallback;
import com.ucas.iplay.ui.fragment.PostNewFragment;
import com.ucas.iplay.ui.fragment.TagsFragment;
import com.ucas.iplay.ui.fragment.TimeLineFragment;


/**
 * Created by ivanchou on 1/15/2015.
 */
public class MainActivity extends BaseActivity implements NavigationDrawerCallback {
    private CharSequence mTitle;

    private static final int TIME_LINE_FRAGMENT = 0;
    private static final int JOINTED_FRAGMENT = 1;
    private static final int POSTNEW_FRAGMENT = 2;
    private static final int CHANGETAGS_FRAGMENT = 3;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TimeLineFragment mTimeLineFragment;
    private JointedFragment mJointedFragment;
    private PostNewFragment mPostnewFragment;
    private TagsFragment mTagsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.e(TAG, "-------" + position + "------------");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (position) {
            case TIME_LINE_FRAGMENT:
                if (mTimeLineFragment == null) {
                    mTimeLineFragment = new TimeLineFragment();
                }
                mTitle = getString(R.string.drawer_item_timeline);
                fragmentTransaction.replace(R.id.content_frame, mTimeLineFragment);
                break;
            case JOINTED_FRAGMENT:
                if (mJointedFragment == null) {
                    mJointedFragment = new JointedFragment();
                }
                mTitle = getString(R.string.drawer_item_jointed);
                fragmentTransaction.replace(R.id.content_frame, mJointedFragment);
                break;
            case POSTNEW_FRAGMENT:
//                if (mPostnewFragment == null) {
//                    mPostnewFragment = new PostNewFragment();
//                }
//                mTitle = getString(R.string.drawer_item_postnew);
//                fragmentTransaction.replace(R.id.content_frame, mPostnewFragment);
                Intent intent = new Intent(MainActivity.this, PostNewActivity.class);
                startActivity(intent);
                break;
            case CHANGETAGS_FRAGMENT:
                if (mTagsFragment == null) {
                    mTagsFragment = new TagsFragment();
                }
                mTitle = getString(R.string.drawer_item_changetags);
                fragmentTransaction.replace(R.id.content_frame, mTagsFragment);
//                Intent intentChangeTags = new Intent(MainActivity.this, ListTagsAcitvity.class);
//                startActivity(intentChangeTags);
        }
        setTitle(mTitle);
        fragmentTransaction.commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // 打开 关闭 drawer 的两种不同的 menu 状态
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
