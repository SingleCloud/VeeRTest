package com.genlan.veertest.model.net;

import com.genlan.veertest.model.db.WebPageBean;

import java.util.List;

/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

public class ResponseBean {

    private int code;
    private String message;
    private List<WebPageBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WebPageBean> getData() {
        return data;
    }

    public void setData(List<WebPageBean> data) {
        this.data = data;
    }

}
