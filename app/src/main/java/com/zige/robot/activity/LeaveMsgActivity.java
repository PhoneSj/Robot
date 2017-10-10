package com.zige.robot.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.TIMMessage;
import com.zige.robot.AppConfig;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.interf.ActionClickListener;
import com.zige.robot.interf.IMMsgListener;
import com.zige.robot.service.ServiceLogin;
import com.zige.robot.utils.DialogUtils;
import com.zige.robot.utils.FileUtils;
import com.zige.robot.utils.MediaUtils;
import com.zige.robot.view.CirclePercentView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by lidingwei on 2017/7/25.
 * 留言
 */
public class LeaveMsgActivity extends BaseActivity {

    private static final String TAG = "LeaveMsgActivity";
    private static final long MAX_TIME_LENGTH = 10 * 1000; //10s 录制时间

    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;
    @BindView(R.id.rl_empty)
    RelativeLayout mRlEmpty;
    @BindView(R.id.circle_percent_view)
    CirclePercentView mCirclePercentView;
    @BindView(R.id.iv_return_voice_click)
    ImageView mIvReturnVoiceClick;
    @BindView(R.id.iv_camera_transfer_click)
    ImageView mIvCameraTransferClick;

    private Dialog sendDialog;//发送dialog

    private MediaUtils mMediaUtils;

    long startTime;

    int media_type = 0; // 0视频 1 语音

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_leave_msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initSendDialog(); //初始化发送dialog
        mMediaUtils = new MediaUtils(mContext); //音频工具类
        FileUtils.getSaveFolder(AppConfig.DEFAULT_VOICE_FILE_PATH); //新建语音视频文件夹
        FileUtils.getSaveFolder(AppConfig.DEFAULT_VIDEO_FILE_PATH);
        mCirclePercentView.setOnTouchListener(mOnTouchListener);
        mCirclePercentView.setOnEndListener(new CirclePercentView.CircleAnimationEndListener() {
            @Override
            public void onEndAnimation() {
                //10s时间结束了
                stopRecord(true);
            }
        });
        mMediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);
        mMediaUtils.setSurfaceView(mSurfaceView); //视频录制设置
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteAllFiles(new File(AppConfig.DEFAULT_VOICE_FILE_PATH)); //清除媒体文件
        FileUtils.deleteAllFiles(new File(AppConfig.DEFAULT_VIDEO_FILE_PATH));
    }


    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "onTouch: ACTION_DOWN" );
                    startTime = System.currentTimeMillis();
                    mCirclePercentView.startAnimation(0, 360, MAX_TIME_LENGTH); //开始录音
                    if(media_type ==0){
                        videoRecord();
                    }else {
                        soundRecord();
                    }
                    mMediaUtils.record();
                    ret = true;
                    break;


                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouch: ACTION_UP" );
                    if (System.currentTimeMillis() - startTime < 1000) {
                        toastShow("时间太短");
                        stopRecord(false);
                    } else {
                        stopRecord(true);
                    }
                    break;
            }
            return ret;

        }
    };


    /**
     * 录制语音
     */
    private void soundRecord() {
        mMediaUtils.setTargetDir(new File(AppConfig.DEFAULT_VOICE_FILE_PATH));
        mMediaUtils.setTargetName("" + System.currentTimeMillis() + ".mp3");
    }

    /**
     * 录制视频
     */
    private void videoRecord() {
        mMediaUtils.setTargetDir(new File(AppConfig.DEFAULT_VIDEO_FILE_PATH));
        mMediaUtils.setTargetName("" + System.currentTimeMillis() + ".mp4");
    }

    /**
     * 停止录制
     *
     * @param bSave true 保留文件  false 删除文件
     */
    private void stopRecord(boolean bSave) {
        mCirclePercentView.setRestView(); //重置录制进度条
        if (bSave) {
            if (media_type == 0) {
                sendFileTip("是否发送录制视频");
            } else {
                sendFileTip("是否发送录制语音");
            }
            mMediaUtils.stopRecordSave();
            sendDialog.show();
        } else {
            mMediaUtils.stopRecordUnSave();
        }
    }

    /**
     * 发送文件到robot
     */
    private void sendFile2Tx() {
        showProgressDialog("发送中", false);
        String fileName = "";
        if (media_type == 0) {
            fileName = "video_" + System.currentTimeMillis() + ".mp4";
        } else {
            fileName = "voice_" + System.currentTimeMillis() + ".mp3";
        }
        ServiceLogin.getInstance().sendFileMsg(mApplication.getUserInfo().getDeviceid(), mMediaUtils.getTargetFilePath(), fileName, new IMMsgListener() {
            @Override
            public void onSuccess(TIMMessage msg) {
                dismissProgressDialog();
                if (media_type == 0) {
                    toastShow("发送视频成功");
                } else {
                    toastShow("发送音频成功");
                }

            }

            @Override
            public void onError(int code, String desc) {
                dismissProgressDialog();
                if (media_type == 0) {
                    toastShow("发送视频失败");
                } else {
                    toastShow("发送音频失败");
                }
            }
        });
    }


    /**
     * 是否发送音视频dialog
     */
    private void initSendDialog() {
        sendDialog = DialogUtils.createTipDialog(mContext, "是否发送录制视频", "发送", "取消", new ActionClickListener() {
            @Override
            public void clickLeft() {
                //发送
                sendFile2Tx();
                sendDialog.dismiss();
            }

            @Override
            public void clickRight() {
                //取消
                mMediaUtils.deleteTargetFile(); //删除目标文件
                sendDialog.dismiss();
            }
        });
    }

    /**
     * 发送语音提示
     *
     * @param txt
     */
    private void sendFileTip(String txt) {
        ((TextView) sendDialog.findViewById(R.id.tv_tip)).setText(txt); //提示语
        sendDialog.show();
    }

    /**
     * 后退
     */
    private void backAction() {
        if (media_type == 1) {
            media_type = 0;
            mMediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);
            mMediaUtils.setSurfaceView(mSurfaceView); //视频录制设置
            mRlEmpty.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.VISIBLE);
            mIvCameraTransferClick.setVisibility(View.VISIBLE);
            mIvReturnVoiceClick.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    @OnClick({R.id.iv_return_voice_click, R.id.iv_camera_transfer_click, R.id.rl_back_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return_voice_click: //录音
                media_type = 1;
                mMediaUtils.setRecorderType(MediaUtils.MEDIA_AUDIO); //语音录制设置
                mRlEmpty.setVisibility(View.VISIBLE);
                mSurfaceView.setVisibility(View.GONE);
                mIvCameraTransferClick.setVisibility(View.GONE);
                mIvReturnVoiceClick.setVisibility(View.GONE);
                break;
            case R.id.iv_camera_transfer_click: //切换前后相机摄像头
                mMediaUtils.switchCamera();
                break;
            case R.id.rl_back_return: //返回
                backAction();
                break;
        }
    }
}
