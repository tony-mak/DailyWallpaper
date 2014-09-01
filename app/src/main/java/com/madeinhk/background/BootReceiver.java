package com.madeinhk.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.madeinhk.utils.AlarmHelper;

/**
 * Created by tony on 24/8/14.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AlarmHelper helper = new AlarmHelper(context);
            helper.scheduleFetchWallpaperTask();
        }
    }
}
