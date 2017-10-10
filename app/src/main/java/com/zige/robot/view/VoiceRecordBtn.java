package com.zige.robot.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.zige.robot.AppConfig;
import com.zige.robot.R;
import com.zige.robot.interf.SuccessVoiceListener;
import com.zige.robot.utils.MediaRecorderManager;
import com.zige.robot.utils.DialogManager;
import com.zige.robot.utils.FileUtils;



/**录音按钮View
 * 用于展示简易的图标
 * Created by Administrator on 2015/126.
 * ldw  lidingwei
 */
public class VoiceRecordBtn extends AppCompatButton implements MediaRecorderManager.AudioStageListener {

    private static final String TAG = "VoiceRecordBtn";
    public static final int MAX_TIME = 3*60; //3分钟
    public static final int REDMIND_TIME = 10; //剩余10s提醒
    //当前录音时长
    private float mTime = 0;
    //垂直方向滑动取消的临界距离
    public static final int DISTANCE_Y_CANCEL = 50;

    Context mContext;
    DialogManager mDialogManager;
    MediaRecorderManager mMediaRecorderManager;
    Handler mHandler;
    //震动类
    private Vibrator vibrator;

    boolean bRecording=false;
    //是否是取消录音状态的标志
    boolean bCancelRecord = false;
    //时间是否到了
    boolean bOverTime = false;
    //震动标志为
    boolean bVibrator = false;

    long startTime;


    public VoiceRecordBtn(Context context) {
        this(context, null);
        initView(context);
    }

    public VoiceRecordBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }



    private void initView(Context context){
        this.mContext = context;
        setBackgroundResource(R.drawable.voice_on);
        mDialogManager = new DialogManager(context); //录音dialog动画
        //获取录音保存位置
        FileUtils.getSaveFolder(AppConfig.DEFAULT_VOICE_FILE_PATH); //新建语音视频文件夹
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
                        mDialogManager.dimissDialog();
                        setBackgroundResource(R.drawable.voice_on);
                        mMediaRecorderManager.release();
                        mSuccessVoiceListener.getVoiceMsgPath(mMediaRecorderManager.getCurrentFilePath(), mMediaRecorderManager.getVoice_time_length());
                        break;
                    case MSG_VOICE_RECORDING:
                        mDialogManager.updateVoiceLevel(mMediaRecorderManager.getVoiceLevel(7));
                        break;

                    case MSG_REMIND_TIME:
                        if (!bVibrator) {
                            Toast.makeText(mContext, "还剩余10s",Toast.LENGTH_LONG).show();
                            bVibrator = true;
                            doShock();
                        }
                        break;

                }

            }
        };
    }

    SuccessVoiceListener mSuccessVoiceListener;
    public void setOnSuccessVoiceListener(SuccessVoiceListener listener){
        this.mSuccessVoiceListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int current_x = (int) event.getX();
        int current_y = (int) event.getY();
        boolean ret = false;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                mDialogManager.showRecordingDialog();
                startTime = System.currentTimeMillis();
                mMediaRecorderManager.prepareAudio(); //开始录音
                bRecording = true;
                setBackgroundResource(R.drawable.voice_off);
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
                Log.d(TAG, "onTouchEvent: ACTION_UP");
                if(System.currentTimeMillis() - startTime < 1000){
                    bCancelRecord = true;
                    Log.d(TAG, "onTouchEvent: " +"time is too short");
                    Toast.makeText(mContext, "时间太短了", Toast.LENGTH_LONG).show();
                }
                if(!bOverTime){
                    mDialogManager.dimissDialog();
                    setBackgroundResource(R.drawable.voice_on);
                    if(bCancelRecord){
                        mMediaRecorderManager.cancel(); //不保存
                    }else {
                        mMediaRecorderManager.release();
                        mSuccessVoiceListener.getVoiceMsgPath(mMediaRecorderManager.getCurrentFilePath(), mMediaRecorderManager.getVoice_time_length());
                    }
                }
                reset();
                break;
        }
        return ret;
    }


    private void reset(){
        mTime = 0;
        bVibrator = false;
        bOverTime= false;
        bRecording =false;
        bCancelRecord = false;
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

    /*
    * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
    * */
    private void doShock() {
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
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


    @Override
    public void wellPrepared() {

    }
}
