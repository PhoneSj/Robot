package com.zige.robot.bean;

import java.io.Serializable;

/**
 * Created by zhanghuan on 2016/12/26.
 */

public class UserInfo implements Serializable {
    private int userId;
    private String nickname;
    private String userrole; //用户角色,  family管理员
    private String usericon; // 头像
    private String deviceid; // 机器人设备id,
    private String admin; // 管理员名称,
    private String createtime; //注册时间,
    private String sig; //用户sig,
    private String child_nickname; //小朋友昵称,
    private String child_sex; //绑定时设置的小朋友性别,
    private String child_yearold; //小朋友年龄
    private String hxPwd;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getChild_nickname() {
        return child_nickname;
    }

    public void setChild_nickname(String child_nickname) {
        this.child_nickname = child_nickname;
    }

    public String getChild_sex() {
        return child_sex;
    }

    public void setChild_sex(String child_sex) {
        this.child_sex = child_sex;
    }

    public String getChild_yearold() {
        return child_yearold;
    }

    public void setChild_yearold(String child_yearold) {
        this.child_yearold = child_yearold;
    }

    public String getHxPwd() {
        return hxPwd;
    }

    public void setHxPwd(String hxPwd) {
        this.hxPwd = hxPwd;
    }
}
