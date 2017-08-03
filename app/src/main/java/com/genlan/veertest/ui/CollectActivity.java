package com.genlan.veertest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.genlan.veertest.R;
import com.genlan.veertest.controller.CollectListAdapter;
import com.genlan.veertest.model.db.DBOperateImpl;
import com.genlan.veertest.model.db.WebPageBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Description  收藏页面
 * Author Genlan
 * Date 2017/8/3
 */

public class CollectActivity extends AppCompatActivity {


    /**
     * 数据库查询数据监听
     * */
    private Observable<ArrayList<WebPageBean>> mQueryAllOb;


    private ArrayList<WebPageBean> mList;
    private CollectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        mList = new ArrayList<>();
        initTitle();
        initListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQueryAllOb = DBOperateImpl.getInstance(this).doQueryAll();
        mQueryAllOb.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<WebPageBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<WebPageBean> webPageBeen) {
                        mList.clear();
                        mList.addAll(webPageBeen);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBOperateImpl.getInstance(this).close();
        mQueryAllOb.unsubscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_collect);
        toolbar.setTitle("收藏列表");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_collect);
        mAdapter = new CollectListAdapter(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

}
