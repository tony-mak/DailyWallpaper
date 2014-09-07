package com.madeinhk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.madeinhk.app.SettingsFragment;

/**
 * Created by tony on 24/8/14.
 */
public class KeyValueStorage {
    private static final String PREF_NAME = "app";
    private Context mContext;
    private SharedPreferences mSharedPrefernces;


    public KeyValueStorage(Context context) {
        mContext = context.getApplicationContext();
        mSharedPrefernces = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static final String KEY_DOWNLOAD_TIME = "download_time";
    private static final int NOT_AVAILABLE = -1;

    public void saveDownloadTime(long timeStamp) {
        mSharedPrefernces.edit().putLong(KEY_DOWNLOAD_TIME, timeStamp).apply();
    }

    public long getLastDownloadTime() {
        return mSharedPrefernces.getLong(KEY_DOWNLOAD_TIME, NOT_AVAILABLE);
    }

    private static final String KEY_WALLPAPER_PATH = "wallpaper";

    public void saveWallpaperPath(String path) {
        mSharedPrefernces.edit().putString(KEY_WALLPAPER_PATH, path).apply();
    }

    public String getWallpaperPath() {
        return mSharedPrefernces.getString(KEY_WALLPAPER_PATH, null);
    }

    private static final String KEY_FIRST_RUN = "first_run";

    public void saveFirstRun() {
        mSharedPrefernces.edit().putBoolean(KEY_FIRST_RUN, false).apply();
    }

    public boolean getFirstRun() {
        return mSharedPrefernces.getBoolean(KEY_FIRST_RUN, true);
    }

    public boolean getFetchInBackground() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(SettingsFragment.KEY_ENABLE_SET_WALLPAPER, false);
    }
}