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
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.onimus.munote.Constants;
import com.onimus.munote.R;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.bancos.model.Card;
import com.onimus.munote.files.MenuToolbar;

public class CardEditActivity extends MenuToolbar {

    private Context context;

    private Toolbar toolbar;
    //
    private EditText et_desc_card;
    private CheckBox cb_credito;
    private CheckBox cb_debito;
    //
    private CardDao cardDao;
    //
    private long idAtual;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_edit_screen);

        startVariables();
        startAction();
    }

    private void startVariables() {

        context = getBaseContext();
        //
        cardDao = new CardDao(context);
        //
        getParameters();
        //
        toolbar = findViewById(R.id.toolbar);
        et_desc_card = findViewById(R.id.et_desc_card);
        cb_credito = findViewById(R.id.cb_credito);
        cb_debito = findViewById(R.id.cb_debito);
        //
        loadAdmob();
        //
    }

    private void startAction() {
        setSupportActionBar(toolbar);

        setField();

        setAlertDialogUpdateOnClickActivity(R.id.btn_salvar, CardViewActivity.class, context, idAtual, "cartao");
        setActionOnClickActivity(R.id.btn_cancelar, CardViewActivity.class, idAtual);
    }

    private void setField() {
        if (idAtual != -1) {
            Card cAux = cardDao.getCardById(idAtual);

            et_desc_card.setText(cAux.getDescartao());
            if (cAux.getTipo() == 1) {
                cb_credito.setChecked(true);
                cb_debito.setChecked(false);
            }

            if (cAux.getTipo() == 2) {
                cb_credito.setChecked(false);
                cb_debito.setChecked(true);
            }

            if (cAux.getTipo() == 3) {
                cb_credito.setChecked(true);
                cb_debito.setChecked(true);
            }
        }
    }

    private void getParameters() {
        idAtual = getIntent().getLongExtra(Constants.ID_BANCO, 0);

    }

    public void onBackPressed() {
        callListView(CardViewActivity.class, idAtual);
    }

}
