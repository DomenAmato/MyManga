package com.example.domen.mymanga.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.domen.mymanga.BroadcastReceiver.DownloadResultReceiver;
import com.example.domen.mymanga.Fragment.MangaDetailFragment;
import com.example.domen.mymanga.Fragment.MangaListFragment;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import com.example.domen.mymanga.Service.MangaDetailService;
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

/**
 * Created by domen on 20/07/2016.
 */
public class AllMangaActivity extends AppCompatActivity implements MangaListFragment.Communication{

    private boolean isTablet;

    public boolean isTablet() {
        return isTablet;
    }

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    private MangaDetailFragment mangaDetailFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_manga);

        FrameLayout tabletView;
        fm = getSupportFragmentManager();

        fillDb();

        DownloadResultReceiver myReceiver = new DownloadResultReceiver();
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("com.example.domen.mymanga.DOWNLOAD_COMPLETE");

        registerReceiver(myReceiver, myFilter);

        tabletView = (FrameLayout) findViewById(R.id.list_view);
        if(tabletView != null) isTablet = true;

        if(savedInstanceState == null && !isTablet){

            Log.v("MyMangaActivity", "Sono in uno Smartphone o Tablet Portrait");
            MangaListFragment lf = new MangaListFragment();
            fm.beginTransaction().replace(R.id.container,lf).addToBackStack("MyManga").commit();

        }else if(isTablet){
            Bundle mangaBundle = new Bundle();
            mangaBundle.putString(Contract.Manga.COLUMN_MANGA_ID, "");
            mangaDetailFragment = MangaDetailFragment.newInstance(mangaBundle);
            MangaListFragment lf = new MangaListFragment();

            fm.beginTransaction().replace(R.id.list_view,lf).commit();
            fm.beginTransaction().replace(R.id.detail_view,mangaDetailFragment).commit();
        }



    }

    private void fillDb(){

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

    @Override
    public void onItemChoosed(String id) {
        /*
        //if(mangaDetailFragment==null)
        Bundle mangaBundle = new Bundle();
        mangaBundle.putString(Contract.Manga.COLUMN_MANGA_ID, id);

        mangaDetailFragment = MangaDetailFragment.newInstance(mangaBundle);

        if(isTablet) {
            fm.beginTransaction().replace(R.id.detail_view, mangaDetailFragment).commit();
        }
        else{
            fm.beginTransaction().replace(R.id.container,mangaDetailFragment).commit();
*/
        Bundle mangaBundle = new Bundle();
        mangaBundle.putString(Contract.Manga.COLUMN_MANGA_ID,id);



        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, MangaDetailService.class);
        intent.putExtra(Contract.Manga.COLUMN_MANGA_ID, id);

        startService(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(fm.getBackStackEntryCount()==0)
            finish();
    }
}
