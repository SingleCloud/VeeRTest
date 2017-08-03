package com.genlan.veertest.model.constant;

/**
 * Description
 * Author Genlan
 * Date 2017/8/3
 */

public class DatabaseConstant {

    public static final String DATABASE_NAME = "db_VeeR_test.db";

    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "table_news";

    public static final String COLUMN_ID = "column_id";

    public static final String COLUMN_TITLE = "column_title";

    public static final String COLUMN_CATEGORY = "column_category";

    public static final String COLUMN_THUMB_URL = "column_thumb_url";

    public static final String COLUMN_PAGE_URL = "column_page_url";


    public static final String STAT_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_THUMB_URL + " TEXT," +
                    COLUMN_PAGE_URL + " TEXT" +
                    " )";
}
