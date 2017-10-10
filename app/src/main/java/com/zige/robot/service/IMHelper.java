package com.zige.robot.service;

import android.content.Context;
import android.util.Log;

import com.tencent.TIMConnListener;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;

/**
 * Created by jincan on 2016/6/29.
 */
public class IMHelper {
    private static IMHelper instance;
    private final static String TAG = "IMHelper";
    private Context mContext;
    public final static String ImHelper_Connected = "im_connect";
    public final static String ImHelper_DisConnected = "im_disconnect";
    public final static String ImHelper_WifiNeedAuth = "im_wifineedauth";

    private IMHelper(Context context) {
        Log.e(TAG, "IMHelper init Version = " + TIMManager.getInstance().getVersion());
        mContext = context;
        TIMManager.getInstance().setLogLevel(TIMLogLevel.values()[TIMLogLevel.DEBUG.ordinal()]);
        //禁用crash上报
        TIMManager.getInstance().disableCrashReport();
        //设置网络连接监听器，连接建立／断开时回调
        TIMManager.getInstance().setConnectionListener(new TIMConnListener() {//连接监听器
            @Override
            public void onConnected() {//连接建立
//                ToastUtils.showToast("腾讯云连接成功");
                Log.e(TAG, "xxxxxxxxx  connected");
//                ServiceLogin.isLogin = true;
//                mContext.sendBroadcast(new Intent(ImHelper_Connected));
            }

            @Override
            public void onDisconnected(int code, String desc) {//连接断开
                //接口返回了错误码code和错误描述desc，可用于定位连接断开原因
                //错误码code含义请参见错误码表
//                ToastUtils.showToast("腾讯云连接断开");
                Log.e(TAG, "xxxxxxxx  disconnected: " + desc);
//                ServiceLogin.isLogin = false;
//                mContext.sendBroadcast(new Intent(ImHelper_DisConnected));
            }

            @Override
            public void onWifiNeedAuth(String s) {
                Log.e(TAG, "xxxxxxxxxxxxxx    onWifiNeedAuth: " + s);
//                ToastUtils.showToast("腾讯云连接认证");
            }
        });

////        //设置用户状态变更监听器，在回调中进行相应的处理
//        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
//            @Override
//            public void onForceOffline() {
//                //被踢下线
//                //离线状态下被其他终端踢下线
//                ToastUtils.showToast("离线状态下被其他终端踢下线");
////                iContext.startService(new Intent(iContext, ServiceLogin.class));
//            }
//
//            @Override
//            public void onUserSigExpired() {
//                //票据过期，需要换票后重新登录
////                refreshUserSig();
//            }
//        });
        init();
    }

    public static IMHelper getInstance(Context c) {
        if (instance == null) instance = new IMHelper(c);
        return instance;
    }

    public void init() {
        //初始化imsdk
        TIMManager.getInstance().init(mContext);
        //禁止服务器自动代替上报已读
        TIMManager.getInstance().disableAutoReport();
        TIMManager.getInstance().disableRecentContact();
    }

    public void uninit() {
        if (instance == null) {
            Log.e(TAG, "IMHelper not init");
            return;
        }
        TIMManager.getInstance().logout();
    }

}
