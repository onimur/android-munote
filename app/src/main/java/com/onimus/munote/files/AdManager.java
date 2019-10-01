package com.onimus.munote.files;

import android.util.Log;

import com.google.android.gms.ads.AdListener;

import static com.onimus.munote.Constants.*;

class AdManager extends AdListener {
    @Override
    public void onAdClosed() {
        super.onAdClosed();
        Log.i(TAG_ADMOB, "onAdClosed");
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
        Log.i(TAG_ADMOB, "onAdFailedToLoad: error " + errorCode);
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
        Log.i(TAG_ADMOB, "onAdLeftApplication");
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
        Log.i(TAG_ADMOB, "onAdOpened");
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        Log.i(TAG_ADMOB, "onAdLoaded");
    }
}
