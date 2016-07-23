package com.example.domen.mymanga.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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

        String id = intent.getStringExtra(Contract.Manga.COLUMN_MANGA_ID);
        String result;
        try {
            String stringUrl = getBaseContext().getString(R.string.mangaeden_api_detail)+id;
            Log.v("MyMangaService", "Url is "+stringUrl);
            ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                result = downloadUrl(stringUrl);
                //Log.v("MyMangaService", result);
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

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("MyMangaService", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
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
