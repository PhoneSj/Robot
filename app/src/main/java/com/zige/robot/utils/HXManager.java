package com.zige.robot.utils;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.Map;

/**
 * Created by 11475 on 2017/9/29.
 */

public class HXManager {
    public static void sendTxt(String toUsername, int type, String message, EMCallBack callBack) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        String action = message;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.setTo(toUsername);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setAttribute("type", type);
        cmdMsg.setAttribute("action", message);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);

        cmdMsg.setMessageStatusCallback(callBack);
    }
    public static void sendTxt(String toUsername, int type, String message, String account, EMCallBack callBack) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        String action = message;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.setTo(toUsername);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setAttribute("type", type);
        cmdMsg.setAttribute("action", message);
        cmdMsg.setAttribute("account", account);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);

        cmdMsg.setMessageStatusCallback(callBack);
    }

    public static void sendToHost(String toUsername, int type, String msg, Map<String, String> map, EMCallBack callBack) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        String action = msg;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.setTo(toUsername);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setAttribute("type", type);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            cmdMsg.setAttribute(key,value);
        }
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);

        cmdMsg.setMessageStatusCallback(callBack);
    }
}
