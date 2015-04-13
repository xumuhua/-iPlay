package com.ucas.iplay.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ucas.iplay.R;
import com.ucas.iplay.core.model.EventModel;
import com.ucas.iplay.ui.adapter.EventListAdapter;
import com.ucas.iplay.ui.base.BaseFragment;
import com.ucas.iplay.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivanchou on 4/11/15.
 */
public class UserPostFragment extends BaseFragment {
    private ListView mListView;
    private List<EventModel> mEventModelList;
    private EventListAdapter mEventListAdapter;

    public static UserPostFragment newInstance(int page, String title) {
        UserPostFragment userPostFragment = new UserPostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        bundle.putString("title", title);
        userPostFragment.setArguments(bundle);
        return userPostFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventModelList = new ArrayList<EventModel>();
        mEventListAdapter = new EventListAdapter(getActivity(), mEventModelList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alltag, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_alltag);
        mListView.setAdapter(mEventListAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUserPostData();
    }

    private void getUserPostData() {
        RequestParams requestParams = new RequestParams();
        HttpUtil.getEventByUserId(getActivity(), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    getModels(response.getJSONArray("createhistories"));
                    mEventListAdapter.notifyDataSetChanged();
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

    private void getModels(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            EventModel model = new EventModel();
            model.parse(response.getJSONObject(i));
            mEventModelList.add(model);
        }
    }
}
