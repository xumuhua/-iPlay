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

    public void parse(JSONObject jsonObject) throws JSONException {
        tagId = jsonObject.getInt("tagid");
        tagName = jsonObject.getString("tagname");
    }

    public static TagModel fromCursor(Context context, Cursor cursor) {
        TagModel tagModel = new TagModel();
        tagModel.tagId = cursor.getInt(cursor.getColumnIndex(BaseTagsDBInfo.TAG_ID));
        tagModel.tagName = cursor.getString(cursor.getColumnIndex(BaseTagsDBInfo.TAG_NAME));

        return tagModel;
    }
}
