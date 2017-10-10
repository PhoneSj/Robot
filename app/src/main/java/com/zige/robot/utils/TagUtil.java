package com.zige.robot.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zige.robot.BuildConfig;


public class TagUtil {
    private final static String TAG = "TagUtil";
    private static Toast toast;

    /**
     * 弹出提示框
     *
     * @param context
     * @param str
     */
    public static void showToast(String str, Context context) {
        if(toast==null){
             toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else{
            toast.setText(str);
            toast.setDuration( Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    /**
     * 显示debug 数据
     *
     * @param str
     */
    public static void showLogDebug(String str) {
        if (BuildConfig.IS_DEBUG)
            Log.e(TAG, str);
    }
    /**
     * 显示debug 数据
     *
     * @param str
     */
    public static void showLogDebug(String tag,String str) {
        if (BuildConfig.IS_DEBUG)
            Log.e(tag, str);
    }

}
