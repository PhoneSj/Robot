package com.zige.robot.fsj.model.bean;

/**
 * Created by Administrator on 2017/9/9.
 */

public class PayResultBean {

    public static final int STATE_PAYING = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAILE = 2;

    /**
     * orderNo : 5a381bd734404b96815d489bb2cd8f62
     * robotId : 320d139be4c0ab81
     * fee : 5
     * chargeTime : 1000
     * status : 1
     * createTime : 1504782820000
     */

    private String orderNo;
    private String robotId;
    private int fee;
    private int chargeTime;
    private int status;
    private long createTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
