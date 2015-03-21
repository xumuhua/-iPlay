package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ucas.iplay.R;
import com.ucas.iplay.core.dbinfo.EventsDBInfo;

/**
 * Created by ivanchou on 1/23/2015.
 */
public class EventCursorAdapter extends CursorAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public EventCursorAdapter(Context context) {
        super(context, null, false);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.event_lv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.startAtTv = (TextView) view.findViewById(R.id.tv_start_at);
        viewHolder.titleTv = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.avatarIv = (ImageView) view.findViewById(R.id.iv_avatar);
        viewHolder.jointedIv = (ImageView) view.findViewById(R.id.iv_jointed);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.startAtTv.setText(cursor.getString(cursor.getColumnIndex(EventsDBInfo.START_AT)));
        viewHolder.titleTv.setText(cursor.getString(cursor.getColumnIndex(EventsDBInfo.TITLE)));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder {
        public TextView startAtTv;
        public TextView titleTv;
        public ImageView avatarIv;
        public ImageView jointedIv;
    }
}
