package com.example.domen.mymanga.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by domen on 23/07/2016.
 */
public class DownloadResultReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("MyMangaBroadcast", "Broadcast ricevuto");
    }

}
