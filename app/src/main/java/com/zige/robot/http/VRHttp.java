package com.zige.robot.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.zige.robot.bean.CBaseCode;
import com.zige.robot.fsj.util.MyNetworkUtil;
import com.zige.robot.utils.ToastUtils;
import com.zige.robot.utils.Util;
import com.zige.zige.httplibray.AsyncHttp;
import com.zige.zige.httplibray.Listener;
import com.zige.zige.httplibray.NetroidError;
import com.zige.zige.httplibray.Request;
import com.zige.zige.httplibray.request.VRStringRequest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能：网络数据请求类
 * <p>
 * Created by user on 2016/4/26.
 */
public final class VRHttp {

    private static final String TAG = "VRHttp";

    private static AsyncHttp asyncHttp = AsyncHttp.getInstance();

    public static void getSmsCode(final String url, final Map<String, String> mArgs, final VRHttpListener vrHttpListener) {
        asyncHttp.addRequest(new VRStringRequest(Request.Method.POST, url, mArgs, new Listener<String>() {

            @Override
            public void onPreExecute() {
                vrHttpListener.onPreExecute();
            }

            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    CBaseCode codeBean = Parser.getBaseCode(response);
                    if ("0".equals(codeBean.code)) {
                        vrHttpListener.onSuccess(codeBean.message, false);
                    } else {
                        vrHttpListener.onError(codeBean.message);
                    }
                } else {
                    vrHttpListener.onError("response 为空");
                }
            }

            @Override
            public void onError(NetroidError error) {
                super.onError(error);
                vrHttpListener.onError(error.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vrHttpListener.onFinish();
            }
        }), TimeUnit.MINUTES, 10, true);
    }


    /**
     * 通用网路请求
     *
     * @param context
     * @param url
     * @param mArgs
     * @param vrHttpListener
     */
    public static void sendRequest(final Context context, final String url, final Map<String, String> mArgs, final VRHttpListener vrHttpListener) {
        if (!MyNetworkUtil.isNetworkAvailable(context)) {
            ToastUtils.showToast("网络连接不可用,请检查网络");
            return;
        }
        asyncHttp.addRequest(new VRStringRequest(Request.Method.POST, url, mArgs, new Listener<String>() {

            ProgressDialog mPrgsDialog;

            @Override
            public void onPreExecute() {
                mPrgsDialog = ProgressDialog.show(context, null, "正在加载中", true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                vrHttpListener.onPreExecute();
            }


            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    vrHttpListener.onSuccess(response, false);
                }
            }

            @Override
            public void onError(NetroidError error) {
                super.onError(error);
                ToastUtils.showToast("服务器网络出错！");
                vrHttpListener.onError(error.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPrgsDialog.cancel();
                vrHttpListener.onFinish();
            }
        }), TimeUnit.MINUTES, 10, true);
    }

    public static void sendGetRequest(final Context context, final String url, final Map<String, String> mArgs, final VRHttpListener vrHttpListener) {
        if (!MyNetworkUtil.isNetworkAvailable(context)) {
            ToastUtils.showToast("网络连接不可用,请检查网络");
            return;
        }
        asyncHttp.addRequest(new VRStringRequest(Request.Method.GET, url, mArgs, new Listener<String>() {

            ProgressDialog mPrgsDialog;

            @Override
            public void onPreExecute() {
                mPrgsDialog = ProgressDialog.show(context, null, "正在加载中", true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                vrHttpListener.onPreExecute();
            }


            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    vrHttpListener.onSuccess(response, false);
                }
            }

            @Override
            public void onError(NetroidError error) {
                super.onError(error);
                ToastUtils.showToast("服务器网络出错！");
                vrHttpListener.onError(error.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPrgsDialog.cancel();
                vrHttpListener.onFinish();
            }
        }), TimeUnit.MINUTES, 10, true);
    }

    /**
     * 通用网路请求
     *
     * @param context
     * @param url
     * @param mArgs
     * @param vrHttpListener
     */
    public static void sendRequestNoDilalog(final Context context, final String url, final Map<String, String> mArgs, final VRHttpListener vrHttpListener) {
        if (!MyNetworkUtil.isNetworkAvailable(context)) {
            ToastUtils.showToast("网络连接不可用,请检查网络");
            return;
        }
        asyncHttp.addRequest(new VRStringRequest(Request.Method.POST, url, mArgs, new Listener<String>() {
            @Override
            public void onPreExecute() {
                vrHttpListener.onPreExecute();
            }


            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    vrHttpListener.onSuccess(response, false);
                }
            }

            @Override
            public void onError(NetroidError error) {
                super.onError(error);
                ToastUtils.showToast("服务器网络出错！");
                vrHttpListener.onError(error.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vrHttpListener.onFinish();
            }
        }), TimeUnit.MINUTES, 10, true);
    }


    public static void submitFaceInfo(final FragmentActivity mContext, String url, Map<String, String> mArgs, final VRHttpListener vrHttpListener) {

        asyncHttp.addRequest(new VRStringRequest(Request.Method.POST, url, mArgs, new Listener<String>() {

            ProgressDialog mPrgsDialog;

            @Override
            public void onPreExecute() {
                mPrgsDialog = ProgressDialog.show(mContext, null, "正在加载中", true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                vrHttpListener.onPreExecute();
            }


            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response)) {
                    vrHttpListener.onSuccess(response, false);
                }
            }

            @Override
            public void onError(NetroidError error) {
                super.onError(error);
                vrHttpListener.onError(error.toString());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPrgsDialog.cancel();
                vrHttpListener.onFinish();
            }
        }), TimeUnit.MINUTES, 10, true);
    }
}
