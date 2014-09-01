package com.madeinhk.parser;

import android.content.Context;

import com.madeinhk.dailywallpaper.R;

/**
 * Created by tony on 24/8/14.
 */
public class WallpaperInfo {
    private String mLowResUrl;
    private String mHighResUrl;
    private String mDescription;

    public WallpaperInfo(String lowResUrl, String highResUrl, String description) {
        mLowResUrl = lowResUrl;
        mHighResUrl = highResUrl;
        mDescription = description;
    }

    public String getUrl(Context context) {
        boolean largeScreen = context.getResources().getBoolean(R.bool.screen_large);
        if (largeScreen) {
            return mHighResUrl;
        } else {
            return mLowResUrl;
        }
    }

    public String getDescription() {
        return mDescription.replace("©", "");
    }
}
