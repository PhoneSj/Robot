package com.zige.robot.fsj.model.http.response;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ZigeHttpResponse<T> {

    public static final String RESULT_SUCCESS = "成功";

    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
