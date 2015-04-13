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
import com.ucas.iplay.core.model.EventModel;
import com.ucas.iplay.ui.view.EventView;

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
    public Object getItem(int position) {
        getCursor().moveToPosition(position);
        return EventModel.fromCursor(mContext, getCursor());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new EventView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        EventView eventView = (EventView) view;
        EventModel eventModel = EventModel.fromCursor(context, cursor);
        eventView.parse(eventModel);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }
}
