package com.ucas.iplay.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ucas.iplay.R;
import com.ucas.iplay.core.model.TagModel;


/**
 * Created by ivanchou on 1/19/2015.
 */
public class QuickReturnListView extends ListView implements OnScrollListener {

    private Context mContext;
    private FooterTagsView mFooterTagsView;
    private View mFooterLoadingView;

    private int mItemCount;
    private int mItemOffsetY[];
    private boolean scrollIsComputed = false;
    private int mHeight;
    private boolean isBottom = false;// 是否到达底部

    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int mState = STATE_ONSCREEN;
    private int mScrollY;
    private int mMinRawY = 0;

    private int mQuickReturnHeight;
    private TranslateAnimation anim;

    private LayoutInflater mInflater;
    private DataChangeListener mCallbacks;

    public QuickReturnListView(Context context) {
        this(context, null);
    }

    public QuickReturnListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickReturnListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initViews();
    }


    private void initViews() {
        mInflater = LayoutInflater.from(mContext);
        mFooterLoadingView = mInflater.inflate(R.layout.listview_footer_layout, null);
        addFooterView(mFooterLoadingView);
        dismissFooterLoadingView();
        setOnScrollListener(this);
    }


    public void setTagsView(FooterTagsView tagsView) {
        if (mFooterTagsView == null) {
            mFooterTagsView = tagsView;
        }
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mQuickReturnHeight = mFooterTagsView.getHeight();
                        computeScrollY();
                    }
                });
    }


    public void setDataChangeListener(DataChangeListener listener) {
        this.mCallbacks = listener;
    }


    public interface DataChangeListener {
        /**
         * 从网络加载更多的数据
         */
        public void onLoadMore();
    }

    public int getListHeight() {
        return mHeight;
    }

    public void computeScrollY() {
        mHeight = 0;
        mItemCount = getAdapter().getCount();

        /** {https://github.com/LarsWerkman/QuickReturnListView/issues/8#issuecomment-24748156} **/
        if (mItemOffsetY == null || mItemOffsetY.length != mItemCount) {
            mItemOffsetY = new int[mItemCount];
        }
        for (int i = 0; i < mItemCount; ++i) {
            View view = getAdapter().getView(i, null, this);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mItemOffsetY[i] = mHeight;
            mHeight += view.getMeasuredHeight();
            System.out.println(mHeight);
        }
        scrollIsComputed = true;
    }

    public boolean scrollYIsComputed() {
        return scrollIsComputed;
    }

    public int getComputedScrollY() {
        int pos, nScrollY, nItemY;
        View view = null;
        pos = getFirstVisiblePosition();
        view = getChildAt(0);
        nItemY = view.getTop();
        nScrollY = mItemOffsetY[pos] - nItemY;
        return nScrollY;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滚动到底部且 listview 的状态是空闲
        if (isBottom && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            // 加载新的数据
            showFooterLoadingView();
            mCallbacks.onLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // 判断是否到底
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            isBottom = true;
        } else {
            isBottom = false;
        }

        // 处理滚动过程 tagsview 的显示
        mScrollY = 0;
        int translationY = 0;

        if (scrollYIsComputed()) {
            mScrollY = getComputedScrollY();
        }

        int rawY = mScrollY;

        switch (mState) {
            case STATE_OFFSCREEN:
                if (rawY >= mMinRawY) {
                    mMinRawY = rawY;
                } else {
                    mState = STATE_RETURNING;
                }
                translationY = rawY;
                break;

            case STATE_ONSCREEN:
                if (rawY > mQuickReturnHeight) {
                    mState = STATE_OFFSCREEN;
                    mMinRawY = rawY;
                }
                translationY = rawY;
                break;

            case STATE_RETURNING:

                translationY = (rawY - mMinRawY) + mQuickReturnHeight;

                System.out.println(translationY);
                if (translationY < 0) {
                    translationY = 0;
                    mMinRawY = rawY + mQuickReturnHeight;
                }

                if (rawY == 0) {
                    mState = STATE_ONSCREEN;
                    translationY = 0;
                }

                if (translationY > mQuickReturnHeight) {
                    mState = STATE_OFFSCREEN;
                    mMinRawY = rawY;
                }
                break;
        }

        if (mFooterTagsView == null) {
            return;
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
            anim = new TranslateAnimation(0, 0, translationY,
                    translationY);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mFooterTagsView.startAnimation(anim);
        } else {
            mFooterTagsView.setTranslationY(translationY);
        }
    }

    private void showFooterLoadingView() {
        View view = mFooterLoadingView.findViewById(R.id.pb_loding);
        view.setVisibility(View.VISIBLE);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setAlpha(1.0f);
        mFooterLoadingView.findViewById(R.id.tv_load_failed).setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void dismissFooterLoadingView() {
        final View progressbar = mFooterLoadingView.findViewById(R.id.pb_loding);
        progressbar.animate().scaleX(0).scaleY(0).alpha(0.5f).setDuration(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setVisibility(View.GONE);
                    }
                });
        mFooterLoadingView.findViewById(R.id.tv_load_failed).setVisibility(View.GONE);
    }

    private void showErrorFooterLoadingView() {
        View view = mFooterLoadingView.findViewById(R.id.pb_loding);
        view.setVisibility(View.GONE);
        TextView tv = ((TextView) mFooterLoadingView.findViewById(R.id.tv_load_failed));
        tv.setVisibility(View.VISIBLE);
    }
}
