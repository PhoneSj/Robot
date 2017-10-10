package com.zige.robot.http.rxhttp.query;

/**
 * Created by Administrator on 2017/8/1.
 * 科目列表请求json
 */

public class QuerySubjectListBean {
    private int page;
    private int size;
    private String username;
    private String deviceId;
    private String robotDeviceId;

    public QuerySubjectListBean(int page, int size, String username, String deviceId, String robotDeviceId) {
        this.page = page;
        this.size = size;
        this.username = username;
        this.deviceId = deviceId;
        this.robotDeviceId = robotDeviceId;
    }
}
