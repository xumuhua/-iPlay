package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.R;
import com.ucas.iplay.core.db.TagsDataHelper;
import com.ucas.iplay.core.model.TagModel;
import com.ucas.iplay.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hoolee on 2015/4/10.
 */
public class TagsSimpleAdapter extends SimpleAdapter{
    private class TagsAdapterHolder {
        ImageView tagImage;
        TextView tagName;
        TextView tagDesc;
        CheckBox tagCheck;
    }

    private List<Map<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private TagsAdapterHolder holder;
    private boolean checkedStatus[];
    private TagModel tagsModel[];
    public TagsSimpleAdapter(Context c, List<Map<String, Object>> listItems, int resource,
                             String[] from, int[] to, TagModel[] tagsModel){
        super(c, listItems, resource, from, to);
        mAppList = listItems;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = from;
        valueViewID = to;
        this.tagsModel = tagsModel;
        checkedStatus = new boolean[mAppList.size()];
        Arrays.fill(checkedStatus, false);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        super.getView(position, convertView, parent);
        if (convertView != null) {
            holder = (TagsAdapterHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.tags_list, null);
            holder = new TagsAdapterHolder();
            holder.tagImage = (ImageView)convertView.findViewById(valueViewID[0]);
            holder.tagName = (TextView)convertView.findViewById(valueViewID[1]);
            holder.tagDesc = (TextView)convertView.findViewById(valueViewID[2]);
            holder.tagCheck = (CheckBox)convertView.findViewById(valueViewID[3]);
            convertView.setTag(holder);
        }

        Map<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            int mid = (Integer)appInfo.get(keyString[0]);
            holder.tagImage.setImageDrawable(holder.tagImage.getResources().getDrawable(mid));
            holder.tagName.setText((String) appInfo.get(keyString[1]));
            holder.tagDesc.setText((String) appInfo.get(keyString[2]));
            holder.tagCheck.setChecked(checkedStatus[position]);
            holder.tagCheck.setOnClickListener(new MyTagsListener(position));
        }
        return convertView;
    }
    private class MyTagsListener implements View.OnClickListener {
        private int position;
        MyTagsListener(int pos) {
            position = pos;
        }
        @Override
        public void onClick(View v) {
            CheckBox checkBox = holder.tagCheck;
            if (v.getId() == checkBox.getId()){
                checkedStatus[position] = !checkedStatus[position];
                Toast.makeText(mContext, "in MyTagsListener onClick position=" + position
                        + ", isChecked=" + checkedStatus[position], Toast.LENGTH_SHORT).show();
                // insert into local db
                List tagList = new ArrayList<TagModel>();
                tagList.add(tagsModel[position]);
                System.out.println("tagId=" + tagsModel[position].tagId + ", tagName=" + tagsModel[position].tagName);
                new TagsDataHelper(mContext).bulkInsert(tagList);
                // making url request:
                long interestedtags = checkedStatus[position] ? 1 << tagsModel[position].tagId : 0;
                System.out.println("interestedtags=" + interestedtags);
                HttpUtil.changeInterestedTags(mContext, interestedtags, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        }
    }
}
