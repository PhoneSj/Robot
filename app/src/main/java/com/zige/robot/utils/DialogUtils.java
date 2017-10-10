package com.zige.robot.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zige.robot.R;
import com.zige.robot.interf.ActionClickListener;
import com.zige.robot.interf.DialogListener;


/**
 * @author Feel on 2017/4/8 09:21
 */

public class DialogUtils {
    /**
     * @param context
     * @param layoutId dialog布局
     * @return
     */
    public static Dialog createDialogForContentViewInCenter(Context context, int layoutId, int left, int right) {
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        View dialogView = LayoutInflater.from(context).inflate(layoutId, null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕中间
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(left, 0, right, 0);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        //点击外部空间dialog消失
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    /**
     * @param context
     * @param layoutId dialog布局
     * @return
     */
    public static Dialog createDialogForContentViewFromBottom(Context context, int layoutId, int left, int right) {
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        View dialogView = LayoutInflater.from(context).inflate(layoutId, null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置左右边距
        window.getDecorView().setPadding(left, 0, right, 0);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.AnimBottom);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        return dialog;
    }


    /**
     * 删除指令dialog
     */
    public static Dialog createDelDialog(Context mContext, final DialogListener listener) {
        final Dialog delDialog = DialogUtils.createDialogForContentViewInCenter(mContext, R.layout.dialog_del, ScreenUtils
                .dp2px(mContext, 20), ScreenUtils.dp2px(mContext, 20));
        delDialog.findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除指令
                delDialog.dismiss();
                listener.actionClick();
            }
        });
        return delDialog;
    }

    /**
     * 提示操作dialog
     *
     * @param tipTile
     * @return
     */
    public static Dialog createTipDialog(Context context, String tipTile, String left, String right, final ActionClickListener actionClickListener) {
        Dialog dialog = createDialogForContentViewInCenter(context, R.layout.dialog_tip, ScreenUtils
                .dp2px(context, 20), ScreenUtils.dp2px(context, 20));
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.tv_tip)).setText(tipTile); //提示语
        ((TextView) dialog.findViewById(R.id.tv_click_left)).setText(left); //左边按钮
        ((TextView) dialog.findViewById(R.id.tv_click_right)).setText(right); //右边按钮
        dialog.findViewById(R.id.tv_click_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionClickListener.clickLeft();
            }
        });
        dialog.findViewById(R.id.tv_click_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionClickListener.clickRight();
            }
        });
        return dialog;
    }

    /**
     * 自定义的加载动画dialog
     *
     * @param context
     * @param tipMsg
     * @return
     */
    public static Dialog createLoadingDialog(final Context context, String tipMsg, boolean cancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_wait_tip, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        final ImageView iv_loading = (ImageView) v.findViewById(R.id.iv_loading);
        TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);// 提示文字
        tv_tip.setText(tipMsg);// 设置tip
        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.load_animation);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(cancelable);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                iv_loading.startAnimation(animation);
            }
        });
        return loadingDialog;

    }

//    public static Dialog createInputDialog(Context context, String title, boolean cancelable, InputDialogClickListener listener) {
//        return createInputDialog(context, title, null, cancelable, listener);
//    }
//
//    public static Dialog createInputDialog(Context context, String title, String defaultValue, boolean cancelable, InputDialogClickListener listener) {
//        return createInputDialog(context, title, defaultValue, "取消", "确定", cancelable, listener);
//    }
//
//    public static Dialog createInputDialog(Context context, String title, String defaultValue, String left, String right, boolean cancelable, final InputDialogClickListener listener) {
//        Dialog dialog = createDialogForContentViewInCenter(context, R.layout.dialog_tip, ScreenUtils
//                .dp2px(context, 20), ScreenUtils.dp2px(context, 20));
//        dialog.setCanceledOnTouchOutside(false);
//        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
//        if (!TextUtils.isEmpty(defaultValue)) {
//            ((TextView) dialog.findViewById(R.id.et_value)).setText(defaultValue);
//        }
//        ((TextView) dialog.findViewById(R.id.tv_click_left)).setText(left);
//        ((TextView) dialog.findViewById(R.id.tv_click_right)).setText(right);
//        final EditText etValue = (EditText) dialog.findViewById(R.id.et_value);
//        dialog.findViewById(R.id.tv_click_left).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String value = etValue.getText().toString().trim();
//                listener.onClickLeft(value);
//            }
//        });
//        dialog.findViewById(R.id.tv_click_right).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String value = etValue.getText().toString().trim();
//                listener.onClickRight(value);
//            }
//        });
//        return dialog;
//    }
//
//    public interface InputDialogClickListener {
//        void onClickLeft(String value);
//
//        void onClickRight(String value);
//    }

}
