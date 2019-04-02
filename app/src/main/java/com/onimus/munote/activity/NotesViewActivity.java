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
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.HMAuxCard;
import com.onimus.munote.bancos.banco.RecordSpinnerCardAdapter;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.bancos.dao.NotesDao;
import com.onimus.munote.bancos.model.Notes;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.files.FileUtilities;

import java.io.File;
import java.util.ArrayList;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.files.MoneyTextWatcher.formatTextPrice;
import static com.onimus.munote.files.MoneyTextWatcher.getCurrencySymbol;

public class NotesViewActivity extends MenuToolbar {

    private Context context;
    private NotesDao notesDao;
    private Notes nAux;
    private CardDao cardDao;
    private RecordSpinnerCardAdapter adapter;
    private ArrayList<HMAuxCard> hmAux;
    //
    private File imgFile;
    //
    private TextView tv_title_invoice;
    private TextView tv_value;
    private TextView tv_value2;
    private TextView tv_desc_invoice;
    private TextView tv_sp_disabled;
    private TextView tv_selec_date;
    //
    private RadioButton rb_credit;
    private RadioButton rb_debit;
    //
    private LinearLayout ll_hint_spinner;
    //
    private Spinner sp_card;
    private Spinner sp_parcelas;
    //
    private int idCartao;
    private long idAtual;
    //
    private Toolbar toolbar;
    private String caminho;
    //
    private View view_sp_disabled;


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
        getParameters();

        toolbar = findViewById(R.id.toolbar);
        tv_title_invoice = findViewById(R.id.tv_title_invoice);
        tv_value = findViewById(R.id.tv_value);
        tv_value2 = findViewById(R.id.tv_value2);
        tv_desc_invoice = findViewById(R.id.tv_desc_invoice);
        tv_selec_date = findViewById(R.id.tv_selec_date);
        sp_card = findViewById(R.id.sp_card);
        sp_parcelas = findViewById(R.id.sp_parcelas);
        view_sp_disabled = findViewById(R.id.view_sp_disabled);
        tv_sp_disabled = findViewById(R.id.tv_sp_disabled);
        ll_hint_spinner = findViewById(R.id.ll_hint_spinner);
        rb_credit = findViewById(R.id.rb_credit);
        rb_debit = findViewById(R.id.rb_debit);
        //
        tv_value.setText((getString(R.string.tv_value) + " ("+ getCurrencySymbol() + "):"));
        //
        loadAdmob();
        //
    }

    private void startAction() {
        setSupportActionBar(toolbar);

        setSpinnerCard();
        setSpinnerParcel();
        setField();

        setActionOnClick(R.id.ib_foto, new OnButtonClickActionImage());
        setAlertDialogDeleteOnClickActivity(R.id.btn_deletar, NotesActivity.class, context, idAtual, NOTES);
        setActionOnClickActivity(R.id.btn_editar, NotesEditActivity.class, idAtual);
    }

    private void setSpinnerParcel() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.parcelas_array, R.layout.celula_spinner_parcelas_layout);
        adapter.setDropDownViewResource(R.layout.celular_spinner_dropdown_parcelas_layout);
        sp_parcelas.setAdapter(adapter);
    }

    private void setSpinnerCard() {
        adapter = new RecordSpinnerCardAdapter(context, R.layout.celula_spinner_card_layout, cardDao.getListCard());
        sp_card.setAdapter(adapter);
    }

    private void setField() {
        if (idAtual != -1) {

            nAux = notesDao.getNotesById(idAtual);
            idCartao = (int) nAux.getIdCard();
            caminho = nAux.getPhotoNotes();
            //
            File path = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + FOLDER_NAME + FOLDER_NAME_NOTES));
            imgFile = new File(path, caminho);

            setImageSaveToImageButton(caminho, imgFile);
            //
            ll_hint_spinner.setEnabled(false);
            if (idCartao == -1) {
                //linearLayout fica visivel pois o cartão foi deletado
                ll_hint_spinner.setVisibility(View.VISIBLE);
            }

            sp_card.setEnabled(false);
            sp_parcelas.setEnabled(false);
            hmAux = cardDao.getListCard();
            sp_card.setSelection(getSpinnerIndex(sp_card, String.valueOf(idCartao)));
            sp_parcelas.setSelection(nAux.getParcels() - 1);

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
            String data = formatDate(String.valueOf((int) nAux.getDay() + "/" + nAux.getMonth() + "/" + nAux.getYear()));
            tv_selec_date.setText(data);
        }
    }

    private void getParameters() {
        idAtual = getIntent().getLongExtra(DBASE_ID, 0);

    }

    //Verifica em qual posição o IDCartão desejado está;
    public int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxCard model = hmAux.get(i);
            String modelS = model.get(CardDao.ID_CARD);
            if (modelS != null) {
                if (modelS.equals(myString)) {
                    index = i;
                }
            }
        }
        return index;
    }

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

