package com.zige.robot.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zige.robot.AppConfig;
import com.zige.robot.R;
import com.zige.robot.interf.SuccessVoiceListener;
import com.zige.robot.utils.MediaRecorderManager;
import com.zige.robot.utils.DialogManager;
import com.zige.robot.utils.FileUtils;


/**
 * Created by ldw on 2017/8/23.
 * weixin 语音输入
 */

public class WeiXinVoiceInputView extends RelativeLayout implements View.OnTouchListener, MediaRecorderManager.AudioStageListener {

    private static final String TAG = "WeiXinVoiceInputView";
    LinearLayout ll_touch;
    TextView tv_touch_state;

    Context mContext;
    MediaRecorderManager mMediaRecorderManager;
    DialogManager mDialogManager;
    Handler mHandler;
    //震动类
    private Vibrator vibrator;
    //是否是录音状态
    boolean bRecording=false;
    //是否是取消录音状态的标志
    boolean bCancelRecord = false;
    //时间是否到了
    boolean bOverTime = false;
    //震动标志为
    boolean bVibrator = false;

    long startTime;

    public static final int MAX_TIME = 3*60; //3分钟
    public static final int REDMIND_TIME = 10; //剩余10s提醒
    //当前录音时长
    private float mTime = 0;
    //垂直方向滑动取消的临界距离
    public static final int DISTANCE_Y_CANCEL = 50;
    int state = 0; // 0 正常状态   1按住按住状态

    public WeiXinVoiceInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_weixin_voice_input, this);
        ll_touch = findViewById(R.id.ll_touch);
        tv_touch_state = findViewById(R.id.tv_touch_state);
        ll_touch.setOnTouchListener(this);
        initData();
    }


    private void initData(){
        //实例化音量dialog
        mDialogManager = new DialogManager(mContext);
        //新建语音视频文件夹
        FileUtils.getSaveFolder(AppConfig.DEFAULT_VOICE_FILE_PATH);
        //实例化录音核心类
        mMediaRecorderManager = MediaRecorderManager.getInstance(AppConfig.DEFAULT_VOICE_FILE_PATH);
        //录音准备完毕监听
        mMediaRecorderManager.setOnAudioStageListener(this);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_OVER_TIME:
                        Toast.makeText(mContext, "时间到了",Toast.LENGTH_LONG).show();
                        bOverTime = true;
                        state = 0;
                        updateView();
                        mMediaRecorderManager.release();
                        if(mSuccessVoiceListener!=null)
                        mSuccessVoiceListener.getVoiceMsgPath(mMediaRecorderManager.getCurrentFilePath(), mMediaRecorderManager.getVoice_time_length());
                        break;
                    case MSG_VOICE_RECORDING:
                        mDialogManager.updateVoiceLevel(mMediaRecorderManager.getVoiceLevel(7));
                        break;

                    case MSG_REMIND_TIME:
                        if (!bVibrator) {
                            Toast.makeText(mContext, "剩余"+ REDMIND_TIME+"s",Toast.LENGTH_LONG).show();
                            bVibrator = true;
                            doShock();
                        }
                        break;

                }
            }
        };
    }


    /*
  * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
  * */
    private void doShock() {
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }


    private void updateView(){
        if(state == 0){ //正常状态
            ll_touch.setBackgroundResource(R.drawable.weixin_voice_input_normal);
            tv_touch_state.setText(getResources().getString(R.string.down_start_speak));
            mDialogManager.dimissDialog();
        }else if(state == 1){ //录音状态
            ll_touch.setBackgroundResource(R.drawable.weixin_voice_input_press);
            tv_touch_state.setText(getResources().getString(R.string.up_stop_speak));
            mDialogManager.showRecordingDialog();
        }
    }

    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {// 判断是否在左边，右边
//            return false;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) { //上边 下边
            return true;
        }
        return false;
    }

    public static final int MSG_OVER_TIME       = 1;
    public static final int MSG_VOICE_RECORDING = 2;
    public static final int MSG_REMIND_TIME     = 3;

    // 获取音量大小的runnable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            while (bRecording) {
                try {
                    //最长mMaxRecordTimes
                    if (mTime > MAX_TIME) {
                        //超时
                        mHandler.sendEmptyMessage(MSG_OVER_TIME);//超时取消录音
                        return;
                    }
                    if(MAX_TIME - mTime <REDMIND_TIME){
                        mHandler.sendEmptyMessage(MSG_REMIND_TIME);//剩余
                    }
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_RECORDING); //正在录音
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void reset(){
        mTime = 0;
        bVibrator = false;
        bOverTime= false;
        bRecording =false;
        bCancelRecord = false;
    }

    SuccessVoiceListener mSuccessVoiceListener;
    public void setOnSuccessVoiceListener(SuccessVoiceListener listener){
        this.mSuccessVoiceListener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        int current_x = (int) event.getX();
        int current_y = (int) event.getY();
        boolean ret = false;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                state = 1;
                updateView();
                startTime = System.currentTimeMillis();
                mMediaRecorderManager.prepareAudio(); //开始录音
                bRecording = true;
                new Thread(mGetVoiceLevelRunnable).start();
                ret = true;
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE" + "----current_x: "+ current_x + "----current_y: "+ current_y);
                //通过手势x y 判断是否取消录音的状态
                if(wantToCancel(current_x, current_y)){
                    bCancelRecord = true;
                    mDialogManager.setTipState(1);
                    Log.d(TAG, "onTouchEvent: " +"cancel success" );
                }else {
                    bCancelRecord = false;
                    mDialogManager.setTipState(0);
                    Log.d(TAG, "onTouchEvent: " +"cancel fail" );
                }
                break;

            case MotionEvent.ACTION_UP:
                state = 0;
                updateView();
                if(System.currentTimeMillis() - startTime < 1000){
                    bCancelRecord = true;
                    Toast.makeText(mContext, "时间太短了", Toast.LENGTH_LONG).show();
                }
                if(!bOverTime){
                    setBackgroundResource(R.drawable.voice_on);
                    if(bCancelRecord){
                        mMediaRecorderManager.cancel(); //不保存
                    }else {
                        mMediaRecorderManager.release();
                        if(mSuccessVoiceListener !=null)
                        mSuccessVoiceListener.getVoiceMsgPath(mMediaRecorderManager.getCurrentFilePath(), mMediaRecorderManager.getVoice_time_length());
                    }
                }
                reset();
                break;
        }
        return ret;
    }

    @Override
    public void wellPrepared() {

    }
}
