package com.genlan.veertest.controller;

import android.view.View;

/**
 * Description
 * Author Genlan
 * Date 2017/8/3
 */
@FunctionalInterface
public interface OnListItemClickListener {

    /**
     * @param view the view what were clicked
     * @param id   com.genlan.veertest.model.net.ResponseBean$id
     */
    void onClick(View view, int id);

}
