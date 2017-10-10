package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.callsdk.ILVCallListener;
import com.tencent.callsdk.ILVCallManager;
import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.PlayerUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidingwei on 2017/5/25 0025.
 */
public class ComingCallDialogActivity extends BaseActivity implements ILVCallListener {

    @Override
    protected int initPageLayoutID() {
        return R.layout.dialog_coming_call;
    }

    int CallId;
    int CallType;
    String HostId;
    PlayerUtil mPlayerUtil;
    //不重复点击
    boolean bAccept = false; //拒绝
    boolean bRefuse = false; //接收
    /**
     * 播放铃声
     */
    private void startComingMusic(){
        mPlayerUtil = new PlayerUtil(mContext);
        mPlayerUtil.playAssetsFile("incoming.ogg", true);
    }

    /**
     * 停止播放铃声
     */
    private void stopComingMusic(){
        if(mPlayerUtil!=null){
            mPlayerUtil.stop();
        }
    }

    TextView tv_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);
        ILVCallManager.getInstance().addCallListener(this);
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        CallId = getIntent().getIntExtra("CallId",0);
        CallType = getIntent().getIntExtra("CallType",0);
        HostId = getIntent().getStringExtra("HostId");
        tv_userName.setText("馒头邀请你视频通话");
    }


    @Override
    protected void onResume() {
        super.onResume();
        startComingMusic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopComingMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ILVCallManager.getInstance().removeCallListener(this);
    }

    private void acceptCall(int callId, String hostId, int callType) {
        Intent intent = new Intent();
        intent.setClass(this, VideoCallActivity.class);
//        intent.setClass(this, VideoCall2Activity.class);
        intent.putExtra("HostId", hostId);
        intent.putExtra("CallId", callId);
        intent.putExtra("CallType", callType);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.btn_cancel, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                backAction();
                break;
            case R.id.btn_ok:
                if(!bAccept){
                    bAccept= true;
                    acceptCall(CallId, HostId, CallType);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backAction();
        super.onBackPressed();
    }

    private void backAction(){
        if(!bRefuse){
            bRefuse = true;
            ILVCallManager.getInstance().rejectCall(CallId);
        }

    }

    @Override
    public void onCallEstablish(int callId) {
        Log.d("onCallEstablish4444", "" + callId);
    }

    @Override
    public void onCallEnd(int callId, int endResult, String endInfo) {
        //发送方取消了视频通话
        Log.d("onCallEnd4444", "" + callId + endInfo);
        finish();
    }

    @Override
    public void onException(int iExceptionId, int errCode, String errMsg) {
        Log.d("onException4444", "" + iExceptionId + errMsg);
        finish();
    }
}