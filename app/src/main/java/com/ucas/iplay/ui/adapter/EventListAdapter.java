package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ucas.iplay.R;
import com.ucas.iplay.core.model.EventModel;

import java.util.List;

/**
 * Created by ivanchou on 1/21/2015.
 */
public class EventListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<EventModel> mEventsList;

    public EventListAdapter(Context context, List<EventModel> eventsList) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mEventsList = eventsList;
    }

    @Override
    public int getCount() {
        return mEventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.event_lv_item, null);
            viewHolder = new ViewHolder();
            viewHolder.startAtTv = (TextView) convertView.findViewById(R.id.tv_time_scope);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.avatarIv = (ImageView) convertView.findViewById(R.id.iv_user_avatar);
//            viewHolder.jointedIv = (ImageView) convertView.findViewById(R.id.iv_jointed);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 填充数据
        if (mEventsList.size() > 0) {
            EventModel event = mEventsList.get(position);
            viewHolder.startAtTv.setText(event.startAt);
            viewHolder.titleTv.setText(event.title);
            // 设置用户头像跟是否参加的图片标识
//            viewHolder.avatarIv.setImageResource(R.drawable.ic_drawer);
//            viewHolder.jointedIv.setImageResource(R.drawable.ic_drawer);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView startAtTv;
        public TextView titleTv;
        public ImageView avatarIv;
        public ImageView jointedIv;
    }
}
