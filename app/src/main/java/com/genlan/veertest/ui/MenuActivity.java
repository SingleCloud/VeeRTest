package com.genlan.veertest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.genlan.veertest.R;
import com.genlan.veertest.biz.net.HttpReal;
import com.genlan.veertest.controller.MenuListAdapter;
import com.genlan.veertest.model.db.DBOperateImpl;
import com.genlan.veertest.model.db.WebPageBean;
import com.genlan.veertest.model.net.ResponseBean;
import com.genlan.veertest.model.constant.IntentConstant;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Description  主菜单
 * Author Genlan
 * Date 2017/8/2
 */

public class MenuActivity extends AppCompatActivity {

    /**
     * 适配器对像链
     * */
    private ArrayList<WebPageBean> mList;

    /**
     * http请求监听
     * */
    private Observer<ResponseBean> mObserver;

    private SwipeRefreshLayout mSRLMenu;
    private MenuListAdapter mAdapter;
    private ArrayList<Observable> mObservables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mList = new ArrayList<>();
        mObservables = new ArrayList<>();
        initObserver();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doHttpRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBOperateImpl.getInstance(this).close();
        mObservables.forEach(observable -> observable.unsubscribeOn(AndroidSchedulers.mainThread()));
    }

    private void initView() {
        RecyclerView mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        mRvMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_menu);
        setSupportActionBar(toolbar);
        mAdapter = new MenuListAdapter(mList, this, (view, id) -> {
            Intent intent = new Intent();
            intent.setClass(MenuActivity.this, WebActivity.class);
            WebPageBean bean = null;
            for (WebPageBean temp : mList) {
                if (temp.getId() == id) {
                    bean = temp;
                }
            }
            intent.putExtra(IntentConstant.INTENT_WEB_SERIALIZABLE, bean);
            startActivity(intent);
        });
        mRvMenu.setAdapter(mAdapter);
        mSRLMenu = (SwipeRefreshLayout) findViewById(R.id.srl_menu);
        mSRLMenu.setOnRefreshListener(this::doHttpRequest);

        findViewById(R.id.btn_query).setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CollectActivity.class);
            startActivity(intent);
        });
    }

    private void initObserver() {
        mObserver = new SimpleObserver<ResponseBean>() {

            @Override
            public void onNext(ResponseBean responseBean) {
                mList.clear();
                mList.addAll(responseBean.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MenuActivity.this, "网络异常，请下拉刷新", Toast.LENGTH_LONG).show();
                if (mSRLMenu.isRefreshing()) {
                    mSRLMenu.setRefreshing(false);
                }
            }

            @Override
            public void onComplete() {
                if (mSRLMenu.isRefreshing()) {
                    mSRLMenu.setRefreshing(false);
                }
            }
        };
    }

    private void doHttpRequest() {
        Observable<ResponseBean> ob = HttpReal.doGet();
        mObservables.add(ob);
        ob.observeOn(AndroidSchedulers.mainThread()).subscribe(mObserver);
    }

    private abstract class SimpleObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
