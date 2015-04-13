package com.ucas.iplay.core.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ivanchou on 1/27/2015.
 */
public class UserModel {

    public int userId;// 用户id

    public String avatar;// 用户头像 url

    public int schoolId;

    public String schoolName;

    public long birthday;

    public int sex;

    public String phone;

    public String name;

    public long registerTime;

    public int academyId;

    public String academyName;

    public String sign;// 个人介绍

    public long interestedTags;

    public int majorId;

    public String majorName;

    public long lastLoginTime;

    public String thumbnailPhoto;

    public String realname;

    public int qq;

    public void parse(JSONObject jsonObject) throws JSONException {
        userId = jsonObject.getInt("userid");
        avatar = jsonObject.getString("photo");
        birthday = jsonObject.getLong("birthday");
        sex = jsonObject.getInt("sex");
        phone = jsonObject.getString("phone");
        name = jsonObject.getString("nickname");
        registerTime = jsonObject.getLong("registertime");
        schoolId = jsonObject.getInt("schoolid");
        academyId = jsonObject.getInt("academyid");
        majorId = jsonObject.getInt("majorid");
        schoolName = jsonObject.getString("schoolname");
        academyName = jsonObject.getString("academyname");
        majorName = jsonObject.getString("majorname");
        sign = jsonObject.getString("sign");
        realname = jsonObject.getString("realname");
        interestedTags = jsonObject.getLong("interestedtags");
        qq = jsonObject.getInt("qq");
    }
}
