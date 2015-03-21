package com.ucas.iplay.core.dbinfo;

import android.provider.BaseColumns;

/**
 * event 表的数据库字段
 * Created by ivanchou on 1/28/2015.
 */
public class BaseEventsDBInfo implements BaseColumns {

    public static final String EVENT_ID = "event_id";
    public static final String CREATED_AT = "created_at";
    public static final String JOINTED = "jointed";

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_AVATAR = "user_avator";

    public static final String START_AT = "start_at";
    public static final String END_AT = "end_at";
    public static final String ENDROLL_BEFORE = "endroll_before";
    public static final String PLACE_AT = "place_at";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String TAGS = "tags";
    public static final String MAX_PEOPLE = "max_people";
    public static final String RESTRICTION = "restriction";

    public static final String THUMBNAIL_PIC = "thumbnail_pic";
    public static final String ORIGINAL_PIC = "original_pic";
}
