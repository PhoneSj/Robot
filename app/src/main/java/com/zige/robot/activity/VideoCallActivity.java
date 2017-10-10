package com.zige.robot.activity;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVView;
import com.tencent.callsdk.ILVBCallMemberListener;
import com.tencent.callsdk.ILVCallConstants;
import com.tencent.callsdk.ILVCallListener;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVCallOption;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.PlayerUtil;
import com.zige.robot.utils.TagUtil;

import java.util.List;


/**
 * Created by Administrator on 2017/5/15.
 * 视频聊天
 */

public class   VideoCallActivity extends BaseActivity implements ILVCallListener, View.OnClickListener, ILVBCallMemberListener {

    private static final String TAG = "VideoCallActivity";

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_video_call;
    }

    private String mHostId;
    private int mCallId;
    private int mCallType;

    AVRootView av_root_view;
    /* 挂通话 */
    private Button btnEndCall;
    /* 摄像头 */
    private Button mCameraChange;
    /* 视频截图 */
    private Button mScreenshots;
    /* 视频录像 */
    private Button mVideo;
    /*外放*/
    private Button btn_sound;
    /*麦克风*/
    private Button btn_mic;
    /*挂断*/
    private Button btn_cancel_call;


    private RelativeLayout rl_control;
    private RelativeLayout rl_wait_connecting;
    private RelativeLayout rl_video_view;
    private RelativeLayout rl_coming_wait;
    private int mCurCameraId = ILiveConstants.FRONT_CAMERA;

    private boolean bCameraEnable = true;
    private boolean bMicEnalbe = false;
    private boolean bSpeaker = true;
    boolean bSender = false;
    boolean bVideoEnd = false;
    boolean bCancel = false; //延时关闭电话

    AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏


        av_root_view = (AVRootView) findViewById(R.id.av_root_view);
        rl_video_view = (RelativeLayout) findViewById(R.id.rl_video_view);

        rl_coming_wait = (RelativeLayout) findViewById(R.id.rl_coming_wait);
        //视频通话结束按钮
        btnEndCall = (Button) findViewById(R.id.video_call_chat_end);

        //摄像头切换
        mCameraChange = (Button) findViewById(R.id.video_call_chat_camera_change);   //旋转测试按钮
        //外放
        btn_sound = (Button) findViewById(R.id.btn_sound);
        /*麦克风*/
        btn_mic = (Button) findViewById(R.id.btn_mic);
        /*挂断*/
        btn_cancel_call = (Button) findViewById(R.id.btn_cancel_call);

        btn_cancel_call.setOnClickListener(this);
        btn_sound.setOnClickListener(this);
        btn_mic.setOnClickListener(this);
        //视频截图
        mScreenshots = (Button) findViewById(R.id.video_call_chat_screenshots);
        //视频录像
        mVideo = (Button) findViewById(R.id.video_call_chat_video);

        rl_control = (RelativeLayout) findViewById(R.id.rl_control);
        rl_wait_connecting = (RelativeLayout) findViewById(R.id.rl_wait_connecting);

        findViewById(R.id.video_call_return_button).setOnClickListener(this);

        btnEndCall.setOnClickListener(this);
        mCameraChange.setOnClickListener(this);
        mScreenshots.setOnClickListener(this);
        mVideo.setOnClickListener(this);

        ILVCallManager.getInstance().addCallListener(this); //设置电话回调
        Intent intent = getIntent();
        mCallId = intent.getIntExtra("CallId", 0);
        mHostId = intent.getStringExtra("HostId");
        mCallType = intent.getIntExtra("CallType", ILVCallConstants.CALL_TYPE_VIDEO);
        ILVCallOption option = new ILVCallOption(mHostId)
                .callTips("VideoCall")
                .setMemberListener(this)
                .setCallType(mCallType);
        if (0 == mCallId) {
            // 发起呼叫
            bSender = true;
            rl_wait_connecting.setVisibility(View.VISIBLE); //等待对方接听
            startComingMusic();
            List<String> nums = intent.getStringArrayListExtra("CallNumbers");
            if (nums.size() > 1) {
                mCallId = ILVCallManager.getInstance().makeMutiCall(nums, option, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        TagUtil.showLogDebug(module + errMsg + "");
                    }
                });
            } else {
                mCallId = ILVCallManager.getInstance().makeCall(nums.get(0), option, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d(TAG, "makeCall  onSuccess:  ");
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.d(TAG, "makeCall  onError:  " + errCode +"  "+ errMsg);
//                        TagUtil.showLogDebug(module + errMsg + "");

                    }
                });
            }
        } else {
            // 接听呼叫
            bSender = false;
            rl_coming_wait.setVisibility(View.VISIBLE); //等待自己接听
            ILVCallManager.getInstance().acceptCall(mCallId, option);
        }
        rl_control.setVisibility(View.GONE);
        mHandler.sendEmptyMessageDelayed(1, 1500); //延时打开关闭按钮
        ILiveLoginManager.getInstance().setUserStatusListener(new ILiveLoginManager.TILVBStatusListener() {
            @Override
            public void onForceOffline(int error, String message) {
                Log.d(TAG, "onForceOffline: ");
                finish();
            }
        });
        av_root_view.setAutoOrientation(true);
        av_root_view.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT); //小屏放右侧
        ILVCallManager.getInstance().initAvView(av_root_view);
        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL); //最大的通话声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (maxVolume < 7 ? maxVolume : 7), AudioManager.FLAG_PLAY_SOUND); //调低声音达到降噪的效果
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ILVCallManager.getInstance().enableMic(false);   //麦克风默认关闭
//                ILiveSDK.getInstance().getAvAudioCtrl().setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_SPEAKER); // 打开外放
//            }
//        }, 1000);


    }


    @Override
    protected void performHandleMessage(Message msg) {
        super.performHandleMessage(msg);
       if(msg.what ==2){
            if(TextUtils.isEmpty(av_root_view.getViewByIndex(0).getIdentifier()) ||TextUtils.isEmpty(av_root_view.getViewByIndex(1).getIdentifier())){
                //有一路为空继续
                Log.d(TAG, "performHandleMessage: " + " 0: "+av_root_view.getViewByIndex(0).getIdentifier()
                +" 1: "+av_root_view.getViewByIndex(1).getIdentifier());
                mHandler.sendEmptyMessageDelayed(2,1000);
            }else {
                setCamerConfig();
                Log.d(TAG, "onCallEstablish->1:"  + av_root_view.getViewByIndex(0).getIdentifier() + "/" + av_root_view.getViewByIndex(1).getIdentifier());
                 if (ILiveLoginManager.getInstance().getMyUserId().equals(av_root_view.getViewByIndex(0).getIdentifier())) {
                     Log.d(TAG, "performHandleMessage: 与大屏交换" );
                      av_root_view.swapVideoView(0, 1);     // 与大屏交换
                    }
                    av_root_view.getViewByIndex(0).setRotate(true);
                    av_root_view.getViewByIndex(0).setRotation(270);
            }
        }else if(msg.what ==1){
           bCancel = true;
       }
    }

    PlayerUtil mPlayerUtil;

    /**
     * 播放音乐
     */
    private void startComingMusic() {
        mPlayerUtil = new PlayerUtil(this);
        mPlayerUtil.playAssetsFile("outgoing.ogg", true);
    }

    /**
     * 停止音乐
     */
    private void stopComingMusic() {
        if (mPlayerUtil != null) {
            mPlayerUtil.stop();
            mPlayerUtil= null;
        }
    }

    /**
     * 摄像头控制
     */
    private void changeCamera() {
        if (bCameraEnable) {
            ILVCallManager.getInstance().enableCamera(mCurCameraId, false);
            av_root_view.closeUserView(ILiveLoginManager.getInstance().getMyUserId(), AVView.VIDEO_SRC_TYPE_CAMERA, true);
        } else {
            ILVCallManager.getInstance().enableCamera(mCurCameraId, true);
        }
        bCameraEnable = !bCameraEnable;
    }


    /**
     * 麦克风控制
     */
    private void changeMic() {
        if (bMicEnalbe) {
            ILVCallManager.getInstance().enableMic(false);
        } else {
            ILVCallManager.getInstance().enableMic(true);
        }
        bMicEnalbe = !bMicEnalbe;
    }

    /**
     * 听筒和扬声器切换
     */
    private void changeSpeaker() {
        if (bSpeaker) {
            ILiveSDK.getInstance().getAvAudioCtrl().setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_HEADSET);
        } else {
            ILiveSDK.getInstance().getAvAudioCtrl().setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_SPEAKER);
        }
        bSpeaker = !bSpeaker;
    }

    /**
     * 切换摄像头
     */
    private void switchCamera() {
        mCurCameraId = (ILiveConstants.FRONT_CAMERA == mCurCameraId) ? ILiveConstants.BACK_CAMERA : ILiveConstants.FRONT_CAMERA;
        ILVCallManager.getInstance().switchCamera(mCurCameraId);
    }


    PowerManager.WakeLock wakeLock;

    @Override
    protected void onResume() {
        //onResume  中启用
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.acquire();
        ILVCallManager.getInstance().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopComingMusic();
        //onPause 中禁用
        if (wakeLock != null) {
            wakeLock.release();
        }
        ILVCallManager.getInstance().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        ILVCallManager.getInstance().removeCallListener(this);
        ILVCallManager.getInstance().onDestory();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }


    private void backAction() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                 dismissProgressDialog();
        if(!bCancel){
            return;
        }
        if(!bVideoEnd){
            bVideoEnd = true; //关闭了通话
            ILVCallManager.getInstance().endCall(mCallId);
        }


//    }
//        }, 1500);
//        showProgressDialog("关闭通话，请稍后", false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_call_return_button: //返回
                backAction();
                break;

            case R.id.btn_cancel_call: //返回
                backAction();
                break;

            case R.id.video_call_chat_screenshots: //截屏
                break;

            case R.id.btn_sound: //外放
                changeSpeaker();
                if (bSpeaker) {
                    btn_sound.setBackgroundResource(R.drawable.video_sound_off);
                } else {
                    btn_sound.setBackgroundResource(R.drawable.video_sound_on);
                }
                break;
            case R.id.video_call_chat_end: //结束视频通话
                backAction();
                break;
            case R.id.video_call_chat_camera_change: //摄像头切换
                switchCamera();
                break;
            case R.id.video_call_chat_video: //录像
                break;
            case R.id.btn_mic: //麦克风
                changeMic();
                if (bMicEnalbe) {
                    btn_mic.setBackgroundResource(R.drawable.video_voice_off);
                } else {
                    btn_mic.setBackgroundResource(R.drawable.video_voice_on);
                }
                break;
        }
    }


    @Override
    public void onCallEstablish(int callId) {
        ILVCallManager.getInstance().enableMic(false);   //麦克风默认关闭
        ILiveSDK.getInstance().getAvAudioCtrl().setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_SPEAKER); // 打开外放
        try {
            Log.d(TAG, "onCallEstablish->0:" + callId + "  " + av_root_view.getViewByIndex(0).getIdentifier() + "/" + av_root_view.getViewByIndex(1).getIdentifier());
//            bVideoSucc = true;
            av_root_view.swapVideoView(0, 1);
            // 设置点击小屏切换及可拖动
            for (int i = 1; i < /*ILiveConstants.MAX_AV_VIDEO_NUM*/2; i++) {
                final int index = i;
                AVVideoView minorView = av_root_view.getViewByIndex(i);
                if (ILiveLoginManager.getInstance().getMyUserId().equals(minorView.getIdentifier())) {
                    minorView.setMirror(true);      // 本地镜像
                }
                minorView.setDragable(true);    // 小屏可拖动
//              minorView.setVisibility(View.GONE);
//                minorView.setRotate(false);
//                minorView.setDiffDirectionRenderMode(AVVideoView.ILiveRenderMode.SCALE_TO_FIT);
//                minorView.setSameDirectionRenderMode(AVVideoView.ILiveRenderMode.SCALE_TO_FIT);
                minorView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        av_root_view.swapVideoView(0, index);     // 与大屏交换
                        return false;
                    }
                });
            }
            mHandler.sendEmptyMessageDelayed(2, 1000); //判断是两路视频是否显示
        } catch (Exception e) {
            toastShow("网络不好");
        }
    }

    //设置画面参数
    private void setCamerConfig() {
        if (bSender) {
            rl_wait_connecting.setVisibility(View.GONE);
            stopComingMusic();
        } else {
            rl_coming_wait.setVisibility(View.GONE);
        }
         rl_control.setVisibility(View.VISIBLE);
//        av_root_view.getViewByIndex(0).setRotate(false);
//        av_root_view.getViewByIndex(1).setRotate(false);

        //设定在方向不一致情况下，是铺满屏幕还是留黑边
//        av_root_view.getViewByIndex(0).setDiffDirectionRenderMode(AVVideoView.ILiveRenderMode.SCALE_TO_FIT);
//        av_root_view.getViewByIndex(0).setRotation(0);
        av_root_view.getViewByIndex(0).setVisibility(View.VISIBLE);
//                                  av_root_view.getViewByIndex(1).setVisibility(View.GONE);
    }

    @Override
    public void onCallEnd(int callId, int endResult, String endInfo) {
        // TODO: 2017/5/16 0016 视频结束
        Log.e(TAG, " onCallEnd  callId:" + callId + "_endResult:" + endResult + "_endInfo:" + endInfo);
        finish();
    }

    @Override
    public void onException(int iExceptionId, int errCode, String errMsg) {
        // TODO: 2017/5/16 0016 视频异常
        Log.e(TAG, " onException iExceptionId:" + iExceptionId + "_errCode:" + errCode + "_errMsg:" + errMsg);
        finish();
    }


    @Override
    public void onCameraEvent(String id, boolean bEnable) {
        Log.d(TAG, "onCameraEvent: " + id + " "+ (bEnable?"true": "false"));
    }

    @Override
    public void onMicEvent(String id, boolean bEnable) {
        Log.d(TAG, "onMicEvent: " + id + " "+ (bEnable?"true": "false"));
    }




}
