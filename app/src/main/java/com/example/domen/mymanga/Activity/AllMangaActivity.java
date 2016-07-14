package com.example.domen.mymanga.Activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.domen.mymanga.Models.AllMAngaAdapter;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.Models.MyMangaDb;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;
import com.example.domen.mymanga.Utils.GridAutofitLayoutManager;

public class AllMangaActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    private AutoSpanRecyclerView allMangaListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_manga);

        allMangaListView = (AutoSpanRecyclerView)findViewById(R.id.all_manga_list);
        allMangaListView.setGridLayoutManager(RecyclerView.VERTICAL, R.layout.all_manga_item, 1);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(!pref.getBoolean("NotFirst", false)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("NotFirst", true);
            editor.commit();



            Log.v("MyMangaActivity", "Prima creazione");
        }else
            Log.v("MyMangaActivity", "Non è la prima creazione");


        getSupportLoaderManager().initLoader(1, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                AllMangaActivity.this,
                Uri.parse(Contract.BASE_CONTENT_URI+"/"+Contract.Manga.TABLE_NAME),
                new String[]{
                        Contract.Manga.COLUMN_TITLE,
                        Contract.Manga.COLUMN_IMG
                },
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        AllMAngaAdapter myAdapter = new AllMAngaAdapter(data);
        allMangaListView.setAdapter(myAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
