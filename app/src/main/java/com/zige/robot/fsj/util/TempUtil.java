//package com.zige.robot.fsj.util;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import com.zige.robot.App;
//import com.zige.robot.fsj.Constants;
//import com.zige.robot.fsj.model.bean.CallInfoBean;
//
///**
// * Created by Administrator on 2017/9/13.
// * 临时控制SP存储的工具类
// */
//
//public class TempUtil {
//
//    public static void saveRobotIdToSP(String robotDeviceId) {
//        getSP().edit().putString(Constants.SP_ROBOT_ID, robotDeviceId).apply();
//        LogUtil.showI("robot device id:" + robotDeviceId);
//    }
//
//    public static String getRobotIdFromSP() {
//        return getSP().getString(Constants.SP_ROBOT_ID, null);
//    }
//
//    public static void saveConnectedHXContact(String contact) {
//        getSP().edit().putString(Constants.SP_REMOTE_HX_CONTACT, contact).apply();
//        LogUtil.showI("connected hx contact:" + contact);
//    }
//
//    public static String getConnectedHXContact() {
//        return getSP().getString(Constants.SP_REMOTE_HX_CONTACT, null);
//    }
//
//    public static void saveHXAcount(String acount) {
//        getSP().edit().putString(Constants.SP_HX_ACOUNT, acount).apply();
//        LogUtil.showI("hx aount:" + acount);
//    }
//
//    public static String getHXAcount() {
//        return getSP().getString(Constants.SP_HX_ACOUNT, null);
//    }
//
//    public static void saveHXPassword(String password) {
//        getSP().edit().putString(Constants.SP_HX_PASSWORD, password).apply();
//        LogUtil.showI("hx password:" + password);
//    }
//
//    public static String getHXPassword() {
//        return getSP().getString(Constants.SP_HX_PASSWORD, null);
//    }
//
////    public static void saveConsumeTime(int minute) {
////        getSP().edit().putInt(Constants.SP_CONSUME_TIME, minute).apply();
////    }
//
//    public static void saveCallMessage(CallInfoBean bean) {
//        getSP().edit().putString(Constants.SP_CALL_FROM, bean.getCallerId()).apply();
//        getSP().edit().putString(Constants.SP_CALL_TO, bean.getCalledId()).apply();
//        getSP().edit().putInt(Constants.SP_CONSUME_TIME, bean.getConsumeTime()).apply();
//        getSP().edit().putString(Constants.SP_CALL_ID, bean.getId()).apply();
//    }
//
//    public static CallInfoBean getCallMessage() {
//        CallInfoBean bean = new CallInfoBean();
//        bean.setCallerId(getSP().getString(Constants.SP_CALL_FROM, null));
//        bean.setCalledId(getSP().getString(Constants.SP_CALL_TO, null));
//        bean.setConsumeTime(getSP().getInt(Constants.SP_CONSUME_TIME, 0));
//        bean.setId(getSP().getString(Constants.IT_CALL_ID, null));
//        return bean;
//    }
//
//    public static int getConsumeTime() {
//        return getSP().getInt(Constants.SP_CONSUME_TIME, 0);
//    }
//
//    private static SharedPreferences getSP() {
//        return App.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
//    }
//}
