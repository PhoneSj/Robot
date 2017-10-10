package com.zige.robot.bean;

/**
 * Created by Administrator on 2017/5/24.
 */

public class VoiceRecordBean {
    private String path;
    private long time;
    private boolean isSendSuccess;//是否发送成功
    private boolean isPlay;//是否正在播放

    public VoiceRecordBean(String path, long time, boolean isSendSuccess, boolean isPlay) {
        this.path = path;
        this.time = time;
        this.isSendSuccess = isSendSuccess;
        this.isPlay = isPlay;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }
}
