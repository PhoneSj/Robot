package com.zige.robot.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.tencent.av.sdk.AVConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.zige.robot.R;

/**
 * Created by zhanghuan on 2017/1/17.
 */

public class Util {
    private static final String TAG = "Util";
    private static final String PACKAGE = "com.reeman";
    public static final int DEFAULT_SDK_APP_ID = 2127983299;
    public static final String DEFAULT_ACCOUNT_TYPE = "5812";

    public static String modifyAppid = "";
    public static String modifyUid = "";

    public static String inputYuvFilePath = "/sdcard/123.yuv";
    public static int yuvWide = 320;
    public static int yuvHigh = 240;
    public static int yuvFormat = 100;
    public static String outputYuvFilePath = "/sdcard/123";
    public volatile static long auth_bits = 0;

    public static final String ACTION_ONLINE_OTHER = PACKAGE + ".ACTION_ONLINE_OTHER";

    /**
     * 机器人电量信息
     */
    public static final String ACTION_STATE_ROBOT_ELE = PACKAGE + ".ACTION_STATE_ROBOT_ELE";

    /**
     * 机器人当前运行的APP
     */
    public static final String ACTION_STATE_ROBOT = PACKAGE + ".ACTION_STATE_ROBOT";

    /**
     * 确认当前机器人在线
     */
    public static final String ROBOT_ONLINE = "com.reeman.activity.ui.MainActivity.ROBOT_ONLINE";

    /**
     * 邀请到达
     */
    public static final String ACTION_INVITE_ARRIVE = PACKAGE + ".ACTION_INVITE_ARRIVE";
    /**
     * 房间没有成员了
     */
    public static final String ACTION_NO_MEMBERS = PACKAGE + ".ACTION_NO_MEMBERS";
    /**
     * 接收方回应失败
     */
    public static final String ACTION_ANSWER_FAIL = PACKAGE + ".ACTION_ANSWER_FAIL";

    /**
     * 接收方接受邀请完成通知
     */
    public static final String ACTION_ACCEPT_COMPLETE = PACKAGE + ".ACTION_ACCEPT_COMPLETE";
    /**
     * 绑定消息提醒
     */
    public static final String ACTION_BIND_RECEIVER = "com.reeman.ACTION_BIND_RECEIVER";
    /**
     * 发起方收到接收方接受邀请通知
     */
    public static final String ACTION_INVITE_ACCEPTED = PACKAGE + ".ACTION_INVITE_ACCEPTED";
    /**
     * 发起方取消邀请通知
     */
    public static final String ACTION_INVITE_CANCELED = PACKAGE + ".ACTION_INVITE_CANCELED";
    /**
     * 发起方收到接收方拒绝邀请通知
     */
    public static final String ACTION_INVITE_REFUSED = PACKAGE + ".ACTION_INVITE_REFUSED";
    /**
     * 接收方收到邀请通知
     */
    public static final String ACTION_RECV_INVITE = PACKAGE + ".ACTION_RECV_INVITE";
    /**
     * 接收方拒绝邀请完成通知
     */
    public static final String ACTION_REFUSE_COMPLETE = PACKAGE + ".ACTION_REFUSE_COMPLETE";

    public static final String ACTION_START_CONTEXT_COMPLETE = PACKAGE + ".ACTION_START_CONTEXT_COMPLETE";
    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE + ".ACTION_CLOSE_CONTEXT_COMPLETE";
    /**
     * 电话接通的时候调用
     */
    public static final String ACTION_ROOM_CREATE_COMPLETE = PACKAGE + ".ACTION_ROOM_CREATE_COMPLETE";
    /**
     * 房间关闭之后的调用
     */
    public static final String ACTION_CLOSE_ROOM_COMPLETE = PACKAGE + ".ACTION_CLOSE_ROOM_COMPLETE";
    public static final String ACTION_SURFACE_CREATED = PACKAGE + ".ACTION_SURFACE_CREATED";
    public static final String ACTION_MEMBER_CHANGE = PACKAGE + ".ACTION_MEMBER_CHANGE";
    public static final String ACTION_MEMBER_ADD = PACKAGE + ".ACTION_MEMBER_ADD";
    public static final String ACTION_VIDEO_SHOW = PACKAGE + ".ACTION_VIDEO_SHOW";
    public static final String ACTION_VIDEO_CLOSE = PACKAGE + ".ACTION_VIDEO_CLOSE";
    public static final String ACTION_ENABLE_CAMERA_COMPLETE = PACKAGE + ".ACTION_ENABLE_CAMERA_COMPLETE";
    public static final String ACTION_SWITCH_CAMERA_COMPLETE = PACKAGE + ".ACTION_SWITCH_CAMERA_COMPLETE";
    public static final String ACTION_OUTPUT_MODE_CHANGE = PACKAGE + ".ACTION_OUTPUT_MODE_CHANGE";
    public static final String ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE = PACKAGE
            + ".ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE";
    public static final String ACTION_CHANGE_AUTHRITY = PACKAGE + ".ACTION_CHANGE_AUTHRITY";
    public static final String EXTRA_TOAV_TYPE = "toavtype";
    public static final String EXTRA_RELATION_ID = "relationId";
    public static final String EXTRA_AV_ERROR_RESULT = "av_error_result";
    public static final String EXTRA_VIDEO_SRC_TYPE = "videoSrcType";
    public static final String EXTRA_IS_ENABLE = "isEnable";
    public static final String EXTRA_IS_FRONT = "isFront";
    public static final String EXTRA_IDENTIFIER = "identifier";
    public static final String EXTRA_SELF_IDENTIFIER = "selfIdentifier";
    public static final String EXTRA_ROOM_ID = "roomId";
    public static final String EXTRA_IS_VIDEO = "isVideo";
    public static final String EXTRA_IDENTIFIER_LIST_INDEX = "QQIdentifier";

    public static int getScreenWidth(Context c) {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context c) {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static String getRootDir(Context context, String subDir) {
        File dir = new File(Environment.getExternalStorageDirectory(), "/reeman/accountinfo/" + subDir);// context.getExternalFilesDir(null);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir.toString();
    }

    private static ParcelFileDescriptor getFile(String fileName) {
        try {
            File file = new File(fileName);
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(file,
                    ParcelFileDescriptor.MODE_CREATE | ParcelFileDescriptor.MODE_READ_WRITE);
            return fd;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "WL_DEBUG getFile error : " + e);
        }
        return null;
    }

    private static ArrayList<String> InputStreamToStringArray(InputStream inputStream) {
        ArrayList<String> result = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static ArrayList<String> FileToStringArray(Context context, String subDir, String fileName) {
        String rootDir = getRootDir(context, subDir);
        ParcelFileDescriptor fd = getFile(rootDir + "/" + fileName);
        FileInputStream fis = null;
        ArrayList<String> result = null;

        if (null == fd) {
            return result;
        }

        try {
            fis = new ParcelFileDescriptor.AutoCloseInputStream(fd);
            result = InputStreamToStringArray(fis);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e(TAG, "WL_DEBUG FileToStringArray error : " + e);
                }
            }
        }

        return result;
    }

    private static void StringArrayToFile(Context context, String subDir, String fileName,
                                          ArrayList<String> stringArray) {
        String rootDir = getRootDir(context, subDir);
        ParcelFileDescriptor fd = getFile(rootDir + "/" + fileName);
        FileOutputStream fos = null;

        if (null == fd) {
            return;
        }

        try {
            fos = new ParcelFileDescriptor.AutoCloseOutputStream(fd);

            for (String string : stringArray) {
                fos.write(string.getBytes());
                fos.write("\n".getBytes());
            }
        } catch (IOException e) {
            Log.e(TAG, "WL_DEBUG StringArrayToFile error : " + e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "WL_DEBUG StringArrayToFile error : " + e);
                }
            }
        }
    }

    public static ArrayList<String> getIdentifierList(Context context) {
        ArrayList<String> result = FileToStringArray(context, "accountinfo", "openid_list.txt");
        if (result == null || result.size() == 0) {
            setIdentifierList(context,
                    new ArrayList<String>(Arrays.asList(context.getResources()
                                                               .getStringArray(R.array.openid_list))));
            result = FileToStringArray(context, "accountinfo", "openid_list.txt");
        }

        return result;
    }

    public static void setIdentifierList(Context context, ArrayList<String> stringArray) {
        StringArrayToFile(context, "accountinfo", "openid_list.txt", stringArray);
    }

    public static ArrayList<String> getUserSigList(Context context) {
        ArrayList<String> result = FileToStringArray(context, "accountinfo", "openkey_list.txt");
        if (result == null || result.size() == 0) {
            setUserSigList(context,
                    new ArrayList<String>(Arrays.asList(context.getResources()
                                                               .getStringArray(R.array.openkey_list))));
            result = FileToStringArray(context, "accountinfo", "openkey_list.txt");
        }

        return result;
    }

    public static void setUserSigList(Context context, ArrayList<String> stringArray) {
        StringArrayToFile(context, "accountinfo", "openkey_list.txt", stringArray);
    }

    public static ProgressDialog newProgressDialog(Context context, int titleId) {
        ProgressDialog result = new ProgressDialog(context);
        result.setTitle(titleId);
        result.setIndeterminate(true);
        result.setCancelable(false);

        return result;
    }

    public static AlertDialog newErrorDialog(Context context, int titleId) {
        return new AlertDialog.Builder(context).setTitle(titleId)
                                               .setMessage(R.string.error_code_prefix)
                                               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                   }
                                               })
                                               .create();
    }

    public static void switchWaitingDialog(Context ctx, ProgressDialog waitingDialog, int dialogId, boolean isToShow) {
        if (isToShow) {
            if (waitingDialog == null || !waitingDialog.isShowing()) {
                if (ctx instanceof Activity) {
                    ((Activity) ctx).showDialog(dialogId);
                }
            }
        } else {
            if (waitingDialog != null && waitingDialog.isShowing()) {
                waitingDialog.dismiss();
            }
        }
    }

//    /**
//     * 判断网络是wifi还是手机
//     * @param context
//     * @return
//     */
//    public static int getNetWork(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()) {
//            String type = networkInfo.getTypeName();
//            if (type.equalsIgnoreCase("WIFI")) {
//                return 0;
//            } else if (type.equalsIgnoreCase("MOBILE")) {
//                return 1;
//            }
//        }
//        return -1;
//    }
//
//    /**
//     * 获取网络类型
//     *
//     * @param context
//     * @return
//     */
//    public static int getNetWorkType(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()) {
//            String type = networkInfo.getTypeName();
//
//            if (type.equalsIgnoreCase("WIFI")) {
//                Log.i("sql", "网店网络是wifi");
//                return AVConstants.NETTYPE_WIFI;
//            } else if (type.equalsIgnoreCase("MOBILE")) {
//                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//                if (mobileInfo != null) {
//                    switch (mobileInfo.getType()) {
//                        case ConnectivityManager.TYPE_MOBILE:// 手机网络
//                            switch (mobileInfo.getSubtype()) {
//                                case TelephonyManager.NETWORK_TYPE_UMTS:
//                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
//                                case TelephonyManager.NETWORK_TYPE_HSDPA:
//                                case TelephonyManager.NETWORK_TYPE_HSUPA:
//                                case TelephonyManager.NETWORK_TYPE_HSPA:
//                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
//                                case TelephonyManager.NETWORK_TYPE_EHRPD:
//                                case TelephonyManager.NETWORK_TYPE_HSPAP:
//                                    return AVConstants.NETTYPE_3G;
//                                case TelephonyManager.NETWORK_TYPE_CDMA:
//                                case TelephonyManager.NETWORK_TYPE_GPRS:
//                                case TelephonyManager.NETWORK_TYPE_EDGE:
//                                case TelephonyManager.NETWORK_TYPE_1xRTT:
//                                case TelephonyManager.NETWORK_TYPE_IDEN:
//                                    return AVConstants.NETTYPE_2G;
//                                case TelephonyManager.NETWORK_TYPE_LTE:
//                                    return AVConstants.NETTYPE_4G;
//                                default:
//                                    return AVConstants.NETTYPE_NONE;
//                            }
//                    }
//                }
//
//            }
//        }
//        return AVConstants.NETTYPE_NONE;
//    }
//
//    /*
//     * 是否存在网络
//     */
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo info = connectivity.getActiveNetworkInfo();
//            if (info != null && info.isConnected()) {
//                // 当前网络是连接的
//                if (info.getState() == NetworkInfo.State.CONNECTED) {
//                    // 当前所连接的网络可用
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public static int changeNum(String num) {
        String src_3 = num.substring(2, 11);
        return Integer.valueOf(src_3);
    }
}
