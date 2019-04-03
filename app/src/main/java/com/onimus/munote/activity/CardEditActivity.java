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

import com.onimus.munote.R;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.bancos.model.Card;
import com.onimus.munote.files.MenuToolbar;

import static com.onimus.munote.Constants.*;

public class CardEditActivity extends MenuToolbar {

    private Context context;

    private Toolbar toolbar;
    //
    private EditText et_desc_card;
    private CheckBox cb_credit;
    private CheckBox cb_debit;
    //
    private CardDao cardDao;
    //
    private long idActual;

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
        cb_credit = findViewById(R.id.cb_credit);
        cb_debit = findViewById(R.id.cb_debit);
        //
        loadAdmob();
    }

    private void startAction() {
        setSupportActionBar(toolbar);

        setField();

        setAlertDialogUpdateOnClickActivity(R.id.btn_save, CardViewActivity.class, context, idActual, CARD);
        setActionOnClickActivity(R.id.btn_cancel, CardViewActivity.class, idActual);
    }

    private void setField() {
        if (idActual != -1) {
            Card cAux = cardDao.getCardById(idActual);

            et_desc_card.setText(cAux.getDescCard());
            if (cAux.getType() == 1) {
                cb_credit.setChecked(true);
                cb_debit.setChecked(false);
            }

            if (cAux.getType() == 2) {
                cb_credit.setChecked(false);
                cb_debit.setChecked(true);
            }

            if (cAux.getType() == 3) {
                cb_credit.setChecked(true);
                cb_debit.setChecked(true);
            }
        }
    }

    private void getParameters() {
        idActual = getIntent().getLongExtra(DBASE_ID, 0);

    }

    public void onBackPressed() {
        callListView(CardViewActivity.class, idActual);
    }

}
