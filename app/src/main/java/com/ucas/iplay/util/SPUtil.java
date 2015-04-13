package com.ucas.iplay.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ucas.iplay.app.Config;

/**
 * Created by ivanchou on 1/18/2015.
 */
public class SPUtil {
    public static final String NICKNAME = "nickname";
    public static final String REALNAME = "realname";
    public static final String USERID = "userid";
    public static final String PASSWORD = "password";
    public static final String SESSIONID = "sessionid";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final String MOBILEPHONE_NO = "mobilephoneno";
    public static final String SIGN = "sign";
    public static final String PHOTO_URL = "photourl";
    public static final String SCHOOL_ID = "schoolid";
    public static final String SCHOOL_NAME = "schoolname";
    public static final String ACADEMY_ID = "academyid";
    public static final String ACADEMY_NAME = "academyname";
    public static final String MAJOR_ID = "majorid";
    public static final String MAJOR_NAME = "majorname";
    public static final String INTERESTED_TAGS = "interestedtags";
    public static final String THUMBNAIL_PHOTO_URL = "thumnailphotourl";
    public static final String LAST_LOGIN_TIME = "lastlognitime";
    public static final String REGISTER_TIME = "registertime";
    public static final String USER_EVENTS = "userevents";

    private static SPUtil mSpUtil;
    private SharedPreferences mSP;

    public SPUtil(Context context) {
        mSP = context.getSharedPreferences(Config.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static final SPUtil getSPUtil(Context context) {
        if (mSpUtil == null) {
            mSpUtil = new SPUtil(context);
        }
        return mSpUtil;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return mSP.getString(key, "");
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.remove(key);
        editor.commit();
    }
}
