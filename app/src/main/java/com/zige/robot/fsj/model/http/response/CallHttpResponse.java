package com.zige.robot.fsj.model.http.response;

/**
 * Created by Administrator on 2017/9/15.
 */

public class CallHttpResponse<T> {

    public static final String RESULT_SUCCESS = "success";

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
