package com.zige.robot.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zige.robot.R;

/**
 * Created by Administrator on 2017/5/18.
 */

public class PopupWindowUtils {

    /**
     * @param context
     * @param popId 布局id
     * @param layoutId 最外层布局id
     * @return
     */
    public static PopupWindow createTopPopStyle(Context context, final int layoutId, final int popId ){
        final PopupWindow popupWindow= new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mMenuView = inflater.inflate(layoutId, null);
        popupWindow.setContentView(mMenuView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);// 获得焦点 抢夺了Activity的焦点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//解决遮挡华为手机的虚拟键盘，但是会把布局顶上去
        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.pop_backgroud)));
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(popId).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        return popupWindow;
    }


    /**
     *
     * @param context
     * @param popId
     * @param layoutId
     * @return
     */
    public static PopupWindow createBottomPopStyle(Context context,final int popId, final int layoutId){
        final PopupWindow popupWindow= new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View mMenuView = inflater.inflate(layoutId, null);
        popupWindow.setContentView(mMenuView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);// 获得焦点 抢夺了Activity的焦点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//解决遮挡华为手机的虚拟键盘，但是会把布局顶上去
        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.pop_backgroud)));
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(popId).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        return popupWindow;
    }
    public static void setButtomShow(final PopupWindow popupWindow, final View view, final int layoutId){

        popupWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x80000000));
        // 设置弹出窗体显示时的动画，从底部向上弹出
        popupWindow.setAnimationStyle(R.style.take_photo_anim);

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = v.findViewById(layoutId).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }
}
