package com.ucas.iplay.ui.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucas.iplay.R;
import com.ucas.iplay.core.model.EventModel;
import com.ucas.iplay.core.model.UserModel;
import com.ucas.iplay.ui.UserActivity;
import com.ucas.iplay.util.StringUtil;

/**
 * Created by ivanchou on 4/5/15.
 */
public class EventView extends RelativeLayout implements View.OnClickListener{
    private Context mContext;
    public EventView(Context context) {
        this(context, null);
        mContext = context;
    }

    public EventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EventView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private View mJointed;// 采用颜色标记用户是否已经参加某活动
    private TextView mTitleTv;
    private TextView mTimeScopeTv;
    private ImageView mAvatorIv;
    private TextView mTagNameTv;

    private long mEventId;
    private UserModel mUser;

    private void init() {
        inflate(getContext(), R.layout.event_lv_item, this);
        mJointed = findViewById(R.id.v_jointed);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mTimeScopeTv = (TextView) findViewById(R.id.tv_time_scope);
        mAvatorIv = (ImageView) findViewById(R.id.iv_user_avatar);
        mTagNameTv = (TextView) findViewById(R.id.tv_tag);
        mAvatorIv.setOnClickListener(this);
    }

    public void parse(EventModel event) {
        mEventId = event.eventId;
        mTitleTv.setText(event.title);
        String timeScope = StringUtil.parseLongTimeToString(event.startAt) + " ~ " + StringUtil.parseLongTimeToString(event.endAt);
        mTimeScopeTv.setText(timeScope);
//        mAvatorIv.setImageResource(event.author.avatar);
//        mTagNameTv.setText(event.tags);

    }

    public long getEventId() {
        return mEventId;
    }

    private void setJointed(View v) {
        v.setBackgroundResource(android.R.color.holo_blue_bright);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_avatar:
                Intent intent = new Intent(mContext, UserActivity.class);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
