package com.zige.robot.fsj.model.bean;

/**
 * Created by Administrator on 2017/9/13.
 */

public class CallRemainTimeBean {

    /**
     * robotId : 123456
     * remain : 50<剩余时长>
     */

    private String robotId;
    private String remain;

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }
}
