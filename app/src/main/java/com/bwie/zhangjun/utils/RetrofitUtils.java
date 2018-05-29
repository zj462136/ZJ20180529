package com.bwie.zhangjun.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lenovo on 2018/4/29.
 */

public class RetrofitUtils { private static volatile RetrofitUtils instance;
    private final ApiService apiService;

    private RetrofitUtils(String baseUrl){
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
        apiService = retrofit.create(ApiService.class);

    }
    public static RetrofitUtils getInstance(String baseUrl){
        if (instance==null){
            synchronized (RetrofitUtils.class){
                if (null == instance){
                    instance = new RetrofitUtils(baseUrl);
                }
            }
        }
        return instance;
    }
    public ApiService getApiService(){
        return apiService;
    }
}
