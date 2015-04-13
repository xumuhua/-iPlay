package com.ucas.iplay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.ui.base.BaseActivity;
import com.ucas.iplay.util.HttpUtil;
import com.ucas.iplay.util.SPUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by ivanchou on 1/15/2015.
 */
public class EntranceActivity extends BaseActivity {
    private SPUtil mSpUtil;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpUtil = SPUtil.getSPUtil(this);
        if (!mSpUtil.get("logintime").equals("")) {
            long lastLogin = Long.parseLong(mSpUtil.get("logintime"));
            Calendar calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis() - lastLogin > 24 * 60 * 6000) {
                Log.e(TAG, "log in again");
                logIn();
            }
        } else {
            logIn();
        }

//        if (mSpUtil.get("user").equals("")) {
//            mIntent = new Intent(getApplicationContext(), LoginActivity.class);
//        } else {
        mIntent = new Intent(getApplicationContext(), MainActivity.class);
        mIntent.putExtra("user", mSpUtil.get("user"));
//        }

        startActivity(mIntent);
        finish();
    }


    private void logIn() {
        String name = "609881037@qq.com";
        String pwd = "E10ADC3949BA59ABBE56E057F20F883E";
        HttpUtil.logIn(this, name, pwd, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String sessionId = (String) response.get("sessionid");
                    mSpUtil.put("sessionid", sessionId);
                    Calendar calendar = Calendar.getInstance();
                    mSpUtil.put("logintime", String.valueOf(calendar.getTimeInMillis()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
