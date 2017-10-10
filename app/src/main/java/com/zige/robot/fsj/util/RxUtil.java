package com.zige.robot.fsj.util;

import com.zige.robot.fsj.model.http.exception.ApiException;
import com.zige.robot.fsj.model.http.response.AlbumHttpResponse;
import com.zige.robot.fsj.model.http.response.CallHttpResponse;
import com.zige.robot.fsj.model.http.response.ZigeHttpResponse;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Phone on 2017/7/17.
 */

public class RxUtil {

    /**
     * 简化线程：上游工作在io线程，下游工作在主线程
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 统一处理Zige网络请求的返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<ZigeHttpResponse<T>, T> handleZigeResult() {
        return new FlowableTransformer<ZigeHttpResponse<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<ZigeHttpResponse<T>> upstream) {
                return upstream.flatMap(new Function<ZigeHttpResponse<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(@NonNull ZigeHttpResponse<T> zigeHttpResponse) throws Exception {
                        LogUtil.showW("code:" + zigeHttpResponse.getCode());
                        LogUtil.showW("msg:" + zigeHttpResponse.getMessage());
                        if (ZigeHttpResponse.RESULT_SUCCESS
                                .equals(zigeHttpResponse.getMessage())) {
                            return createData(zigeHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException("服务器返回错误"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一处理Call网络请求的返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<CallHttpResponse<T>, T> handleCallResult() {
        return new FlowableTransformer<CallHttpResponse<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<CallHttpResponse<T>> upstream) {
                return upstream.flatMap(new Function<CallHttpResponse<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(@NonNull CallHttpResponse<T> zigeHttpResponse) throws Exception {
                        LogUtil.showW("code:" + zigeHttpResponse.getCode());
                        LogUtil.showW("msg:" + zigeHttpResponse.getMsg());
                        if (CallHttpResponse.RESULT_SUCCESS
                                .equals(zigeHttpResponse.getMsg())) {
                            return createData(zigeHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException("服务器返回错误"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一处理Call网络请求的返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<AlbumHttpResponse<T>, T> handleAlbumResult() {
        return new FlowableTransformer<AlbumHttpResponse<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<AlbumHttpResponse<T>> upstream) {
                return upstream.flatMap(new Function<AlbumHttpResponse<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(@NonNull AlbumHttpResponse<T> albumHttpResponse) throws Exception {
                        LogUtil.showW("code:" + albumHttpResponse.getCode());
                        LogUtil.showW("msg:" + albumHttpResponse.getMessage());
                        if (AlbumHttpResponse.RESULT_SUCCESS
                                .equals(albumHttpResponse.getMessage())) {
                            return createData(albumHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException("服务器返回错误"));
                        }
                    }
                });
            }
        };
    }


    /**
     * 构建Flowable
     *
     * @param results
     * @param <T>
     * @return
     */
    private static <T> Publisher<T> createData(final T results) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                e.onNext(results);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }
}
