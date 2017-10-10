package com.zige.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by ldw on 2017/8/21.
 * 二维码扫描
 */

public class CaptureActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final String TAG = "CaptureActivity";
    @BindView(R.id.zxingview)
    ZXingView mZxingview;


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_capture;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleName("扫描二维码");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mZxingview.setDelegate(this);//设置扫描二维码的代理
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZxingview.startCamera();//打开相机预览
        mZxingview.showScanRect(); //显示扫描框
        mZxingview.startSpot();//必须开启识别

    }

    @Override
    protected void onStop() {
        super.onStop();
        mZxingview.stopCamera();//关闭预览,释放摄像头
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mZxingview.onDestroy();//销毁控件

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        startActivity(new Intent(mContext, InputInfoActivity.class).putExtra("QrCode", result));
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }


    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
