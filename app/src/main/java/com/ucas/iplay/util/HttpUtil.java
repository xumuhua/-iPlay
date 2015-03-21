package com.ucas.iplay.util;

import android.content.Context;
import android.util.Log;

import com.ucas.iplay.app.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by ivanchou on 1/21/2015.
 */
public class HttpUtil {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public HttpUtil(Context context) {
        this.mContext = context;
        client.setTimeout(10 * 1000);
        client.setCookieStore(new PersistentCookieStore(mContext));
        client.addHeader("user-agent", "ucasdemo");
    }

    /** post数据交互 */
    public void post(String url, RequestParams params,
                     TextHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    /** get数据交互 */
    public void get(String url, RequestParams params,
                    AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }
    /** get数据交互 */
    public void get(String url, RequestParams params,
                    TextHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }


    /**
     * 注册
     */
    public static void singUp(Context context) {

    }

    /**
     * 登陆
     */
    public static void logIn(Context context, String name, String pwd, final JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("username", name);
        params.put("password", StringUtil.parseStringToMD5(pwd));
        new AsyncHttpClient().post(context, LOG_IN, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(responseString);
                responseHandler.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

        });
    }

    /**
     * 获取所有标签
     * @param responseHandler
     */
    public static void getAllTags(Context context, final JsonHttpResponseHandler responseHandler) {
        new AsyncHttpClient().get(context, TAGS, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                responseHandler.onSuccess(statusCode, headers, response);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 获取主时间线（不带参数的）
     * @param page
     * @param responseHandler
     */
    public static void getLatestEvents(Context context, int page, final JsonHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));
        new AsyncHttpClient().get(context, LATEST_EVENTS, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                responseHandler.onSuccess(statusCode, headers, response);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 获取活动详情
     * @param eventId 活动id
     * @param responseHandler
     */
    public static void getEventByEventId(Context context, int eventId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        SharedPreferencesUtil sp = SharedPreferencesUtil.getSharedPreferencesUtil(context);
        params.put("sessionid", sp.get("sessionid"));
        params.put("activityid", eventId);
        new AsyncHttpClient().get(context, EVENT_DETAILS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 修改活动信息
     * @param eventId
     * @param responseHandler
     */
    public static void modifyEventByEventId(Context context, int eventId, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 删除某一活动
     * @param eventId
     * @param responseHandler
     */
    public static void deleteEventByEventId(Context context, int eventId, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @param responseHandler
     */
    public static void getUserInfoByUserId(Context context, int userId, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 获取个人信息
     * @param responseHandler
     */
    public static void getSelfInfo(Context context, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 修改个人信息
     */
    public static void modifySelfInfo(Context context) {

    }

    /**
     * 获取参加活动的用户列表
     * @param eventId
     * @param responseHandler
     */
    public static void getJointedUserOfEvent(Context context, int eventId, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 获取用户发布活动纪录
     * @param page
     * @param pageSize
     * @param beginId
     * @param responseHandler
     */
    public static void getEventByUserId(Context context, int page, int pageSize, int beginId, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 获取用户参与的活动纪录
     * @param page
     * @param pageSize
     * @param beginId
     * @param responseHandler
     */
    public static void getJointedEventOfUser(Context context, int page, int pageSize, int beginId, JsonHttpResponseHandler responseHandler) {

    }

    /**
     * 改变参与否状态
     * @param eventId
     * @param responseHandler
     */
    public static void changeStateOfEvent(Context context, int eventId, JsonHttpResponseHandler responseHandler) {

    }

    /** 注册 **/
    private static final String SIGN_UP = Config.URL.COMMON + "authorize/newregister.html";

    private static final String SCHOOLS = Config.URL.COMMON + "public/shools.json";

    private static final String ACADEMIES = Config.URL.COMMON + "public/academies.json";

    private static final String MAJORS = Config.URL.COMMON + "public/majors.json";

    private static final String TAGS = Config.URL.COMMON + "public/tags.json";

    /** 活动列表 **/
    private static final String LATEST_EVENTS = Config.URL.COMMON + "activities/list.json";

    /** 活动详情 **/
    private static final String EVENT_DETAILS = Config.URL.COMMON + "activities/show.json";

    /** 登陆 **/
    private static final String LOG_IN = Config.URL.COMMON + "authorize/login.json";

    /** 登出 **/
    private static final String LOG_OUT = Config.URL.COMMON + "authorize/logout.html";

    /** 创建活动 **/
    private static final String CREATE_EVENT = Config.URL.COMMON + "activities/create.html";

    /** 活动参与者 **/
    private static final String JOINER = Config.URL.COMMON + "join/show.json";

    /** 用户发布的所有活动 **/
    private static final String USER_EVENTS = Config.URL.COMMON + "activities/createhistory.json";

    /** 用户对活动的操作 **/
    private static final String USER_EVENT_STATE = Config.URL.COMMON + "join/changestate.html";

    /** 用户参与的所有活动 **/
    private static final String USER_JOIN_EVENTS = Config.URL.COMMON + "join/joinhistory.json";

    /** 查看个人信息 **/
    private static final String USER_SELFINFO = Config.URL.COMMON + "authorize/lookselfinfo.json";

    /** 修改补全个人信息 **/
    private static final String CHANGE_SELFINFO = Config.URL.COMMON + "authorize/changeselfinfo.html";

    /** 修改活动信息 **/
    private static final String CHANGE_EVENTINFO = Config.URL.COMMON + "activities/changeinfo.json";

    /** 删除活动 **/
    private static final String DEL_EVENT = Config.URL.COMMON + "activities/delete.html";
}
