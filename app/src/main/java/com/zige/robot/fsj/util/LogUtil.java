package com.zige.robot.fsj.util;

import android.content.res.Resources;
import android.util.Log;

import com.zige.robot.BuildConfig;
import com.zige.robot.R;

/**
 * Created by Administrator on 2017/9/12.
 */

public class LogUtil {

    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "phone";

    public static void showV(String msg) {
        showV(TAG, msg);
    }

    public static void showD(String msg) {
        showD(TAG, msg);
    }

    public static void showI(String msg) {
        showI(TAG, msg);
    }

    public static void showW(String msg) {
        showW(TAG, msg);
    }

    public static void showE(String msg) {
        showE(TAG, msg);
    }

    public static void showV(String msg, boolean show) {
        showV(TAG, msg, show);
    }

    public static void showD(String msg, boolean show) {
        showD(TAG, msg, show);
    }

    public static void showI(String msg, boolean show) {
        showI(TAG, msg, show);
    }

    public static void showW(String msg, boolean show) {
        showW(TAG, msg, show);
    }

    public static void showE(String msg, boolean show) {
        showE(TAG, msg, show);
    }

    public static void showV(String tag, String msg) {
        showV(tag, msg, true);
    }

    public static void showI(String tag, String msg) {
        showI(tag, msg, true);
    }

    public static void showD(String tag, String msg) {
        showD(tag, msg, true);
    }

    public static void showW(String tag, String msg) {
        showW(tag, msg, true);
    }

    public static void showE(String tag, String msg) {
        showE(tag, msg, true);
    }

    public static void showV(String tag, String msg, boolean show) {
        if (isDebug && show) {
            Log.v(tag, msg);
        }
    }

    public static void showD(String tag, String msg, boolean show) {
        if (isDebug && show) {
            Log.d(tag, msg);
        }
    }

    public static void showI(String tag, String msg, boolean show) {
        if (isDebug && show) {
            Log.i(tag, msg);
        }
    }

    public static void showW(String tag, String msg, boolean show) {
        if (isDebug && show) {
            Log.w(tag, msg);
        }
    }

    public static void showE(String tag, String msg, boolean show) {
        if (isDebug && show) {
            Log.e(tag, msg);
        }
    }

}
