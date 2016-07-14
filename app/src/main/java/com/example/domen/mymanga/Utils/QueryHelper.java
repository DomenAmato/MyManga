package com.example.domen.mymanga.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by domen on 14/07/2016.
 */
public class QueryHelper {

    public static final int setBulkInsert(Context context, String table, SQLiteDatabase myDb, ContentValues[] values){

        int returnCount = 0;

        myDb.beginTransaction();

        try {

            for (ContentValues value : values) {

                final long _id = myDb.replace(table, null, value);

                if (_id != -1)
                    returnCount++;
            }

            myDb.setTransactionSuccessful();

        } finally {
            myDb.endTransaction();
        }

        return returnCount;
    }
}
