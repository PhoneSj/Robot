package com.zige.robot.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/18.
 */

public class StudyTimeBean implements Serializable{
    public String subject;
    public String startTime;
    public String endTime;
    public int isOpen;

    public StudyTimeBean(String subject, String startTime, String endTime, int isOpen) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpen = isOpen;
    }



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

}
