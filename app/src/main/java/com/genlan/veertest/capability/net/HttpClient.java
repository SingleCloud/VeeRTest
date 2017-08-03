package com.genlan.veertest.capability.net;

import com.genlan.veertest.MyApplication;
import com.genlan.veertest.model.constant.HttpConstant;
import com.genlan.veertest.util.FileUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

public class HttpClient {

    private HttpClient() {
        throw new RuntimeException();
    }

    private static OkHttpClient generateOKClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        本DEMO的接口为Pragma →no-cache，故不建立缓存
//        builder.cache(obtainCache());
        builder.connectTimeout(60, TimeUnit.SECONDS);
        return builder.build();
    }

    public static Retrofit generateRetrofitClient() {
        return new Retrofit.Builder()
                .client(generateOKClient())
//                .baseUrl("")  //demo非同一接口，不使用baseURL
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://mingke.veervr.tv:1920/")
                .build();
    }

    private static Cache obtainCache() {
        Cache cache = null;
        boolean createCacheFile = false;
        File cacheFile;
        try {
            createCacheFile = FileUtil.createFile(HttpConstant.HTTP_CACHE_FILE_NAME);
        } catch (IOException ignore) {
        }
        if (createCacheFile) {
            cacheFile = new File(MyApplication.getInstance().getFilesDir(), HttpConstant.HTTP_CACHE_FILE_NAME);
            if (cacheFile.exists()) {
                cache = new Cache(cacheFile, HttpConstant.HTTP_CACHE_SIZE);
            }
        }
        return cache;
    }

}
