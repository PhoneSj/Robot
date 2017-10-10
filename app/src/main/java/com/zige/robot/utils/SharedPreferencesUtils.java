package com.zige.robot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zige.robot.App;
import com.zige.robot.fsj.Constants;
import com.zige.robot.fsj.model.bean.CallInfoBean;
import com.zige.robot.fsj.util.LogUtil;


/**
 * 共享配置工具类
 * Created by SONY-SONG on 2015/3/23.
 */
public class SharedPreferencesUtils {
    /**
     * shared_preference文件名
     */
//    private static final String sharedPreferenceName = Constants.SP_NAME;
//    private static int mode = Context.MODE_PRIVATE;

    public static final String keep_login_userCount = "keep_login_userCount";
    public static final String keep_login_password = "keep_login_password";
    public static final String message_isOpen = "message_isOpen";
    public static final String auto_login = "auto_login";


    public static Object getValue(Context context, String key, Object value) {
        SharedPreferences sp = getSP();
        if (value instanceof Boolean)
            return sp.getBoolean(key, (Boolean) value);
        else if (value instanceof Integer)
            return sp.getInt(key, (Integer) value);
        else if (value instanceof Float)
            return sp.getFloat(key, (Float) value);
        else if (value instanceof Long)
            return sp.getLong(key, (Long) value);
        else if (value instanceof String)
            return sp.getString(key, (String) value);
        else
            return null;
    }

    public static boolean saveValue(Context context, String key, Object value) {
        SharedPreferences sp = getSP();
        Editor ed = sp.edit();
        if (value instanceof Boolean)
            return ed.putBoolean(key, (Boolean) value).commit();
        else if (value instanceof Integer)
            return ed.putInt(key, (Integer) value).commit();
        else if (value instanceof Float)
            return ed.putFloat(key, (Float) value).commit();
        else if (value instanceof Long)
            return ed.putLong(key, (Long) value).commit();
        else if (value instanceof String)
            return ed.putString(key, (String) value).commit();
        else
            return false;
    }

    public static void saveRobotIdToSP(String robotDeviceId) {
        getSP().edit().putString(Constants.SP_ROBOT_ID, robotDeviceId).apply();
        LogUtil.showI("robot device id:" + robotDeviceId);
    }

    public static String getRobotIdFromSP() {
        return getSP().getString(Constants.SP_ROBOT_ID, null);
    }

    public static void saveConnectedHXContact(String contact) {
        getSP().edit().putString(Constants.SP_REMOTE_HX_CONTACT, contact).apply();
        LogUtil.showI("connected hx contact:" + contact);
    }

    public static String getConnectedHXContact() {
        return getSP().getString(Constants.SP_REMOTE_HX_CONTACT, null);
    }

    public static void saveHXAcount(String acount) {
        getSP().edit().putString(Constants.SP_HX_ACOUNT, acount).apply();
        LogUtil.showI("hx aount:" + acount);
    }

    public static String getHXAcount() {
        return getSP().getString(Constants.SP_HX_ACOUNT, null);
    }

    public static void saveHXPassword(String password) {
        getSP().edit().putString(Constants.SP_HX_PASSWORD, password).apply();
        LogUtil.showI("hx password:" + password);
    }

    public static String getHXPassword() {
        return getSP().getString(Constants.SP_HX_PASSWORD, null);
    }

//    public static void saveConsumeTime(int minute) {
//        getSP().edit().putInt(Constants.SP_CONSUME_TIME, minute).apply();
//    }

    public static void saveCallMessage(CallInfoBean bean) {
        getSP().edit().putString(Constants.SP_CALL_FROM, bean.getCallerId()).apply();
        getSP().edit().putString(Constants.SP_CALL_TO, bean.getCalledId()).apply();
        getSP().edit().putInt(Constants.SP_CONSUME_TIME, bean.getConsumeTime()).apply();
        getSP().edit().putString(Constants.SP_CALL_ID, bean.getId()).apply();
    }

    public static CallInfoBean getCallMessage() {
        CallInfoBean bean = new CallInfoBean();
        bean.setCallerId(getSP().getString(Constants.SP_CALL_FROM, null));
        bean.setCalledId(getSP().getString(Constants.SP_CALL_TO, null));
        bean.setConsumeTime(getSP().getInt(Constants.SP_CONSUME_TIME, 0));
        bean.setId(getSP().getString(Constants.IT_CALL_ID, null));
        return bean;
    }

    public static int getConsumeTime() {
        return getSP().getInt(Constants.SP_CONSUME_TIME, 0);
    }

    private static SharedPreferences getSP() {
        return App.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
    }
}
