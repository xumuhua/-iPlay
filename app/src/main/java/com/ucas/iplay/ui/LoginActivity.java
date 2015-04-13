package com.ucas.iplay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ucas.iplay.R;
import com.ucas.iplay.ui.base.BaseActivity;
import com.ucas.iplay.util.HttpUtil;
import com.ucas.iplay.util.SPUtil;
import com.ucas.iplay.util.StringUtil;

import org.apache.http.Header;

public class LoginActivity extends BaseActivity {

    private Intent mIntent;
    private EditText mAccount;
    private EditText mPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mAccount = (EditText) findViewById(R.id.et_login_account);
        mPassword = (EditText) findViewById(R.id.et_login_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MD5", mPassword.getText().toString());

                Log.i("MD5",  StringUtil.parseStringToMD5(mPassword.getText().toString()));
                HttpUtil.logIn(getApplicationContext(), mAccount.getText().toString(), mPassword.getText().toString(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                                Gson gson = new Gson();
                                User newUser = gson.fromJson(responseString, User.class);
                                if (newUser.getReturncode() == 0) {
                                    newUser.setSharedpreference(getApplicationContext());
                                    mIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mIntent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                super.onFailure(statusCode, headers, responseString, throwable);
                            }
                        }
                );


            }
        });
    }

    public class User {
        int schoolid;
        String birthday;
        int sex;
        String phone;
        String nickname;
        String registertime;
        int academyid;
        int userid;
        String sessionid;
        String msg;
        String sign;
        String interestedtags;
        int majorid;
        String lastlogintime;
        int returncode;
        String thumbnailphoto;
        String realname;
        String qq;

        public int getSchoolid() {
            return schoolid;
        }

        public void setSchoolid(int schoolid) {
            this.schoolid = schoolid;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRegistertime() {
            return registertime;
        }

        public void setRegistertime(String registertime) {
            this.registertime = registertime;
        }

        public int getAcademyid() {
            return academyid;
        }

        public void setAcademyid(int academyid) {
            this.academyid = academyid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getSessionid() {
            return sessionid;
        }

        public void setSessionid(String sessionid) {
            this.sessionid = sessionid;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getInterestedtags() {
            return interestedtags;
        }

        public void setInterestedtags(String interestedtags) {
            this.interestedtags = interestedtags;
        }

        public int getMajorid() {
            return majorid;
        }

        public void setMajorid(int majorid) {
            this.majorid = majorid;
        }

        public String getLastlogintime() {
            return lastlogintime;
        }

        public void setLastlogintime(String lastlogintime) {
            this.lastlogintime = lastlogintime;
        }

        public int getReturncode() {
            return returncode;
        }

        public void setReturncode(int returncode) {
            this.returncode = returncode;
        }

        public String getThumbnailphoto() {
            return thumbnailphoto;
        }

        public void setThumbnailphoto(String thumbnailphoto) {
            this.thumbnailphoto = thumbnailphoto;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        @Override
        public String toString() {
            String str = "user:{" +"schoolid:"+schoolid + ",birthday:" +birthday + ",sex:"+sex + ",phone:"
                    + phone + ",nickname:" + nickname + ",registertime:" + registertime + ",academyid:" + academyid
                    +",userid:" + userid + ",sessionid:" + sessionid + ",msg:" + msg + ",sign:" +sign
                    + ".interestedtags" + interestedtags + ",majorid:" + majorid + ",lastlogintime:"
                    +lastlogintime + ",returncode:" + returncode + ",thumbnailphoto:"+thumbnailphoto
                    +",realname:" + realname +",qq:" +qq ;
            return str;
        }

        public void setSharedpreference( Context context) {
            SPUtil spUtil = new SPUtil(context);
            spUtil.put("scohoolid", schoolid + "");
            spUtil.put("birthday", birthday);
            spUtil.put("sex", sex + "");
            spUtil.put("phone", phone);
            spUtil.put("nickname", nickname);
            spUtil.put("registertime", registertime);
            spUtil.put("academuid", academyid + "");
            spUtil.put("userid", userid + "");
            spUtil.put("sessionid", sessionid);
            spUtil.put("msg", msg);
            spUtil.put("sign", sign);
            spUtil.put("interestedtags", interestedtags);
            spUtil.put("majorid", majorid + "");
            spUtil.put("lastlogintime", lastlogintime);
            spUtil.put("returncode", returncode + "");
            spUtil.put("thumnailphoto", thumbnailphoto);
            spUtil.put("realname", realname);
            spUtil.put("qq",qq);
        }
    }


}
