package com.zige.robot.fsj.model.http.response;

import com.zige.robot.fsj.util.LogUtil;

/**
 * Created by PhoneSj on 2017/9/25.
 */

public class AlbumHttpResponse<T> {

    public static final String RESULT_SUCCESS = "SUCCESS";

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

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        if (code.equalsIgnoreCase("200") && message.equalsIgnoreCase(RESULT_SUCCESS)) {
            LogUtil.showI(getClass().getSimpleName() + "网络返回成功");
            return true;
        } else {
            LogUtil.showI(getClass().getSimpleName() + "网络返回失败");
            return false;
        }
    }
}
