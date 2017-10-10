package com.zige.robot.utils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class QuickClick {
    private static long lastClickTime;

    public static boolean isThreeClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean oneClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean halfClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 200) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
