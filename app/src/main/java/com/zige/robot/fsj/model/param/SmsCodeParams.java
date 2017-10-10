package com.zige.robot.fsj.model.param;

/**
 * Created by PhoneSj on 2017/10/9.
 * 发送短信验证码的请求参数
 */

public class SmsCodeParams {

    private String phone;
    private String type;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
