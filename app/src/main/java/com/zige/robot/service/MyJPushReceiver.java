package com.zige.robot.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    String str;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
//            SharedPreferencesUtils.saveValue(context,"jpushString",bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//            opreate(context);
            //打开自定义的Activity
//            Intent i = new Intent(context, JPushIntentActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声
        JPushInterface.setPushNotificationBuilder(1,builder);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


//    private void opreate(Context context){
//        String respone = (String) SharedPreferencesUtils.getValue(context,"jpushString","");
//        Log.e("wu","推送    "+respone);
//        try {
//            JSONObject jsonObject = new JSONObject(respone);
//            String mtype = jsonObject.getString("mtype");
//            String vtype = jsonObject.getString("vtype");
//            String value = jsonObject.getString("value");
//
//            if(mtype.equals("0")){
//                //0视频
//                if(vtype.equals("0")){
//                    //o 普通视频
//                    Intent intent = new Intent();
//                    intent.putExtra("vid", Integer.valueOf(value));
//                    intent.setClass(context,VideoDetailActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                    context.startActivity(intent);
//                    clearData(context);
//                }else  if(vtype.equals("1")){
//                    //o VR视频
//                    Intent intent = new Intent();
//                    intent.putExtra("vid", Integer.valueOf(value));
//                    intent.setClass(context,VRVideoDetailsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                    context.startActivity(intent);
//                    clearData(context);
//                } else if (vtype.equals("2")) {
//                    // 课程
//                    if (!AppContext.getInstance().isLogin()) {
//                        Toast.makeText(context, "您还没有登录，请先去登录", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    Intent intent = new Intent();
//                    intent.putExtra("courseId", Integer.valueOf(value));
//                    intent.setClass(context,VideoCourseActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                    context.startActivity(intent);
//                    clearData(context);
//                }
//            }else if(mtype.equals("1")){
//                //webview
//                Intent intent = new Intent();
//                intent.putExtra("link",value);
//                intent.setClass(context,WebViewActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(intent);
//                clearData(context);
//            }else {
//                Intent intent = new Intent();
//                intent.setClass(context,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(intent);
//                clearData(context);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//    private void clearData(Context context){
//        SharedPreferencesUtils.saveValue(context,"jpushString",null);
//    }
}
