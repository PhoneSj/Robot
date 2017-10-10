package com.zige.robot.http.rxhttp.query;

/**
 * Created by ldw on 2017/8/5.
 * 创建修改科目bean
 */

public class QuerySubjectBean {

    private String username; //手机号
    private String deviceId; //设备号
    private String robotDeviceId; //机器人设备号
    private Long subjectId; //科目id
    private String subjectName; //科目名

    public QuerySubjectBean(String username, String deviceId, String robotDeviceId, Long subjectId, String subjectName) {
        this.username = username;
        this.deviceId = deviceId;
        this.robotDeviceId = robotDeviceId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }
}
