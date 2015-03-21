package com.ucas.iplay.core.dbinfo;

import com.ucas.iplay.app.Config;
import com.ucas.iplay.core.db.Column.DataType;
import com.ucas.iplay.core.db.SQLiteTable;

/**
 * event 表的定义
 * Created by ivanchou on 1/28/2015.
 */
public class EventsDBInfo extends BaseEventsDBInfo {

    public static final SQLiteTable TABLE = new SQLiteTable(Config.DATABASE.EVENTS_TABLE_NAME)
            .addColumn(EVENT_ID, DataType.INTEGER)
            .addColumn(CREATED_AT, DataType.TEXT)
            .addColumn(JOINTED, DataType.INTEGER)
            .addColumn(USER_ID, DataType.INTEGER)
            .addColumn(USER_NAME, DataType.TEXT)
            .addColumn(USER_AVATAR, DataType.TEXT)
            .addColumn(START_AT, DataType.TEXT)
            .addColumn(END_AT, DataType.TEXT)
            .addColumn(ENDROLL_BEFORE, DataType.TEXT)
            .addColumn(PLACE_AT, DataType.TEXT)
            .addColumn(TITLE, DataType.TEXT)
            .addColumn(CONTENT, DataType.TEXT)
            .addColumn(TAGS, DataType.INTEGER)
            .addColumn(MAX_PEOPLE, DataType.INTEGER)
            .addColumn(RESTRICTION, DataType.INTEGER)
            .addColumn(THUMBNAIL_PIC, DataType.TEXT)
            .addColumn(ORIGINAL_PIC, DataType.TEXT);
}
