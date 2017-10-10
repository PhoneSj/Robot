package com.zige.robot.fsj.model.bean;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class AlbumUploadBean {

    public enum UploadState {
        WAITING, UPLOADING, FINISH, FAIL
    }

    private String path;
    private UploadState state;
    private double percent;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UploadState getState() {
        return state;
    }

    public void setState(UploadState state) {
        this.state = state;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
