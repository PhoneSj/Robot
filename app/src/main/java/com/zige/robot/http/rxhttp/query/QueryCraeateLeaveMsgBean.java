package com.zige.robot.http.rxhttp.query;

/**
 * Created by ldw on 2017/8/9.
 * 新建一个留言
 */

public class QueryCraeateLeaveMsgBean {
    private String username;
    private String deviceId;
    private String robotDeviceId;
    private Long homeworkId;
    private Long  duration;
    private Integer  messageType;
    private String messageContent;

    public QueryCraeateLeaveMsgBean(String username, String deviceId, String robotDeviceId, Long homeworkId, Long duration, Integer messageType, String messageContent) {
        this.username = username;
        this.deviceId = deviceId;
        this.robotDeviceId = robotDeviceId;
        this.homeworkId = homeworkId;
        this.duration = duration;
        this.messageType = messageType;
        this.messageContent = messageContent;
    }




}
