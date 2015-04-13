package com.ucas.iplay.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.R;
import com.ucas.iplay.core.model.UserModel;
import com.ucas.iplay.ui.base.BaseFragment;
import com.ucas.iplay.util.HttpUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivanchou on 1/19/2015.
 */
public class UserInfoFragment extends BaseFragment {

    private int mPage;
    private String mTitle;

    private ImageView mAvatarIv;
    private TextView mNameTv;
    private TextView mSexTv;
    private TextView mSignTv;
    private TextView mBirthdayTv;
    private TextView mSchoolTv;
    private TextView mAcademyTv;
    private TextView mMajorTv;

    public static UserInfoFragment newInstance(int page, String title) {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        bundle.putString("title", title);
        userInfoFragment.setArguments(bundle);
        return userInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("page", 0);
        mTitle = getArguments().getString("title");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        mAvatarIv = (ImageView) view.findViewById(R.id.iv_user_avatar);
        mNameTv = (TextView) view.findViewById(R.id.tv_username);
        mSexTv = (TextView) view.findViewById(R.id.tv_sex);
        mSignTv = (TextView) view.findViewById(R.id.tv_sign);
        mBirthdayTv = (TextView) view.findViewById(R.id.tv_birthday);
        mSchoolTv = (TextView) view.findViewById(R.id.tv_school);
        mAcademyTv = (TextView) view.findViewById(R.id.tv_academy);
        mMajorTv = (TextView) view.findViewById(R.id.tv_major);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 根据 url 请求头像

        // 载入个人基本信息
        getUserData();
    }

    private void getUserData() {
        HttpUtil.getSelfInfo(getActivity(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response.toString());
                try {
                    UserModel userModel = new UserModel();
                    userModel.parse(response);

                    mNameTv.setText(userModel.name);
                    mSexTv.setText(String.valueOf(userModel.sex));
                    mSignTv.setText(userModel.sign);
                    mBirthdayTv.setText(String.valueOf(userModel.birthday));
                    mSchoolTv.setText(userModel.schoolName);
                    mAcademyTv.setText(userModel.academyName);
                    mMajorTv.setText(userModel.majorName);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
