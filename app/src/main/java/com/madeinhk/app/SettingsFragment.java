package com.madeinhk.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madeinhk.dailywallpaper.R;
import com.madeinhk.utils.AlarmHelper;

/**
 * Created by tony on 2/9/14.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_ENABLE_SET_WALLPAPER = "enable_set_in_background";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preference_list_fragment, container, false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (KEY_ENABLE_SET_WALLPAPER.equals(key)) {
            boolean setWallpaper = sharedPreferences.getBoolean(KEY_ENABLE_SET_WALLPAPER, false);
            AlarmHelper alarmHelper = new AlarmHelper(getActivity());
            if (setWallpaper) {
                alarmHelper.scheduleFetchWallpaperTask();
            } else {
                alarmHelper.canelFetchWallpaperTask();
            }
        }
    }
}
