package com.baidufacerecog;

import android.os.Environment;
import android.text.TextUtils;

import com.baidu.aip.face.AipFace;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by zige on 2017/6/23.
 */

public class FaceBaidu {
    private static final String LOG_TAG = "FaceService";
    private String MYGROUPNAME = "mentofamliy";
    private AipFace client;
    private String token = "";
    private int faceNum = 0;


    public String getToken() {
        return token;
    }

    public int getFaceNum() {
        return faceNum;
    }

    private boolean isSuccess(JSONObject jo) {
        return jo.optInt("error_code") == 0;
    }

    public boolean detect(String path) {

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");

        // 参数为本地图片路径
        JSONObject response = client.detect(path, options);
        if (!isSuccess(response)) {
            return false;
        }
        faceNum = response.optInt("result_num");
        return faceNum == 1;
    }

    private void creatFaceSet(String path) {
        HashMap<String, String> options2 = new HashMap<String, String>();
        JSONObject res2 = client.addUser(System.currentTimeMillis() + "", "", Arrays.asList(MYGROUPNAME), path,
                options2);
        if (!TextUtils.isEmpty(res2.optString("error_code"))) {
        }
    }

    //
    public String search(String path) {
        HashMap<String, Object> options = new HashMap<String, Object>(1);
        options.put("user_top_num", 1);
        JSONObject res = client.identifyUser(Arrays.asList(MYGROUPNAME), path, options);
        System.out.print(res.toString());
        FaceResultBaidu resultBaidu = new Gson().fromJson(res.toString(), FaceResultBaidu.class);
        if (resultBaidu.getError_code() == 216618) {
            creatFaceSet(path);
            token = "";
        }
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
//        FaceResultMakeSureBaidu resultMakeSureBaidu = new Gson().fromJson(res1.toString(),
//                FaceResultMakeSureBaidu.class);
//        if (resultMakeSureBaidu.getError_code() != 0) {
//            return false;
//        }
//        if (resultMakeSureBaidu.getResults() != null && resultMakeSureBaidu.getResults().get(0) > 90) {
//            // 认识此人，喊出名字打招呼
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }

    private String p = Environment.getExternalStorageDirectory().getPath() + File.separator + "cache.png";

    public void updateFace(String path, final String pid) {
        if (!FileUtil.copyFile(path, p)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> options = new HashMap<String, String>();
                JSONObject res = client.addUser(pid, "", Arrays.asList(MYGROUPNAME), p, options);
                if (TextUtils.isEmpty(res.optString("error_code"))) {
                }
            }
        }).start();

    }


    public boolean addFace(String path) {
        HashMap<String, String> options2 = new HashMap<String, String>();
        long pid = System.currentTimeMillis();
        JSONObject res2 = client.addUser(pid + "", "", Arrays.asList(MYGROUPNAME), path,
                options2);
        if (TextUtils.isEmpty(res2.optString("error_code"))) {
            token = pid + "";
            return true;
        }
        return false;
    }

    public boolean deletePerson(String path) {
        boolean hasFace = detect(path);
        if (hasFace && faceNum == 1) {
            search(path);
            if (TextUtils.isEmpty(token)) {
                return true;
            }
            JSONObject delPerson = client.deleteUser(token);
            if (isSuccess(delPerson)) {
                return deletePerson(path);
            } else {
                return deletePerson(path);
            }
        } else {
            return true;
        }
    }

}
