package com.zige.robot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMFileElem;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.calldemo.event.MessageEvent;
import com.tencent.callsdk.ILVCallConfig;
import com.tencent.callsdk.ILVCallListener;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVCallNotification;
import com.tencent.callsdk.ILVCallNotificationListener;
import com.tencent.callsdk.ILVIncomingListener;
import com.tencent.callsdk.ILVIncomingNotification;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.openqq.IMSdkInt;
import com.zige.robot.App;
import com.zige.robot.activity.ComingCallDialogActivity;
import com.zige.robot.activity.DialogBindActivity;
import com.zige.robot.activity.DialogForceOffLineActivity;
import com.zige.robot.activity.InputInfoActivity;
import com.zige.robot.activity.VideoCallActivity;
import com.zige.robot.interf.IMMsgListener;
import com.zige.robot.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Feel on 2017/3/29 09:26
 */

public class ServiceLogin extends Service implements Observer, ILVCallNotificationListener, ILVIncomingListener, ILVCallListener {

    private static final String TAG = "ServiceLogin2";
    public static boolean isCreate = false;
    public static boolean isLogin = false;
    private static ServiceLogin instance;

    public static final int APP_ID = 1400021969;
    public static final int ACCOUNT_TYPE = 9540;

    public static final String IM_LOGIN_SUC  =  "im_login_suc"; //Im登录成功
    public static final String IM_LOGIN_FAIL = "im_login_fail"; //im登录失败
    public static final String FRIEND_UPDATE = "friend_update"; //im登录失败
//    public static final String FORCE_OFF_LINE = "force_off_line"; //强制踢出

    public ServiceLogin() {
    }

    public static ServiceLogin getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        isCreate = true;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, " login---onDestroy---");
        isCreate = false;
        isLogin = false;
//        ILVCallManager.getInstance().removeIncomingListener(this);
//        ILVCallManager.getInstance().removeCallListener(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            String account = intent.getStringExtra("account"); //云信账号
            String sig = intent.getStringExtra("sig");
            Log.v(TAG, "account----" + account+"    sig----" + sig);
            if (!TextUtils.isEmpty(account)) {
                loginToIM(account, sig);
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }



    /**
     * 登录到云信
     * @param account
     */
    public void loginToIM(String account, String sig) {
      ILiveSDK.getInstance().initSdk(getApplicationContext(), APP_ID, ACCOUNT_TYPE);
      ILVCallManager.getInstance().init(new ILVCallConfig()
                   .setNotificationListener(this)
                   .setAutoBusy(true));
        ILiveLoginManager.getInstance().setUserStatusListener(new ILiveLoginManager.TILVBStatusListener() {
            @Override
            public void onForceOffline(int error, String message) {
                Log.e(TAG, "onForceOffline: " + error +"  "+ message  );
                Intent intent = new Intent();
                intent.setClass(ServiceLogin.this, DialogForceOffLineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        LoginAccount(account, sig);
    }

    public void LoginAccount(String account, String sig){
        ILiveLoginManager.getInstance().iLiveLogin(account, sig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.v(TAG, "----------login succ: " + IMSdkInt.get().getTinyId());
                isLogin = true; //登录成功
                MessageEvent.getInstance(); //初始化消息监听
                onMessageRec();  //设置消息监听
                sendBroadcast(new Intent(IM_LOGIN_SUC));

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.v(TAG, "--------login failed. code: " + errCode + " errmsg: " + errMsg);
                isLogin = false;
                sendBroadcast(new Intent(IM_LOGIN_FAIL));
            }
        });
    }


    /**
     * 消息接收
     */
    private void onMessageRec() {
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
    }


    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            TIMMessage msg = (TIMMessage) data;
            if (msg == null) return;
            Log.v(TAG, "是否已读: " + msg.isRead());
            TIMElem elem = msg.getElement(0);
            //获取当前元素的类型
            TIMElemType elemType = elem.getType();
            if (elemType == TIMElemType.Text) {
                //处理文本消息
//                if (!msg.isRead()) {
                  handleCode(msg,elem);
                // 设置已读
                  msg.getConversation().setReadMessage(msg);
//                }
            } else if (elemType == TIMElemType.Image) {
                //处理图片消息
                handleImage(elem);
            } else if (elemType == TIMElemType.Sound) {
                // 处理语音消息
                handleVoice(elem);
            } else if (elemType == TIMElemType.Video) {
                // 处理视频消息
                handleVideo(elem);
            } else if (elemType == TIMElemType.Custom) {
                // 处理自定义消息
                handleCustom(elem);
            } else if (elemType == TIMElemType.Location) {
                // 处理位置消息
                handleLocation(elem);
            } else if (elemType == TIMElemType.File) {
                // 处理文件消息
                handleFile(elem);
            } else if (elemType == TIMElemType.SNSTips) {
//                handleSNSTips(elem);
            }
        }
    }

    /**
     * 发送文本消息
     *
     * @param peerid
     * @param txt
     */
    public void sendTextMsg(String peerid, String txt) {
        Log.v(TAG, "sendTextMsg----是否登录：" + isLogin + ", peerid: " + peerid + ", txt" + txt);
        if (txt == null || txt.length() < 1) return;
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id
        //构造一条消息
        TIMMessage msg = new TIMMessage();

        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(txt);

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        //发送消息(离线，在线消息)
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e(TAG, "SendMsg ok");
            }
        });
    }



    /**
     * 发送在线文本消息
     *
     * @param peerid
     * @param txt
     */
    public void sendOnLineTextMsg(String peerid, String txt, final IMMsgListener listener) {
        if(TextUtils.isEmpty(peerid)){return;}
        if (txt == null || txt.length() < 1) return;
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(txt);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        //发送在线消息
        conversation.sendOnlineMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                if(code == 6200){
                    ToastUtils.showToast("网络不可用");
                }
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
                listener.onError(code, desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e(TAG, "SendMsg ok");
                listener.onSuccess(msg);
            }
        });
    }

    /**
     * 发送在线离线文本消息
     *
     * @param peerid
     * @param txt
     */
    public void sendTextMsg(String peerid, String txt, final IMMsgListener listener) {
        if(TextUtils.isEmpty(peerid)){return;}
        if (txt == null || txt.length() < 1) return;
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(txt);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        //发送离线文本消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
                if(code == 6200){
                    ToastUtils.showToast("网络不可用");
                }
                listener.onError(code, desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e(TAG, "SendMsg ok");
                listener.onSuccess(msg);
            }
        });
    }

    /**
     * 发送图片消息
     *
     * @param peerid
     * @param path
     */
    public void sendImageMsg(String peerid, String path) {
        Log.v(TAG, "sendImageMsg----是否登录：" + isLogin + ", peerid: " + peerid + ", path" + path);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id

        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加图片
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }

        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e(TAG, "SendMsg ok");
            }
        });
    }


    /**
     * 发送语音消息
     *
     * @param peerid
     * @param path
     * @param duration
     */
    public void sendVoiceMsg(String peerid, String path, long duration) {
        Log.v(TAG, "sendVoiceMsg----是否登录：" + isLogin + ", peerid: " + peerid + ", path" + path);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id

        //构造一条消息
        TIMMessage msg = new TIMMessage();

        //添加语音
        TIMSoundElem elem = new TIMSoundElem();
        elem.setPath(path); //填写语音文件路径
        elem.setDuration(duration);  //填写语音时长

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e(TAG, "SendMsg ok");
            }
        });

    }


    /**
     * 发送文件消息
     *
     * @param peerid
     * @param filePath
     * @param fileName
     */
    public void sendFileMsg(String peerid, String filePath, String fileName, final IMMsgListener listener) {
        Log.v(TAG, "sendFileMsg----是否登录：" + isLogin + ", peerid: " + peerid
                + ", fileName: " + fileName + ", filePath: " + filePath);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id

        //构造一条消息
        TIMMessage msg = new TIMMessage();

        //添加文件内容
        TIMFileElem elem = new TIMFileElem();
        elem.setPath(filePath); //设置文件路径
        elem.setFileName(fileName); //设置消息展示用的文件名称

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
                listener.onError(code, desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                listener.onSuccess(msg);
                Log.e(TAG, "SendMsg ok");
            }
        });

    }

    /**
     * 发送自定义消息
     *
     * @param peerid
     * @param data
     * @param desc
     */
    public void sendCustomMsg(String peerid, byte[] data, String desc) {
        Log.v(TAG, "sendFileMsg----是否登录：" + isLogin + ", peerid: " + peerid
                + ", data: " + data.length + ", desc: " + desc);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peerid);                      //会话对方用户帐号//对方id

        //构造一条消息
        TIMMessage msg = new TIMMessage();

        //向TIMMessage中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data);      //自定义byte[]
        elem.setDesc("this is one custom message"); //自定义描述信息

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d(TAG, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                Log.d(TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                Log.e(TAG, "SendMsg ok");
            }
        });

    }

    /**
     * 发送视频
     */
    public void sendVideoMsg() {

    }

    /**
     * 发送机器人行走的指令
     * @param order
     */
    public void sendRobotMoveOrder(int order, final IMMsgListener listener) {
        String msgContent ="";
        switch (order) {
            case 0:
                msgContent = "front";
                break;
            case 1:
                msgContent = "back";
                break;
            case 2:
                msgContent = "turn_left";
                break;
            case 3:
                msgContent = "turn_right";
                break;
            case 4:
                msgContent = "move_stop";
                break;
        }
        String robotId = App.getInstance().getUserInfo().getDeviceid() ;
        sendOnLineTextMsg(robotId, msgContent, new IMMsgListener() {
            @Override
            public void onSuccess(TIMMessage msg) {
                listener.onSuccess(msg);
            }

            @Override
            public void onError(int code, String desc) {
                listener.onError(code, desc);
            }
        });

    }

    /**
     * 语音播报
     * @param speakContent
     * @param imMsgListener
     */
    public void sendRobotSpeakMsg(String speakContent, final IMMsgListener imMsgListener){
        sendOnLineTextMsg(App.getInstance().getUserInfo().getDeviceid() , "speak_" + speakContent, new IMMsgListener() {
            @Override
            public void onSuccess(TIMMessage msg) {
                imMsgListener.onSuccess(msg);
            }

            @Override
            public void onError(int code, String desc) {
                imMsgListener.onError(code, desc);
            }
        });
    }



    /**
     * 发起呼叫
     */
    public void makeCall(int callType, ArrayList<String> nums) {
        if (!isLogin) {
            Toast.makeText(getApplicationContext(), "网络断开了",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, VideoCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("HostId", ILiveLoginManager.getInstance().getMyUserId());
        intent.putExtra("CallId", 0);
        intent.putExtra("CallType", callType);
        intent.putStringArrayListExtra("CallNumbers", nums);
        startActivity(intent);
    }


    /**
     * 拒绝通话
     * @param mIncomingId
     * @return
     */
    public int rejectCall(int mIncomingId) {
        return ILVCallManager.getInstance().rejectCall(mIncomingId);
    }

    /**
     * 处理文本消息，指令
     *
     * @param elem
     */
    public void handleCode( TIMMessage timMessage,TIMElem elem) {
        //文本元素
        TIMTextElem e = (TIMTextElem) elem;
        String msg = e.getText();
        Log.v(TAG, "文本消息：" + msg);
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(msg.startsWith("guestMsg:")){
            JSONObject object = null;
            try {
                Log.i("JSONObject  ", msg.substring(9));
                object = new JSONObject(msg.substring(9));
                String nickname = object.optString("nickname");
                String childNickname = object.optString("childNickname");
                String childSex = object.optString("childSex");
                String childYearold = object.optString("childYearold");
                String robotDeviceId = object.optString("robotDeviceId");
                String userId = object.optString("userId");
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(ServiceLogin.this, DialogBindActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("childNickname", childNickname);
                intent.putExtra("childSex", childSex);
                intent.putExtra("childYearold", childYearold);
                intent.putExtra("robotDeviceId", robotDeviceId);
                intent.putExtra("userId", userId);
                intent.putExtra("guestId",timMessage.getSender());
                startActivity(intent);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }else if(msg.contains("bindOk:")){
            //主人同意绑定
            Log.i("bindOk  ", msg.substring(7));
            sendBroadcast(new Intent(InputInfoActivity.AGREE_BIND)
            .putExtra("robotDeviceId", msg.substring(7)));
        }else if(msg.contains("bindNo:")){
            //主人不同意绑定
            Log.i("bindNo  ", msg.substring(7));
            sendBroadcast(new Intent(InputInfoActivity.REFUSE_BIND)
            .putExtra("robotDeviceId", msg.substring(7)));
        }


        // 后续处理 code
        if ("it's mine!".equals(msg)) {
        }
    }

    public void handleImage(TIMElem elem) {
        // 图片元素
    }

    public void handleVoice(TIMElem elem) {
        // 语音元素
    }

    public void handleVideo(TIMElem elem) {
        // 视频元素
    }

    public void handleCustom(TIMElem elem) {
        // 自定义元素
    }

    public void handleLocation(TIMElem elem) {
        // 位置元素
    }

    public void handleFile(TIMElem elem) {
        // 文件元素
    }


    @Override
    public void onRecvNotification(int callid, ILVCallNotification notification) {
        Log.d("xxx","onRecvNotification->notify id:" + notification.getNotifId() + "|" + notification.getUserInfo() + "/" + notification.getSender());
    }

    @Override
    public void onNewIncomingCall(int callId, int callType, ILVIncomingNotification notification) {
        //新的来电回调
        Intent intent = new Intent();
        intent.setClass(this, ComingCallDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("CallId", callId);
        intent.putExtra("CallType", callType);
        intent.putExtra("HostId", notification.getSponsorId());
        startActivity(intent);
    }

    @Override
    public void onCallEstablish(int callId) {

    }

    @Override
    public void onCallEnd(int callId, int endResult, String endInfo) {

    }

    @Override
    public void onException(int iExceptionId, int errCode, String errMsg) {

    }
}
