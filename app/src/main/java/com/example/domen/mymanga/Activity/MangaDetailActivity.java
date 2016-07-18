package com.example.domen.mymanga.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.domen.mymanga.R;

/**
 * Created by domen on 18/07/2016.
 */
public class MangaDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);
        Intent i = getIntent();

        Log.v("MyMangaDetail", "ID: "+i.getStringExtra("manga_id"));
    }
}
