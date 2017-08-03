package com.genlan.veertest.controller;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genlan.veertest.R;
import com.genlan.veertest.model.db.WebPageBean;

import java.util.ArrayList;

/**
 * Description
 * Author Genlan
 * Date 2017/8/3
 */

public class CollectListAdapter extends RecyclerView.Adapter<CollectListAdapter.CollectVH> {

    private final LayoutInflater mInflater;
    private final ArrayList<WebPageBean> mList;

    public CollectListAdapter(Context context, ArrayList<WebPageBean> list) {
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public CollectVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectVH(mInflater.inflate(R.layout.item_collect, parent, false));
    }

    @Override
    public void onBindViewHolder(CollectVH holder, int position) {
        holder.mTvTitle.setText(mList.get(position).getTitle());
        holder.mTvCategory.setText(mList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class CollectVH extends RecyclerView.ViewHolder {

        private final AppCompatTextView mTvTitle;
        private final AppCompatTextView mTvCategory;

        CollectVH(View itemView) {
            super(itemView);
            mTvCategory = itemView.findViewById(R.id.tv_item_collect_category);
            mTvTitle = itemView.findViewById(R.id.tv_item_collect_title);
        }
    }
}
