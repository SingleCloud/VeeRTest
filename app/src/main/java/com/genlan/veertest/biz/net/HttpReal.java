package com.genlan.veertest.biz.net;

import com.genlan.veertest.capability.net.HttpClient;
import com.genlan.veertest.model.net.ResponseBean;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

public class HttpReal {

    public static Observable<ResponseBean> doGet() {
        Retrofit retrofit = HttpClient.generateRetrofitClient();
        return retrofit.create(HttpInterface.class).doGet()
                .map(new HttpCheckFunction())//请求结果校验，code!=0则提示错误
                .subscribeOn(Schedulers.io());
    }
}
