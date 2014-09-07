package com.madeinhk.app;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.madeinhk.background.WorkerService;
import com.madeinhk.dailywallpaper.R;
import com.madeinhk.ui.SwipeRefreshLayout;
import com.madeinhk.utils.KeyValueStorage;
import com.madeinhk.utils.WallpaperHelper;

import java.io.File;
import java.io.IOException;

public class ImageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView mImageView;
    private TextView mTextView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Uri mWallpaperUri;
    private Handler mHandler;


    public ImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        mTextView = (TextView) rootView.findViewById(R.id.description_text);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        showHowtoUseIfNeeded();
        mHandler = new Handler();
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WorkerService.ACTION_FETCH_WALLPAPER_DONE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        boolean show = showDownloadedPhotoIfAny();
        if (!show) {
            mSwipeRefreshLayout.setRefreshing(true);
            fetchImage();
        }
    }

    private void showHowtoUseIfNeeded() {
        KeyValueStorage storage = new KeyValueStorage(getActivity());
        if (storage.getFirstRun()) {
            final ShowcaseView showcaseView = new ShowcaseView.Builder(getActivity())
                    .setTarget(new ViewTarget(R.id.imageView, getActivity()))
                    .setContentTitle("How to use?")
                    .setContentText("With no extra effort, your device would update wallpaper from Bing silently in everyday morning. Turn on it in Setting now")
                    .hideOnTouchOutside()
                    .build();
            showcaseView.show();
            storage.saveFirstRun();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    private boolean showDownloadedPhotoIfAny() {
        KeyValueStorage storage = new KeyValueStorage(getActivity());
        String wallpaperPath = storage.getWallpaperPath();
        if (!TextUtils.isEmpty(wallpaperPath)) {
            File file = new File(wallpaperPath);
            if (file.exists()) {
                updateUi(Uri.fromFile(file));
                return true;
            }
        }
        return false;
    }

    private void fetchImage() {
        Intent fetchWallpaperIntent = WorkerService.getFetchWallpaperIntent(getActivity(), false);
        getActivity().startService(fetchWallpaperIntent);
    }

    public void updateUi(Uri uri) {
        mImageView.setImageURI(uri);
        try {
            ExifInterface exifInterface = new ExifInterface(uri.getPath());
            mTextView.setText(exifInterface.getAttribute("UserComment"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        mWallpaperUri = uri;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onRefresh() {
        fetchImage();
    }


    private void shareWallpaper() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, mWallpaperUri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_wallpaper_to)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
            if (mWallpaperUri != null) {
                shareWallpaper();
            }
        } else if (id == R.id.action_wallpaper) {
            if (mWallpaperUri != null) {
                setWallpaper();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWallpaper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpaperHelper wallpaperHelper = new WallpaperHelper(getActivity());
                File wallpaperFile = new File(mWallpaperUri.getPath());
                try {
                    wallpaperHelper.setWallpaper(wallpaperFile);
                    if (mHandler != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), R.string.wallpaper_is_set, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getParcelableExtra(WorkerService.EXTRA_WALLPAPER_URI);
            if (data != null) {
                ImageFragment.this.updateUi(data);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };
}
