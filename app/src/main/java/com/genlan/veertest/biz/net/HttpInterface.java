package com.genlan.veertest.biz.net;

import com.genlan.veertest.model.net.ResponseBean;
import com.genlan.veertest.model.constant.HttpConstant;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

interface HttpInterface {

    @GET(HttpConstant.HTTP_LIST_URL)
    Observable<ResponseBean> doGet();
}
