package com.genlan.veertest.model.db;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Description
 * Author Genlan
 * Date 2017/8/3
 */

interface IDBOperate {

    /**
     * 查询所有数据
     */
    Observable<ArrayList<WebPageBean>> doQueryAll();

    /**
     * 查询单条数据
     */
    Observable<WebPageBean> doQueryById(int id);

    /**
     * 插入单条数据
     */
    Observable<Object> doInsert(WebPageBean bean);

    /**
     * 批量插入数据
     */
    Observable<Object> doInsert(ArrayList<WebPageBean> list);

    /**
     * 删除某条数据
     */
    Observable<Object> doDeleteById(int id);

}
