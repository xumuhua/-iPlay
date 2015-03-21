package com.ucas.iplay.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ucas.iplay.app.Config;
import com.ucas.iplay.core.dbinfo.EventsDBInfo;
import com.ucas.iplay.core.dbinfo.TagsDBInfo;

/**
 * Created by ivanchou on 1/21/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String EVENT_TABLE_NAME = Config.DATABASE.EVENTS_TABLE_NAME;

    /** 创建表的 SQL 语句 */
    private static final String EVENT_TABLE_CREATE = "CREATE TABLE " + EVENT_TABLE_NAME + " " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "create_at INTEGER NOT NULL, " +
            "jointed INTEGER, " +
            "author_id INTEGER NOT NULL, " +
            "start_at INTEGER NOT NULL, " +
            "place_at TEXT NOT NULL, " +
            "title TEXT NOT NULL, " +
            "text TEXT, " +
            "tags INTEGER NOT NULL, " +
            "thumbnail_pic TEXT, " +
            "original_pic TEXT)";

    public DBHelper(Context context) {
        super(context, Config.DATABASE.NAME, null, Config.DATABASE.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(EVENT_TABLE_CREATE);
        EventsDBInfo.TABLE.create(db);
        TagsDBInfo.TABLE.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE_NAME);
        EventsDBInfo.TABLE.drop(db);
        TagsDBInfo.TABLE.drop(db);
    }

}
