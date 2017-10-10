package com.zige.robot.http.rxhttp;

import android.content.Context;
import android.util.Log;

import rx.Subscriber;


/**
 * 订阅者基类：对异常统一处理
 * 目测权限处理可以放这里做
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "BaseSubscriber";

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onNext(T t) {
        this.onUserSuccess(t);
    }

    protected abstract void onUserSuccess(T t);

    protected void onUserError(Throwable ex) {
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: "+ e.getMessage());
        onUserError(e);
    }

    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted: ");
    }


}
