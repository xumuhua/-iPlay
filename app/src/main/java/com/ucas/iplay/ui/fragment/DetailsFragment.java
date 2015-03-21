package com.ucas.iplay.ui.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.core.db.EventsDataHelper;
import com.ucas.iplay.ui.base.BaseFragment;
import com.ucas.iplay.core.model.EventModel;
import com.ucas.iplay.R;
import com.ucas.iplay.util.HttpUtil;
import com.ucas.iplay.core.model.UserModel;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends BaseFragment {

    private Activity mActivity;
    private View mDetailsView;
    private View mFragmentContainerView;
    private PosterAlbumFragment mPosterAlbumFragment;
    private DetailsCallback mCallback;
    private EventsDataHelper mEventsDataHelper;

    public static final int POSTER_ON_CLICK = 0;
    public static final int MAP_ON_CLICK = 1;

    /*  事件参数    */
    private EventModel mEvent;
    private int mEventID;
    private int mUserID;

    private TextView mAuthorNickView;
    private TextView mPlaceAtView;
    private TextView mStartAtView;
    private TextView mTitleView;
    private TextView mContentView;
    private TextView mEndAtView;
    private TextView mSupportView;
    private Button mMapButton;
    private ImageView mPosterView;

    private ImageView [] mImageViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*  获取时间ID和用户ID */
        mEventID = savedInstanceState.getInt("EventID",0);
        mEventsDataHelper = new EventsDataHelper(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*  获取界面组件  */
        View view = inflater.inflate(R.layout.fragment_details,container,false);
        mDetailsView = view;

        mAuthorNickView = (TextView) mDetailsView.findViewById(R.id.tv_details_author_nick);
        mPlaceAtView = (TextView) mDetailsView.findViewById(R.id.tv_details_place_at);
        mStartAtView = (TextView) mDetailsView.findViewById(R.id.tv_details_start_at);
        mTitleView = (TextView) mDetailsView.findViewById(R.id.tv_details_title);
        mMapButton = (Button) mDetailsView.findViewById(R.id.bt_details_map);
        mPosterView = (ImageView) mDetailsView.findViewById(R.id.iv_details_poster_view);
        mEndAtView = (TextView) mDetailsView.findViewById(R.id.tv_details_end_at);
        mContentView = (TextView) mDetailsView.findViewById(R.id.tv_details_content);
        mSupportView = (TextView) mDetailsView.findViewById(R.id.tv_details_supporter);

        /*  设置监听器   */
        setListener();

        /*  初始化界面组件 */
        initData();
        drawView();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        try {
            mCallback = (DetailsCallback)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement DetailsCallback!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
        releaseFragmentStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseFragmentStack();
    }

    /*  绘制界面    */
    private void drawView(){
        mAuthorNickView.setText(mEvent.author.name);
        mPlaceAtView.setText(mEvent.placeAt);
        mStartAtView.setText(mEvent.startAt);
        mTitleView.setText(mEvent.title);
        mContentView.setText(mEvent.content);
        mEndAtView.setText(mEvent.endAt);
        //mSupportView.setText(mEvent.supporter);
    }

    /*  获取数据后刷新界面   */
    public void getData(){
        drawView();

        mAuthorNickView.invalidate();
        mPlaceAtView.invalidate();
        mStartAtView.invalidate();
        mTitleView.invalidate();
        mContentView.invalidate();
        mEndAtView.invalidate();
        mSupportView.invalidate();
    }

    /*  设置事件    */
    public void setEvent(EventModel event){
        mEvent = event;
        getData();
        return;
    }

    /*  设置监听器   */
    private void setListener()
    {

        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        mPosterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailsFragmentClick(POSTER_ON_CLICK);
            }
        });
    }

    /*  释放fragment队列*/
    private void releaseFragmentStack(){
        for (int i =0;i<getFragmentManager().getBackStackEntryCount();i++){
            getFragmentManager().popBackStack();
        }
    }
    /*   启动线程，获取活动  */
    private void initData(){
        mEvent = mEventsDataHelper.queryById(mEventID);
        if (mEvent == null){
            createTestEvent();
            getDataFromHttp();
        }
    }

    /*  从服务器获取数据    */

    private void getDataFromHttp(){
        HttpUtil.getEventByEventId(getActivity(),mEventID,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                getModelFromJason(response);
                List<EventModel> modles = new ArrayList<EventModel>();
                modles.add(mEvent);
                mEventsDataHelper.bulkInsert(modles);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    /*  解析海报图像  */
    private void reSolvePosterImage(){
        int [] mImages = new int[] {
        };

        mImageViews = new ImageView[mImages.length];
        for (int i=0;i<mImages.length;i++){
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setImageResource(mImages[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            mImageViews[i] = imageView;
        }
    }

    private void createTestEvent(){
        if(mEvent == null) {
            mEvent = new EventModel();
        }
        mEvent.author = new UserModel();
        mEvent.author.name = "";
        mEvent.author.userId = 0;
        mEvent.startAt = "--/-/- --:--";
        mEvent.endAt = "--/-/- --:--";
        mEvent.placeAt = "";
        mEvent.title = "";
        mEvent.content = "";
        reSolvePosterImage();

    }

    private void getModelFromJason(JSONObject jsonObject){
        if(mEvent==null){
            mEvent = new EventModel();
        }
        try {
            mEvent.parse(jsonObject);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*  详情界面借口函数
    *   onDetailsFragmentClick
    *       收集来自详情界面的点击事件
    *   getEvent
    *       详情界面读取事件*/
    public void onDetailsFragmentClick(int viewID) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (viewID){
            case DetailsFragment.POSTER_ON_CLICK:
                if (mPosterAlbumFragment == null){
                    mPosterAlbumFragment = new PosterAlbumFragment();
                }
                mPosterAlbumFragment.setImageViews(mImageViews);
                fragmentTransaction.add(R.id.content_frame,mPosterAlbumFragment).addToBackStack("DetailsFragment");
                break;
            case DetailsFragment.MAP_ON_CLICK:
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    public EventModel getEvent() {
        return null;
    }

    /*End Test*/
    public interface DetailsCallback{

    }

}
