package com.example.admin.munotes.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.example.admin.munotes.R;
import com.example.admin.munotes.files.MenuToolbar;
import com.example.admin.munotes.Constants;
import com.example.admin.munotes.files.Permission;


public class MenuActivity extends MenuToolbar {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        setPermissions();

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