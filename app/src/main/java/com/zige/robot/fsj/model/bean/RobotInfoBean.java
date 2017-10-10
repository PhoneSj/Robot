package com.zige.robot.fsj.model.bean;

/**
 * Created by Administrator on 2017/9/12.
 * 机器人信息
 */

public class RobotInfoBean {


    /**
     * id : 0
     * icon :
     * robotDeviceId :
     * deviceName :
     * nickname :
     */

    private int id;
    private String icon;
    private String robotDeviceId;
    private String deviceName;
    private String nickname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRobotDeviceId() {
        return robotDeviceId;
    }

    public void setRobotDeviceId(String robotDeviceId) {
        this.robotDeviceId = robotDeviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
