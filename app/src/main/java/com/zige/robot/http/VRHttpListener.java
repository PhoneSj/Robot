package com.zige.robot.http;

import com.zige.zige.httplibray.Request;

/**
 * 功能：网络请求后结果的返回
 * <p>
 * Created by user on 2016/4/26.
 */
public abstract class VRHttpListener<T> {

    public void onPreExecute() {}

    /** Inform when {@link Request} execute is finish,
     * whatever success or error or cancel, this callback
     * method always invoke if request is done. */
    public void onFinish() {}

    /** Called when response success. */
    public abstract void onSuccess(T response, boolean isCache);

    public void noDate(){};

    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    public void onError(String error) {}

    /** Inform when the {@link Request} is truly cancelled. */
    public void onCancel() {}

    /**
     * Inform When the {@link Request} cache non-exist or expired,
     * this callback method is opposite by the onUsedCache(),
     * means the http retrieving will happen soon.
     */
    public void onNetworking() {}

    /** Inform when the cache already use,
     * it means http networking won't execute. */
    public void onUsedCache() {}

    /** Inform when {@link Request} execute is going to retry. */
    public void onRetry() {}

    /**
     * Inform when download progress change, this callback method only available
     * when request was {@link com.zige.zige.httplibray.request.FileDownloadRequest}.
     */
    public void onProgressChange(long fileSize, long downloadedSize) {
    }
}

