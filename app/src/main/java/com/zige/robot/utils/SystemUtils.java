package com.zige.robot.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.zige.robot.App;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 公用的工具类
 * Created by Feel-Wu on 2015/3/24.
 */
public class SystemUtils {

    /**
     * 得到app名称
     *
     * @param
     * @return
     */
    public static String getApplicationName(Context conte) {
        Context context = App.getInstance();
        String str = context.getApplicationInfo().name;
        if (str != null)
            return str;
        int i = context.getApplicationInfo().labelRes;
        if (i > 0)
            str = context.getString(i);
        return str;
    }
    /**
     * 得到app版本号
     *
     * @param
     * @return
     */
    public static int getVersionCode(Context cont) {
        Context context = App.getInstance();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 得到app版本名
     *
     * @param
     * @return
     */
    public static String getVersionName(Context conte) {
        Context context = App.getInstance();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    /**
     * 得到sd卡是否挂载
     *
     * @return
     */
    public static boolean getSDCardState() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 得到sd卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        if (getSDCardState())
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        return null;
    }


    /**
     * 检测某程序是否安装
     */
    public static boolean isInstalledApp(Context cont, String packageName) {
        Context context = App.getInstance();
        Boolean flag = false;

        try {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> pkgs = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (PackageInfo pkg : pkgs) {
                // 当找到了名字和该包名相同的时候，返回
                if ((pkg.packageName).equals(packageName)) {
                    return flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 安装.apk文件
     *
     * @param context
     */
    public void install(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName) || context == null) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装.apk文件
     *
     * @param context
     */
    public void install(Context context, File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据mac地址+deviceid
     * 获取设备唯一编码
     * @return
     */
    public static String getDeviceKey() {
//        Context context = App.getInstance();
//        String macAddress = "";
//        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
//        if (null != info) {
//            macAddress = info.getMacAddress();
//        }
//        TelephonyManager telephonyManager =
//                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceId = telephonyManager.getDeviceId();
//        return deviceId;
        Context context = App.getInstance();
        String macAddress = "";
        @SuppressLint("WifiManagerLeak") WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return Md5Algorithm.getMD5("android" + macAddress + deviceId);
    }



    /**
     * 获取手机及SIM卡相关信息
     * @param
     * @return
     */
    public static Map<String, String> getPhoneInfo(Context cont) {
        Context context = App.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String imsi = tm.getSubscriberId();
        String phoneMode = android.os.Build.MODEL;
        String phoneSDk = android.os.Build.VERSION.RELEASE;
        map.put("imei", imei);
        map.put("imsi", imsi);
        map.put("phoneMode", phoneMode + "##" + phoneSDk);
        map.put("model", phoneMode);
        map.put("sdk", phoneSDk);
        return map;
    }

    public static void execShell(String cm){
        String cmd = "adb shell am start -a com.nibiru.videostart --es PATH sdcard/YN040363_thm.MP4 --ei TYPE_2DOR3D 0 --ei TYPE_MODEL 0 --ei TYPE_DECODE 1";

//        try{
//            //权限设置
//            Process p = Runtime.getRuntime().exec("su");
//            //获取输出流
//            OutputStream outputStream = p.getOutputStream();
//            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
//            //将命令写入
//            dataOutputStream.writeBytes(cmd);
//            //提交命令
//            dataOutputStream.flush();
//
////            dataOutputStream.writeBytes("exit\n");
////            dataOutputStream.flush();
//
//            //关闭流操作
////            outputStream.close();
////            dataOutputStream.close();
//            p.waitFor();
//        }
//        catch(Throwable t)
//        {
//            t.printStackTrace();
//        }

        String path = "file:///android_asset/v.sh";

        String buffer = "adb shell am start -a com.nibiru.videostart --es PATH sdcard/YN040363_thm.MP4 --ei TYPE_2DOR3D 0 --ei TYPE_MODEL 0 --ei TYPE_DECODE 1";
        String myPaht = getExternalSdCardPath() + "v.sh";
        FileUtils.copyFile(new File(path), new File(myPaht));

        File file = new File(myPaht);
        if (file.exists()) {
            Log.i("dddddd", file.getAbsolutePath());
        } else {
//            file.mkdir();
        }

        Log.i("ddd", path);

        StringBuffer output = new StringBuffer();

        Process p;
        try {
//            p = Runtime.getRuntime().exec("./v.sh");
            p = Runtime.getRuntime().exec(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = output.toString();
        System.out.print(response);
    }

    public static String getExternalSdCardPath() {

        if (SystemUtils.getSDCardState()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
     *
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        String[] toSearch = FileUtils.readFile("/etc/vold.fstab").split(" ");
        ArrayList<String> out = new ArrayList<String>();
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i].contains("dev_mount")) {
                if (new File(toSearch[i + 2]).exists()) {
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

    public static boolean isMobileNO(String mobiles){

        Pattern p = Pattern.compile("^((177)|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();

        }
}
