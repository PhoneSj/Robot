package com.zige.robot.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zige.robot.R;
import com.zige.robot.activity.CaptureActivity;
import com.zige.robot.base.BaseLazyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by  on 2016/12/31.
 * 信息填写
 */

public class RobotInfoFragment extends BaseLazyFragment {

    @BindView(R.id.btn_scan_qr)
    Button mBtnScanQr;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_scan_qr)
    public void onViewClicked() {
        //扫描二维码
        if(mPermissionChecker.isLackPermissions(new String[]{Manifest.permission.CAMERA})){
            mPermissionChecker.requestPermissions();
        }else {
            startActivity(new Intent(mContext, CaptureActivity.class));
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPermissionChecker.hasAllPermissionsGranted(grantResults)){
            startActivity(new Intent(mContext, CaptureActivity.class));
        }else {
            mPermissionChecker.setMessage("没有开启相机权限,请在设置中开启权限后再使用");
            mPermissionChecker.showDialog();
        }
    }

}