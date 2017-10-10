package com.zige.robot.fsj.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/9/18.
 * 网络工具类
 */

public class MyNetworkUtil {

    public static class NetworkType {
        public static final int NETTYPE_NONE = 0;
        public static final int NETTYPE_LINE = 1;
        public static final int NETTYPE_WIFI = 2;
        public static final int NETTYPE_3G = 3;
        public static final int NETTYPE_2G = 4;
        public static final int NETTYPE_4G = 5;
    }

    public static boolean checkNetwork(View layoutRoot) {
        if (!isNetworkAvailable(layoutRoot.getContext())) {
            SnackbarUtil.showLong(layoutRoot, "当前网络不可用");
            return false;
        }
        return true;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断网络是wifi还是手机
     *
     * @param context
     * @return
     */
    public static int getNetWork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return 0;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return 1;
            }
        }
        return -1;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                Log.i("sql", "网店网络是wifi");
                return NetworkType.NETTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobileInfo != null) {
                    switch (mobileInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:// 手机网络
                            switch (mobileInfo.getSubtype()) {
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                    return NetworkType.NETTYPE_3G;
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    return NetworkType.NETTYPE_2G;
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    return NetworkType.NETTYPE_4G;
                                default:
                                    return NetworkType.NETTYPE_NONE;
                            }
                    }
                }

            }
        }
        return NetworkType.NETTYPE_NONE;
    }
}
