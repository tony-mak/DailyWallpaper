package com.madeinhk.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.madeinhk.dailywallpaper.R;

public class MyActivity extends Activity {
    private Fragment mFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mActionBar = getActionBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        String[] itemList = this.getResources().getStringArray(R.array.navigation_drawer_list);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, itemList));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle provides convenient helpers for tying together the
        // prescribed interactions between a top-level sliding drawer and the action bar.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * The action bar home/up action should open or close the drawer.
         * mDrawerToggle will take care of this.
         */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerLayout.closeDrawer(mDrawerList);
            selectItem(position);
        }
    }

    private static final int PAGE_TODAY = 0;
    private static final int PAGE_SETTINGS = 1;
    private static final int PAGE_ABOUT = 2;

    private void selectItem(int pos) {
        switch (pos) {
            case PAGE_TODAY:
                mFragment = new ImageFragment();
                break;
            case PAGE_SETTINGS:
                mFragment = new SettingsFragment();
                break;
            case PAGE_ABOUT:
                mFragment = new AboutFragment();
                break;
            default:
                throw new IllegalArgumentException("no page " + pos);
        }
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mFragment).commit();
        mDrawerList.setItemChecked(pos, true);
    }
}
