package com.ucas.iplay.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.R;
import com.ucas.iplay.core.db.TagsDataHelper;
import com.ucas.iplay.ui.adapter.TagsCursorAdapter;
import com.ucas.iplay.ui.base.BaseFragment;
import com.ucas.iplay.ui.view.TagView;
import com.ucas.iplay.util.HttpUtil;
import com.ucas.iplay.util.SPUtil;

/**
 * Created by ivanchou on 4/11/15.
 */
public class TagsFragment extends BaseFragment implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private View mLoadingView;
    private TagsCursorAdapter mTagsAdapter;
    private TagsDataHelper mTagsDataHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTagsDataHelper = new TagsDataHelper(getActivity());
        mTagsAdapter = new TagsCursorAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alltag, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_alltag);
        mLoadingView = view.findViewById(R.id.pb_loding);

        mListView.setAdapter(mTagsAdapter);
        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mTagsDataHelper.query().length == 0) {
            getData();
        }
    }

    private void getData() {
        HttpUtil.getAllTags(getActivity(), new JsonHttpResponseHandler() {

        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mTagsDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        long tags = mTagsDataHelper.getInterestedTagsCode();
        SPUtil sharedPreferencesUtil = SPUtil.getSPUtil(getActivity());
        sharedPreferencesUtil.put(SPUtil.INTERESTED_TAGS, String.valueOf(tags));
        mTagsAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTagsAdapter.changeCursor(null);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TagView tagView = (TagView) view;
        int tagId = tagView.getTagId();
    }
}
