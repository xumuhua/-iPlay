package com.ucas.iplay.app;

import android.os.Environment;

/**
 * 应用配置
 * Created by ivanchou on 1/15/2015.
 */
public class Config {
    public static final int PAGE_SIZE = 20;// 每页条数
    public static final String PREFS_NAME = "ucasdemo";//SharedPreferences 名字

    public static final class URL {
        public static final String SERVER = "http://ssh.freeshell.ustc.edu.cn:43175/iplay2.3/";
        public static final String COMMON = SERVER + "api/";
        public static final String CAPTCHA = COMMON + "getCaptcha"; //验证码
        public static final String LOGIN = COMMON + "login";
    }

    /**
     * 开发选项设置 *
     */
    public static final class MODE {
        public static final boolean ISDEVEL = true;// 开发者模式
        public static final boolean ISDEBUG = true;// 调试模式
        public static final boolean ISCYCLE = true;// 查看Activity、Fragment生命周期
    }

    /**
     * 数据库设置 *
     */
    public static final class DATABASE {
        public static final int VERSION = 1;// 版本
        public static final String NAME = "ucasdemo.db"; // 数据库名
        public static final String EVENTS_TABLE_NAME = "event";
        public static final String TAGS_TABLE_NAME = "tag";
        public static final String PATH = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/com.ivanchou.ucasdemo/";// 路径

    }

}
