package com.zige.robot.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;

/**
 * 添加馒头
 */
public class OpenRobotActvity extends BaseActivity {


    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_open_robot_actvity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleName("添加馒头");
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_scan_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPermissionChecker.isLackPermissions(new String[]{Manifest.permission.CAMERA})){
                    mPermissionChecker.requestPermissions();
                }else {
                    startActivity(CaptureActivity.class);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPermissionChecker.hasAllPermissionsGranted(grantResults)){
            startActivity(CaptureActivity.class);
        }else {
            mPermissionChecker.setMessage("没有开启相机权限,请在设置中开启权限后再使用");
            mPermissionChecker.showDialog();
        }
    }

}
