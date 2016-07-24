package com.example.domen.mymanga.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.example.domen.mymanga.Activity.AllMangaActivity;
import com.example.domen.mymanga.Fragment.MangaDetailFragment;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;
import org.json.JSONObject;


/**
 * Created by domen on 23/07/2016.
 */
public class DownloadResultReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("MyMangaBroadcast", "Broadcast ricevuto");

        //Riferimento all'Activity del contesto
        AllMangaActivity myActivity = (AllMangaActivity) context;

        /*
        *   Preparazione del Bundle con le informazioni da passare al Fragment
        */
        Bundle mangaBundle = new Bundle();
        mangaBundle.putString(Contract.Manga.COLUMN_MANGA_ID, intent.getStringExtra(Contract.Manga.COLUMN_MANGA_ID));
        String author;
        String chapter;
        String title;
        String image;

        //Recupero il JSON dall'extra dell'Intent
        String stringJSON = intent.getBundleExtra("myExtraBundle").getString("Result");

        /*
        *   Parsing della Stringa JSON
        */
        try {
            JSONObject mangaJSON = new JSONObject(stringJSON);
            author = mangaJSON.getString("author");
            mangaBundle.putString("author", author);
            chapter = mangaJSON.getString("chapters_len");
            mangaBundle.putString("chapter", chapter);
            title = mangaJSON.getString("title");
            mangaBundle.putString(Contract.Manga.COLUMN_TITLE, title);
            image = myActivity.getString(R.string.im_base_url)+mangaJSON.getString("image");
            mangaBundle.putString(Contract.Manga.COLUMN_IMG, image);

            /*
            *   Creo un'istanza del Fragment con i dati del Bundle creato
            */
            MangaDetailFragment mangaDetailFragment = MangaDetailFragment.newInstance(mangaBundle);
            FragmentManager fm = myActivity.getSupportFragmentManager();

            /*
            *   Avvio il fragment dei dettagli
            *   Smartphone -> nello stack
            *   Tablet -> Rimpiazza quella giù visualizzata
            */
            if(!myActivity.isTablet())
                fm.beginTransaction().replace(R.id.container,mangaDetailFragment).addToBackStack("MyManga").commit();
            else
                fm.beginTransaction().replace(R.id.detail_view,mangaDetailFragment).commit();

        }catch (Exception e){

            Log.e("MyMangaBroadcast", e.getMessage());
        }

    }

}
