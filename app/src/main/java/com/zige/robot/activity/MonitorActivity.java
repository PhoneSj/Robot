package com.zige.robot.activity;

import android.Manifest;
import android.app.Service;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tencent.TIMMessage;
import com.tencent.av.TIMAvManager;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.callsdk.ILVBCallMemberListener;
import com.tencent.callsdk.ILVCallConstants;
import com.tencent.callsdk.ILVCallListener;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVCallOption;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRecordOption;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.zige.robot.R;
import com.zige.robot.adapter.CommonLvAdapter;
import com.zige.robot.adapter.CommonLvViewHolder;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.interf.IMMsgListener;
import com.zige.robot.service.ServiceLogin;
import com.zige.robot.utils.QuickClick;

import java.util.ArrayList;
import java.util.List;

import static com.zige.robot.R.id.rl_front;


/**
 * @author Feel on 2017/4/11 14:03
 * 监控
 */

public class MonitorActivity extends BaseActivity implements ILVCallListener, View.OnClickListener,ILVBCallMemberListener, View.OnTouchListener
{

    private static final String TAG ="MonitorActivity";

    int mCallId=0;
    ILVCallOption option;
    boolean bMicEnalbe = false;
    boolean bSpeaker = false;
    boolean bControl = false;
    boolean bVideoSucc = false;
    CommonLvAdapter<String> commonLvAdapterVoice;

    AudioManager mAudioManager;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_monitor;
    }

    AVRootView av_root_view;
    RelativeLayout rl_control_robot;
    RelativeLayout rl_control_state;
    RelativeLayout video_view;
    ListView listview;
    ImageView iv_sound;
    ImageView iv_video;
    ImageView iv_control;
    ImageView iv_photo;
    ImageView iv_voice;


    /**
     * 控制遥控方向指示
     * @param state
     */
    private void setControlViewState(int state){
        if(state == 0){//前
            rl_control_state.setBackgroundResource(R.drawable.telecontrol_up);
        }else  if(state == 1){//后
            rl_control_state.setBackgroundResource(R.drawable.telecontrol_down);
        }else  if(state == 2){//左
            rl_control_state.setBackgroundResource(R.drawable.telecontrol_left);
        }else  if(state == 3){//右
            rl_control_state.setBackgroundResource(R.drawable.telecontrol_right);
        }else  if(state == 4){//停
            rl_control_state.setBackgroundResource(R.drawable.telecontrol_un_control);
        }

    }


    long startTime;
    long stopTime;

    IMMsgListener mIMMsgListener = new IMMsgListener() {
        @Override
        public void onSuccess(TIMMessage msg) {
            toastShow("ok");
        }

        @Override
        public void onError(int code, String desc) {
            toastShow(desc);
        }
    };

    public void startRecord() {
        ILiveRecordOption option = new ILiveRecordOption();
        String filename = String.valueOf(System.currentTimeMillis());
        option.fileName("sxb_" + ILiveLoginManager.getInstance().getMyUserId() + "_" + filename);
        option.classId(123);
        option.recordType(TIMAvManager.RecordType.VIDEO);
        ILiveRoomManager.getInstance().startRecordVideo(option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.i(TAG, "start record success ");
                 toastShow("开始录制");

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.e(TAG, "start record error " + errCode + "  " + errMsg);
                toastShow("录制失败");

            }
        });
    }

    public void stopRecord() {
        ILiveRoomManager.getInstance().stopRecordVideo(new ILiveCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                Log.d(TAG, "stopRecord->success");
                toastShow("停止录制");
                for (String url : data) {
                    Log.d(TAG, "stopRecord->url:" + url);
                }

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                toastShow("停止失败");
                Log.e(TAG, "stopRecord->failed:" + module + "|" + errCode + "|" + errMsg);

            }
        });
    }

    /**
     * 麦克风控制
     */
    private void changeMic() {
        if (bMicEnalbe) {
            ILVCallManager.getInstance().enableMic(false); //关闭麦克风
        } else {
            ILVCallManager.getInstance().enableMic(true); //开启麦克风
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


    private void initAdapter(){
        List<String> voiceList = new ArrayList<>();
        for (int i=0; i<5; i++){
            voiceList.add("今天天气怎么样?");
        }
        commonLvAdapterVoice = new CommonLvAdapter<String>(mContext, voiceList, R.layout.adapter_cornor_bg) {
            @Override
            public void convert(CommonLvViewHolder holder, final String bean, int position) {
                holder.setText(R.id.tv_content, bean);
                holder.setOnClickListener(R.id.tv_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceLogin.getInstance().sendRobotSpeakMsg(bean, new IMMsgListener() {
                            @Override
                            public void onSuccess(TIMMessage msg) {
                                toastShow("ok");
                            }

                            @Override
                            public void onError(int code, String desc) {

                            }
                        });
                    }
                });
            }
        };
        listview.setAdapter(commonLvAdapterVoice);

    }




    /**
     * 发送视频请求
     */
    private void startMonitorRequest(){
        String receive_robot = mApplication.getUserInfo().getDeviceid();
        mCallId = ILVCallManager.getInstance().makeCall(receive_robot, option);
        ILiveLoginManager.getInstance().setUserStatusListener(new ILiveLoginManager.TILVBStatusListener() {
            @Override
            public void onForceOffline(int error, String message) {
                Log.d(TAG, "onForceOffline: "+ error +"  "+ message);
                finish();
            }
        });
        av_root_view.setAutoOrientation(true);
        ILVCallManager.getInstance().initAvView(av_root_view);

    }

    /**
     * 6.0权限获取
     */
    private void checkPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP_MR1){
                String[] mPermissionList = new String[]{
                        Manifest.permission.CAMERA,
                };
                ActivityCompat.requestPermissions(this,mPermissionList,1);
            }
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP_MR1){
                String[] mPermissionList = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                };
                ActivityCompat.requestPermissions(this,mPermissionList,1);
            }
        }

    }
    PowerManager.WakeLock wakeLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏

        iv_sound = (ImageView) findViewById(R.id.iv_sound);
        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_control = (ImageView) findViewById(R.id.iv_control);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        av_root_view = (AVRootView) findViewById(R.id.av_root_view);
        rl_control_robot = (RelativeLayout) findViewById(R.id.rl_control_robot);
        rl_control_state = (RelativeLayout) findViewById(R.id.rl_control_state);
        video_view = (RelativeLayout) findViewById(R.id.video_view);
        listview = (ListView) findViewById(R.id.listview);

        findViewById(R.id.rl_sound).setOnClickListener(this);
        findViewById(R.id.rl_video).setOnClickListener(this);
        findViewById(R.id.rl_control).setOnClickListener(this);
        findViewById(R.id.rl_photo).setOnClickListener(this);
        findViewById(R.id.rl_voice).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bMicEnalbe = true;
                    ILVCallManager.getInstance().enableMic(true);
                    iv_voice.setBackgroundResource(R.drawable.video_voice_off);
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
                    bMicEnalbe = false;
                    ILVCallManager.getInstance().enableMic(false);
                    iv_voice.setBackgroundResource(R.drawable.video_voice_on);
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.iv_screen_shot).setOnClickListener(this);
        findViewById(R.id.iv_timeline).setOnClickListener(this);
        findViewById(R.id.rl_back_action).setOnClickListener(this);

        findViewById(R.id.rl_front).setOnTouchListener(this);
        findViewById(R.id.rl_back).setOnTouchListener(this);
        findViewById(R.id.rl_left).setOnTouchListener(this);
        findViewById(R.id.rl_right).setOnTouchListener(this);

        ILVCallManager.getInstance().addCallListener(this);
        String hostId = ILiveLoginManager.getInstance().getMyUserId(); //主人qq id
        option = new ILVCallOption(hostId)
                .callTips("Monitor")
                .setMemberListener(this)
                .setCallType(ILVCallConstants.CALL_TYPE_VIDEO);
        startMonitorRequest(); //开启监控请求
        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL); //最大的通话声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (maxVolume < 7 ? maxVolume : 7), AudioManager.FLAG_PLAY_SOUND); //调低声音达到降噪的效果
//        initAdapter(); //语音命令
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
            checkPermission(); //Android 6.0权限
        }
        //麦克风默认关闭
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ILVCallManager.getInstance().enableMic(false);
                ILiveSDK.getInstance().getAvAudioCtrl().setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_HEADSET);
            }
        }, 1000);
    }

    int cameraId;
    android.hardware.Camera mCamera;
    @Override
    protected void onResume() {
        ILVCallManager.getInstance().onResume();
        //onResume  中启用
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.acquire();
        super.onResume();
    }

    @Override
    protected void onPause() {
        ILVCallManager.getInstance().onPause();
        //onPause 中禁用
        if (wakeLock != null) {
            wakeLock.release();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ILVCallManager.getInstance().removeCallListener(this); //移出监听
        ILVCallManager.getInstance().onDestory();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }


    private void backAction(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                ILVCallManager.getInstance().endCall(mCallId); //关闭监控
            }
        }, 1500);
        showProgressDialog("关闭监控，请稍后", false);

    }

    @Override
    public void onCallEstablish(int callId) {
        //电话建立成功
        bVideoSucc = true;
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
                Log.d(TAG, "onCallEstablish->1:"  + av_root_view.getViewByIndex(0).getIdentifier() + "/" + av_root_view.getViewByIndex(1).getIdentifier());
                if (ILiveLoginManager.getInstance().getMyUserId().equals(av_root_view.getViewByIndex(0).getIdentifier())) {
                    Log.d(TAG, "performHandleMessage: 与大屏交换" );
                    av_root_view.swapVideoView(0, 1);     // 与大屏交换
                }
                av_root_view.getViewByIndex(1).setVisibility(View.GONE);

            }
        }else if(msg.what == 1){
            finish();
        }
    }

    @Override
    public void onCallEnd(int callId, int endResult, String endInfo) {
        Log.d(TAG, "onCallEnd");
        //电话结束
        finish();
    }

    @Override
    public void onException(int iExceptionId, int errCode, String errMsg) {
        //异常事件处理
        Log.d(TAG, "onException:"+ errCode + "--errMsg:"+ errMsg);
        finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_sound: //扬声器
                changeSpeaker();
                if(bSpeaker){
                    iv_sound.setBackgroundResource(R.drawable.video_sound_off);
                }else {
                    iv_sound.setBackgroundResource(R.drawable.video_sound_on);
                }
                break;

            case R.id.rl_video: //录制
                toastShow("呵呵");
                break;

            case R.id.rl_control: //遥控
                bControl = !bControl;
                if(bControl){
                    rl_control_robot.setVisibility(View.VISIBLE);
                }else {
                    rl_control_robot.setVisibility(View.GONE);
                }
                break;

            case R.id.iv_screen_shot: //截图
                        // Take screen shot
                break;

            case R.id.rl_voice: //麦克风
//                changeMic();
//                if(bMicEnalbe){
//                    iv_voice.setBackgroundResource(R.drawable.video_voice_off);
//                }else {
//                    iv_voice.setBackgroundResource(R.drawable.video_voice_on);
//                }
             break;

            case R.id.iv_timeline: //时间轴
                break;

            case R.id.rl_back_action:
               backAction();
                break;
        }
    }

    @Override
    public void onCameraEvent(String id, boolean bEnable) {

    }

    @Override
    public void onMicEvent(String id, boolean bEnable) {

    }

//    @Override
//    public void onMemberEvent(String id, boolean bEnter) {
//        Log.e("onMemberEvent", bEnter?"true":"false");
//        if(!TextUtils.isEmpty(id) && !id.equals(ILiveLoginManager.getInstance().getMyUserId())&& !bEnter){
//            finish();
//        }
//    }

    int state;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (QuickClick.oneClick()) {
                    // 点击过快不响应
                    return false;
                }
                startTime = System.currentTimeMillis();
                switch (v.getId()) {
                    case rl_front:
                        state = 0;
                        Log.i("main", "=============长前进");
                        break;
                    case R.id.rl_back:
                        state = 1;
                        Log.i("main", "=============长后退");
                        break;
                    case R.id.rl_left:
                        state = 2;
                        Log.i("main", "=============长向左");
                        break;
                    case R.id.rl_right:
                        state = 3;
                        Log.i("main", "=============长向右");
                        break;
                    default:
                        break;
                }
                setControlViewState(state);
                ServiceLogin.getInstance().sendRobotMoveOrder(state, new IMMsgListener() {
                    @Override
                    public void onSuccess(TIMMessage msg) {

                    }

                    @Override
                    public void onError(int code, String desc) {

                    }
                });
                break;

            case MotionEvent.ACTION_UP:
                 state = 4;
                 setControlViewState(4);
                 stopTime = System.currentTimeMillis();
                if (stopTime - startTime < 300) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                ServiceLogin.getInstance().sendRobotMoveOrder(state, new IMMsgListener() {
                                    @Override
                                    public void onSuccess(TIMMessage msg) {

                                    }

                                    @Override
                                    public void onError(int code, String desc) {

                                    }
                                });
                                Log.i("call", "短按停止=====");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Log.i("call", "长按停止=====");
                    ServiceLogin.getInstance().sendRobotMoveOrder(state, new IMMsgListener() {
                        @Override
                        public void onSuccess(TIMMessage msg) {

                        }

                        @Override
                        public void onError(int code, String desc) {

                        }
                    });
                }
                break;
        }
        return true;
    }
}
