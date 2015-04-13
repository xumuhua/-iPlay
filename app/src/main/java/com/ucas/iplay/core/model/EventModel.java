package com.ucas.iplay.core.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ucas.iplay.core.dbinfo.BaseEventsDBInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivanchou on 1/21/2015.
 */
public class EventModel {

    public long eventId;// 事件 ID

    public String createdAt;// 事件创建时间

    public int jointed;// 用户对该事件的操作

    public UserModel author;// 事件的发布者

    public String startAt;// 事件的开始时间

    public String endAt;// 事件的结束时间

    public String endrollBefore;// 报名截至时间

    public String placeAt;// 事件的地点

    public String title;// 事件的主题

    public String content;// 事件的详细内容

    public long tags;// 事件的标签属性

    public long maxPeople;// 活动的最大人数限制

    public int restriction;// 访问权限

    public String thumbnailPic;

    public String originalPic;

    public EventModel() {

    }

    public void parse(JSONObject jsonObject) throws JSONException {
        eventId = jsonObject.getLong("activityid");
        createdAt = jsonObject.getString("createdat");
//        jointed = jsonObject.getInt("jointed");

        /** 注释掉的功能上相同 根据返回的 json 格式选择 **/
        author = new UserModel();
        author.userId = jsonObject.getInt("authorid");
//        author.name = jsonObject.getString("authornick");
//        author.avatar = jsonObject.getString("authorphoto");

//        author.parse(jsonObject.getJSONObject("author"));

        startAt = jsonObject.getString("startat");
        endAt = jsonObject.getString("endat");
//        endrollBefore = jsonObject.getString("endrollbefore");
        placeAt = jsonObject.getString("placeat");
        title = jsonObject.getString("title");
        content = jsonObject.getString("text");
        tags = jsonObject.getLong("tags");
        maxPeople = jsonObject.getLong("maxpeople");
//        restriction = jsonObject.getInt("restriction");
//        thumbnailPic = jsonObject.getString("thumbnailpic");
//        originalPic = jsonObject.getString("originalpic");
    }

    public static EventModel fromCursor(Context context, Cursor cursor) {
        EventModel eventModel = new EventModel();
        eventModel.eventId = cursor.getLong(cursor.getColumnIndex(BaseEventsDBInfo.EVENT_ID));
        eventModel.createdAt = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.CREATED_AT));
        eventModel.jointed = cursor.getInt(cursor.getColumnIndex(BaseEventsDBInfo.JOINTED));

        UserModel userModel = new UserModel();
        userModel.userId = cursor.getInt(cursor.getColumnIndex(BaseEventsDBInfo.USER_ID));
        userModel.name = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.USER_NAME));
        userModel.avatar = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.USER_AVATAR));
        eventModel.author = userModel;

        eventModel.startAt = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.START_AT));
        eventModel.endAt = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.END_AT));
        eventModel.endrollBefore = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.ENDROLL_BEFORE));
        eventModel.placeAt = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.PLACE_AT));
        eventModel.title = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.TITLE));
        eventModel.content = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.CONTENT));
        eventModel.tags = cursor.getLong(cursor.getColumnIndex(BaseEventsDBInfo.TAGS));
        eventModel.maxPeople = cursor.getLong(cursor.getColumnIndex(BaseEventsDBInfo.MAX_PEOPLE));
        eventModel.restriction = cursor.getInt(cursor.getColumnIndex(BaseEventsDBInfo.RESTRICTION));
        eventModel.thumbnailPic = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.THUMBNAIL_PIC));
        eventModel.originalPic = cursor.getString(cursor.getColumnIndex(BaseEventsDBInfo.ORIGINAL_PIC));
        return eventModel;
    }
}
