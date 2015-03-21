package com.ucas.iplay.ui.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ucas.iplay.app.Config;
import com.ucas.iplay.app.IplayApp;
//import com.ucas.iplay.core.model.BaseSerial;

/**
 * Created by ivanchou on 1/15/2015.
 */
public class BaseActivity extends ActionBarActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private final boolean ISCYCLE = Config.MODE.ISCYCLE;
    protected IplayApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IplayApp) getApplication();
        app.addActivity(this);
        logLifeCycle();
    }

//    protected <V extends BaseSerial> V getSerializableExtra(final String name) {
//        return (V) getIntent().getSerializableExtra(name);
//    }

    /**
     * Get intent extra
     *
     * @param name
     * @return int
     */
    protected int getIntExtra(final String name) {
        return getIntent().getIntExtra(name, -1);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return string
     */
    protected String getStringExtra(final String name) {
        return getIntent().getStringExtra(name);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return string array
     */
    protected String[] getStringArrayExtra(final String name) {
        return getIntent().getStringArrayExtra(name);
    }

    // 以下6个重写方法用于调试查看activity生命周期
    @Override
    protected void onStart() {
        super.onStart();
        logLifeCycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logLifeCycle();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logLifeCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logLifeCycle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        logLifeCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logLifeCycle();
    }

    private void logLifeCycle() {
        if (ISCYCLE) {
            String methodName = Thread.currentThread().getStackTrace()[3]
                    .getMethodName();
            Log.d(TAG, "[" + TAG + "]→" + methodName + "!!!");
        }
    }
}
