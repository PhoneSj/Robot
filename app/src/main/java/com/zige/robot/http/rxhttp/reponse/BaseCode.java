package com.zige.robot.http.rxhttp.reponse;

import com.iflytek.msc.MSC;

/**
 * Created by Administrator on 2017/8/1.
 */

public class BaseCode {
    public static final int SUCCESS_CODE = 200;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int code;
    private String message;

}
