package com.zige.robot.fsj;

import android.os.Environment;

import com.zige.robot.App;

import java.io.File;

/**
 * Created by Phone on 2017/7/14.
 * <p>维持常量的类</p>
 */

public class Constants {

    //================= PATH ====================

    public static final String PATH_DATA = App
            .getInstance()
            .getCacheDir()
            .getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";//缓存路径

    public static final String PATH_SDCARD = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "zige" + File.separator + "robot";

    //========================sp=======================
//    public static final String SP_NIGHT_MODE = "night_mode";
//
//    public static final String SP_NO_IMAGE = "no_image";
//
//    public static final String SP_AUTO_CACHE = "auto_cache";
//
//    public static final String SP_CURRENT_ITEM = "current_item";
//
//    public static final String SP_LIKE_POINT = "like_point";
//
//    public static final String SP_VERSION_POINT = "version_point";
//
//    public static final String SP_MANAGER_POINT = "manager_point";

    public static final String SP_NAME = "fsj";//文件名

    public static final String SP_USERNAME = "username";

    public static final String SP_NICKNAME = "nickname";

    public static final String SP_PHONE = "phone";

    public static final String SP_HX_ACOUNT = "sp_hx_acount";

    public static final String SP_HX_PASSWORD = "sp_hx_password";

    public static final String SP_ROBOT_ID = "robot_id";

    public static final String SP_REMOTE_HX_CONTACT = "remote_hx_contact";

    public static final String SP_CALL_FROM = "call_from";

    public static final String SP_CALL_TO = "call_to";

    public static final String SP_CONSUME_TIME = "consume_time";

    public static final String SP_CALL_ID = "call_id";

    //========================intent=======================
    public static final String IT_PHONE = "phone";

    public static final String IT_CALLER_ID = "aller_id";

    public static final String IT_CALLED_ID = "alled_id";

    public static final String IT_CALL_ID = "call_id";

    public static final String IT_CONSUME_TIME = "onsume_time";

    public static final int ALBUM_PHOTO_SPAN_COUNT = 5;

    //========================request code====================
    public static final int REQUEST_CODE_REGISTER = 0x0000;


    //========================result code====================
    public static final int RESULT_CODE_REGISTER = 0x0100;

    //========================other====================
    public static final long DEFAULT_SELECTED_ALBUM_ID = 200;//默认选中的相册id
}
