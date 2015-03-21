package com.ucas.iplay.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ucas.iplay.app.Config;

/**
 * Created by ivanchou on 1/18/2015.
 */
public class SharedPreferencesUtil {
    private static SharedPreferencesUtil mSpUtil;
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(Config.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static final SharedPreferencesUtil getSharedPreferencesUtil(Context context) {
        if (mSpUtil == null) {
            mSpUtil = new SharedPreferencesUtil(context);
        }
        return mSpUtil;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
