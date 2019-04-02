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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.files.Permission;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.files.Permission.neverAskAgainSelected;


public class MenuActivity extends MenuToolbar {

    private LinearLayout ll_btn_menu;
    private Button btn_request_permission;
    private Button btn_menu_galeria;
    private Button btn_menu_invoice;
    private Button btn_menu_card;
    private Button btn_menu_bank;

    @Override
    protected void onResume() {
        super.onResume();
        setPermissionsIfFirstEntryActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //reseta o sharedpreference
        setSharedPreferencesToFirstPermissionActivity(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        //
        loadAdmob();
        //
        startVariables();
        checkPermissions();
    }

    private void startVariables() {
        ll_btn_menu = findViewById(R.id.ll_btn_menu);
        btn_menu_galeria = findViewById(R.id.btn_menu_galeria);
        btn_menu_invoice = findViewById(R.id.btn_menu_invoice);
        btn_menu_card = findViewById(R.id.btn_menu_card);
        btn_menu_bank = findViewById(R.id.btn_menu_bank);
        btn_request_permission = findViewById(R.id.btn_request_permission);
    }

    private void checkPermissions() {
        ll_btn_menu.removeView(btn_menu_galeria);
        ll_btn_menu.removeView(btn_menu_invoice);
        ll_btn_menu.removeView(btn_menu_card);
        ll_btn_menu.removeView(btn_menu_bank);
        ll_btn_menu.removeView(btn_request_permission);

        //Se tiver as permissões os botões serão habilitados
        if (Permission.hasPermissions(this, permissions())) {
            ll_btn_menu.addView(btn_menu_galeria);
            ll_btn_menu.addView(btn_menu_invoice);
            ll_btn_menu.addView(btn_menu_card);
            ll_btn_menu.addView(btn_menu_bank);


            setActionOnClick(btn_menu_galeria, new OnButtonClickGallery());
            setActionOnClickActivity(btn_menu_invoice, NotesActivity.class);
            setActionOnClickActivity(btn_menu_card, CardActivity.class);
            // setActionOnClickActivity(R.id.btn_menu_bank, BankActivity.class);

        } else {
            ll_btn_menu.addView(btn_request_permission);
            setActionOnClick(btn_request_permission, new OnButtonClickRequestPermission());

        }
    }

    private void setPermissionsIfFirstEntryActivity() {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        boolean firstEntry = sharedPreferences.getBoolean(PERMISSION_FIRST_ENTRY_ACTIVITY, true);
        if (firstEntry) {
            setPermissions();
        }
    }

    private void setPermissions() {
        // O código de solicitação usado em ActivityCompat.requestPermissions ()
        // e retornado no onRequestPermissionsResult da Activity ()
        if (!Permission.hasPermissions(this, permissions())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (neverAskAgainSelected(this, permissions())) {
                    displayNeverAskAgainDialog();
                } else {
                    ActivityCompat.requestPermissions(this, permissions(), REQUEST_PERMISSIONS);
                }
            }
        }
    }

    private String[] permissions() {
        return new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
                //   android.Manifest.permission.READ_CONTACTS,
                //  android.Manifest.permission.WRITE_CONTACTS,
                //   android.Manifest.permission.READ_SMS,
        };
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.text_dialog_permission));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.text_permit_manually, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                startActivityForResult(intent, REQUEST_PERMISSIONS);
            }
        });
        builder.setNegativeButton(getString(R.string.alert_title_cancel), null);
        builder.show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (REQUEST_PERMISSIONS == requestCode) {
            int count = 0;
            for (int aGrantResult : grantResults) {
                if (aGrantResult == PackageManager.PERMISSION_GRANTED) {
                    //contador para cada permissão garantida
                    count++;
                }
            }
            //Se o contador for igual a quantidade de permissão então Permissão foi um sucesso
            if (count == permissions.length) {
                Log.i(LOG_I, getString(R.string.text_permission_granted));
                Toast.makeText(this, getString(R.string.text_permission_granted), Toast.LENGTH_LONG).show();
            } else {
                Permission.setShouldShowStatus(this, permissions);
            }
        }
        setSharedPreferencesToFirstPermissionActivity(false);
        checkPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSIONS) {
            setSharedPreferencesToFirstPermissionActivity(false);
            checkPermissions();
        }
    }

    private void setSharedPreferencesToFirstPermissionActivity(boolean f) {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PERMISSION_FIRST_ENTRY_ACTIVITY, f).apply();
    }

    private class OnButtonClickGallery implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            openESFileExplorer();

        }
    }

    private class OnButtonClickRequestPermission implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setPermissions();

        }
    }
}