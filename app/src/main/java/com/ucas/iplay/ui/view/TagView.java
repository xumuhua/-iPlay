package com.ucas.iplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucas.iplay.R;
import com.ucas.iplay.core.model.TagModel;

/**
 * Created by ivanchou on 4/11/15.
 */
public class TagView extends RelativeLayout implements View.OnClickListener {
    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private static final int TAG_NOT_INTERESTED = 0;
    private static final int TAG_INTERESTED = 1;

    private int mType = TAG_NOT_INTERESTED;

    private TextView mTitleTv;
    private TextView mDetailTv;
    private CheckBox mInterestedCb;

    private int mTagId;

    private void init() {
        inflate(getContext(), R.layout.tag_lv_item, this);
        mTitleTv = (TextView) findViewById(R.id.tv_tag_title);
        mDetailTv = (TextView) findViewById(R.id.tv_tag_detail);
        mInterestedCb = (CheckBox) findViewById(R.id.cb_tag_interest);
        mInterestedCb.setOnClickListener(this);
    }

    public void parse(TagModel tagModel) {
        mTagId = tagModel.tagId;
        mTitleTv.setText(tagModel.tagName);
        if (tagModel.tagDetail != null) {
            mDetailTv.setVisibility(VISIBLE);
            mDetailTv.setText(tagModel.tagDetail);
        } else {
            mDetailTv.setVisibility(GONE);
        }

        setTagInterested(tagModel.interested);

    }

    private void setTagInterested(int interested) {
        if (interested == 1) {
            mType = TAG_INTERESTED;
            mInterestedCb.setChecked(true);
        } else if (interested == 0) {
            mType = TAG_NOT_INTERESTED;
            mInterestedCb.setChecked(false);
        }
    }

    public int getTagId() {
        return mTagId;
    }

    @Override
    public void onClick(View v) {
        if (mType == TAG_NOT_INTERESTED) {
            setTagInterested(TAG_INTERESTED);
            if (mOnInterestedClickListener != null) {
                mOnInterestedClickListener.onClick(mTagId, TAG_INTERESTED);
            }
        } else if (mType == TAG_INTERESTED) {
            setTagInterested(TAG_NOT_INTERESTED);
            if (mOnInterestedClickListener != null) {
                mOnInterestedClickListener.onClick(mTagId, TAG_NOT_INTERESTED);
            }
        }

    }

    private OnInterestedClickListener mOnInterestedClickListener;

    public void setOnInterestedClickListener(OnInterestedClickListener onInterestedClickListener) {
        mOnInterestedClickListener = onInterestedClickListener;
    }

    public static interface OnInterestedClickListener {
        public void onClick(int tagId, int interested);
    }
}

