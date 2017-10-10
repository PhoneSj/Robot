package com.zige.robot.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zige.robot.R;


public class DialogManager {

    private Dialog mDialog;

    private Context mContext;

    ImageView iv_volume_level; //音量条
    TextView  tv_tip; //提示

    public DialogManager(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_volume, null);
        mDialog.setContentView(view);
        iv_volume_level  = (ImageView) view.findViewById(R.id.iv_volume_level);
        tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        mDialog.show();
        setTipState(0);
    }

    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void updateVoiceLevel(int level) {
        if (level > 0 && level < 6) {
        } else {
            level = 5;
        }
        if (mDialog != null && mDialog.isShowing()) {
            int resId = mContext.getResources().getIdentifier("volume_" + level,
                    "drawable", mContext.getPackageName());
            iv_volume_level.setBackgroundResource(resId);
        }
    }

    public void setTv_tip(String tipMsg){
        tv_tip.setText(tipMsg);
    }

    public void setTv_tip_color(int color){
       tv_tip.setTextColor(color);
    }

    /**
     *
     * @param state 0 手指上滑取消录音 1 松开取消录音
     */
    public void setTipState(int state){
        if(state ==0){
            setTv_tip(mContext.getResources().getString(R.string.move_up_cancel_record));
            setTv_tip_color(mContext.getResources().getColor(R.color.color_666));
        }else if(state ==1){
            setTv_tip(mContext.getResources().getString(R.string.touch_up_cancel_record));
            setTv_tip_color(mContext.getResources().getColor(R.color.color_fe5365));
        }
    }

}
