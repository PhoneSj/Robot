package com.zige.robot;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.blankj.utilcode.util.Utils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;
import com.zige.robot.bean.UserInfo;
import com.zige.robot.fsj.ui.call.base.CallManager;
import com.zige.robot.fsj.ui.call.base.CallReceiver;
import com.zige.robot.fsj.util.CallUtil;
import com.zige.robot.fsj.util.LogUtil;
import com.zige.robot.greendao.GreenDaoManager;
import com.zige.robot.http.rxhttp.ApiBox;
import com.zige.robot.service.IMHelper;
import com.zige.robot.utils.SharedPreferencesUtils;
import com.zige.zige.httplibray.AsyncHttp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;


/**
 * Created by zhanghuan on 2016/3/17.
 */
public class App extends Application {

    public static final String TAG = "phone";

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    private static App instance;
    private HashMap<String, Object> map = new HashMap<String, Object>();
    private UserInfo userInfo;
    private String phone; //用户登录的手机号码

    public static Stack<AppCompatActivity> getActivityStack() {
        return activityStack;
    }

    private static Stack<AppCompatActivity> activityStack; //Activity 管理栈

    // 全局的上下文对象
    protected static Context context;
    //来电广播
    private CallReceiver callReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
        IMHelper.getInstance(this); // IM初始化
        ApiBox.Builder builder = new ApiBox.Builder();
        builder.application(this).debug(BuildConfig.IS_DEBUG).build();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        Utils.init(this);
        context = this;
        // 初始化环信sdk
        initHyphenate();

        //初始化屏幕宽高
        getScreenSize();
        //初始化数据库
        Realm.init(getApplicationContext());
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);//屏幕尺寸信息写入参数中
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        //如果启动App时，手机整处于横屏，宽高值交换（这里保持宽<高）
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    private void initHyphenate() {

        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }


        // 初始化sdk的一些配置
        EMOptions options = new EMOptions();
        //options.enableDNSConfig(false);
        //options.setIMServer("118.193.28.212");
        //options.setImPort(31097);
        //options.setRestServer("118.193.28.212:31080");
        //// 动态设置appkey，如果清单配置文件设置了 appkey，这里可以不用设置
        //options.setAppKey("easemob-demo#chatdemoui");

        options.setAutoLogin(false);
        // 设置小米推送 appID 和 appKey
        options.setMipushConfig("2882303761517573806", "5981757315806");

        // 设置消息是否按照服务器时间排序
        options.setSortMessageByServerTime(false);

        // 初始化环信SDK,一定要先调用init()
        EMClient.getInstance().init(context, options);

        // 开启 debug 模式
        EMClient.getInstance().setDebugMode(true);

        // 设置通话广播监听器
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance()
                                                           .callManager()
                                                           .getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        context.registerReceiver(callReceiver, callFilter);

        // 通话管理类的初始化
        CallManager.getInstance().init(context);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init() {
        CrashReport.initCrashReport(getApplicationContext(), BuildConfig.Bugly_Id, true);
        AsyncHttp.setContext(this);  // 初始化网络请求
        GreenDaoManager.getInstance(); //初始化greendao
        AppConfig.getAppConfig().init();
        SpeechUtility.createUtility(this, String.format("%s=%s", SpeechConstant.APPID, BuildConfig.Xunfei_Id)); // 初始化讯飞
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<AppCompatActivity>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public String currentActivityName() {
        return activityStack.lastElement().getClass().getName();
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activityStack != null && activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (AppCompatActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static App getInstance() {
        return instance;
    }


    public void put(String key, Object object) {
        map.put(key, object);
    }

    public Object get(String key) {
        return map.get(key);
    }


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        // TODO: 2017/9/13 存入robotId
        if (userInfo != null) {
            SharedPreferencesUtils.saveRobotIdToSP(userInfo.getDeviceid());
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void exitApp() {
        SharedPreferencesUtils.saveRobotIdToSP(null);
        SharedPreferencesUtils.saveConnectedHXContact(null);
        //退出环信登录
        CallUtil.logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtil.showI("logout hx success");
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.showI("logout hx error");
            }

            @Override
            public void onProgress(int i, String s) {
                LogUtil.showI("logout hx progress");
            }
        });

        if (activityStack != null) {
            synchronized (activityStack) {
                for (Activity act : activityStack) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }
}
