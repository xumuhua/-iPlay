package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ucas.iplay.R;

/**
 * Created by ivanchou on 1/26/2015.
 */
public class DrawerAdapter extends BaseAdapter{

    private Context mContext;
    private String[] mTitles;

    public DrawerAdapter(Context context){
        mContext = context;
        mTitles = new String[]{
                context.getResources().getString(R.string.drawer_item_timeline),
                context.getResources().getString(R.string.drawer_item_jointed),
                context.getResources().getString(R.string.drawer_item_postnew),
                context.getResources().getString(R.string.drawer_item_changetags),
        };
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public String getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getIconId(int position) {
        switch (position){
            case 0:
            case 1:
            case 2:
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView item = (TextView) convertView;
        if(item == null){
            item = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_drawer_item, null);
        }
        item.setText(getItem(position));
//        item.setCompoundDrawablesWithIntrinsicBounds(getIconId(position), 0, 0, 0);
        return item;
    }
}
