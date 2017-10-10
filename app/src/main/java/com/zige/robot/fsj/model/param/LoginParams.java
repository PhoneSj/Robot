package com.zige.robot.fsj.model.param;

/**
 * Created by PhoneSj on 2017/10/9.
 * 用户登录请求参数
 */

public class LoginParams {

    private String account;
    private String password;
    private String deviceId;
    private String pushKey;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }
}
