package com.ucas.iplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ucas.iplay.R;
import com.ucas.iplay.core.model.TagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchou on 1/20/2015.
 */
public class FooterTagsView extends ViewGroup {
    public enum TagMode {SINGLE, MULTI}

    private Context mContext;
    private List<List<View>> mAllViews = new ArrayList<List<View>>();// 存储所有的 tag view
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    private TagModel[] mTags;
    private LayoutInflater mInflater;

    private int tags;// 纪录选中的 tag
    private static final int SINGLE_CLICK = 0;
    private static final int LONG_CLICK = 1;
    private OnTagClickListener mCallbacks;
    private TagMode mMode;


    /**
     * 代码中 new 的时候调用
     */
    public FooterTagsView(Context context) {
        this(context, null);
    }

    /**
     * 布局文件中引用时调用
     */
    public FooterTagsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 自定义属性
     */
    public FooterTagsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;
        int height = 0;

        // 纪录每一行的宽高
        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量子 view 的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            // 换行
            if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                //纪录行高度
                height += lineHeight;
                lineHeight = childHeight;
            } else { // 未换行
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            // 处理最后一个控件
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        int childCount = getChildCount();
        List<View> listViews = new ArrayList<View>();

        // 计算每一行的高度、确定每一行的 tag
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin
                    > width - getPaddingLeft() - getPaddingRight()) {
                mLineHeight.add(lineHeight);
                mAllViews.add(listViews);

                // 重置宽高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                // 重置 list
                listViews = new ArrayList<View>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            listViews.add(child);
        }
        // 最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(listViews);

        // 开始布局子 view
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            listViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            for (int j = 0; j < listViews.size(); j++) {
                View child = listViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void setCustomTags(TagModel[] tagModels) {
        if (tagModels == null || tagModels.length == 0) {
            setVisibility(View.GONE);
            return;
        }
        mTags = tagModels;
        initTagsView();
    }

    private void initTagsView() {
        removeAllViews();
        for (int i = 0; i < mTags.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.textview_tags, this, false);
            tv.setText(mTags[i].tagName);
            tv.setTag(mTags[i]);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    calculateTags(v, SINGLE_CLICK);
                    refreshTagsView();
                    mCallbacks.onTagClickRefresh(tags);
                }
            });
            tv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    calculateTags(v, LONG_CLICK);
                    refreshTagsView();
                    mCallbacks.onTagLongClickRefresh(tags);
                    return true;
                }
            });
            addView(tv);
        }
    }

    /**
     * 生成 tags 字段
     * @param v
     * @param state
     */
    private void calculateTags(View v, int state) {
        TagModel tagModel = (TagModel) v.getTag();
        int position = tagModel.tagId;

        switch (state) {
            case SINGLE_CLICK:
                // 生成 tags 字段，取消选中状态
                if (mMode.equals(TagMode.MULTI)) {
                    if (((1 << position) & tags) != 0) {
                        tags &= ~(1 << position);
                    } else { // 选中状态
                        tags |= (1 << position);
                    }
                } else {
                    tags = 0;
                    tags |= (1 << position);
                }
                break;

            case LONG_CLICK:
                // 取消所有
                tags = 0;
                tags |= (1 << position);
                break;
            default:
                break;
        }
    }

    /**
     * 根据 tags 刷新 tagsview 的选中状态
     */
    private void refreshTagsView() {
        View v;
        int position;
        int resId;
        for (int i = 0; i < mTags.length; i++) {
            v = findViewWithTag(mTags[i]);
            position = mTags[i].tagId;
            if (((1 << position) & tags) != 0) {
                resId = R.drawable.tv_selected_bg;
            } else {
                resId = R.drawable.tv_unselected_bg;
            }
            v.setBackgroundResource(resId);
        }
    }


    public void setOnTagClickListener(OnTagClickListener listener) {
        this.mCallbacks = listener;
    }

    public interface OnTagClickListener {
        /**
         * 点击 tag 触发刷新
         * @param tags
         */
        public void onTagClickRefresh(int tags);

        /**
         * 长按实现单选
         * @param tags
         */
        public void onTagLongClickRefresh(int tags);
    }

    public void setMode(TagMode mMode) {
        this.mMode = mMode;
    }
}
