package com.evansappwriter.liveyourlist;

import android.app.Application;
import android.content.Context;

/**
 * Created by markevans on 8/3/16.
 */
public class LifeYourListApplication extends Application {
    private static Context mContext;
    private static LifeYourListApplication sInstance;

    public static Context getContext(){
        return mContext;
    }
    public static LifeYourListApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;

        sInstance = this;
    }
}
