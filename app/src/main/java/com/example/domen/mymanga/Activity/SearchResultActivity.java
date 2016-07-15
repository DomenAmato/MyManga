package com.example.domen.mymanga.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.domen.mymanga.Models.AllMangaAdapter;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;

import java.util.ArrayList;

/**
 * Created by domen on 15/07/2016.
 */
public class SearchResultActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {


    private AutoSpanRecyclerView resultView;
    private AllMangaAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_search_result);

        resultView = (AutoSpanRecyclerView) findViewById(R.id.result_list);
        resultView.setGridLayoutManager(RecyclerView.VERTICAL, R.layout.all_manga_item, 1);

        myAdapter = new AllMangaAdapter(null);
        resultView.setAdapter(myAdapter);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Bundle queryBundle = new Bundle();
            queryBundle.putString("Query", query);
            getSupportLoaderManager().initLoader(2, queryBundle, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = {"'%"+args.getString("Query")+"%'"};
        Log.v("MyMangaLoader", "Sono nel search Loader");
        return new CursorLoader(
                SearchResultActivity.this,
                Uri.parse(Contract.BASE_CONTENT_URI+"/"+Contract.Manga.TABLE_NAME+"/"+args.getString("Query")),
                new String[]{
                        Contract.Manga.COLUMN_MANGA_ID,
                        Contract.Manga.COLUMN_TITLE,
                        Contract.Manga.COLUMN_IMG
                },
                null, selectionArgs, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       Log.v("MyMangaLoader", ""+data.getCount());
        myAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);
    }
}
