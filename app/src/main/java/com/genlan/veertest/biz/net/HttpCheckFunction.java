package com.genlan.veertest.biz.net;

import com.genlan.veertest.model.net.ResponseBean;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Description  网络请求统一处理
 * Author Genlan
 * Date 2017/8/3
 */

class HttpCheckFunction implements Function<ResponseBean, ResponseBean> {

    @Override
    public ResponseBean apply(@NonNull ResponseBean responseBean) throws Exception {
        if (responseBean.getCode() != 0)
            throw new HttpException(responseBean.getMessage());
        return responseBean;
    }
}
