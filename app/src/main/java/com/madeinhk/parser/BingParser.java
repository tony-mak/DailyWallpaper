package com.madeinhk.parser;


import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tony on 24/8/14.
 */
public class BingParser {
    private static final String BING_URL = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

    /**
     * @return url of the wallpaper
     */
    public WallpaperInfo parse() throws ParseException {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(BING_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = IOUtils.toString(in, "UTF-8");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject imageObject = jsonObject.getJSONArray("images").getJSONObject(0);
            String lowResUrl =  "https://www.bing.com/" + imageObject.getString("url");
            String highResUrl = lowResUrl.replace("_1366x768", "_1920x1080");
            String description = imageObject.getString("copyright");
            WallpaperInfo wallpaperInfo = new WallpaperInfo(lowResUrl, highResUrl, description);
            return wallpaperInfo;
        } catch (IOException | JSONException ex) {
            throw new ParseException(ex.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
