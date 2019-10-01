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


import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.database.database.HMAuxNotes;
import com.onimus.munote.database.database.RecordListNotesAdapter;
import com.onimus.munote.database.dao.NotesDao;
import com.onimus.munote.database.model.NotesActivityViewModel;
import com.onimus.munote.database.model.Filters;
import com.onimus.munote.files.MenuToolbar;
import com.onimus.munote.fragments.FilterDialogFragment;

import java.util.Calendar;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.database.dao.NotesDao.TOTAL;
import static com.onimus.munote.files.ChangeMonth.changeMonthToExtension;
import static com.onimus.munote.files.ConvertType.convertToLong;
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

    //
    private FilterDialogFragment mFilterDialog = new FilterDialogFragment( );
    private NotesActivityViewModel mViewModel;
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
        mFilterDialog = new FilterDialogFragment( );
        //
        toolbar.setTitle(R.string.title_invoice);
        setSupportActionBar(toolbar);
        //
        setActionOnClickActivity(R.id.btn_add, NotesAddActivity.class, -1L);
        setActionOnClick(R.id.cv_filter_bar, new OnCardViewClickAction());
        setActionOnClick(R.id.btn_clear_filter, new OnClearFilterClickAction());


        lv_note.setOnItemClickListener((parent, view, position, id) -> {
            HMAuxNotes item = (HMAuxNotes) parent.getItemAtPosition(position);

            callListView(NotesViewActivity.class, convertToLong(item.get(NotesDao.ID_NOTES)));
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
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        int monthActual = (Calendar.getInstance().get(Calendar.MONTH) + 1);

        //Envia o texto referente dos filtros para a descrição do filtro na Activity
        tv_filter.setText(filters.getSearchDescription(this));
        tv_sort_by.setText(filters.getOrderDescription(this));

        if (filters.getSearchYearMonthTrue()) {
            tv_year_month_filter.setText(filters.getSearchYearMonth(this));
        } else {
            tv_year_month_filter.setText((context.getString(R.string.text_in) + " " + changeMonthToExtension(monthActual, context) + " " + yearActual));
        }

        //
        NotesDao notesDao = new NotesDao(context);
        //
        int month = filters.getMonth();
        int year = filters.getYear();
        long idCard = filters.getIdCard();
        int type = filters.getType();
        String sortBy = filters.getOrderDescriptionDB();

        //Envia os adaptadores atualizados para a ListView
        if (year == -2 || month == -2 || idCard == -2L || type == -2) {
            adapter = new RecordListNotesAdapter(context, R.layout.cel_listview_notes_layout, notesDao.getListNotes(yearActual, monthActual, -2L, 3, NotesDao.DAY));
            lv_note.setAdapter(adapter);

            HMAuxNotes text = notesDao.getNotesTotal(yearActual, monthActual, -1L, 3);
            String total = text.get(TOTAL);
            if (total != null) {
                total = formatTextPrice(total);
            }
            tv_total.setText(total);
        } else {
            adapter.updateDataChanged(notesDao.getListNotes(year, month, idCard, type, sortBy));
            //
            HMAuxNotes text = notesDao.getNotesTotal(year, month, idCard, type);
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
            mFilterDialog.show(getSupportFragmentManager(), TAG_FILTER_DIALOG);

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
