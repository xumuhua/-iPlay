package com.ucas.iplay.ui;

import android.content.Intent;
import android.os.Bundle;

import com.ucas.iplay.ui.base.BaseActivity;
import com.ucas.iplay.util.SharedPreferencesUtil;


/**
 * Created by ivanchou on 1/15/2015.
 */
public class EntranceActivity extends BaseActivity {
    private SharedPreferencesUtil mSpUtil;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpUtil = SharedPreferencesUtil.getSharedPreferencesUtil(this);

//        if (mSpUtil.get("user").equals("")) {
//            mIntent = new Intent(getApplicationContext(), LoginActivity.class);
//        } else {
            mIntent = new Intent(getApplicationContext(), MainActivity.class);
            mIntent.putExtra("user", mSpUtil.get("user"));
//        }

        startActivity(mIntent);
        finish();
    }
}
