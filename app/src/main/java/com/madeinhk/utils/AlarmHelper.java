package com.madeinhk.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.madeinhk.background.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by tony on 24/8/14.
 */
public class AlarmHelper {
    private Context mContext;

    private static final int REQUEST_CODE = 1;
    private static final long DAY_IN_MILLISECOND = 24 * 60 * 60 * 1000;

    public AlarmHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    // TODO: More intelligence later
    public static long getFetchWallpaperTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private PendingIntent getPendingIntent() {
        Intent broadcastIntent = new Intent(mContext, AlarmReceiver.class);
        return PendingIntent.getBroadcast(mContext, REQUEST_CODE, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void scheduleFetchWallpaperTask() {
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        long timeToFetch = getFetchWallpaperTime();
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeToFetch, DAY_IN_MILLISECOND, getPendingIntent());
    }

    public void canelFetchWallpaperTask() {
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getPendingIntent());
    }
}
