package com.baidufacerecog;


import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.aip.face.AipFace;
import com.baidufacerecog.Bean.RecogAddUserEntity;
import com.baidufacerecog.Bean.RecogResultEntity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*************************************
 功能：
 创建者： kim_tony
 创建日期：2017/7/24
 *************************************/

public class BaiduFaceHelper implements BaiduFaceInterface {
    public static String MYGROUPNAME = "mentofamliy";

    private static BaiduFaceHelper baiduFace = new BaiduFaceHelper();
    private AipFace client;

    private BaiduFaceHelper() {
        init();
    }

    public static BaiduFaceHelper getDefault() {
        return baiduFace;
    }


    private void init() {
        // 初始化一个FaceClient
        client = new AipFace(BuildConfig.AppId, BuildConfig.ApiKey, BuildConfig.SecretKey);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(4000); //建立连接的超时时间（单位：毫秒）
        client.setSocketTimeoutInMillis(50000); //通过打开的连接传输数据的超时时间（单位：毫秒）
    }

    //根据人脸检测
    public Observable<RecogResultEntity> checkByPatch(final String imagePath) {
        return Observable.create(new Observable.OnSubscribe<RecogResultEntity>() {
            @Override
            public void call(Subscriber<? super RecogResultEntity> subscriber) {
                // 参数为本地图片路径
                JSONObject response = client.detect(imagePath, new HashMap());
                Log.d("tagutil", "checkByPatch: " + response.toString());

                /**
                 * {"result_num":1,"result":[{"location":{"left":29,"top":1624,"width":704,"height":751},"face_probability":0.99400395154953,"rotation_angle":5,"yaw":-5.995879650116,"pitch":5.2541041374207,"roll":4.4290328025818}],"log_id":2642522097}
                 * api说明http://ai.baidu.com/docs#/Face-Java-SDK/top
                 */

                RecogResultEntity entity = getServerBean(response.toString(), RecogResultEntity.class);
                subscriber.onNext(entity);
            }

        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    //查看是否已存在personid
    public Observable<String> searchFace(final String imagePath) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(search(imagePath));
            }

        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io());

    }


    Gson gson;

    public <T> T getServerBean(String json, Class<?> cls) {
        T object = null;
        if (gson == null) {
            gson = new Gson();
        }
        object = (T) gson.fromJson(json, cls);
        return object;
    }


    @Override
    public Observable<RecogAddUserEntity> addSet(final String group, final String path) {
        final String personId = System.currentTimeMillis() + "";
        return Observable.create(new Observable.OnSubscribe<RecogAddUserEntity>() {
            @Override
            public void call(Subscriber<? super RecogAddUserEntity> subscriber) {
                JSONObject res2 = client.addUser(personId, "", Arrays.asList(group), path,
                        new HashMap<String, String>());
                RecogAddUserEntity recogAddUserEntity = getServerBean(res2.toString(), RecogAddUserEntity.class);
                recogAddUserEntity.personId = personId;
                Log.d("tagutil", "subscribeActual: " + res2.toString());
                subscriber.onNext(recogAddUserEntity);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

//        if (!TextUtils.isEmpty(res2.optString("error_code"))) {
//            return false;
//        }
//        return true;
    }


    String p = Environment.getExternalStorageDirectory().getPath() + File.separator + "cache.png";

    @Override
    public void updateFace(final String group, String path, final String pid) {
        if (!FileUtil.copyFile(path, p)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> options = new HashMap<String, String>();
                JSONObject res = client.addUser(pid, "", Arrays.asList(group), p, options);
                RecogAddUserEntity resultEntity = getServerBean(res.toString(), RecogResultEntity.class);
                if (resultEntity.isError()) {
                }
            }
        }).start();


    }


    //是否识别成功
    public boolean isSucess(RecogResultEntity entity) {
        return entity.result != null &&
                entity.result_num == 1 &&
                entity.result.get(0).face_probability > 0.80;
    }

    public String search(String path) {
        String token = "";
        HashMap<String, Object> options = new HashMap<String, Object>(1);
        options.put("user_top_num", 1);
        JSONObject res = client.identifyUser(Arrays.asList(MYGROUPNAME), path, options);
        System.out.print(res.toString());
        FaceResultBaidu resultBaidu = new Gson().fromJson(res.toString(), FaceResultBaidu.class);
//        if (resultBaidu.getError_code() == 216618) {
//            creatFaceSet(path);
//            token = "";
//        }
        if (resultBaidu.getError_code() != 0 || resultBaidu.getResult_num() == 0) {
            token = "";
        }


        String bdperson_id = "", bdperson_name = "";
        double confidence = 0;
        for (FaceResultBaidu.ResultBean resultBean : resultBaidu.getResult()) {
            bdperson_id = resultBean.getUid();
            bdperson_name = resultBean.getUser_info();
            confidence = resultBean.getScores().get(0);
            break;
        }
        if (confidence > 90) {
            token = bdperson_id;
        }
        if (confidence < 80) {
            token = "";
        }
        if (makeSure(path, bdperson_id)) {
            token = bdperson_id;
        }
        return token;


    }

    private boolean makeSure(String path, String pid) {
        HashMap<String, Object> options1 = new HashMap<String, Object>(1);
        options1.put("top_num", 1);
        JSONObject res1 = client.verifyUser(pid, Arrays.asList(MYGROUPNAME), path, options1);
        FaceResultMakeSureBaidu resultMakeSureBaidu = new Gson().fromJson(res1.toString(),
                FaceResultMakeSureBaidu.class);
        if (resultMakeSureBaidu.getError_code() != 0) {
            return false;
        }
        if (resultMakeSureBaidu.getResults() != null && resultMakeSureBaidu.getResults().get(0) > 90) {
            // 认识此人，喊出名字打招呼
            return true;
        } else {
            return false;
        }

    }


    private void creatFaceSet(String path) {
        HashMap<String, String> options2 = new HashMap<String, String>();
        JSONObject res2 = client.addUser(System.currentTimeMillis() + "", "", Arrays.asList(MYGROUPNAME), path,
                options2);
        if (!TextUtils.isEmpty(res2.optString("error_code"))) {
        }
    }

}
