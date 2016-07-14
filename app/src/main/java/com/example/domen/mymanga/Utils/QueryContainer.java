package com.example.domen.mymanga.Utils;

import android.util.Log;

import com.example.domen.mymanga.Models.Contract;

import java.util.ArrayList;

/**
 * Created by domen on 14/07/2016.
 */
public class QueryContainer {

    private static final String START_STATEMENTS = "create table if not exists ";

    public static final String MANGA_STATEMENTS = START_STATEMENTS + Contract.Manga.TABLE_NAME + "(" +
            Contract.Manga.COLUMN_ID + " integer primary key, " +
            Contract.Manga.COLUMN_MANGA_ID + " varchar(20) not null," +
            Contract.Manga.COLUMN_IMG + " varchar(200), " +
            Contract.Manga.COLUMN_TITLE + " varchar(50));";


    public static String createFirstInsert(ArrayList<Manga> manga){

        String query = "INSERT INTO "+Contract.Manga.TABLE_NAME+ "('"+
                Contract.Manga.COLUMN_MANGA_ID+"','"+Contract.Manga.COLUMN_IMG+"','"+Contract.Manga.COLUMN_TITLE+"') "
                + "VALUES('";

        for (int i = 0; i < manga.size(); i++){
            if(i==0)
                query= query+manga.get(i).getId()+"','"+manga.get(i).getImgUrl()+"','"+manga.get(i).getTitle()+"')";
            else
                query= query+",('"+manga.get(i).getId()+"','"+manga.get(i).getImgUrl()+"','"+manga.get(i).getTitle()+"')";
        }
        Log.v("MyMangaInsert", query);
        return query;
    }
}
