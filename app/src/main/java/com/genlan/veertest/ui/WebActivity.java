package com.genlan.veertest.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.genlan.veertest.R;
import com.genlan.veertest.model.constant.IntentConstant;
import com.genlan.veertest.model.db.DBOperateImpl;
import com.genlan.veertest.model.db.WebPageBean;
import com.genlan.veertest.util.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Description  网页浏览器
 * Author Genlan
 * Date 2017/8/2
 */

public class WebActivity extends AppCompatActivity {

    /**
     * 待访问网页
     */
    private String mURL;

    /**
     * webView
     * power by tencent X5
     */
    private X5WebView mWebView;
    /**
     * 浏览器进度条
     */
    private ProgressBar mProgressBar;

    /**
     * 收藏按键
     */
    private MenuItem mAddItem;

    /**
     * 本次访问数据的实体
     * 用于在取消收藏时的数据删除和收藏时的ID
     */
    private WebPageBean mIntentData;

    private ArrayList<Observable> mObservables;

    private Toolbar mToolbar;

    /**
     * 是否被收藏过
     */
    private boolean sIsExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mObservables = new ArrayList<>();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_web);
        mProgressBar.setVisibility(View.GONE);
        mToolbar = (Toolbar) findViewById(R.id.tb_web);
        setSupportActionBar(mToolbar);
        mWebView = (X5WebView) findViewById(R.id.wv_web);
        mIntentData = (WebPageBean) getIntent().getSerializableExtra(IntentConstant.INTENT_WEB_SERIALIZABLE);
        mURL = mIntentData.getPage_url();
        sIsExist = false;
        initTitle();
        initWeb();
        queryData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
        mObservables.forEach(observable -> observable.unsubscribeOn(AndroidSchedulers.mainThread()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        mAddItem = menu.findItem(R.id.menu_add);
        return true;
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
        Toolbar.OnMenuItemClickListener onMenuItemClick = menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_add:
                    if (!sIsExist) {
                        Observable<Object> ob = DBOperateImpl.getInstance(WebActivity.this).doInsert(mIntentData);
                        ob
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SimpleObserver<Object>() {
                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        Toast.makeText(WebActivity.this, "收藏失败!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Toast.makeText(WebActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                                        sIsExist = !sIsExist;
                                        mAddItem.setTitle(getString(R.string.menu_minus));

                                    }
                                });
                        mObservables.add(ob);
                    } else {
                        Observable<Object> ob = DBOperateImpl.getInstance(WebActivity.this).doDeleteById(mIntentData.getId());
                        ob
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SimpleObserver<Object>() {
                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        Toast.makeText(WebActivity.this, "取消收藏失败!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Toast.makeText(WebActivity.this, "取消收藏成功!", Toast.LENGTH_SHORT).show();
                                        sIsExist = !sIsExist;
                                        mAddItem.setTitle(getString(R.string.menu_add));
                                    }
                                });
                        mObservables.add(ob);
                    }
                    break;
                case R.id.menu_refresh:
                    mWebView.loadUrl(mURL);
                    break;
                default:
                    break;
            }
            return true;
        };

        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initWeb() {
        mWebView.loadUrl(mURL);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(i);
                if (i == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                mToolbar.setTitle(s);
            }
        });
    }

    private void queryData() {
        Observable<WebPageBean> ob = DBOperateImpl.getInstance(this).doQueryById(mIntentData.getId());
        ob.observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleObserver<WebPageBean>() {
            @Override
            public void onNext(@NonNull WebPageBean bean) {
                sIsExist = null != bean;
            }
        });
        mObservables.add(ob);
    }

    private abstract class SimpleObserver<T> implements Observer<T> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull T t) {

        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
