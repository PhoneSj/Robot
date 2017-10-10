package com.zige.robot.fsj.model.sp;

import com.hyphenate.chat.EMMessage;
import com.zige.robot.fsj.model.bean.CallInfoBean;

/**
 * Created by Administrator on 2017/9/9.
 */

public interface SPHelper {

    /**
     * 获取用户名
     *
     * @return
     */
    int fetchUsername();

    /**
     * 获取该用户在机器人上的称呼
     *
     * @return
     */
    String fetchNickname();

    /**
     * 获取该用户注册的手机号
     *
     * @return
     */
    String fetchPhone();

    /**
     * 保存环信账号
     */
    void saveHxAcount(String hxAcount);

    /**
     * 获取环信账号
     *
     * @return
     */
    String fetchHXAcount();

    /**
     * 保存环信密码
     */
    void saveHxPassword(String hxPwd);

    /**
     * 获取环信密码
     *
     * @return
     */
    String fetchHxPassword();

    /**
     * 保存远程机器人的环信账号
     */
    void saveHxRemoteAcount(String romoteAcount);

    /**
     * 获取远程机器人的环信账号
     *
     * @return
     */
    String fetchHxRemoteAcount();

    /**
     * 保存当前连接的机器人id
     */
    void saveConnectedRobot(String cntRobot);

    /**
     * 获取当前连接的机器人id
     *
     * @return
     */
    String fetchConnectedRobot();

    /**
     * 保存本次视频通话时长
     */
    void saveConsumeTime(int minute);

    /**
     * 获取本次视频通话时长
     *
     * @return
     */
    int fetchConsumeTime();

    void saveCallMessage(CallInfoBean bean);

    CallInfoBean getCallMessage();
}
