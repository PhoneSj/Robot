package com.zige.robot.http.rxhttp.query;

/**
 * Created by ldw on 2017/8/9.
 */

public class QueryUserBean {
    private String username;
    private String deviceId;
    private String robotDeviceId;
    private Long leaveMessageId;


    public QueryUserBean(String username, String deviceId, String robotDeviceId) {
        this.username = username;
        this.deviceId = deviceId;
        this.robotDeviceId = robotDeviceId;
    }


    public QueryUserBean(String username, String deviceId, String robotDeviceId, Long leaveMessageId) {
        this.username = username;
        this.deviceId = deviceId;
        this.robotDeviceId = robotDeviceId;
        this.leaveMessageId = leaveMessageId;
    }

}
