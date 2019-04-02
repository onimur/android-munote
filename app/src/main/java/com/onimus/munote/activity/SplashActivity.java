/*
 *
 *  * Created by Murillo Comino on 09/02/19 12:26
 *  * Github: github.com/MurilloComino
 *  * StackOverFlow: pt.stackoverflow.com/users/128573
 *  * Email: murillo_comino@hotmail.com
 *  *
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 09/02/19 12:11
 *
 */

package com.onimus.munote.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;

import static com.onimus.munote.Constants.*;

public class SplashActivity extends MenuToolbar {

    private TextView tv_progress;
    int progressCount = 0;
    Handler h = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mostra a tela de Splash
        setContentView(R.layout.splash_screen);


        tv_progress = findViewById(R.id.tv_progress);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.adAPPId));
        //
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_USER, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(SPLASH_FIRST_ENTRY, false)) {
            startApp();
        } else {
            addPreferenceFirstEntry(sharedPreferences);
            setSplash();
        }

    }

    private void addPreferenceFirstEntry(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SPLASH_FIRST_ENTRY, true);
        editor.apply();
    }

    private void setSplash() {
        // Inicia um Processo em segundo plano
        new Thread(new Runnable() {
            public void run() {
                doWork();

                startApp();

            }
        }).start();
    }

    private void doWork() {
        while (progressCount < 100) {
            progressCount += 4;
            h.post(new Runnable() {
                public void run() {
                    //Atualiza o TextView com a porcentagem do Progresso
                    tv_progress.setText(String.valueOf(progressCount + "%"));
                }
            });
            try {
                //Realiza Processo o processo
                Thread.sleep(SPLASH_DELAY);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Inicia a Próxima Activity
    private void startApp() {
        callActivity(getBaseContext(), DialogContractActivity.class);
    }

}