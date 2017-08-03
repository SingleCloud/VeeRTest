package com.genlan.veertest.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.genlan.veertest.model.constant.DatabaseConstant;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Description
 * Author Genlan
 * Date 2017/8/3
 */

public class DBOperateImpl implements IDBOperate {

    private static DBOperateImpl sInstance;
    private DBHelper mDBHelper;

    private DBOperateImpl(Context context) {
        if (sInstance != null)
            throw new RuntimeException();
        mDBHelper = new DBHelper(context);
    }

    public static DBOperateImpl getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DBOperateImpl.class) {
                if (sInstance == null) {
                    sInstance = new DBOperateImpl(context);
                }
            }
        }
        return sInstance;
    }

    public void close() {
        mDBHelper.close();
        sInstance = null;
    }

    @Override
    public Observable<ArrayList<WebPageBean>> doQueryAll() {
        Observable<ArrayList<WebPageBean>> ob = Observable.create(e -> {
            ArrayList<WebPageBean> list = null;
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseConstant.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    WebPageBean bean = new WebPageBean();
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_TITLE));
                    String category = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_CATEGORY));
                    String thumbUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_THUMB_URL));
                    String pageUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_PAGE_URL));

                    bean.setId(id);
                    bean.setTitle(title);
                    bean.setCategory(category);
                    bean.setThumb_url(thumbUrl);
                    bean.setPage_url(pageUrl);
                    list.add(bean);
                }
            }
            if (cursor != null)
                cursor.close();
            e.onNext(list);
            e.onComplete();
        });
        ob.subscribeOn(Schedulers.io());
        return ob;
    }

    @Override
    public Observable<WebPageBean> doQueryById(int id) {
        Observable<WebPageBean> ob = Observable.create(e -> {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseConstant.TABLE_NAME,
                    null,
                    DatabaseConstant.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);
            WebPageBean bean = null;
            while (cursor.moveToNext()) {
                bean = new WebPageBean();
                int idNew = cursor.getInt(cursor.getColumnIndex(DatabaseConstant.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_TITLE));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_CATEGORY));
                String thumbUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_THUMB_URL));
                String pageUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstant.COLUMN_PAGE_URL));

                bean.setId(idNew);
                bean.setTitle(title);
                bean.setCategory(category);
                bean.setThumb_url(thumbUrl);
                bean.setPage_url(pageUrl);
            }
            cursor.close();
            if (bean != null)
                e.onNext(bean);
            e.onComplete();
        });
        ob.subscribeOn(Schedulers.io());
        return ob;
    }

    @Override
    public Observable<Object> doInsert(WebPageBean bean) {
        Observable<Object> ob = Observable.create(e -> {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseConstant.COLUMN_ID, bean.getId());
            values.put(DatabaseConstant.COLUMN_CATEGORY, bean.getCategory());
            values.put(DatabaseConstant.COLUMN_PAGE_URL, bean.getPage_url());
            values.put(DatabaseConstant.COLUMN_THUMB_URL, bean.getThumb_url());
            values.put(DatabaseConstant.COLUMN_TITLE, bean.getTitle());
            db.insert(DatabaseConstant.TABLE_NAME, null, values);
            e.onComplete();
        });
        ob.subscribeOn(Schedulers.io());
        return ob;
    }

    @Override
    public Observable<Object> doInsert(ArrayList<WebPageBean> list) {
        Observable<Object> ob = Observable.create(e -> {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.beginTransaction();
            for (WebPageBean bean : list) {
                ContentValues values = new ContentValues();
                values.put(DatabaseConstant.COLUMN_ID, bean.getId());
                values.put(DatabaseConstant.COLUMN_CATEGORY, bean.getCategory());
                values.put(DatabaseConstant.COLUMN_PAGE_URL, bean.getPage_url());
                values.put(DatabaseConstant.COLUMN_THUMB_URL, bean.getThumb_url());
                values.put(DatabaseConstant.COLUMN_TITLE, bean.getTitle());
                db.insert(DatabaseConstant.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
//            db.endTransaction();
            e.onComplete();
        });
        ob.subscribeOn(Schedulers.io());
        return ob;
    }

    @Override
    public Observable<Object> doDeleteById(int id) {
        Observable<Object> ob = Observable.create(e -> {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.delete(DatabaseConstant.TABLE_NAME, DatabaseConstant.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            e.onComplete();
        });
        ob.subscribeOn(Schedulers.io());
        return ob;
    }
}
