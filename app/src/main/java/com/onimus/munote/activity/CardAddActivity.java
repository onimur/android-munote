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
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.onimus.munote.R;

import com.onimus.munote.files.MenuToolbar;

import static com.onimus.munote.Constants.*;

public class CardAddActivity extends MenuToolbar {

    private Context context;

    private EditText et_desc_card;

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
        cb_credito = findViewById(R.id.cb_credito);
        cb_debito = findViewById(R.id.cb_debito);
        //
        toolbar = findViewById(R.id.toolbar);
        //
        loadAdmob();
        //
    }


    private void startAction() {

        setSupportActionBar(toolbar);
        setActionOnClick(R.id.btn_limpar, new OnButtonClickActionLimpar());
        setAlertDialogToReturnOnClickActivity(R.id.btn_cancelar, CardActivity.class, CARD);
        setActionOnClick(R.id.btn_salvar, new OnButtonClickActionSave());
    }

    private class OnButtonClickActionLimpar implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            clearField(et_desc_card, true);
            clearField(cb_credito);
            clearField(cb_debito);
        }
    }

    private class OnButtonClickActionSave implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validation(CARD)) {
                saveCard(idAtual, context);
                callActivity(context, CardActivity.class);

            }
        }
    }

    private void getParameters() {
        idAtual = getIntent().getLongExtra(DBASE_ID, 0);

    }

    public void onBackPressed() {
        setAlertDialogToReturnOnClickActivity(CardActivity.class, CARD);
    }
}
