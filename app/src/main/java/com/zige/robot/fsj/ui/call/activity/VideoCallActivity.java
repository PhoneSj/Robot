package com.zige.robot.fsj.ui.call.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMVideoCallHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.media.EMCallSurfaceView;
import com.superrtc.sdk.VideoView;
import com.zige.robot.App;
import com.zige.robot.R;
import com.zige.robot.fsj.model.bean.CallInfoBean;
import com.zige.robot.fsj.model.bean.CallRemainTimeBean;
import com.zige.robot.fsj.model.event.CallEvent;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.ui.album.util.HttpConfig;
import com.zige.robot.fsj.ui.call.base.CallActivity;
import com.zige.robot.fsj.ui.call.base.CallManager;
import com.zige.robot.fsj.ui.call.util.VMDimenUtil;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.fsj.util.RxUtil;
import com.zige.robot.fsj.util.ToastUtil;
import com.zige.robot.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by lzan13 on 2016/10/18.
 * 视频通话界面处理
 */
public class VideoCallActivity extends CallActivity {

    // 视频通话帮助类
    private EMVideoCallHelper videoCallHelper;
    // SurfaceView 控件状态，-1 表示通话未接通，0 表示本小远大，1 表示远小本大
    private int surfaceState = -1;

    private EMCallSurfaceView localSurface = null;
    private EMCallSurfaceView oppositeSurface = null;
    private RelativeLayout.LayoutParams localParams = null;
    private RelativeLayout.LayoutParams oppositeParams = null;

    // 使用 ButterKnife 注解的方式获取控件
    @BindView(R.id.layout_surface_container)
    RelativeLayout layoutSurfaceContainer;
    @BindView(R.id.ibtn_call_control_cancel)
    ImageButton ibtnCallControlCancel;
    @BindView(R.id.ibtn_call_control_change_camera)
    ImageButton ibtnCallControlChangeCamera;
    @BindView(R.id.ibtn_call_control_mic)
    ImageButton ibtnCallControlMic;
    @BindView(R.id.ibtn_call_control_speaker)
    ImageButton ibtnCallControlSpeaker;
    @BindView(R.id.ibtn_call_control_camera)
    ImageButton ibtnCallControlCamera;
    @BindView(R.id.ibtn_call_control_screenshot)
    ImageButton ibtnCallControlScreenshot;
    @BindView(R.id.ibtn_call_control_record)
    ImageButton ibtnCallControlRecord;
    @BindView(R.id.layout_control)
    RelativeLayout layoutControl;
    @BindView(R.id.tv_call_negative_desc)
    TextView tvCallNegativeDesc;
    @BindView(R.id.tv_call_negative_available_time)
    TextView tvCallNegativeAvailableTime;
    @BindView(R.id.ibtn_call_negative_refuse)
    ImageButton ibtnCallNegativeRefuse;
    @BindView(R.id.ibtn_call_negative_answer)
    ImageButton ibtnCallNegativeAnswer;
    @BindView(R.id.layout_negative)
    LinearLayout layoutNegative;
    @BindView(R.id.iv_call_positive_robot)
    ImageView ivCallPositiveRobot;
    @BindView(R.id.tv_call_positive_available_time)
    TextView tvCallPositiveAvailableTime;
    @BindView(R.id.tv_call_positive_desc)
    TextView tvCallPositiveDesc;
    @BindView(R.id.ibtn_call_position_cancel)
    ImageButton ibtnCallPositionCancel;
    @BindView(R.id.tv_reject)
    TextView tvReject;
    @BindView(R.id.layout_position)
    RelativeLayout layoutPosition;
    @BindView(R.id.layout_root)
    CoordinatorLayout layoutRoot;

    //测试控件
    TextView testView;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_call_vedio);
//        ButterKnife.bind(this);
//
//        initView();
//        testView = (TextView) findViewById(R.id.testView);
//        testView.setText(SharedPreferencesUtils.getConnectedHXContact());
//    }

    @Override
    protected int getLayout() {
        return R.layout.activity_call_vedio;
    }

    @Override
    protected void initEventAndData() {
        initView();
        testView = (TextView) findViewById(R.id.testView);
        testView.setText(SharedPreferencesUtils.getConnectedHXContact());
//        mPresenter.getRemainTime();
        String robotId = SharedPreferencesUtils.getRobotIdFromSP();
        HttpConfig.getInstance()
                  .getApi()
                  .getCallRemainTime(robotId)
                  .compose(RxUtil.<CallHttpResponse<CallRemainTimeBean>>rxSchedulerHelper())
                  .compose(RxUtil.<CallRemainTimeBean>handleCallResult())
                  .subscribe(new Consumer<CallRemainTimeBean>() {
                      @Override
                      public void accept(CallRemainTimeBean callRemainTimeBean) throws Exception {
                          tvCallNegativeAvailableTime.setText("可用通话剩余" + callRemainTimeBean.getRemain() + "分钟");
                          tvCallPositiveAvailableTime.setText("可用通话剩余" + callRemainTimeBean.getRemain() + "分钟");
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          ToastUtil.shortShow("获取剩余通话失败");
                          LogUtil.showE("获取剩余通话失败");
                      }
                  });

    }

    /**
     * 重载父类方法,实现一些当前通话的操作，
     */
    @Override
    protected void initView() {
        super.initView();
        if (CallManager.getInstance().isInComingCall()) {
            //来电，显示两个按钮（接听、不接听）
            layoutNegative.setVisibility(View.VISIBLE);
            layoutPosition.setVisibility(View.GONE);
        } else {
            //去电，显示一个按钮（结束通话）
            layoutNegative.setVisibility(View.GONE);
            layoutPosition.setVisibility(View.VISIBLE);
        }

        //麦克风是否开启
        ibtnCallControlMic.setActivated(!CallManager.getInstance().isOpenMic());
        //摄像头是否开启!
        ibtnCallControlCamera.setActivated(!CallManager.getInstance().isOpenCamera());
        //扬声器是否开启
        ibtnCallControlSpeaker.setActivated(CallManager.getInstance().isOpenSpeaker());
        //录音是否开启
        ibtnCallControlRecord.setActivated(CallManager.getInstance().isOpenRecord());

        // 初始化视频通话帮助类
        videoCallHelper = EMClient.getInstance().callManager().getVideoCallHelper();

        // 初始化显示通话画面
        initCallSurface();
        // 判断当前通话是刚开始，还是从后台恢复已经存在的通话
        if (CallManager.getInstance().getCallState() == CallManager.CallState.ACCEPTED) {
            layoutNegative.setVisibility(View.GONE);
            layoutPosition.setVisibility(View.GONE);
            layoutControl.setVisibility(View.VISIBLE);
            refreshCallTime();
            // 通话已接通，修改画面显示
            onCallSurface();
        }

        try {
            // 设置默认摄像头为前置
            EMClient.getInstance()
                    .callManager()
                    .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        CallManager.getInstance().setCallCameraDataProcessor();
    }

    @OnClick({R.id.layout_control, R.id.ibtn_call_control_cancel, R.id.ibtn_call_control_camera, R.id.ibtn_call_control_mic, R.id.ibtn_call_control_speaker,
            R.id.ibtn_call_control_screenshot, R.id.ibtn_call_control_record, R.id.ibtn_call_negative_refuse, R.id.ibtn_call_negative_answer,
            R.id.ibtn_call_position_cancel})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.layout_control:
                onControlLayout();
                break;
            case R.id.ibtn_call_control_camera:
                // 转换摄像头
                changeCamera();
                break;
            case R.id.ibtn_call_control_mic:
                //麦克是否启用
                onMicrophone();
                break;
            case R.id.ibtn_call_control_speaker:
                //扬声器是否启用
                onSpeaker();
                break;
            case R.id.ibtn_call_control_screenshot:
                //截图
                onScreenShot();
                break;
            case R.id.ibtn_call_control_record:
                //录音
                onRecordCall();
                break;
            case R.id.ibtn_call_control_cancel:
            case R.id.ibtn_call_position_cancel:
                // 结束通话
                endCall();
                break;
            case R.id.ibtn_call_negative_refuse:
                // 拒接来电
                rejectCall();
                break;
            case R.id.ibtn_call_negative_answer:
                // 接收来电
                answerCall();
                break;
        }
    }

    /**
     * 操作区域的显示与隐藏
     */
    private void onControlLayout() {
        if (layoutControl.isShown()) {
            layoutControl.setVisibility(View.GONE);
        } else {
            layoutControl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 退出全屏通话界面
     */
    private void exitFullScreen() {
        CallManager.getInstance().addFloatWindow();
        // 结束当前界面
        onFinish();
    }

    /**
     * 切换
     */
    private void changeCamera() {
        // 根据切换摄像头开关是否被激活确定当前是前置还是后置摄像头
        try {
            if (EMClient.getInstance().callManager().getCameraFacing() == 1) {
                EMClient.getInstance().callManager().switchCamera();
                EMClient.getInstance().callManager().setCameraFacing(0);
                // 设置按钮图标
                ibtnCallControlChangeCamera.setImageResource(R.drawable.vt_camera_nor);
            } else {
                EMClient.getInstance().callManager().switchCamera();
                EMClient.getInstance().callManager().setCameraFacing(1);
                // 设置按钮图标
                ibtnCallControlChangeCamera.setImageResource(R.drawable.vt_camera_press);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    private void onMicrophone() {
        try {
            // 根据麦克风开关是否被激活来进行判断麦克风状态，然后进行下一步操作
            if (ibtnCallControlMic.isActivated()) {
                // 设置按钮状态
                ibtnCallControlMic.setActivated(false);
                // 暂停语音数据的传输
                EMClient.getInstance().callManager().resumeVoiceTransfer();
                CallManager.getInstance().setOpenMic(true);
            } else {
                // 设置按钮状态
                ibtnCallControlMic.setActivated(true);
                // 恢复语音数据的传输
                EMClient.getInstance().callManager().pauseVoiceTransfer();
                CallManager.getInstance().setOpenMic(false);
            }
        } catch (HyphenateException e) {
            Log.e(App.TAG, "exception code: %d, %s" + ",code:" + e.getErrorCode() + "msg:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 摄像头开关
     */
    private void onCamera() {
        try {
            // 根据摄像头开关按钮状态判断摄像头状态，然后进行下一步操作
            if (ibtnCallControlCamera.isActivated()) {
                // 设置按钮状态
                ibtnCallControlCamera.setActivated(false);
                // 暂停视频数据的传输
                EMClient.getInstance().callManager().resumeVideoTransfer();
                CallManager.getInstance().setOpenCamera(true);
            } else {
                // 设置按钮状态
                ibtnCallControlCamera.setActivated(true);
                // 恢复视频数据的传输
                EMClient.getInstance().callManager().pauseVideoTransfer();
                CallManager.getInstance().setOpenCamera(false);
            }
        } catch (HyphenateException e) {
            Log.e(App.TAG, "exception code: %d, %s" + ",code:" + e.getErrorCode() + ",msg:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 扬声器开关
     */
    private void onSpeaker() {
        // 根据按钮状态决定打开还是关闭扬声器
        if (ibtnCallControlSpeaker.isActivated()) {
            // 设置按钮状态
            ibtnCallControlSpeaker.setActivated(false);
            CallManager.getInstance().closeSpeaker();
            CallManager.getInstance().setOpenSpeaker(false);
        } else {
            // 设置按钮状态
            ibtnCallControlSpeaker.setActivated(true);
            CallManager.getInstance().openSpeaker();
            CallManager.getInstance().setOpenSpeaker(true);
        }
    }

    /**
     * 录制视屏通话内容
     */
    private void onRecordCall() {
        // 根据开关状态决定是否开启录制
        if (ibtnCallControlRecord.isActivated()) {
            // 设置按钮状态
            ibtnCallControlRecord.setActivated(false);
            String path = videoCallHelper.stopVideoRecord();
            CallManager.getInstance().setOpenRecord(false);
            File file = new File(path);
            if (file.exists()) {
                Toast.makeText(activity, "录制视频成功 " + path, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "录制失败/(ㄒoㄒ)/~~", Toast.LENGTH_LONG).show();
            }
        } else {
            // 设置按钮状态
            ibtnCallControlRecord.setActivated(true);
            // 先创建文件夹
            String dirPath = getExternalFilesDir("").getAbsolutePath() + "/videos";
            File dir = new File(dirPath);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            videoCallHelper.startVideoRecord(dirPath);
            Log.d(App.TAG, "开始录制视频");
            Toast.makeText(activity, "开始录制", Toast.LENGTH_LONG).show();
            CallManager.getInstance().setOpenRecord(true);
        }
    }

    /**
     * 保存通话截图
     */
    private void onScreenShot() {
        String dirPath = getExternalFilesDir("").getAbsolutePath() + "/videos/";
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        String path = dirPath + "video_" + System.currentTimeMillis() + ".jpg";
        boolean result = videoCallHelper.takePicture(path);
        Toast.makeText(activity, "截图保存成功 " + path, Toast.LENGTH_LONG).show();
    }

    /**
     * 接听通话
     */
    @Override
    protected void answerCall() {
        super.answerCall();
        layoutControl.setVisibility(View.VISIBLE);
        layoutNegative.setVisibility(View.GONE);
        layoutPosition.setVisibility(View.GONE);
    }

    /**
     * 初始化通话界面控件
     */
    private void initCallSurface() {
        // 初始化远程画面控件
        oppositeSurface = new EMCallSurfaceView(activity);
        oppositeParams = new RelativeLayout.LayoutParams(0, 0);
        oppositeParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        oppositeParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        oppositeSurface.setLayoutParams(oppositeParams);
        layoutSurfaceContainer.addView(oppositeSurface);

        // 初始化本地画面控件
        localSurface = new EMCallSurfaceView(activity);
        localParams = new RelativeLayout.LayoutParams(0, 0);
        localParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        localParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        localParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        localParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        localSurface.setLayoutParams(localParams);
        layoutSurfaceContainer.addView(localSurface);

        localSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/9/7
                onControlLayout();
            }
        });

        //本地视频画面显示在底层
        localSurface.setZOrderOnTop(false);
        localSurface.setZOrderMediaOverlay(true);

        // 设置本地和远端画面的显示方式，是填充，还是居中
        localSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        oppositeSurface.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        // 设置通话画面显示控件
        EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
    }

    /**
     * 接通通话，这个时候要做的只是改变本地画面 view 大小，不需要做其他操作
     */
    private void onCallSurface() {
        Log.i(App.TAG, "onCallSurface");
        // 更新通话界面控件状态
        surfaceState = 0;

        int width = VMDimenUtil.dp2px(activity, 96);
        int height = VMDimenUtil.dp2px(activity, 128);
        int leftMargin = VMDimenUtil.dp2px(activity, 16);
        int bottomMargin = VMDimenUtil.dp2px(activity, 96);

        localParams = new RelativeLayout.LayoutParams(width, height);
        localParams.width = width;
        localParams.height = height;
        localParams.leftMargin = leftMargin;
        localParams.bottomMargin = bottomMargin;
        localParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        localParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        localSurface.setLayoutParams(localParams);

        localSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCallSurface();
            }
        });

        oppositeSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onControlLayout();
            }
        });
    }

    /**
     * 切换通话界面，这里就是交换本地和远端画面控件设置，以达到通话大小画面的切换
     */
    private void changeCallSurface() {
        if (surfaceState == 0) {
            surfaceState = 1;
            EMClient.getInstance().callManager().setSurfaceView(oppositeSurface, localSurface);
        } else {
            surfaceState = 0;
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallEvent(CallEvent event) {
        if (event.isState()) {
            refreshCallView(event);
        }
        if (event.isTime()) {
            // 不论什么情况都检查下当前时间
            refreshCallTime();
        }
    }

    private void showBusyDailog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("占线中...")
               .setMessage("机器人正在视频通话中")
               .setIcon(R.drawable.dial_busy_ico)
               .setNegativeButton("等待", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               })
               .setNegativeButton("挂断", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       CallManager.getInstance().saveCallMessage();
                       CallManager.getInstance().reset();
                       setResult(0);
                   }
               })
               .create()
               .show();
    }

    private void showNoResponseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("暂时无人接听")
               .setIcon(R.drawable.dial_nobody_ico)
               .setMessage("是否进入巡航模式")
               .setNegativeButton("挂断", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                       setResult(0);
                   }
               })
               .setPositiveButton("进入巡航模式", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       // TODO: 2017/9/11
                   }
               })
               .create()
               .show();
    }

    private void showNetworkExceptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Mento连接异常")
               .setIcon(R.drawable.dial_neterror_ico)
               .setMessage("出现网络故障或机器人未开启")
               .setNeutralButton("挂断", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                       setResult(0);
                   }
               })
               .create()
               .show();
    }

    /**
     * 刷新通话界面
     */
    private void refreshCallView(CallEvent event) {
        EMCallStateChangeListener.CallError callError = event.getCallError();
        EMCallStateChangeListener.CallState callState = event.getCallState();
        switch (callState) {
            case CONNECTING: // 正在呼叫对方，TODO 没见回调过
                Log.i(App.TAG, "正在呼叫对方" + callError);
                break;
            case CONNECTED: // 正在等待对方接受呼叫申请（对方申请与你进行通话）
                Log.i(App.TAG, "正在连接" + callError);
                if (CallManager.getInstance().isInComingCall()) {
                    tvCallNegativeDesc.setText(R.string.call_connected_is_incoming);
                } else {
                    tvCallPositiveDesc.setText(R.string.call_connected);
                }
                break;
            case ACCEPTED: // 通话已接通
                Log.i(App.TAG, "通话已接通");
                tvCallNegativeDesc.setText(R.string.call_accepted);
                tvCallPositiveDesc.setText(R.string.call_accepted);
                // 通话接通，更新界面 UI 显示
                onCallSurface();
                saveCallInfoToSP();
                break;
            case DISCONNECTED: // 通话已中断
                Log.i(App.TAG, "通话已结束" + callError);
                onFinish();
                if (callError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                    showBusyDailog();
                } else if (callError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                    showNoResponseDialog();
                } else {
                    setResult(0);
                }
                break;
            case NETWORK_DISCONNECTED:
                Toast.makeText(activity, "对方网络断开", Toast.LENGTH_SHORT).show();
                Log.i(App.TAG, "对方网络断开");
                showNetworkExceptionDialog();
                break;
            case NETWORK_NORMAL:
                Log.i(App.TAG, "网络正常");
                break;
            case NETWORK_UNSTABLE:
                Toast.makeText(activity, "网络不稳定", Toast.LENGTH_SHORT).show();
                if (callError == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
                    Log.i(App.TAG, "没有通话数据" + callError);
                } else {
                    Log.i(App.TAG, "网络不稳定" + callError);
                }
                showNetworkExceptionDialog();
                break;
            case VIDEO_PAUSE:
                Toast.makeText(activity, "对方已暂停视频传输", Toast.LENGTH_SHORT).show();
                Log.i(App.TAG, "对方已暂停视频传输");
                break;
            case VIDEO_RESUME:
                Toast.makeText(activity, "对方已恢复视频传输", Toast.LENGTH_SHORT).show();
                Log.i(App.TAG, "对方已恢复视频传输");
                break;
            case VOICE_PAUSE:
                Toast.makeText(activity, "对方已暂停语音传输", Toast.LENGTH_SHORT).show();
                Log.i(App.TAG, "对方已暂停语音传输");
                break;
            case VOICE_RESUME:
                Toast.makeText(activity, "对方已恢复语音传输", Toast.LENGTH_SHORT).show();
                Log.i(App.TAG, "对方已恢复语音传输");
                break;
            default:
                break;
        }
    }

    /**
     * 刷新通话时间显示
     */
    private void refreshCallTime() {
        int t = CallManager.getInstance().getCallTime();
        int h = t / 60 / 60;
        int m = t / 60 % 60;
        int s = t % 60 % 60;
        String time = "";
        if (h > 9) {
            time = "" + h;
        } else {
            time = "0" + h;
        }
        if (m > 9) {
            time += ":" + m;
        } else {
            time += ":0" + m;
        }
        if (s > 9) {
            time += ":" + s;
        } else {
            time += ":0" + s;
        }
        // TODO: 2017/9/21 没有涉及ui显示通话时长
//        if (!callTimeView.isShown()) {
//            callTimeView.setVisibility(View.VISIBLE);
//        }
//        callTimeView.setText(time);

    }

    /**
     * 屏幕方向改变回调方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onUserLeaveHint() {
        //super.onUserLeaveHint();
        exitFullScreen();
    }

    /**
     * 通话界面拦截 Back 按键，不能返回
     */
    @Override
    public void onBackPressedSupport() {
        //super.onBackPressed();
        exitFullScreen();
    }

    @Override
    protected void onFinish() {
        // release surface view
        if (localSurface != null) {
            if (localSurface.getRenderer() != null) {
                localSurface.getRenderer().dispose();
            }
            localSurface.release();
            localSurface = null;
        }
        if (oppositeSurface != null) {
            if (oppositeSurface.getRenderer() != null) {
                oppositeSurface.getRenderer().dispose();
            }
            oppositeSurface.release();
            oppositeSurface = null;
        }
        super.onFinish();
    }

//    @Override
//    public void showRemainTime(CallRemainTimeBean bean) {
//        tvCallNegativeAvailableTime.setText("可用通话剩余" + bean.getRemain() + "分钟");
//        tvCallPositiveAvailableTime.setText("可用通话剩余" + bean.getRemain() + "分钟");
//    }
//
//    @Override
//    public void showPerssionResult(boolean isGranted) {
//        //不用实现
//    }

    /**
     * 保存通话记录
     */
    private void saveCallInfoToSP() {
        CallInfoBean bean = new CallInfoBean();
        if (CallManager.getInstance().isInComingCall()) {

        }
    }

}
