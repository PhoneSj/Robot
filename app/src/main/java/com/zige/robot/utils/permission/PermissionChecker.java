package com.zige.robot.utils.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by wanbo on 2017/1/12.
 */

public class PermissionChecker {

    public static final int PERMISSION_REQUEST_CODE = 0;
    private String[] permissions;
    private Activity activity;
    private PermissionDialog dialog;

    public PermissionChecker(Activity activity) {
        this.activity = activity;
        this.dialog = new PermissionDialog(activity);
    }

    /**
     * 是否缺少权限
     */
    public boolean isLackPermissions(String[] permissions) {
        if(Build.VERSION.SDK_INT <23){
            return false;
        }
        this.permissions = permissions;
        Checker checker = new Checker(activity);
        return checker.lacksPermissions(permissions);
    }

    public void requestPermissions() {
        if(Build.VERSION.SDK_INT <23){
            return;
        }
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
    }

    public void setTitle(String title){
        dialog.setTitle(title);
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void showDialog(){
        dialog.init();
        dialog.show();
    }

}
