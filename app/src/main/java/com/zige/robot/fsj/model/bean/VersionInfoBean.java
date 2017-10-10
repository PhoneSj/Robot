package com.zige.robot.fsj.model.bean;

/**
 * Created by Administrator on 2017/9/11.
 */

public class VersionInfoBean {

    //版本检查android平台
    public static final String PLATFORM = "android";

    private String versionCode;

    private String versionName;

    private String size;

    private String des;

    public static String getPLATFORM() {
        return PLATFORM;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
