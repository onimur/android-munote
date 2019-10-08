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
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.repository.database.HMAuxCard;
import com.onimus.munote.repository.database.RecordSpinnerCardAdapter;
import com.onimus.munote.repository.dao.CardDao;
import com.onimus.munote.repository.dao.NotesDao;
import com.onimus.munote.repository.model.Notes;
import com.onimus.munote.business.ManageDirectory;
import com.onimus.munote.business.MenuToolbar;
import com.onimus.munote.business.FileUtilities;

import java.io.File;
import java.util.ArrayList;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.business.MoneyTextWatcher.formatTextPrice;
import static com.onimus.munote.business.MoneyTextWatcher.getCurrencySymbol;
import static com.onimus.munote.business.SpinnerIndex.getSpinnerIndex;

public class NotesViewActivity extends MenuToolbar {

    private Context context;
    private NotesDao notesDao;
    private CardDao cardDao;
    //
    private File imgFile;
    //
    private TextView tv_title_invoice;
    private TextView tv_value;
    private TextView tv_value2;
    private TextView tv_desc_invoice;
    private TextView tv_sp_disabled;
    private TextView tv_select_date;
    //
    private RadioButton rb_credit;
    private RadioButton rb_debit;
    //
    private LinearLayout ll_hint_spinner;
    //
    private Spinner sp_card;
    private Spinner sp_parcels;
    private long idActual;
    //
    private Toolbar toolbar;
    //
    private View view_sp_disabled;
    //
    private ManageDirectory md;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view_screen);

        startVariables();
        startAction();
    }

    private void startVariables() {
        context = getBaseContext();
        notesDao = new NotesDao(context);
        cardDao = new CardDao(context);
        md = new ManageDirectory(context);
        getParameters();

        toolbar = findViewById(R.id.toolbar);
        tv_title_invoice = findViewById(R.id.tv_title_invoice);
        tv_value = findViewById(R.id.tv_value);
        tv_value2 = findViewById(R.id.tv_value2);
        tv_desc_invoice = findViewById(R.id.tv_desc_invoice);
        tv_select_date = findViewById(R.id.tv_select_date);
        sp_card = findViewById(R.id.sp_card);
        sp_parcels = findViewById(R.id.sp_parcels);
        view_sp_disabled = findViewById(R.id.view_sp_disabled);
        tv_sp_disabled = findViewById(R.id.tv_sp_disabled);
        ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
        rb_credit = findViewById(R.id.rb_credit);
        rb_debit = findViewById(R.id.rb_debit);
        //
        loadAdmob();
        //
    }

    private void startAction() {
        setSupportActionBar(toolbar);
        //
        tv_value.setText((getString(R.string.tv_value) + " (" + getCurrencySymbol() + "):"));
        setSpinnerCard();
        setSpinnerParcel();
        setField();

        setActionOnClick(R.id.ib_photo, new OnButtonClickActionImage());
        setAlertDialogDeleteOnClickActivity(R.id.btn_delete, NotesActivity.class, context, idActual, NOTES);
        setActionOnClickActivity(R.id.btn_edit, NotesEditActivity.class, idActual);
    }

    private void setSpinnerParcel() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.parcels_array, R.layout.cel_spinner_parcels_layout);
        adapter.setDropDownViewResource(R.layout.cel_spinner_dropdown_parcels_layout);
        sp_parcels.setAdapter(adapter);
    }

    private void setSpinnerCard() {
        RecordSpinnerCardAdapter adapter = new RecordSpinnerCardAdapter(context, R.layout.cel_spinner_card_layout, cardDao.getListCard());
        sp_card.setAdapter(adapter);
    }

    private void setField() {
        if (idActual != -1L) {

            Notes nAux = notesDao.getNotesById(idActual);
            //
            long idCard = nAux.getIdCard();
            String getPath = nAux.getPhotoNotes();
            String path = FOLDER_NAME + FOLDER_NAME_NOTES;
            File dir = md.createInPicture(path);
            imgFile = md.createFile(dir, getPath);

            setImageSaveToImageButton(getPath, imgFile);
            //
            ll_hint_spinner.setEnabled(false);
            if (idCard == -1L) {
                //linearLayout fica visivel pois o cartão foi deletado
                ll_hint_spinner.setVisibility(View.VISIBLE);
            }

            sp_card.setEnabled(false);
            sp_parcels.setEnabled(false);
            ArrayList<HMAuxCard> hmAux = cardDao.getListCard();
            sp_card.setSelection(getSpinnerIndex(sp_card, idCard, hmAux, CardDao.ID_CARD));
            sp_parcels.setSelection(nAux.getParcels() - 1);

            rb_debit.setClickable(false);
            rb_credit.setClickable(false);


            if (nAux.getType() == 1) {
                rb_credit.setChecked(true);
                rb_debit.setEnabled(false);
                tv_sp_disabled.setEnabled(true);
                view_sp_disabled.setVisibility(View.INVISIBLE);

            } else {
                rb_debit.setChecked(true);
                rb_credit.setEnabled(false);
                view_sp_disabled.setVisibility(View.VISIBLE);
                tv_sp_disabled.setEnabled(false);
            }

            tv_title_invoice.setText(nAux.getTitleNotes());
            tv_desc_invoice.setText(nAux.getDescNotes());
            String price = nAux.getPriceNotes();
            //formata o valor como 0,00 ou 0.00
            if (price != null) {
                price = formatTextPrice(price);
            }
            tv_value2.setText(price);

            //formata a data
            String data = formatDate(nAux.getDay() + "/" + nAux.getMonth() + "/" + nAux.getYear());
            tv_select_date.setText(data);
        }
    }

    private void getParameters() {
        idActual = getIntent().getLongExtra(DBASE_ID, 0);

    }

    //Verifica em qual posição o IDCartão desejado está;
    private class OnButtonClickActionImage implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            //IMPORTANTE PRA CONSEGUIR LER A IMAGEM:
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(FileUtilities.getUri(getApplicationContext(), imgFile), "image/*");
            startActivity(intent);

        }
    }

    public void onBackPressed() {
        callActivity(context, NotesActivity.class);
    }
}

