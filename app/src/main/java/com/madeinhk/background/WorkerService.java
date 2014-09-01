package com.madeinhk.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.madeinhk.parser.BingParser;
import com.madeinhk.parser.ParseException;
import com.madeinhk.parser.WallpaperInfo;
import com.madeinhk.utils.DownloadHelper;
import com.madeinhk.utils.KeyValueStorage;
import com.madeinhk.utils.WallpaperHelper;

import java.io.File;
import java.io.IOException;

public class WorkerService extends IntentService {
    public static final String ACTION_FETCH_WALLPAPER = "com.madeinhk.fetch_wallpaper";
    public static final String ACTION_FETCH_WALLPAPER_DONE = "com.madeinhk.fetch_wallpaper_done";
    public static final String EXTRA_WALLPAPER_URI = "wallpaper";
    public WorkerService(String name) {
        super(WorkerService.class.getName());
    }

    public WorkerService() {
        super(WorkerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_FETCH_WALLPAPER:
                Intent doneIntent = new Intent(ACTION_FETCH_WALLPAPER_DONE);
                File wallpaperFile = fetchWallpaper();
                if (wallpaperFile != null) {
                    KeyValueStorage storage = new KeyValueStorage(this);
                    storage.saveDownloadTime(System.currentTimeMillis());
                    storage.saveWallpaperPath(wallpaperFile.getAbsolutePath());
                    doneIntent.putExtra(EXTRA_WALLPAPER_URI, Uri.fromFile(wallpaperFile));
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(doneIntent);
                AlarmReceiver.completeWakefulIntent(intent);
                break;
        }
    }

    private File fetchWallpaper() {
        BingParser bingParser = new BingParser();
        try {
            WallpaperInfo wallpaperInfo = bingParser.parse();
            DownloadHelper downloadHelper = new DownloadHelper(WorkerService.this);
            File wallpaperFile = downloadHelper.startDownload(wallpaperInfo.getUrl(this));
            if (wallpaperFile != null) {
                WallpaperHelper wallpaperHelper = new WallpaperHelper(this);
                wallpaperHelper.setWallpaper(wallpaperFile);
                ExifInterface exifInterface = new ExifInterface(wallpaperFile.getAbsolutePath());
                exifInterface.setAttribute("UserComment", wallpaperInfo.getDescription());

                exifInterface.saveAttributes();
                exifInterface = new ExifInterface(wallpaperFile.getAbsolutePath());
                return wallpaperFile;
            }
        } catch (ParseException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Intent getFetchWallpaperIntent(Context context) {
        Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(WorkerService.ACTION_FETCH_WALLPAPER);
        return intent;
    }

}
