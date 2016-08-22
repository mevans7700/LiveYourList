package com.evansappwriter.liveyourlist.ui.drawer;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.ui.BaseActivity;

/**
 * Created by markevans on 7/8/16.
 */
public class Drawer implements NavigationView.OnNavigationItemSelectedListener  {
    private static final String TAG = "Drawer";

    private final BaseActivity mActivity;
    private DrawerLayout mDrawerLayout;

    public Drawer(BaseActivity activity) {
        mActivity = activity;
        init();
    }

    public void open() {
        if (mDrawerLayout != null) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public boolean isOpen() {
        if (mDrawerLayout != null) {
            return mDrawerLayout.isDrawerOpen(GravityCompat.START);
        }

        return false;
    }

    public void close() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void init() {
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, mDrawerLayout, mActivity.getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
