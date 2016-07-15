package com.example.domen.mymanga.Activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.domen.mymanga.Models.AllMangaAdapter;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Utils.AutoSpanRecyclerView;
import com.example.domen.mymanga.Utils.Manga;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

            InputStream jsonStream = getResources().openRawResource(R.raw.manga_default);
            String mangaJSON;
            ArrayList<Manga> allManga = new ArrayList<>();
            String imURI = "https://cdn.mangaeden.com/mangasimg/200x/";

            ArrayList<ContentValues> mangaContList = new ArrayList<>();
            try {
                mangaJSON = readIt(jsonStream);
                JSONObject jObj = new JSONObject(mangaJSON);
                JSONArray mangaList = jObj.getJSONArray("manga");
                for(int i = 0; i < mangaList.length(); i++ ){
                    JSONObject manga = mangaList.getJSONObject(i);
                    ContentValues mangaCont = new ContentValues();
                    mangaCont.put(Contract.Manga.COLUMN_MANGA_ID,manga.getString("i"));
                    mangaCont.put(Contract.Manga.COLUMN_TITLE,manga.getString("t"));
                    mangaCont.put(Contract.Manga.COLUMN_IMG,imURI+manga.getString("im"));
                    mangaContList.add(mangaCont);
                }

                ContentValues[] simpleArray = new ContentValues[mangaContList.size()];
                getContentResolver().bulkInsert(Uri.parse(Contract.BASE_CONTENT_URI+"/"+Contract.Manga.TABLE_NAME) ,
                        mangaContList.toArray(simpleArray));

            }catch (Exception e){
                Log.e("MyMangaDb", "Eccezione: "+e.toString());
            }

            Log.v("MyMangaActivity", mangaContList.toString());
        }else
            Log.v("MyMangaActivity", "Non è la prima creazione");


        getSupportLoaderManager().initLoader(1, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
        searchView.setSearchableInfo(
                        searchManager.getSearchableInfo(cn));
        //searchView.setSearchableInfo(
        //        searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                AllMangaActivity.this,
                Uri.parse(Contract.BASE_CONTENT_URI+"/"+Contract.Manga.TABLE_NAME),
                new String[]{
                        Contract.Manga.COLUMN_MANGA_ID,
                        Contract.Manga.COLUMN_TITLE,
                        Contract.Manga.COLUMN_IMG
                },
                null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        AllMangaAdapter myAdapter = new AllMangaAdapter(data);
        allMangaListView.setAdapter(myAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {

        Reader reader = null;
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }

}
