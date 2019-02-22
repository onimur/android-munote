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

package com.example.admin.munotes.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.example.admin.munotes.Constants;
import com.example.admin.munotes.R;

import com.example.admin.munotes.files.MenuToolbar;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CardAddActivity extends MenuToolbar {

    private Context context;

    private EditText et_desc_card;
    private EditText et_number_card;

    private CheckBox cb_credito;
    private CheckBox cb_debito;

    private long idAtual;

    private Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_add_screen);


        startVariables();
        startAction();
    }

    private void startVariables() {
        context = getBaseContext();
        //
        getParameters();
        //
        et_desc_card = findViewById(R.id.et_desc_card);
        et_number_card = findViewById(R.id.et_number_card);
        cb_credito = findViewById(R.id.cb_credito);
        cb_debito = findViewById(R.id.cb_debito);
        //
        toolbar = findViewById(R.id.toolbar);
        //
        setAds();

    }


    private void startAction() {

        setSupportActionBar(toolbar);
        setActionOnClick(R.id.btn_limpar, new OnButtonClickActionLimpar());
        setAlertDialogToReturnOnClickActivity(R.id.btn_cancelar, CardActivity.class, "cartao");
        setActionOnClick(R.id.btn_salvar, new OnButtonClickActionSave());
    }

    private class OnButtonClickActionLimpar implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            clearField(et_desc_card, true);
            clearField(et_number_card, false);

            clearField(cb_credito);
            clearField(cb_debito);
        }
    }

    private class OnButtonClickActionSave implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validation("cartao")) {
                saveCard(idAtual, context);
                callActivity(context, CardActivity.class);

            }
        }
    }

    private void getParameters() {
        idAtual = getIntent().getLongExtra(Constants.ID_BANCO, 0);

    }

    public void onBackPressed() {
        setAlertDialogToReturnOnClickActivity(CardActivity.class, "cartao");
    }


}
