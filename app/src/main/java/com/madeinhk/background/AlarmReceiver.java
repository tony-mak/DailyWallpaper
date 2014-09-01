package com.madeinhk.background;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.madeinhk.utils.AlarmHelper;
import com.madeinhk.utils.KeyValueStorage;

/**
 * Created by tony on 24/8/14.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        KeyValueStorage storage = new KeyValueStorage(context);
        long lastDownloadTime = storage.getLastDownloadTime();
        long fetchWallpaperTime = AlarmHelper.getFetchWallpaperTime();
        if (fetchWallpaperTime > lastDownloadTime) {
            Intent serviceIntent = WorkerService.getFetchWallpaperIntent(context);
            WakefulBroadcastReceiver.startWakefulService(context, serviceIntent);
        }
    }
}