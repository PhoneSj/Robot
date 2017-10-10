package com.zige.robot.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.callsdk.ILVCallConstants;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVIncomingListener;
import com.tencent.callsdk.ILVIncomingNotification;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.zige.robot.R;
import com.zige.robot.SpeechPlugin;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.bean.AnswerCode;
import com.zige.robot.fsj.ui.album.AlbumLocalActivity;
import com.zige.robot.fsj.ui.album.AlbumRemoteActivity;
import com.zige.robot.fsj.ui.call.activity.MainCallActivity;
import com.zige.robot.fsj.util.MyNetworkUtil;
import com.zige.robot.http.Parser;
import com.zige.robot.interf.IRecognizeListener;
import com.zige.robot.interf.ITextUnderStandListener;
import com.zige.robot.utils.ScreenUtils;
import com.zige.robot.utils.Util;
import com.zige.robot.utils.permission.PermissionChecker;
import com.zige.robot.view.MyVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ldw on 2017/4/27.
 */

public class MainActivity extends BaseActivity implements ILVIncomingListener {

    TextView tv_me;
    TextView tv_robot;
    ImageView iv_robot_log;
    MyVideoView video_view;
    LinearLayout ll_horizon;


//    int[] imageSelTabs = {R.drawable.interactive_on, R.drawable.instruction_on, R.drawable.video_on,
//            R.drawable.set_on, R.drawable.telecontrol_on, R.drawable.monitoring_on,
//            R.drawable.homework_on, R.drawable.leave_a_message_on, R.drawable.robot_on};

    //    int[] imageSelTabs = {R.drawable.instruction_on, R.drawable.video_on, R.drawable.homework_on,
//            R.drawable.telecontrol_on, R.drawable.set_on,
//            R.drawable.robot_on, R.drawable.ic_launcher};
    int[] imageSelTabs = {R.drawable.video_on, R.drawable.homework_on,
            R.drawable.telecontrol_on, R.drawable.photo_act, R.drawable.set_on};

    //    String[] textTabs ={"互动","播音","通话", "设置","遥控","监控", "作业","留言","机器人"};
    String[] textTabs = {"通话", "作业", "遥控", "相册", "设置"};


    //视频通话，视频监控权限
    static final String[] PERMISSIONS_1 = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    //视频，语音留言权限
    static final String[] PERMISSIONS_2 = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //作业录音，读写sd卡权限
    static final String[] PERMISSIONS_3 = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    static final String[] ppermissions_4 = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private int clickType = 0;//0 视频通话  1视频监控  2语音和视频留言  3作业

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_me = (TextView) findViewById(R.id.tv_me);
        tv_robot = (TextView) findViewById(R.id.tv_robot);
        iv_robot_log = (ImageView) findViewById(R.id.iv_robot_log);
        video_view = (MyVideoView) findViewById(R.id.video_view);
        ll_horizon = (LinearLayout) findViewById(R.id.ll_horizon); //滑动包裹器
        setMsgVisible(View.INVISIBLE, View.INVISIBLE); //隐藏对话框(暂时隐藏这个功能)
        //来电监听
        ILVCallManager.getInstance().addIncomingListener(this);
        //初始化图片选择器
        initImageTabAdapter();
        //初始化讯飞
        SpeechPlugin.CreateInstance(mContext);
        //权限检测器
        mPermissionChecker = new PermissionChecker(mContext);
        //test
        iv_robot_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                playRobotVideo();
            }
        });
    }

    /**
     * @param left_v  左边对话框
     * @param right_v 右边对话框
     */
    private void setMsgVisible(int left_v, int right_v) {
        tv_me.setVisibility(left_v);
        tv_robot.setVisibility(right_v);
    }

    /**
     * 首页tab选择器
     */
    private void initImageTabAdapter() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int itemWith = ScreenUtils.getScreenW(mContext) / 5;
        for (int i = 0; i < imageSelTabs.length; i++) {
            View itemView = inflater.inflate(R.layout.layout_image_tab, null);
            LinearLayout ll_tab = (LinearLayout) itemView.findViewById(R.id.ll_tab);
            ll_tab.getLayoutParams().width = itemWith;
            ImageView iv = (ImageView) itemView.findViewById(R.id.iv_tab);
            TextView tv = (TextView) itemView.findViewById(R.id.tv_tab);
            iv.setImageResource(imageSelTabs[i]);
            tv.setText(textTabs[i]);
            iv.setTag(i); //设置tag对象，方便onClick回调中获取
            iv.setOnClickListener(tabClickListener);
            ll_horizon.addView(itemView);
        }
    }

    /**
     * 首页tab点击事件
     */
//    View.OnClickListener tabClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int position = (int) v.getTag();
//            if (position == 0) {
//                //播音
//                goSpeakVoice();
//            } else if (position == 1) {
//                //通话
////                goVideoCall();
//                // TODO: 2017/9/7 使用环信
//                goCallPage();
//            } else if (position == 2) {
//                //作业
//                goHomeWork();
//            } else if (position == 3) {
//                //遥控
//                goControlRobot();
//            } else if (position == 4) {
//                //设置
//                goSetting();
//            } else if (position == 5) {
//                //机器人
//                goRobotInfo();
//            } else if (position == 6) {
//                goAlbum();
//            }
//        }
//    };

    View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (position == 0) {
                //通话
                //                goVideoCall();
                // TODO: 2017/9/7 使用环信
                goCallPage();
            } else if (position == 1) {
                //作业
                goHomeWork();
            } else if (position == 2) {
                //遥控
                goControlRobot();
            } else if (position == 3) {
                goAlbum();
            } else if (position == 4) {
                //设置
                goSetting();
            }
        }
    };

    private void goAlbum() {
        //检查权限
        if (mPermissionChecker.isLackPermissions(ppermissions_4)) {
            mPermissionChecker.requestPermissions();
        } else {
            startActivity(AlbumRemoteActivity.class);
        }
//        startActivity(AlbumLocalActivity.class);
    }

    //播音
    private void goSpeakVoice() {
        startActivity(SpeakVoiceActivity.class);
    }

    //通话
    private void goVideoCall() {
        clickType = 0;
        //检查权限
        if (mPermissionChecker.isLackPermissions(PERMISSIONS_1)) {
            mPermissionChecker.requestPermissions();
        } else {
            startVideoCall();
        }
    }

    private void goCallPage() {
        Intent callIntent = new Intent(this, MainCallActivity.class);
        startActivity(callIntent);
    }

    //作业
    private void goHomeWork() {
        clickType = 3;
        if (mPermissionChecker.isLackPermissions(PERMISSIONS_3)) {
            mPermissionChecker.requestPermissions();
        } else {
            startActivity(HomeWorkNewActivity.class);
        }
    }

    //遥控
    private void goControlRobot() {
        startActivity(ControlActivity.class);
    }

    //监控
    private void goRobotMonitor() {
        clickType = 1;
        if (mPermissionChecker.isLackPermissions(PERMISSIONS_1)) {
            mPermissionChecker.requestPermissions();
        } else {
            startVideoMonitor();
        }
    }

    //设置
    private void goSetting() {
        startActivity(SettingActivity.class);
    }

    //机器人
    private void goRobotInfo() {
        startActivity(RobotInfoActivity.class);
    }

    //互动
    private void goInteraction() {
        startActivity(InteractionActivity.class);
    }

    //留言
    private void goLeaveMsg() {
        clickType = 2;
        if (mPermissionChecker.isLackPermissions(PERMISSIONS_2)) {
            mPermissionChecker.requestPermissions();
        } else {
            startActivity(LeaveMsgActivity.class);
        }
    }


    /**
     * 发起呼叫
     */
    public void makeCall(int callType, ArrayList<String> nums) {
        Intent intent = new Intent();
        intent.setClass(this, VideoCallActivity.class);
        intent.putExtra("HostId", ILiveLoginManager.getInstance().getMyUserId());
        intent.putExtra("CallId", 0);
        intent.putExtra("CallType", callType);
        intent.putStringArrayListExtra("CallNumbers", nums);
        startActivity(intent);
    }


    /**
     * 视频通话
     */
    private void startVideoCall() {
        //网络是否可用
        if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
            toastShow("当前网络不可用");
            return;
        }
        List<String> robot = new ArrayList<>();
        robot.add(mApplication.getUserInfo().getDeviceid());
        makeCall(ILVCallConstants.CALL_TYPE_VIDEO, (ArrayList<String>) robot);
    }

    /**
     * 视频监控
     */
    private void startVideoMonitor() {
        if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
            toastShow("当前网络不可用");
            return;
        }
//                if("family".equals(AppContext.getInstance().getUserInfo().getUserrole())){
//                    if(Util.getNetWork(mContext) == 1){
//                        toastShow("当前是手机流量,请注意");
//                    }
        startActivity(MonitorActivity.class);
//                }else {
//                    toastShow("您还不是家人，不能使用此功能");
//                }
    }


    /**
     * 语音识别监听
     */
    IRecognizeListener mIRecognizeListener = new IRecognizeListener() {
        @Override
        public void onRecognizeOver(String text) {
            setMsgVisible(View.VISIBLE, View.GONE);
            tv_me.setText(text);
            SpeechPlugin.getInstance().onTextUnderstand(text); //语义理解
        }
    };
    /**
     * 语义理解监听
     */
    ITextUnderStandListener mITextUnderStandListener = new ITextUnderStandListener() {
        @Override
        public void onUnderStandOver(String text) {
//            if(TextUtils.isEmpty(text)){
//                SpeechPlugin.getInstance().startSpeak("我没有听清楚，能再说一遍吗?");
//            }else {
//                setMsgVisible(View.VISIBLE, View.VISIBLE);
//                tv_robot.setText(text);
//            }
            AnswerCode answerCode = Parser.getAnswer(text);
            if (answerCode.rc == 0 && !TextUtils.isEmpty(answerCode.answer.text)) {
                tv_robot.setText(answerCode.answer.text);
                setMsgVisible(View.VISIBLE, View.VISIBLE);
                SpeechPlugin.getInstance().startSpeak(answerCode.answer.text);
            } else {
                SpeechPlugin.getInstance().startSpeak("主人您说什么，我没有听清楚，再说一遍啦");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        SpeechPlugin.getInstance().startRecognize(); //开启听写
//        SpeechPlugin.getInstance().setIRecognizeListener(mIRecognizeListener);
//        SpeechPlugin.getInstance().setITextUnderStandListener(mITextUnderStandListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        video_view.stopPlayback();
//        iv_robot_log.setVisibility(View.VISIBLE);
//        SpeechPlugin.getInstance().stopSpeak();
//        SpeechPlugin.getInstance().stopRecognize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ILVCallManager.getInstance().removeIncomingListener(this);
//        SpeechPlugin.getInstance().onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (mPermissionChecker.hasAllPermissionsGranted(grantResults)) {
                    if (clickType == 0) {
                        startVideoCall();
                    } else if (clickType == 1) {
                        startVideoMonitor();
                    } else if (clickType == 2) {
                        startActivity(LeaveMsgActivity.class);
                    } else if (clickType == 3) {
                        startActivity(HomeWorkNewActivity.class);
                    }
                } else {
                    mPermissionChecker.showDialog();
                }
                break;
        }
    }

    /**
     * 加载gif动画
     *
     * @param resId
     */
    private void loadRobotGif(int resId) {
        Glide.with(MainActivity.this)
             .load(resId)
             .asGif()
             .dontAnimate()
             .diskCacheStrategy(DiskCacheStrategy.NONE)
             .into(iv_robot_log);
    }

    int robot_num = 0;

    private void playRobotVideo() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_robot_log.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);
        //播放完成回调
        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                iv_robot_log.setVisibility(View.VISIBLE);
            }
        });
        //设置视频路径
        robot_num++;
        if (robot_num % 2 == 0) {
            video_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.robot_log1));
        } else {
            video_view.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.robot_log2));
        }
        video_view.start();
    }


    /**
     * 用户第一次 点击back键时，提示用户‘再按一次返回键退出’
     * 第二次点击back键时将退出app。
     */
    private long firstime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondtime = System.currentTimeMillis();
            if (secondtime - firstime > 3000) {
                toastShow("再按一次返回键退出");
                firstime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNewIncomingCall(int callId, int callType, ILVIncomingNotification notification) {
        Intent intent = new Intent();
        intent.setClass(this, ComingCallDialogActivity.class);
        intent.putExtra("CallId", callId);
        intent.putExtra("CallType", callType);
        intent.putExtra("HostId", notification.getSponsorId());
        startActivity(intent);
    }
}

