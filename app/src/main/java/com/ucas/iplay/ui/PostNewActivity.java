package com.ucas.iplay.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ucas.iplay.R;
import com.ucas.iplay.app.IplayApp;
import com.ucas.iplay.util.HttpUtil;
import com.ucas.iplay.util.SPUtil;
import com.ucas.iplay.util.StringUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

/**
 * Created by ivanchou on 3/22/15.
 */
public class PostNewActivity extends FragmentActivity implements View.OnClickListener, CalendarDatePickerDialog.OnDateSetListener, RadialTimePickerDialog.OnTimeSetListener {
    protected final String TAG = this.getClass().getSimpleName();
    protected IplayApp app;

    private static final String START_DATE_PICKER = "start_date_picker";
    private static final String START_TIME_PICKER = "start_time_picker";
    private static final String END_DATE_PICKER = "end_date_picker";
    private static final String END_TIME_PICKER = "end_time_picker";

    private final int SELECT_PHOTO = 1;


    private TextView mStartDateTv;
    private TextView mStartTimeTv;
    private TextView mEndDateTv;
    private TextView mEndTimeTv;

    private EditText mTitleEt;
    private EditText mPlaceEt;
    private EditText mContentEt;

    private Switch mStatusSw;
    private Button mSelectorBtn;
    private ImageView mPictureIv;

    private String mImagePath;
    private boolean mStatus = false;

    private long startDate;
    private long startTime;
    private long endDate;
    private long endTime;
    private long startAt;
    private long endAt;

    private SPUtil sp;



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECT_PHOTO:
                    mImagePath = (String) msg.obj;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IplayApp) getApplication();
        app.addActivity(this);
        setContentView(R.layout.fragment_postnew);


        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        mStartDateTv = (TextView) findViewById(R.id.tv_start_date);
        mStartTimeTv = (TextView) findViewById(R.id.tv_start_time);
        mEndDateTv = (TextView) findViewById(R.id.tv_end_date);
        mEndTimeTv = (TextView) findViewById(R.id.tv_end_time);

        mTitleEt = (EditText) findViewById(R.id.et_title);
        mPlaceEt = (EditText) findViewById(R.id.et_place);
        mContentEt = (EditText) findViewById(R.id.et_content);

        mStatusSw = (Switch) findViewById(R.id.sw_status);
        mSelectorBtn = (Button) findViewById(R.id.btn_picture_selector);
        mPictureIv = (ImageView) findViewById(R.id.iv_event_picture);


        mStartDateTv.setOnClickListener(this);
        mStartTimeTv.setOnClickListener(this);
        mEndDateTv.setOnClickListener(this);
        mEndTimeTv.setOnClickListener(this);
        mSelectorBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        FragmentManager fm;
        Calendar calendar;
        CalendarDatePickerDialog calendarDatePickerDialog;
        RadialTimePickerDialog timePickerDialog;

        switch (v.getId()) {
            case R.id.tv_start_date:
                fm = getSupportFragmentManager();
                calendar = Calendar.getInstance();
                calendarDatePickerDialog = CalendarDatePickerDialog
                        .newInstance(PostNewActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                calendarDatePickerDialog.show(fm, START_DATE_PICKER);
                break;
            case R.id.tv_start_time:
                fm = getSupportFragmentManager();
                calendar = Calendar.getInstance();
                timePickerDialog = RadialTimePickerDialog
                        .newInstance(PostNewActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                DateFormat.is24HourFormat(PostNewActivity.this));
                timePickerDialog.show(fm, START_TIME_PICKER);
                break;
            case R.id.tv_end_date:
                fm = getSupportFragmentManager();
                calendar = Calendar.getInstance();
                calendarDatePickerDialog = CalendarDatePickerDialog
                        .newInstance(PostNewActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                calendarDatePickerDialog.show(fm, END_DATE_PICKER);
                break;
            case R.id.tv_end_time:
                fm = getSupportFragmentManager();
                calendar = Calendar.getInstance();
                timePickerDialog = RadialTimePickerDialog
                        .newInstance(PostNewActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                                DateFormat.is24HourFormat(PostNewActivity.this));
                timePickerDialog.show(fm, END_TIME_PICKER);
                break;

            case R.id.sw_status:
                if (mStatusSw.isChecked()) {
                    mStatus = false;
                } else {
                    mStatus = true;
                }
                break;
            case R.id.btn_picture_selector:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        String tag = calendarDatePickerDialog.getTag();
        Calendar calendar = Calendar.getInstance();
        if (tag.equals(START_DATE_PICKER)) {
            calendar.set(year, monthOfYear, dayOfMonth);
            startDate = calendar.getTimeInMillis();
            mStartDateTv.setText(StringUtil.parseDateToString(year, monthOfYear, dayOfMonth));
            return;
        }
        if (tag.equals(END_DATE_PICKER)) {
            calendar.set(year, monthOfYear, dayOfMonth);
            endDate = calendar.getTimeInMillis();
            mEndDateTv.setText(StringUtil.parseDateToString(year, monthOfYear, dayOfMonth));
            return;
        }

    }


    @Override
    public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hourOfDay, int minute) {
        String tag = radialTimePickerDialog.getTag();
        if (tag.equals(START_TIME_PICKER)) {
            startTime = (hourOfDay * 60 + minute) * 6000;
            mStartTimeTv.setText(StringUtil.parseTimeToString(hourOfDay, minute));
            return;
        }
        if (tag.equals(END_TIME_PICKER)) {
            endTime = (hourOfDay * 60 + minute) * 6000;
            mEndTimeTv.setText(StringUtil.parseTimeToString(hourOfDay, minute));
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 返回图片路径
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    String picturePath = StringUtil.getRealPathFromURI(this, imageUri);

                    Message msg = new Message();
                    msg.what = SELECT_PHOTO;
                    msg.obj = picturePath;
                    mHandler.sendMessage(msg);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_event_post:
                postNewEvent();
                break;
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布新活动
     */
    private void postNewEvent() {
        RequestParams params = new RequestParams();
        startAt = startDate + startTime;
        endAt = endDate + endTime;
        // 打包所有请求参数
        sp = SPUtil.getSPUtil(this);
        params.put("sessionid", sp.get("sessionid"));
        params.put("startat", String.valueOf(startAt));
        params.put("endat", String.valueOf(endAt));
        params.put("enrollbefore", "1000020020");
        params.put("placeat", "Beijing, China");
        params.put("title", mTitleEt.getText());
        params.put("text", mContentEt.getText());
        params.put("tags", "1");
        params.put("maxpeople", "0");
        params.put("status", mStatus?1:0);
//        params.put("myactivityid", "6951713b-6a53-45c5-b884-22fcd2ec9a87");


        HttpUtil.createNewEvent(this, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, response.toString());
                if (mImagePath != null) {
                    try {
                        uploadEventPhoto(String.valueOf(response.get("activityid")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString);
            }
        });
    }


    private void uploadEventPhoto(String eventId) {
        RequestParams params = new RequestParams();
        params.put("activityid", eventId);
        params.put("sessionid", sp.get("sessionid"));
        File f = new File(mImagePath);
        if (f.exists()) {
            try {
                params.put("picname", f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }

        HttpUtil.uploadPhoto(this, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, response.toString());
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
