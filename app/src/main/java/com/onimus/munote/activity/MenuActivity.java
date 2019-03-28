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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Layout;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.Constants;
import com.onimus.munote.files.Permission;


public class MenuActivity extends MenuToolbar {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);

        setContractDialog();
        setPermissions();
        //
        loadAdmob();
        //
        setActionOnClick(R.id.btn_menu_galeria, new OnButtonClickGallery());
        setActionOnClickActivity(R.id.btn_menu_invoice, NotesActivity.class);
        setActionOnClickActivity(R.id.btn_menu_card, CardActivity.class);
        // setActionOnClickActivity(R.id.btn_menu_bank, BankActivity.class);

    }

    private void setContractDialog() {
        View view = View.inflate(this, R.layout.checkbox_contract, null);
        final CheckBox cb_contract = view.findViewById(R.id.checkbox);
        final TextView textView = view.findViewById(R.id.textview);

        //Justifica o texto
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            WebView webView = new WebView(view.getContext());
            webView.setVerticalScrollBarEnabled(false);

            LinearLayout linearLayout = view.findViewById(R.id.linear_layout);
            linearLayout.removeView(textView);
            linearLayout.addView(webView);

            String newContentString = String.valueOf(fromHtml("<![CDATA[<body style=\"text-align:justify;color:gray;background-color:white; \">"
                            + getResources().getString(R.string.message_dialog_terms)
                            + "</body>]]>"));

            webView.loadData(newContentString, "text/html; charset=utf-8", "utf-8");

        }
        cb_contract.setText(getString(R.string.text_cb_terms));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_terms)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!cb_contract.isChecked()) {
                            storeDialogStatus(false);
                            MenuActivity.this.finish();
                        } else {
                            storeDialogStatus(true);
                        }

                    }
                })
                .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        storeDialogStatus(false);
                        dialog.cancel();
                        MenuActivity.this.finish();
                    }
                });
        AlertDialog mDialog = builder.create();
//        mDialog.show();

        if (getDialogStatus()) {
            mDialog.hide();
        } else {
            mDialog.show();
        }
    }

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckTerms", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("terms", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckTerms", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("terms", false);
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