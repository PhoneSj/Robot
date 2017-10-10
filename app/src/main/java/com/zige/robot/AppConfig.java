package com.zige.robot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.zige.robot.utils.SystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * <p>
 * Created by zhanghuan on 2016/3/17.
 */
public class AppConfig {

    private final static String APP_CONFIG = "config";

    public final static String CONF_COOKIE = "cookie";

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";

    public static boolean DEBUG = false;

    public final static String DEFAULT_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    // 默认存放缓存的路径
    public final static String DEFAULT_SAVE_VIDEOLIST_PATH =
            Environment.getExternalStorageDirectory()
                    + File.separator
                    + "/ketang/localVideo/list.txt";
    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + "/zige/image/";

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "ketang"
            + File.separator + "download" + File.separator + "video" + File.separator;

    //默认录音文件路径
    public static final String DEFAULT_VOICE_FILE_PATH =  Environment.getExternalStorageDirectory().getPath() + "/zige/sound";
    //默认视频文件存放路径
    public static final String DEFAULT_VIDEO_FILE_PATH =  Environment.getExternalStorageDirectory().getPath() + "/zige/video";
    //二维码图片生成路径
    public static final String DEFAULT_QR_CODE_PATH =  Environment.getExternalStorageDirectory().getPath() + "/zige/qr/myQr.jpg";


    private Context mContext;
    private static AppConfig appConfig;
    private String version = "1.0";
    private String platform = "android";

    public static AppConfig getAppConfig() {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = App.getInstance();
        }
        return appConfig;
    }

    public void init() {
        version = SystemUtils.getVersionName(mContext);
    }

    public String getVerison() {
        if ("1.0".equals(version)) {
            version = SystemUtils.getVersionName(mContext);
        }
        return version;
    }


    public String getPlatform() {
        return platform;
    }



    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
