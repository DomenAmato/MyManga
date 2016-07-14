package com.example.domen.mymanga.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.domen.mymanga.Utils.QueryContainer;

/**
 * Created by domen on 14/07/2016.
 */
public class MyMangaDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "mymanga.db";
    private static final int DB_VERSION = 1;
    private Context currContext;

    public MyMangaDb(Context context){

        super(context, DB_NAME, null, DB_VERSION);
        currContext = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(QueryContainer.MANGA_STATEMENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
