package com.example.domen.mymanga.Models;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.Models.MyMangaDb;

public class MyMangaProvider extends ContentProvider {

    private MyMangaDb db;

    private static final int CODE_All_MANGA = 100;
    private static final int CODE_MANGA = 101;
    private static final int CODE_MANGA_PROVA = 500;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.Manga.TABLE_NAME, CODE_All_MANGA);
        matcher.addURI(authority, Contract.Manga.TABLE_NAME + "/#", CODE_MANGA);
        matcher.addURI(authority, Contract.Manga.TABLE_NAME + "/insertProva", CODE_MANGA_PROVA);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        db = new MyMangaDb(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String whereClause, String[] selectionArgs, String order) {

        Cursor result = null;

        switch (sUriMatcher.match(uri)) {
            case CODE_All_MANGA:
                //String[] columns = {Contract.Manga.COLUMN_ID, Contract.Manga.COLUMN_TITLE, Contract.Manga.COLUMN_IMG};
                result = db.getReadableDatabase()
                        .query(Contract.Manga.TABLE_NAME,
                                projection,
                                whereClause,
                                selectionArgs,
                                null,
                                null,
                                order);
                break;
            case CODE_MANGA:
                result = db.getReadableDatabase()
                        .query(Contract.Manga.TABLE_NAME,
                                projection,
                                Contract.Manga.TABLE_NAME + "." + Contract.Manga.COLUMN_ID + " = ?",
                                selectionArgs,
                                null,
                                null,
                                order);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Uri result = null;

        switch (sUriMatcher.match(uri))
        {
            case CODE_MANGA_PROVA:
                db.getWritableDatabase().insert(Contract.Manga.TABLE_NAME, null , contentValues);
                break;
        }

        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase myDb = db.getWritableDatabase();
        myDb.beginTransaction();

        int returnCount = 0;

        switch (sUriMatcher.match(uri)) {
            case CODE_All_MANGA:
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        Log.i("MyMangaDB", "bulk insert: " + uri.toString() + ", " + returnCount);

        if (returnCount > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return returnCount;
    }
}