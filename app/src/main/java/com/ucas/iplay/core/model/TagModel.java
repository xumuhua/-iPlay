package com.ucas.iplay.core.model;

import android.content.Context;
import android.database.Cursor;

import com.ucas.iplay.core.dbinfo.BaseTagsDBInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivanchou on 1/27/2015.
 */
public class TagModel {

    public int tagId;

    public String tagName;

    public String tagDetail;

    public int interested;

    public void parse(JSONObject jsonObject) throws JSONException {
        tagId = jsonObject.getInt("tagid");
        tagName = jsonObject.getString("tagname");
//        tagDetail = jsonObject.getString("tagdetail");
//        interested = jsonObject.getInt("interested");
    }

    public static TagModel fromCursor(Context context, Cursor cursor) {
        TagModel tagModel = new TagModel();
        tagModel.tagId = cursor.getInt(cursor.getColumnIndex(BaseTagsDBInfo.TAG_ID));
        tagModel.tagName = cursor.getString(cursor.getColumnIndex(BaseTagsDBInfo.TAG_NAME));
        tagModel.tagDetail = cursor.getString(cursor.getColumnIndex(BaseTagsDBInfo.TAG_DETAIL));
        tagModel.interested = cursor.getInt(cursor.getColumnIndex(BaseTagsDBInfo.IS_INTERESTED));
        return tagModel;
    }
}
