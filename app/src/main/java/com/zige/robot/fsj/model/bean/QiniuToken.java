package com.zige.robot.fsj.model.bean;

import com.zige.robot.fsj.util.LogUtil;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class QiniuToken {

    public static final String SUCCESS = "成功";

    /**
     * code : 0000
     * message : 成功
     * qiniuToken : 1l08p2WVHtrziPOkGvN1Eee1e4I9Dt52fPbx7M1m:zBEyyKvUOTBBgJLVT8YPdlyo1K4=:eyJzY29wZSI6InppZ2UtcHVibGljLXN0YXRpYzpkZnNrZ2tza2Rma3NrZCIsImRlYWRsaW5lIjoxNTA2NDAxNjYyfQ==
     */

    private String code;
    private String message;
    private String qiniuToken;

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

    public String getQiniuToken() {
        return qiniuToken;
    }

    public void setQiniuToken(String qiniuToken) {
        this.qiniuToken = qiniuToken;
    }

    public boolean isSuccess() {
        if (message.equalsIgnoreCase("成功")) {
            LogUtil.showI(getClass().getSimpleName() + "网络返回正确");
            return true;
        } else {
            LogUtil.showI(getClass().getSimpleName() + "网络返回错误");
            return false;
        }
    }
}
