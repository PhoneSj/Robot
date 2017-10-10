package com.zige.robot.fsj.util;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zige.robot.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/9/15.
 */

public class CallUtil {

    /**
     * 环信登录
     *
     * @param callBack
     */
    public static void login(EMCallBack callBack) {
        String hxAcount = SharedPreferencesUtils.getHXAcount();
        String hxPassword = SharedPreferencesUtils.getHXPassword();
        EMClient.getInstance().login(hxAcount, hxPassword, callBack);
    }

    /**
     * 环信登录
     *
     * @param callBack
     */
    public static void login(String phone, String pwd, EMCallBack callBack) {
        EMClient.getInstance().login(phone, pwd, callBack);
    }

    /**
     * 环信退出登录
     *
     * @param callBack
     */
    public static void logout(EMCallBack callBack) {
        EMClient.getInstance().logout(true, callBack);
    }

    /**
     * 环信账号注册
     *
     * @param hxAcount
     * @param hxPassword
     */
    public static void register(final String hxAcount, final String hxPassword) {
        if (hxAcount.isEmpty() || hxPassword.isEmpty()) {
            LogUtil.showW("环信注册账号、密码不能为空");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建一个账户
                    EMClient.getInstance().createAccount(hxAcount, hxPassword);
                } catch (HyphenateException e) {
                    String str = "sign up error " + e.getErrorCode() + "; " + e.getMessage();
                    LogUtil.showW(str);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
