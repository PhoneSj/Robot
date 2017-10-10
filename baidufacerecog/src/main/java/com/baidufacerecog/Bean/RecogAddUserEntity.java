package com.baidufacerecog.Bean;

import android.util.Log;

import java.util.ArrayList;

/**
 * 说明：添加用户结果
 * 创建者： kim
 * 创建日期：2017/7/24:19:02
 */


public class RecogAddUserEntity {
    public long log_id;

    public String error_msg;//错误消息
    private long error_code; //错误码
    public String personId;  //用户

    //是否添加用户失败   true失败
    public boolean isError() {
        Log.d("tagutil", "error_msg = " + error_msg + "log_id=" + log_id);
        return !(error_code == 0);
    }
}
