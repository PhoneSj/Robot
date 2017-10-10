package com.zige.robot.fsj.model.bean;

/**
 * Created by PhoneSj on 2017/9/21.
 */

public class CallInfoBean {

    private String callerId;
    private String calledId;
    private int consumeTime;
    private String id;

    public CallInfoBean() {
    }

    public CallInfoBean(String callerId, String calledId, int consumeTime, String id) {
        this.callerId = callerId;
        this.calledId = calledId;
        this.consumeTime = consumeTime;
        this.id = id;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getCalledId() {
        return calledId;
    }

    public void setCalledId(String calledId) {
        this.calledId = calledId;
    }

    public int getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(int consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
