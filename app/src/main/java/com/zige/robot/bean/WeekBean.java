package com.zige.robot.bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class WeekBean {
    String code;
    String weekName;

    public WeekBean(String code, String weekName) {
        this.code = code;
        this.weekName = weekName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }
}
