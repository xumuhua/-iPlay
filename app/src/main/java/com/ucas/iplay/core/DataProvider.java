package com.ucas.iplay.core;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.ucas.iplay.app.Config;
import com.ucas.iplay.core.db.DBHelper;

public class DataProvider extends ContentProvider {
    protected final String TAG = this.getClass().getSimpleName();

    private static final String AUTHORITY = "com.ucas.iplay";
    private static final String SCHEME = "content://";
    private static final String PATH_TAGS = "/tags";
    private static final String PATH_EVENTS = "/events";

    public static final Uri TAGS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_TAGS);
    public static final Uri EVENTS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_EVENTS);

    private static final int EVENT = 1;// 单条消息
    private static final int EVENTS = 2;// 全部消息
    private static final int TAGS = 3;// 全部标签
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH) {{
            addURI(AUTHORITY, "events", EVENTS);
            addURI(AUTHORITY, "events/#", EVENT);// 带通配符的用于请求单条消息
            addURI(AUTHORITY, "tags", TAGS);
    }};

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, " --------- on create ");
        }
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, " --------- insert ");
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = 0;
        db.beginTransaction();
        try {
            rowId = db.insert(matchTable(uri), null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();

        }

        if (rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, " --------- bulk insert ");
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for(ContentValues contentValues : values){
                db.insertWithOnConflict(matchTable(uri), BaseColumns._ID, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            return values.length;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        throw new SQLException("Failed to insert row into "+ uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, " ---------  delete ");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = -1;
        db.beginTransaction();
        try {
            count = db.delete(matchTable(uri), selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, " --------- update  ");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = -1;
        db.beginTransaction();
        try {
            count = db.update(matchTable(uri), values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, " --------- query ");
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(matchTable(uri));

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
//        int flag = URI_MATCHER.match(uri);
//        String type = null;
//        switch (flag) {
//            case EVENT:
//                type = "vnd.android.cursor.item/event";
//                break;
//            case EVENTS:
//                type = "vnd.android.cursor.dir/events";
//                break;
//        }
//        return type;
        return null;
    }

    private String matchTable(Uri uri) {
        String table;
        switch (sUriMatcher.match(uri)){
            case EVENT:
            case EVENTS:
                table = Config.DATABASE.EVENTS_TABLE_NAME;
                break;
            case TAGS:
                table = Config.DATABASE.TAGS_TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        return table;
    }
}
