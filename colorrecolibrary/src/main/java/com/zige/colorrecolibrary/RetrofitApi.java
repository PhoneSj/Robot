package com.zige.colorrecolibrary;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zige on 2017/8/7.
 */

public class RetrofitApi {
    private static RetrofitApi retrofitApi;

    public static RetrofitApi getInstance(String host) {
        synchronized (RetrofitApi.class) {
            if (retrofitApi == null) {
                retrofitApi = new RetrofitApi();
                retrofitApi.init(host);
            }
        }

        return retrofitApi;
    }

    Retrofit retrofit;
    Api api;

    private void init(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    public Api getApi(){
        return api;
    }

}
