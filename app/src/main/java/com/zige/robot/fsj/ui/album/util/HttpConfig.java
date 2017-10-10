package com.zige.robot.fsj.ui.album.util;

import com.zige.robot.fsj.model.http.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PhoneSj on 2017/9/26.
 */

public class HttpConfig {

    private static HttpConfig retrofitApi;
    Retrofit retrofit;
    ApiService api;

    public static HttpConfig getInstance() {
        synchronized (HttpConfig.class) {
            if (retrofitApi == null) {
                retrofitApi = new HttpConfig();
                retrofitApi.init();
            }
        }

        return retrofitApi;
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                //                .baseUrl(BuildConfig.BASE_URL)
                .baseUrl(ApiService.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

    public ApiService getApi() {
        return api;
    }

}
