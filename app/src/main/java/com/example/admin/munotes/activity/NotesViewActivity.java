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
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.munotes.Constants;
import com.example.admin.munotes.R;
import com.example.admin.munotes.bancos.banco.HMAuxCard;
import com.example.admin.munotes.bancos.banco.RecordSpinnerCardAdapter;
import com.example.admin.munotes.bancos.dao.CardDao;
import com.example.admin.munotes.bancos.dao.NotesDao;
import com.example.admin.munotes.bancos.model.Notes;
import com.example.admin.munotes.files.MenuToolbar;
import com.example.admin.munotes.files.FileUtilities;

import java.io.File;
import java.util.ArrayList;

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
    private TextView tv_desc_invoice;
    private TextView tv_selec_date;
    //
    //
    private LinearLayout ll_hint_spinner;
    //
    private Spinner sp_card;
    //
    private int idCartao;
    private long idAtual;
    //
    private Toolbar toolbar;
    private String caminho;


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
        tv_desc_invoice = findViewById(R.id.tv_desc_invoice);
        tv_selec_date = findViewById(R.id.tv_selec_date);
        sp_card = findViewById(R.id.sp_card);
        ll_hint_spinner = findViewById(R.id.ll_hint_spinner);

        adapter = new RecordSpinnerCardAdapter(context, R.layout.celula_spinner_card_layout, cardDao.getListCard());
        sp_card.setAdapter(adapter);


    }

    private void startAction() {
        setSupportActionBar(toolbar);

        setField();

        setActionOnClick(R.id.ib_foto, new OnButtonClickActionImage());
        setAlertDialogDeleteOnClickActivity(R.id.btn_deletar, NotesActivity.class, context, idAtual, "notas");
        setActionOnClickActivity(R.id.btn_editar, NotesEditActivity.class, idAtual);
    }

    private void setField() {
        if (idAtual != -1) {

            nAux = notesDao.getNotesById(idAtual);
            idCartao = (int) nAux.getIdcartao();
            caminho = nAux.getFotonotas();
            //
            File path = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + Constants.FOLDER_NAME + Constants.FOLDER_NAME_NOTES));
            imgFile = new File(path, caminho);

            setImageSaveToImageButton(caminho, imgFile);
            //
            ll_hint_spinner.setEnabled(false);
            if (idCartao == -1) {
                //linearLayout fica visivel pois o cartão foi deletado
                ll_hint_spinner.setVisibility(View.VISIBLE);
            }

            sp_card.setEnabled(false);
            hmAux = cardDao.getListCard();
            sp_card.setSelection(getSpinnerIndex(sp_card, String.valueOf(idCartao)));

            tv_title_invoice.setText(nAux.getTitlenotas());
            tv_value.setText(nAux.getPreconotas());
            tv_desc_invoice.setText(nAux.getDesnotas());

            //formata a data
            String data = formatDate(String.valueOf((int) nAux.getDia() + "/" + nAux.getMes() + "/" + nAux.getAno()));
            tv_selec_date.setText(data);
        }
    }

    private void getParameters() {
        idAtual = getIntent().getLongExtra(Constants.ID_BANCO, 0);

    }

    //Verifica em qual posição o IDCartão desejado está;
    public int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxCard model = hmAux.get(i);
            String modelS = model.get(CardDao.IDCARTAO);
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void returnActivityToFragment(Context context, final Class<?> _class) {
        //Retorna pra Activity anterior chamando o Fragment 3 e colocando os dados pra ano e mes, e recupera a listvire;
        //não funcionando do jeito desejado
        Intent mIntent = new Intent(context, _class);
        mIntent.putExtra("activity", "notasview");
        mIntent.putExtra("ano", String.valueOf(nAux.getAno()));
        mIntent.putExtra("mes", String.valueOf(nAux.getMes()));
        startActivity(mIntent);
        //
        finish();
    }

    public void onBackPressed() {
        returnActivityToFragment(context, NotesActivity.class);
    }
}

