package com.ucas.iplay.ui.fragment;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ucas.iplay.R;
import com.ucas.iplay.app.Config;
import com.ucas.iplay.core.db.EventsDataHelper;
import com.ucas.iplay.core.db.TagsDataHelper;
import com.ucas.iplay.core.model.EventModel;
import com.ucas.iplay.core.model.TagModel;
import com.ucas.iplay.core.tasker.AllTagsParseTask;
import com.ucas.iplay.ui.adapter.EventCursorAdapter;
import com.ucas.iplay.ui.view.FooterTagsView;
import com.ucas.iplay.ui.view.FooterTagsView.OnTagClickListener;
import com.ucas.iplay.ui.view.QuickReturnListView;
import com.ucas.iplay.ui.view.QuickReturnListView.DataChangeListener;
import com.ucas.iplay.ui.base.BaseFragment;
import com.ucas.iplay.util.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchou on 1/19/2015.
 */
public class TimeLineFragment extends BaseFragment implements OnRefreshListener, DataChangeListener, OnTagClickListener, LoaderCallbacks<Cursor> {
    private SwipeRefreshLayout mSwipeLayout;// 下拉刷新
    private QuickReturnListView mListView;
    private List<EventModel> mEventsList;


    private FooterTagsView footerTagsView;
    private EventCursorAdapter mEventCursorAdapter;
    private EventsDataHelper mEventsDataHelper;
    private TagsDataHelper mTagsDataHelper;

    private int mPage;
    private boolean mLoadFromCache;

    private TagModel[] mTags;
    private HttpUtil mHttpUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventsList = new ArrayList<EventModel>();
        mEventsDataHelper = new EventsDataHelper(context);
        mTagsDataHelper = new TagsDataHelper(context);
        mEventCursorAdapter = new EventCursorAdapter(context);
        mHttpUtil = new HttpUtil(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listview_timeline, container, false);
        mListView = (QuickReturnListView) view.findViewById(R.id.lv_maintimeline);
        footerTagsView = (FooterTagsView) view.findViewById(R.id.ftv_footer);

        mTags = mTagsDataHelper.query();
        if (mTags != null || mTags.length != 0) {
            footerTagsView.setCustomTags(mTags);
            footerTagsView.setOnTagClickListener(this);
            mListView.setTagsView(footerTagsView);
        }

        // 设置 listview 自动加载下一页
        mListView.setDataChangeListener(this);
        mListView.setAdapter(mEventCursorAdapter);

        // 设置下拉刷新
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mTags.length == 0) {
            // 标签为空则清空所有
            mEventsDataHelper.empty();
            getTagsData();
        } else {
            getEventsData();
        }
    }

    /**
     * 从服务器获取标签信息
     */
    public void getTagsData() {
        // 首先获得所有标签信息存入数据库
        HttpUtil.getAllTags(getActivity(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, "----> " + response);
                try {
                    JSONArray array = response.getJSONArray("academies");
                    new AllTagsParseTask(getActivity()) {
                        @Override
                        protected void onPostExecute(ArrayList<TagModel> tagModels) {
                            mTagsDataHelper.bulkInsert(tagModels);
                            // 再获取所有活动 调用 getEventsData
                            getEventsData();
                        }
                    }.execute(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    /**
     * 加载活动
     */
    public void getEventsData() {
        if (mEventsDataHelper.query().length == 0) {
            // 第一次载入若数据库为空，则请求服务器
            mPage = 1;
            getData();
            mLoadFromCache = false;
        } else {
            // 否则加载数据库缓存
            mLoadFromCache = true;
        }
    }

    /**
     * 从服务器获取活动
     */
    public void getData() {
        if (mPage == 1) {
            mLoadFromCache = false;
        }
        // 加载活动
        HttpUtil.getLatestEvents(getActivity(), mPage, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, "----> " + response);
                try {
                    if (mPage == 1) {
                        mEventsDataHelper.empty();
                    }
                    mEventsDataHelper.bulkInsert(getModels(response.getJSONArray("activities")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "----> failure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    /**
     * 下拉刷新的回调
     */
    @Override
    public void onRefresh() {
        mPage = 1;
        getData();
//        mSwipeLayout.setRefreshing(true);
    }

    /**
     * 滚送到底部的回调
     */
    @Override
    public void onLoadMore() {
        Log.e(TAG, "----on load more----");

        // 从网络加载更多的数据

    }

    /**
     * 点击标签触发的回调
     * @param tags 选中的 tag 列表
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTagClickRefresh(int tags) {

        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, "------- tags " + Integer.toBinaryString(tags));
        }

        /**
         * 1. 在本地过滤一遍
         * 2. 请求服务器
         * 3. 服务器响应后清空本地
         */

        // 服务器获取数据
        getData();
        // changeCursor
    }

    /**
     * 长按标签触发的回调，长按的事件定义为单选
     * @param tags 选中的 tag 列表
     */
    @Override
    public void onTagLongClickRefresh(int tags) {
        if (Config.MODE.ISDEBUG) {
            Log.e(TAG, "----on long click refresh----");
            Log.e(TAG, "------- tags " + Integer.toBinaryString(tags));
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, "-------on create loader-----");
        return mEventsDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "-------on load finished-----");

        mEventCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, "-------on loader reset-----");

        mEventCursorAdapter.changeCursor(null);
    }

    /**
     * 填充假数据 card list view
     */
    private void initEvenListData() {

        for (int i = 0; i < 10; i++) {
            EventModel event = new EventModel();
            event.startAt = "16:00";
            event.title = "测试一下";
            mEventsList.add(event);
        }
    }


    private ArrayList<EventModel> getModels(JSONArray response) throws JSONException {
        ArrayList<EventModel> models = new ArrayList<EventModel>();
        for(int i = 0; i < response.length(); i++){
            EventModel model = new EventModel();
            model.parse(response.getJSONObject(i));
            models.add(model);
        }
        return models;
    }

//    /**
//     * 填充假数据 simple list view
//     *
//     * @return
//     */
//    private List<String> getData() {
//        list.clear();
//        for (int i = 0; i < 14; i++) {
//            list.add(i + "--" + mTags[i % 7]);
//        }
//
//        if (tags == 0) {
//            return list;
//        }
//
//        int tmp;
//        for (int i = 0; i < mTags.length; i++) {
//            tmp = tags;
//            if (((tmp >> i) & 1) == 0) {
//                for (int j = 0, len = list.size(); j < len; j++) {
//                    if (list.get(j).contains(mTags[i])) {
//                        list.remove(j);
//                        len--;
//                        j--;
//                    }
//                }
//            }
//        }
//        return list;
//    }

}
