package com.ucas.iplay.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ucas.iplay.app.Config;
import com.ucas.iplay.core.DataProvider;
import com.ucas.iplay.core.dbinfo.TagsDBInfo;
import com.ucas.iplay.core.model.TagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchou on 1/27/2015.
 */
public class TagsDataHelper extends BaseDataHelper {
    private Context mContext;

    public TagsDataHelper(Context context) {
        super(context);
        mContext = context;
    }

    protected ContentValues getContentValues(TagModel tag) {
        ContentValues values = new ContentValues();

        values.put(TagsDBInfo.TAG_ID, tag.tagId);
        values.put(TagsDBInfo.TAG_NAME, tag.tagName);

        return values;
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.TAGS_CONTENT_URI;
    }

    @Override
    protected String getTableName() {
        return Config.DATABASE.TAGS_TABLE_NAME;
    }

    public TagModel[] query() {
        TagModel[] tags;
        Cursor cursor = query(null, null, null, null);
        if (cursor.moveToFirst()) {
            ArrayList<TagModel> tagModelArrayList = new ArrayList<TagModel>();
            do {
                tagModelArrayList.add(TagModel.fromCursor(mContext, cursor));
            } while (cursor.moveToNext());
            tags = tagModelArrayList.toArray(new TagModel[tagModelArrayList.size()]);
        } else {
            tags = new TagModel[0];
        }
        cursor.close();
        return tags;
    }


    public int bulkInsert(List<TagModel> tags) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for(TagModel tag : tags){
            ContentValues values = getContentValues(tag);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        return bulkInsert(contentValues.toArray(valueArray));
    }
}
