package com.zige.robot.utils;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zige.robot.HttpLink;
import com.zige.robot.http.VRHttp;
import com.zige.robot.http.VRHttpListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*************************************
 功能：
 创建者： kim_tony
 创建日期：2017/7/23
 *************************************/

public class QiniuUtil {

    private static final String TAG = "QiniuUtil";
    Dialog loadingDialog;


    /****
     * 获取七牛Token
     *
     * 参数：headPortraitLocalPath   图片路径
     * 参数：callBack  获取token后的回调
     */
    public static void getQiNiuToken(final Activity activity,
                                     final String headPortraitLocalPath, final UploadCallBack callBack) {

        final String headPortraitName = FileUtils.getFileName(headPortraitLocalPath);
        Map<String, String> map = new HashMap<String, String>();
        map.put("fileKey", headPortraitName);
        try {
            VRHttp.sendRequestNoDilalog(activity, HttpLink.GET_NIQIU_TOKEN, map, new VRHttpListener() {
                @Override
                public void onSuccess(Object response, boolean isCache) {
                    Log.d(TAG, " onSuccess run at " + Thread.currentThread().getName());
                    String json = response.toString();
                    try {
                        JSONObject object = new JSONObject(json);
                        int code = object.getInt("code");
                        if (code == 0) {
                            final Dialog loadingDialog = DialogUtils.createLoadingDialog(activity, "正在处理...", true);
                            loadingDialog.setCanceledOnTouchOutside(false);
                            loadingDialog.show();
                            final String qiNiuToken = object.getString("qiniuToken");
                            Configuration.Builder configuration = new Configuration.Builder();
                            configuration.retryMax(2);
                            configuration.responseTimeout(7);
                            UploadManager uploadManager = new UploadManager(configuration.build());
                            uploadManager.put(headPortraitLocalPath, headPortraitName, qiNiuToken, new UpCompletionHandler() {
                                @Override
                                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                                    Log.d(TAG, "complete  run at: " + Thread.currentThread().getName());
                                    cloaseLoadingDialog(loadingDialog);
                                    if (200 == responseInfo.statusCode) {
                                        TagUtil.showLogDebug("上传七牛成功 地址：" + getQniuImageUrl(headPortraitName));
                                        callBack.isOk(getQniuImageUrl(headPortraitName), headPortraitName,
                                                qiNiuToken);
                                    } else {
                                        Log.e("wu", "上传七牛失败");
                                        callBack.isFail();
                                    }
                                }
                            }, null);
                        }
                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //关闭加载dialog
    private static void cloaseLoadingDialog(Dialog loadingDialog) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    /**
     * 拼接七牛图片地址
     *
     * @param name
     * @return
     */

    public static String getQniuImageUrl(String name) {
//        return "http://" + "7xkpsq.com2.z0.glb.qiniucdn.com" + "/" + name;
        return "http://r.mento.ai" + "/" + name;
    }

    public interface UploadCallBack {
        //*** url 上传后的地址  ***
        // *** localPath  本地取的图片名称 ***
        // *** token 七牛授权码 ***
        void isOk(String url, String localPath, String token);

        void isFail();

    }

}
