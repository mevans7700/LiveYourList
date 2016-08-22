package com.evansappwriter.liveyourlist.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.ui.drawer.Drawer;
import com.evansappwriter.liveyourlist.util.Utils;
import com.evansappwriter.liveyourlist.util.Keys;


/**
 * Created by markevans on 7/8/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BASEACTIVITY";

    private Toolbar mToolbar;
    private Drawer mDrawer;

    public SharedPreferences sharedPrefs;

    private static Handler sHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);

        sharedPrefs = getSharedPreferences(Keys.PREFS_NAME, Context.MODE_PRIVATE);

        Utils.setStrictMode(true);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawer = new Drawer(this);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isOpen()) {
            mDrawer.close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /********************************/
    // DialogFragment Manager
    /********************************/

    /**
     * Used to post runnables to the main thread.
     *
     * @return the activity handler
     */
    public Handler getHandler() {
        if (sHandler == null) {
            sHandler = new Handler();
        }
        return sHandler;
    }

    public void showProgress(String message) {
        Bundle args = new Bundle();
        args.putString(Keys.DIALOG_MESSAGE_KEY, message);
        showDialogFragment(Keys.DIALOG_GENERAL_LOADING, args);
    }

    /**
     * show a general error with title, message and data customizable
     *
     * @param title   the title of the error popup
     * @param message the body of the error
     * @param data
     */
    protected void showError(String title, String message, Bundle data) {
        Bundle args = new Bundle();
        args.putString(Keys.DIALOG_TITLE_KEY, title);
        args.putString(Keys.DIALOG_MESSAGE_KEY, message);
        args.putBundle(Keys.DIALOG_DATA_KEY, data);
        showDialogFragment(Keys.DIALOG_GENERAL_ERROR, args);
    }

    /**
     * show a general message with title, message and data customizable
     *
     * @param title   the title of the message popup
     * @param message the body of the message
     * @param data
     */
    protected void showMessage(String title, String message, Bundle data) {
        Bundle args = new Bundle();
        args.putString(Keys.DIALOG_TITLE_KEY, title);
        args.putString(Keys.DIALOG_MESSAGE_KEY, message);
        args.putBundle(Keys.DIALOG_DATA_KEY, data);
        showDialogFragment(Keys.DIALOG_GENERAL_MESSAGE, args);
    }


    protected void showDialogFragment(final int id, final Bundle args) {
        showDialogFragment(id, args, false);
    }

    protected void showDialogFragment(final int id, final Bundle args, final boolean cancelable) {
        if (!isFinishing()) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    DialogFragment dialog;
                    switch (id) {
                        case Keys.DIALOG_GENERAL_LOADING:
                        case Keys.DIALOG_GENERAL_MESSAGE:
                        case Keys.DIALOG_GENERAL_ERROR:
                        default:
                            dialog = AlertDialogFragment.newInstance(id, args);
                    }
                    dialog.setCancelable(cancelable);
                    dialog.show(getSupportFragmentManager(), "dialog_" + id);
                }
            });
        }
    }

    protected void showDialogFragment(final int id, final Bundle args, final boolean cancelable, final AlertDialogFragment.OnDialogDoneListener l) {
        if (!isFinishing()) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    AlertDialogFragment dialog = AlertDialogFragment.newInstance(id, args);
                    dialog.setCancelable(cancelable);
                    dialog.setListener(l);
                    dialog.show(getSupportFragmentManager(), "dialog_" + id);
                }
            });
        }
    }

    private void showDialogFragment(final int id, final Bundle args, final boolean cancelable, final Fragment f) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    AlertDialogFragment dialog = AlertDialogFragment.newInstance(id, args);
                    dialog.setCancelable(cancelable);
                    dialog.setTargetFragment(f, 1);
                    dialog.show(getSupportFragmentManager(), "dialog_" + id);
                }
            }
        });
    }

    protected void dismissDialogFragment(final int id) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                AlertDialogFragment dialog = (AlertDialogFragment) getSupportFragmentManager().findFragmentByTag("dialog_" + id);
                if (dialog != null) {
                    dialog.dismissAllowingStateLoss();
                }
            }
        });
    }

    public void dismissProgress() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                AlertDialogFragment popup = (AlertDialogFragment) getSupportFragmentManager().findFragmentByTag("dialog_" + Keys.DIALOG_GENERAL_LOADING);
                if (popup != null) {
                    popup.dismissAllowingStateLoss();
                }
            }
        });
    }
}
