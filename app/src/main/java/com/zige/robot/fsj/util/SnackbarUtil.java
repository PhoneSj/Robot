package com.zige.robot.fsj.util;

import android.app.Activity;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Phone on 2017/7/18.
 */

public class SnackbarUtil {

    public static void showLong(View view, int res) {
        showLong(view, Resources.getSystem().getString(res));
    }

    public static void showShort(View view, int res) {
        showShort(view, Resources.getSystem().getString(res));
    }

    public static void showIndefinite(View view, int res) {
        showIndefinite(view, Resources.getSystem().getString(res));
    }

    public static void showLong(View view, String msg) {
        show(view, msg, Snackbar.LENGTH_LONG);
    }

    public static void showShort(View view, String msg) {
        show(view, msg, Snackbar.LENGTH_SHORT);
    }

    public static void showIndefinite(View view, String msg) {
        show(view, msg, Snackbar.LENGTH_INDEFINITE);
    }

    private static void show(final View view, final String msg, final int duration) {
        if (view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(view, msg, duration).show();
                }
            });
        } else {
            LogUtil.showE("view为空");
        }
    }

}
