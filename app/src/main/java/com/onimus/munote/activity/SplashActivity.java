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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.Constants;

public class SplashActivity extends MenuToolbar {

    TextView tv_progress;
    ProgressBar mProgress;
    int progressCount = 0;
    Handler h = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mostra a tela de Splash
        setContentView(R.layout.splash_screen);


        tv_progress = findViewById(R.id.tv_progress);
        mProgress = findViewById(R.id.splash_screen_progress_bar);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.adAPPId));
        //
        SharedPreferences preferences =
                getSharedPreferences("user_preferences", MODE_PRIVATE);

        if (preferences.contains("first_entry")) {
            startApp();
        } else {
            addPreferenceFirstEntry(preferences);
            setSplash();
        }

    }

    private void addPreferenceFirstEntry(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first_entry", true);
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
                Thread.sleep(Constants.SPLASH_DELAY);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Inicia a Próxima Activity
    private void startApp() {
        callActivity(getBaseContext(), MenuActivity.class);
    }

}