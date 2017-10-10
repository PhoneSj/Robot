package com.zige.robot.fsj.model.param;

/**
 * Created by PhoneSj on 2017/10/9.
 * 重置密码的网络请求参数
 */

public class ResetPwdParams {

    private String phone;
    private String password;
    private String verifyCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
