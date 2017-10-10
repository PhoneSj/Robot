package com.zige.robot.interf;

/**
 * Created by lidngwei on 2017/7/31.
 */

public interface SuccessVoiceListener {
     /**
      * 录制回调监听
      * @param path 语音文件本地路径
      * @param timeLength  录音时间长度  单位：s 
      */
     void getVoiceMsgPath(String path, long timeLength);
}
