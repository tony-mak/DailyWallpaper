package com.madeinhk.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.madeinhk.utils.AlarmHelper;
import com.madeinhk.utils.KeyValueStorage;

/**
 * Created by tony on 24/8/14.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            KeyValueStorage storage = new KeyValueStorage(context);
            if (storage.getFetchInBackground()) {
                AlarmHelper helper = new AlarmHelper(context);
                helper.scheduleFetchWallpaperTask();
            }
        }
    }
}
