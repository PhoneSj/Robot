package com.zige.robot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.zige.robot.App;
import com.zige.robot.activity.DialogBindActivity;
import com.zige.robot.activity.DialogForceOffLineActivity;
import com.zige.robot.activity.InputInfoActivity;
import com.zige.robot.fsj.util.CallUtil;

import java.util.List;

/**
 * @author Feel on 2017/3/29 09:26
 */

public class HXServiceLogin extends Service {

    private static final String TAG = "HXServiceLogin";

    public static final String HX_LOGIN_SUC = "im_login_suc"; //Im登录成功
    public static final String HX_LOGIN_FAIL = "im_login_fail"; //im登录失败
    public static boolean isLogin = false;
    private static HXServiceLogin instance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        instance = this;

        loginToHx();
    }

    //实现ConnectionListener接口
    private EMConnectionListener mEMConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {
            
        }

        @Override
        public void onDisconnected(int error) {
            isLogin = false;
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登录
                Log.d(TAG, "onDisconnected: " + "其他账号登录");
                Intent intent = new Intent();
                intent.setClass(HXServiceLogin.this, DialogForceOffLineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    };

    public void loginToHx() {
        CallUtil.login(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(App.TAG, "sign in hx success");
                EMClient.getInstance().chatManager().loadAllConversations();
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                isLogin = true;
                sendBroadcast(new Intent(HX_LOGIN_SUC));
                setListener();
            }

            @Override
            public void onError(int i, String s) {
                sendBroadcast(new Intent(HX_LOGIN_FAIL));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void setListener() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(mEMConnectionListener);
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.d(TAG, "onMessageReceived: 普通消息");
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            for (int i = 0; i < messages.size(); i++) {
                int type = messages.get(i).getIntAttribute("type", 0);
                String action = messages.get(i).getStringAttribute("action", "");
                Intent intent = new Intent("video_call_control");
                intent.setPackage("com.zige.dube");
                Log.d(TAG, "onCmdMessageReceived: " + type + "   action:" + action);
                if (TextUtils.isEmpty(action))
                    return;
                //                intent.putExtra("type", type);
                //                intent.putExtra("action", action);
                if (type == 1) {//控制运动

                } else if (type == 2) {//模拟跟机器人对话

                } else if (type == 3) {//让机器人说的内容

                } else if (type == 4) {//通知刷新
                    if (action.equals("guestMsg")) {
                        try {
                            String nickname = messages.get(i).getStringAttribute("nickname");
                            String childNickname = messages.get(i).getStringAttribute("childNickname");
                            String childSex = messages.get(i).getStringAttribute("childSex");
                            String childYearold = messages.get(i).getStringAttribute("childYearold");
                            String robotDeviceId = messages.get(i).getStringAttribute("robotDeviceId");
                            String userId = messages.get(i).getStringAttribute("userId");
                            String deviceId = messages.get(i).getStringAttribute("deviceId");
                            Intent intentNew = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setClass(HXServiceLogin.this, DialogBindActivity.class);
                            intent.putExtra("nickname", nickname);
                            intent.putExtra("childNickname", childNickname);
                            intent.putExtra("childSex", childSex);
                            intent.putExtra("childYearold", childYearold);
                            intent.putExtra("robotDeviceId", robotDeviceId);
                            intent.putExtra("deviceId", deviceId);
                            intent.putExtra("userId", userId);
                            startActivity(intentNew);
                        }catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    } else if (action.equals("bindOk")) {
                        //主人同意绑定
                        try {
                            String account = messages.get(i).getStringAttribute("account");
                            Log.i("bindOk  ", account);
                            sendBroadcast(new Intent(InputInfoActivity.AGREE_BIND).putExtra("robotDeviceId", account));
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                       
                    } else if (action.equals("bindNo")) {
                        //主人同意绑定
                        try {
                            sendBroadcast(new Intent(InputInfoActivity.AGREE_BIND));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                sendBroadcast(intent);
            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    public static HXServiceLogin getInstance() {
        return instance;
    }
}
