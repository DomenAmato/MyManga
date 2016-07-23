package com.example.domen.mymanga.BroadcastReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.domen.mymanga.Activity.AllMangaActivity;
import com.example.domen.mymanga.Fragment.MangaDetailFragment;
import com.example.domen.mymanga.Fragment.MangaListFragment;
import com.example.domen.mymanga.Models.Contract;
import com.example.domen.mymanga.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by domen on 23/07/2016.
 */
public class DownloadResultReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("MyMangaBroadcast", "Broadcast ricevuto");
        AllMangaActivity myActivity = (AllMangaActivity) context;
        /*NOT WORK

        intent.getStringExtra("Result");
        Activity myActivity = (Activity)context;
        FrameLayout container = (FrameLayout) myActivity.findViewById(R.id.detail_container);

        LayoutInflater lf = LayoutInflater.from(context);
        View root = lf.inflate(R.layout.fragment_detail, container);



        TextView author = (TextView) root.findViewById(R.id.manga_detail_author);
        Log.v("MyMangaBroadcast", author.toString());

        root.findViewById(R.id.not_selected_layer).setVisibility(View.GONE);
        */

        Bundle mangaBundle = new Bundle();
        mangaBundle.putString(Contract.Manga.COLUMN_MANGA_ID, intent.getStringExtra(Contract.Manga.COLUMN_MANGA_ID));
        String author;
        String chapter;
        String title;
        String image;
        String stringJSON = intent.getBundleExtra("myExtraBundle").getString("Result");
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


            MangaDetailFragment mangaDetailFragment = MangaDetailFragment.newInstance(mangaBundle);

            FragmentManager fm = myActivity.getSupportFragmentManager();

            if(!myActivity.isTablet())
                fm.beginTransaction().replace(R.id.container,mangaDetailFragment).addToBackStack("MyManga").commit();
            else
                fm.beginTransaction().replace(R.id.detail_view,mangaDetailFragment).commit();

        }catch (Exception e){

            Log.e("MyMangaBroadcast", e.getMessage());
        }

    }

}
