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

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.Constants;
import com.onimus.munote.files.Permission;


public class MenuActivity extends MenuToolbar {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        setPermissions();
        //
        loadAdmob();
        //
        setActionOnClick(R.id.btn_menu_galeria, new OnButtonClickGallery());
        setActionOnClickActivity(R.id.btn_menu_invoice, NotesActivity.class);
        setActionOnClickActivity(R.id.btn_menu_card, CardActivity.class);
        // setActionOnClickActivity(R.id.btn_menu_bank, BankActivity.class);

    }

    private void setPermissions() {
        // O código de solicitação usado em ActivityCompat.requestPermissions ()
        // e retornado no onRequestPermissionsResult da Activity ()
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
                //   android.Manifest.permission.READ_CONTACTS,
                //  android.Manifest.permission.WRITE_CONTACTS,
                //   android.Manifest.permission.READ_SMS,
        };
        if (!Permission.hasPermissoes(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.RESQUEST_PERMISSOES);
        }
    }

    private class OnButtonClickGallery implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            openESFileExplorer();

        }
    }

}