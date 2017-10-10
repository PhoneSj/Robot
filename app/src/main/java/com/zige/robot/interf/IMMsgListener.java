package com.zige.robot.interf;

import com.tencent.TIMMessage;

/**
 * @author Feel on 2017/4/5 16:45
 */

public interface IMMsgListener {

    void onSuccess(TIMMessage msg);

    void onError(int code, String desc);

}
