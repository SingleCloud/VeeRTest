package com.genlan.veertest.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.genlan.veertest.model.constant.DatabaseConstant;

/**
 * Description
 * Author Genlan
 * Date 2017/8/3
 */

class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "genlanVeeR";

    DBHelper(Context context) {
        super(context, DatabaseConstant.DATABASE_NAME, null, DatabaseConstant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            Log.i(TAG, "onCreate: succeed");
            db.execSQL(DatabaseConstant.STAT_CREATE_TABLE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO no update
    }
}
