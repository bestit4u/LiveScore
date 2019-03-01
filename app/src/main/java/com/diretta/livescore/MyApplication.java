package com.diretta.livescore;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.onesignal.OneSignal;


public class MyApplication extends Application {

     /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.setApplicationId("1698599093586535");
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}