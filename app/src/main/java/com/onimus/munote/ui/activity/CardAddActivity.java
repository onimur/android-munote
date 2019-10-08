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

package com.onimus.munote.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.onimus.munote.R;

import com.onimus.munote.business.MenuToolbar;

import static com.onimus.munote.Constants.*;

public class CardAddActivity extends MenuToolbar {

    private Context context;

    private EditText et_desc_card;

    private CheckBox cb_credit;
    private CheckBox cb_debit;

    private long idActual;

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
        cb_credit = findViewById(R.id.cb_credit);
        cb_debit = findViewById(R.id.cb_debit);
        //
        toolbar = findViewById(R.id.toolbar);
        //
        loadAdmob();
        //
    }


    private void startAction() {

        setSupportActionBar(toolbar);
        setActionOnClick(R.id.btn_clear, new OnButtonClickActionClear());
        setAlertDialogToReturnOnClickActivity(R.id.btn_cancel, CardActivity.class, CARD);
        setActionOnClick(R.id.btn_save, new OnButtonClickActionSave());
    }

    private class OnButtonClickActionClear implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            clearField(et_desc_card, true);
            clearField(cb_credit);
            clearField(cb_debit);
        }
    }

    private class OnButtonClickActionSave implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (validation(CARD)) {
                saveCard(idActual, context);
                callActivity(context, CardActivity.class);

            }
        }
    }

    private void getParameters() {
        idActual = getIntent().getLongExtra(DBASE_ID, 0);

    }

    public void onBackPressed() {
        setAlertDialogToReturnOnClickActivity(CardActivity.class, CARD);
    }
}
