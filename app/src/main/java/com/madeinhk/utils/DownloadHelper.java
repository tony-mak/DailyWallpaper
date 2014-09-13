package com.madeinhk.utils;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tony on 24/8/14.
 */
public class DownloadHelper {
    private Context mContext;

    public DownloadHelper(Context context) {
        mContext = context.getApplicationContext();
    }


    public File startDownload(String urlString) throws IOException {
        InputStream in = null;
        FileOutputStream fos = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            String fileName = FilenameUtils.getName(urlString);
            File destinationFile = getDestinationFile(fileName);
            fos = new FileOutputStream(destinationFile);
            IOUtils.copy(in, fos);
            return destinationFile;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            urlConnection.disconnect();
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(fos);
        }
        return null;
    }

    private File getDownloadDirectory() throws IOException {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (externalFilesDir == null) {
            throw new IOException("sdcard not ready");
        }
        externalFilesDir.mkdirs();
        return externalFilesDir;
    }

    private File getDestinationFile(String filename) throws IOException {
        File externalFilesDir = getDownloadDirectory();
        return new File(externalFilesDir, filename);
    }

    public void cleanupFile(String filenameNotToRemove) throws IOException {
        File downloadDirectory = getDownloadDirectory();
        for (File file : downloadDirectory.listFiles()) {
            if (file.isFile() && !file.getName().equals(filenameNotToRemove)) {
                file.delete();
            }
        }
    }
}