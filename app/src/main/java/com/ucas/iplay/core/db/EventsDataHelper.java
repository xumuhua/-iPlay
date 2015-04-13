package com.ucas.iplay.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ucas.iplay.app.Config;
import com.ucas.iplay.core.DataProvider;
import com.ucas.iplay.core.dbinfo.BaseEventsDBInfo;
import com.ucas.iplay.core.dbinfo.EventsDBInfo;
import com.ucas.iplay.core.model.EventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchou on 1/27/2015.
 */
public class EventsDataHelper extends BaseDataHelper {

    private Context mContext;
    public EventsDataHelper(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.EVENTS_CONTENT_URI;
    }

    @Override
    protected String getTableName() {
        return Config.DATABASE.EVENTS_TABLE_NAME;
    }

    protected ContentValues getContentValues(EventModel event) {
        ContentValues values = new ContentValues();

        values.put(EventsDBInfo.EVENT_ID, event.eventId);
        values.put(EventsDBInfo.CREATED_AT, event.createdAt);
        values.put(EventsDBInfo.JOINTED, event.jointed);
        values.put(EventsDBInfo.USER_ID, event.author.userId);
        values.put(EventsDBInfo.USER_NAME, event.author.name);
        values.put(EventsDBInfo.USER_AVATAR, event.author.avatar);
        values.put(EventsDBInfo.START_AT, event.startAt);
        values.put(EventsDBInfo.END_AT, event.endAt);
        values.put(EventsDBInfo.ENDROLL_BEFORE, event.endrollBefore);
        values.put(EventsDBInfo.PLACE_AT, event.placeAt);
        values.put(EventsDBInfo.TITLE, event.title);
        values.put(EventsDBInfo.CONTENT, event.content);
        values.put(EventsDBInfo.TAGS, event.tags);
        values.put(EventsDBInfo.MAX_PEOPLE, event.maxPeople);
        values.put(EventsDBInfo.RESTRICTION, event.restriction);
        values.put(EventsDBInfo.THUMBNAIL_PIC, event.thumbnailPic);
        values.put(EventsDBInfo.ORIGINAL_PIC, event.originalPic);

        return values;
    }

    public EventModel[] query() {
        EventModel[] events;
        // 默认按照创建的时间排序
        Cursor cursor = query(null, null, null, BaseEventsDBInfo.CREATED_AT);
        if (cursor.moveToFirst()) {
            ArrayList<EventModel> eventModelArrayList = new ArrayList<EventModel>();
            do {
                eventModelArrayList.add(EventModel.fromCursor(mContext, cursor));
            } while (cursor.moveToNext());
            events = eventModelArrayList.toArray(new EventModel[eventModelArrayList.size()]);
        } else {
            events = new EventModel[0];
        }
        cursor.close();
        return events;
    }

    public EventModel queryById(int eventId) {
        Cursor cursor = query(null, BaseEventsDBInfo.EVENT_ID + "=" + eventId, null, null);
        if (cursor.moveToFirst()) {
            return EventModel.fromCursor(mContext, cursor);
        } else {
            return null;
        }
    }

    public int bulkInsert(List<EventModel> events) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for(EventModel event : events){
            ContentValues values = getContentValues(event);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        return bulkInsert(contentValues.toArray(valueArray));
    }

    /**
     * 更新 jointed
     * @param eventId
     * @param type type = 1 表示感兴趣；type ＝ 2 表示参加
     * @return
     */
    public int updateJointed(int eventId, int type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BaseEventsDBInfo.JOINTED, type);
        String where = BaseEventsDBInfo.EVENT_ID + "=?";
        String[] whereArgs = {String.valueOf(eventId)};
        return update(contentValues, where, whereArgs);
    }
}
