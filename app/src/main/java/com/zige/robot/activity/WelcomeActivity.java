package com.zige.robot.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zige.robot.R;
import com.zige.robot.base.BaseActivity;
import com.zige.robot.utils.permission.PermissionChecker;

/**
 * Created by Administrator on 2017/5/5.
 */

public class WelcomeActivity extends BaseActivity {

    PermissionChecker mPermissionChecker;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionChecker = new PermissionChecker(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionChecker.isLackPermissions(new String[]{Manifest.permission.READ_PHONE_STATE})) {
            mPermissionChecker.requestPermissions();
        } else {
            start2Login();

        }
    }

    private void start2Login() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class);
                finish();
            }
        }, 1000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionChecker.hasAllPermissionsGranted(grantResults)) {
            start2Login();
        } else {
            mPermissionChecker.showDialog();
        }
    }
}
