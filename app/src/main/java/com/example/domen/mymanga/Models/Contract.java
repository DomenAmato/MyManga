package com.example.domen.mymanga.Models;

import android.net.Uri;

/**
 * Created by domen on 14/07/2016.
 */
public class Contract {

    public static final String CONTENT_AUTHORITY = "it.mycompany.domen.mymanga.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public class Manga{

        public static final String TABLE_NAME = "manga";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MANGA_ID = TABLE_NAME + "_id";
        //public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = TABLE_NAME + "_title";
        public static final String COLUMN_IMG = TABLE_NAME + "_img";

    }
}
