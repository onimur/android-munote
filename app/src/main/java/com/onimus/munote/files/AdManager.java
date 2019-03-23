package com.onimus.munote.files;

import android.util.Log;

import com.google.android.gms.ads.AdListener;

public class AdManager extends AdListener {
    @Override
    public void onAdClosed() {
        super.onAdClosed();
        Log.i("AdMob", "onAdClosed");
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
        Log.i("AdMob", "onAdFailedToLoad: error " + errorCode);
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
        Log.i("AdMob", "onAdLeftApplication");
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
        Log.i("AdMob", "onAdOpened");
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        Log.i("AdMob", "onAdLoaded");
    }
}
