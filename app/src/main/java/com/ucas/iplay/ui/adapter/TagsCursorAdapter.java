package com.ucas.iplay.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.core.db.TagsDataHelper;
import com.ucas.iplay.core.model.TagModel;
import com.ucas.iplay.ui.view.TagView;
import com.ucas.iplay.ui.view.TagView.OnInterestedClickListener;
import com.ucas.iplay.util.HttpUtil;
import com.ucas.iplay.util.SPUtil;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by ivanchou on 4/11/15.
 */
public class TagsCursorAdapter extends CursorAdapter implements OnInterestedClickListener {

    private Context mContext;
    private TagsDataHelper mTagsDataHelper;

    public TagsCursorAdapter(Context context) {
        super(context, null, false);
        mContext = context;
        mTagsDataHelper = new TagsDataHelper(context);
    }

    @Override
    public Object getItem(int position) {
        getCursor().moveToPosition(position);
        return TagModel.fromCursor(mContext, getCursor());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new TagView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TagView tagView = (TagView) view;
        TagModel tagModel = TagModel.fromCursor(context, cursor);
        tagView.parse(tagModel);
        tagView.setOnInterestedClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * 订阅 Tag 的回调事件
     * @param tagId
     * @param interested
     */
    @Override
    public void onClick(int tagId, int interested) {
        mTagsDataHelper.updateInterested(tagId, interested);
        long interestedTags = Long.valueOf(SPUtil.getSPUtil(mContext).get(SPUtil.INTERESTED_TAGS));
        HttpUtil.changeInterestedTags(mContext, interestedTags, new JsonHttpResponseHandler() {
            @Override
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
