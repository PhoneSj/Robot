
package com.zige.robot.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.zige.robot.App;


/**
 * Toast处理工具类
 * <p>
 * <ul>
 * 显示文本的Toast
 * <li>{@link #showToast(Context, CharSequence, int)} 显示Toast</li>
 * <li>{@link #showToastLong(Context, CharSequence)} 显示长时间的Toast</li>
 * <li>{@link #showToastShort(Context, CharSequence)} 显示短时间的Toast</li>
 * </ul>
 * <ul>
 * 显示资源id的Toast
 * <li>{@link #showToast(Context, int, int)} 显示Toast</li>
 * <li>{@link #showToastLong(Context, int)} 显示长时间的Toast</li>
 * <li>{@link #showToastShort(Context, int)} 显示短时间的Toast</li>
 * </ul>
 */
public class ToastUtils {

    private static Toast toast = null;

    private ToastUtils() {
    }

    /**
     * 显示时间短的Toast
     *
     * @param context 上下文
     * @param msg     显示的内容
     */
    public static Toast showToastShort(Context context, CharSequence msg) {
        return showToast(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示时间短的Toast
     *
     * @param context 上下文
     * @param resId   显示的资源ID
     */
    public static Toast showToastShort(Context context, int resId) {
        return showToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示时间长的Toast
     *
     * @param context 上下文
     * @param msg     显示的内容
     */
    public static Toast showToastLong(Context context, CharSequence msg) {
        return showToast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示时间长的Toast
     *
     * @param context 上下文
     * @param resId   显示的资源ID
     */
    public static Toast showToastLong(Context context, int resId) {
        return showToast(context, resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示时间短的Toast
     *
     * @param msg 显示的内容
     */
    public static Toast showToastShort(CharSequence msg) {
        return showToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示时间短的Toast
     *
     * @param resId 显示的资源ID
     */
    public static Toast showToastShort(int resId) {
        return showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示时间长的Toast
     *
     * @param msg 显示的内容
     */
    public static Toast showToastLong(CharSequence msg) {
        return showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 显示时间长的Toast
     *
     * @param resId 显示的资源ID
     */
    public static Toast showToastLong(int resId) {
        return showToast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param context  上下文
     * @param resId    显示的资源ID
     * @param duration 时长
     */
    public static Toast showToast(Context context, int resId, int duration) {
        if (context == null) {
            return null;
        }
        String text = context.getString(resId);
        return showToast(context, text, duration);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param context  上下文
     * @param text     要显示的toast内容
     * @param duration 时长
     */
    public static Toast showToast(final Context context, final CharSequence text, final int duration) {
        if (context == null) {
            return null;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            if (toast == null) {
                toast = Toast.makeText(context, text, duration);
            } else {
                toast.setText(text);
            }
            toast.show();
            return toast;
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(context, text, duration);
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                }
            });
        }
        return null;
    }

    /**
     * 弹出提示框
     */
    public static Toast showToast(String str) {
        if (App.getInstance() != null) {
            if (toast == null) {
                toast = Toast.makeText(App.getInstance(), str, Toast.LENGTH_SHORT);
            } else {
                toast.setText(str);
            }
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
        }
        return toast;
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param resId    显示的资源ID
     * @param duration 时长
     */
    public static Toast showToast(int resId, int duration) {
        String text = App.getInstance().getString(resId);
        return showToast(App.getInstance(), text, duration);
    }

    /**
     * 显示Toast，自动处理主线程与非主线程
     *
     * @param text     要显示的toast内容
     * @param duration 时长
     */
    public static Toast showToast(final CharSequence text, final int duration) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            if (toast == null) {
                toast = Toast.makeText(App.getInstance(), text, duration);
            } else {
                toast.setText(text);
            }
            toast.show();
            return toast;
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(App.getInstance(), text, duration);
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                }
            });
        }
        return null;
    }
}
