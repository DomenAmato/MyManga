package com.example.domen.mymanga.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import com.example.domen.mymanga.Activity.AllMangaActivity;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by domen on 23/07/2016.
 */
public class MangaDetailService extends IntentService {

    public MangaDetailService(){
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
        *   Recupero l'id del manga dall'intent
        */
        String id = intent.getStringExtra(Contract.Manga.COLUMN_MANGA_ID);
        String result;

        /*
        *   Scarico un JSON tramite API dal sito mangaeden
        */
        try {
            String stringUrl = getBaseContext().getString(R.string.mangaeden_api_detail)+id;
            Log.v("MyMangaService", "Url is "+stringUrl);
            ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                result = downloadUrl(stringUrl);

                /*
                *   Invio i risultati tramite Intent ad un Broadcast Receiver
                */
                Intent resultBroadcast = new Intent();
                resultBroadcast.setAction(getApplicationContext().getString(R.string.broadcast_action));
                Bundle extraBundle = new Bundle();
                extraBundle.putString("Result", result);
                resultBroadcast.putExtra("myExtraBundle", extraBundle);
                resultBroadcast.putExtra(Contract.Manga.COLUMN_MANGA_ID, id);
                sendBroadcast(resultBroadcast);
            }
        }catch (Exception e){
            Log.e("MyMangaService", e.getMessage());
        }
    }

    /*
    *   Metodo usato per il download dei dati dall'url passata come parametro
    */
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {

            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            Log.d("MyMangaService", "The response is: " + response);
            is = conn.getInputStream();

            String contentAsString = AllMangaActivity.readIt(is);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
