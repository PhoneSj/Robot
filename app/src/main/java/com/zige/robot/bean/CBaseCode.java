package com.zige.robot.bean;

import java.io.Serializable;

/**
 * Created by zhanghuan on 2016/12/26.
 */

public class CBaseCode implements Serializable{
    public String code;
    public String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
