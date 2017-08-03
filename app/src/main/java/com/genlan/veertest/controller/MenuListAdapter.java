package com.genlan.veertest.controller;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.genlan.veertest.R;
import com.genlan.veertest.model.db.WebPageBean;
import com.genlan.veertest.util.ScreenUtil;

import java.util.ArrayList;

/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0x8321;
    private static final int TYPE_CONTENT = 0x8322;


    private final ArrayList<WebPageBean> mList;
    private final Context mContext;
    private final OnListItemClickListener mListener;
    private final LayoutInflater mInflater;

    public MenuListAdapter(ArrayList<WebPageBean> list, Context context, OnListItemClickListener listener) {
        this.mList = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = mInflater.inflate(R.layout.item_menu_header, parent, false);
                return new HeaderVH(view, mListener, mList);
            case TYPE_CONTENT:
                view = mInflater.inflate(R.layout.item_menu_content, parent, false);
                return new ContentVH(view, mListener, mList);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderVH) {
            HeaderVH headerVH = (HeaderVH) holder;
            ViewGroup.LayoutParams params = headerVH.mLayout.getLayoutParams();
            params.height = ScreenUtil.getScreenHeight(mContext) / 4;
            headerVH.mLayout.setLayoutParams(params);
            Glide.with(mContext)
                    .load(mList.get(position).getThumb_url())
                    .apply(new RequestOptions().centerCrop())
                    .into(headerVH.mImageView);
            headerVH.mTvTitle.setText(mList.get(position).getTitle());
        } else if (holder instanceof ContentVH) {
            ContentVH contentVH = (ContentVH) holder;
            Glide.with(mContext)
                    .load(mList.get(position).getThumb_url())
                    .into(contentVH.mImageView);
            contentVH.mTextView.setText(mList.get(position).getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        int minIndex = mList.get(0).getId();
        for (WebPageBean bean : mList) {
            if (bean.getId() < minIndex) {
                minIndex = bean.getId();
            }
        }
        if (mList.get(position).getId() == minIndex)
            return TYPE_HEADER;
        else
            return TYPE_CONTENT;
    }

    private static class HeaderVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnListItemClickListener mListener;

        private final RelativeLayout mLayout;
        private final AppCompatTextView mTvTitle;
        private final AppCompatImageView mImageView;

        private final ArrayList<WebPageBean> mList;

        HeaderVH(View itemView, OnListItemClickListener listener, ArrayList<WebPageBean> list) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mList = list;
            this.mListener = listener;
            this.mLayout = itemView.findViewById(R.id.rl_item_header);
            this.mTvTitle = itemView.findViewById(R.id.tv_item_header_title);
            this.mImageView = itemView.findViewById(R.id.iv_item_header);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onClick(view,mList.get(getLayoutPosition()).getId());
            }
        }
    }

    private static class ContentVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnListItemClickListener mListener;

        private final AppCompatImageView mImageView;
        private final AppCompatTextView mTextView;
        private final ArrayList<WebPageBean> mList;

        ContentVH(View itemView, OnListItemClickListener listener, ArrayList<WebPageBean> list) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mList = list;
            this.mListener = listener;
            this.mImageView = itemView.findViewById(R.id.iv_item_content);
            this.mTextView = itemView.findViewById(R.id.tv_item_content_title);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onClick(view,mList.get(getLayoutPosition()).getId());
            }
        }
    }
}
