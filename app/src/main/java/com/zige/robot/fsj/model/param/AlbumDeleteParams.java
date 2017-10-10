package com.zige.robot.fsj.model.param;

/**
 * Created by PhoneSj on 2017/9/27.
 * 网络请求删除相册的参数
 */

public class AlbumDeleteParams {

    private String username;
    private String robotDeviceId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRobotDeviceId() {
        return robotDeviceId;
    }

    public void setRobotDeviceId(String robotDeviceId) {
        this.robotDeviceId = robotDeviceId;
    }
}
