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


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.onimus.munote.Constants;
import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.HMAuxNotes;
import com.onimus.munote.bancos.banco.RecordListNotesAdapter;
import com.onimus.munote.bancos.dao.NotesDao;
import com.onimus.munote.bancos.model.NotesActivityViewModel;
import com.onimus.munote.files.Filters;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.fragmentos.FilterDialogFragment;

import java.util.Calendar;

import static com.onimus.munote.bancos.DBaseDirectory.createDirectoryDbase;
import static com.onimus.munote.bancos.dao.NotesDao.TOTAL;
import static com.onimus.munote.files.MoneyTextWatcher.formatTextPrice;
import static com.onimus.munote.files.MoneyTextWatcher.getCurrencySymbol;

public class NotesActivity extends MenuToolbar implements FilterDialogFragment.FilterListener {


    private Context context;
    private Toolbar toolbar;
    private TextView tv_filter;
    private TextView tv_sort_by;
    private TextView tv_year_month_filter;
    private ListView lv_note;
    private TextView tv_total;
    private TextView tv_symbol1;

    private String anoAtual;
    private String mesAtual;

    private FilterDialogFragment mFilterDialog;
    private NotesActivityViewModel mViewModel;
    //
    private NotesDao notesDao;
    private RecordListNotesAdapter adapter;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_screen);

        startVariables();
        startAction();

    }

    private void startVariables() {

        context = getBaseContext();

        toolbar = findViewById(R.id.toolbar);
        tv_filter = findViewById(R.id.tv_filter);
        tv_sort_by = findViewById(R.id.tv_sort_by);
        tv_year_month_filter = findViewById(R.id.tv_year_month_filter);
        lv_note = findViewById(R.id.lv_note);
        tv_total = findViewById(R.id.tv_total);
        tv_symbol1 = findViewById(R.id.tv_symbol1);
        //
        loadAdmob();

    }

    private void startAction() {
        // View model
        mViewModel = ViewModelProviders.of(this).get(NotesActivityViewModel.class);
        //
        mFilterDialog = new FilterDialogFragment();
        //
        createDirectoryDbase(context);
        toolbar.setTitle(R.string.title_invoice);
        setSupportActionBar(toolbar);
        //
        setActionOnClickActivity(R.id.btn_adicionar, NotesAddActivity.class, -1L);
        setActionOnClick(R.id.filterBar, new OnCardViewClickAction());
        setActionOnClick(R.id.btn_clear_filter, new OnClearFilterClickAction());


        lv_note.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAuxNotes item = (HMAuxNotes) parent.getItemAtPosition(position);
                String idnotas = item.get(NotesDao.IDNOTAS);
                assert idnotas != null;
                long idnotasL = Long.parseLong(idnotas);

                Intent mIntent = new Intent(context, NotesViewActivity.class);
                mIntent.putExtra(Constants.ID_BANCO, idnotasL);
                //
                startActivity(mIntent);
                //
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Aplica os filtros
        onFilter(mViewModel.getFilters());
    }

    @Override
    public void onFilter(Filters filters) {
        anoAtual = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        mesAtual = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);

        //Envia o texto referente dos filtros para a descrição do filtro na Activity
        tv_filter.setText(filters.getSearchDescription(this));
        tv_sort_by.setText(filters.getOrderDescription(this));

        if (filters.getSearchYearMonthTrue(context)) {
            tv_year_month_filter.setText(filters.getSearchYearMonth(this));
        } else {
            tv_year_month_filter.setText((context.getString(R.string.text_in) + " " + changeMonthToExtension(mesAtual) + " " + anoAtual));
        }

        notesDao = new NotesDao(context);
        //
        String month = filters.getMonth();
        String year = filters.getYear();
        String idcard = filters.getCard();
        String tipo = filters.getTipo();
        String sortBy = filters.getOrderDescriptionDB();

        //Envia os adaptadores atualizados para a ListView
        if (year == null || month == null || idcard == null || tipo == null) {
            adapter = new RecordListNotesAdapter(context, R.layout.celula_listview_notas_layout, notesDao.getListNotes(anoAtual, mesAtual, "-1", "3", NotesDao.DIA));
            lv_note.setAdapter(adapter);

            HMAuxNotes text = notesDao.getNotesTotal(anoAtual, mesAtual, "-1", "3");
            String total = text.get(TOTAL);
            if (total != null) {
                total = formatTextPrice(total);
            }
            tv_total.setText(total);
        } else {
            adapter.updateDataChanged(notesDao.getListNotes(year, month, idcard, tipo, sortBy));
            //
            HMAuxNotes text = notesDao.getNotesTotal(year, month, idcard, tipo);
            String total = text.get(TOTAL);
            if (total != null) {
                total = formatTextPrice(total);
            }
            tv_total.setText(total);
        }
        //
        tv_symbol1.setText(getCurrencySymbol());
        mViewModel.setFilters(filters);
    }

    private class OnCardViewClickAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Mostra o dialogo que contém o filtro
            mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
        }
    }

    private class OnClearFilterClickAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Reseta o filtro
            mFilterDialog = new FilterDialogFragment();
            onFilter(Filters.getDefault());
        }
    }

    public void onBackPressed() {

        callActivity(context, MenuActivity.class);
        super.onBackPressed();
    }

}
