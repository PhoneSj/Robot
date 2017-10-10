package com.zige.robot.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2017/5/19.
 */

@Entity
public class RobotRemind {
    @Id(autoincrement = true)
    private long id;
    private long change_time;
    private String time;
    private String week;//用逗号隔开
    private String member;
    private String context;
    private int isOpen;

    @Generated(hash = 345938244)
    public RobotRemind(long id, long change_time, String time, String week, String member,
            String context, int isOpen) {
        this.id = id;
        this.change_time = change_time;
        this.time = time;
        this.week = week;
        this.member = member;
        this.context = context;
        this.isOpen = isOpen;
    }

    @Generated(hash = 574105791)
    public RobotRemind() {
    }

    public long getChange_time() {
        return change_time;
    }

    public void setChange_time(long change_time) {
        this.change_time = change_time;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMember() {
        return this.member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public String toString() {
        return "{\"id\": " +id+",\"time\":\""+time+"\",\"week\":\""+week+"\",\"member\":\""+member+"\",\"context\":\""+context+"\",\"isOpen\":"+isOpen+"}";
    }
}
