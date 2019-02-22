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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;


import com.example.admin.munotes.R;
import com.example.admin.munotes.bancos.banco.HMAuxNotes;
import com.example.admin.munotes.bancos.banco.RecordSpinnerNotesMonthAdapter;
import com.example.admin.munotes.bancos.banco.RecordSpinnerNotesYearAdapter;
import com.example.admin.munotes.bancos.dao.NotesDao;
import com.example.admin.munotes.files.MenuToolbar;
import com.example.admin.munotes.files.ViewPagerAdapter;
import com.example.admin.munotes.fragmentos.NotesFragmentBoth;
import com.example.admin.munotes.fragmentos.NotesFragmentCredit;
import com.example.admin.munotes.fragmentos.NotesFragmentDebit;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.admin.munotes.Constants.DATA1;
import static com.example.admin.munotes.Constants.DATA2;
import static com.example.admin.munotes.bancos.DBaseDirectory.createDirectoryDbase;

public class NotesActivity extends MenuToolbar {


    private Context context;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Spinner sp_year;
    private Spinner sp_month;
    private NotesDao notesDao;
    private ArrayList<HMAuxNotes> hmAux;

    private String anoAtual;
    private String mesAtual;
    private int positionFragment = -1;

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
        notesDao = new NotesDao(context);

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        sp_year = findViewById(R.id.sp_year);
        sp_month = findViewById(R.id.sp_month);
        //
        setAds();


    }

    private void startAction() {
        anoAtual = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        mesAtual = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        createDirectoryDbase(context);
        setSupportActionBar(toolbar);
        //
        setArrowToSpinnerLowerVersion();
        //
        RecordSpinnerNotesYearAdapter adapter = new RecordSpinnerNotesYearAdapter(context, R.layout.celula_spinner_year_layout, notesDao.getListYearNotes());

        sp_year.setAdapter(adapter);
        //
        //
        sp_year.setSelection(getSpinnerYearIndex(sp_year, anoAtual));
        //
        setActionOnClick(R.id.sp_year, new OnSpinnerYearClickAction());
        //
        setFragmentNotes(anoAtual, mesAtual);
        setActionOnClickActivity(R.id.btn_adicionar, NotesAddActivity.class, -1L);


    }

    private void setFragmentNotes(String newYear, String month) {


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), context, newYear, month);
        //
        viewPager.setAdapter(adapter);

        //recria o fragment na posição que o usuário estava
        if (positionFragment!= -1){
            viewPager.setCurrentItem(positionFragment);

        }

        tabLayout.setupWithViewPager(viewPager);


    }

    //Verifica em qual posição está o ANO;
    public int getSpinnerYearIndex(Spinner spinner, String ano) {
        int index = 0;
        hmAux = notesDao.getListYearNotes();

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxNotes model = hmAux.get(i);
            String modelS = model.get(NotesDao.ANO);
            if (modelS != null) {
                if (modelS.equals(ano)) {
                    index = i;
                }
            }
        }
        return index;
    }

    //Verifica em qual posição está o mês;
    public int getSpinnerMonthIndex(Spinner spinner, String mes, String ano) {
        int index = 0;
        hmAux = notesDao.getListMonthNotes(ano);

        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxNotes model = hmAux.get(i);
            String modelS = model.get(NotesDao.MES);
            if (modelS != null) {
                if (modelS.equals(mes)) {
                    index = i;
                }
            }
        }
        return index;
    }

    private class OnSpinnerYearClickAction implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Ao clicar no item do sp_year, incializa o sp_month, retornando no spinner os mês já cadastrados e caso o ano seja atual, o mes retornado é o atual também.
            sp_year.setSelection(position);
            HMAuxNotes item = (HMAuxNotes) parent.getSelectedItem();
            String newYear = item.get(NotesDao.ANO);

            RecordSpinnerNotesMonthAdapter adapter2 = new RecordSpinnerNotesMonthAdapter(context, R.layout.celula_spinner_month_layout, notesDao.getListMonthNotes(newYear));
            sp_month.setAdapter(adapter2);

            assert newYear != null;
            if (newYear.equals(anoAtual)) {
                sp_month.setSelection(getSpinnerMonthIndex(sp_month, mesAtual, anoAtual));
            } else {
                //envia a ultima posição do mês
                //caso fosse a ultima: adapter2.getCount() -1
                sp_month.setSelection(0);
            }
            //inicializa o spinner_month
            setActionOnClick(R.id.sp_month, new OnSpinnerMonthClickAction(newYear));

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnSpinnerMonthClickAction implements AdapterView.OnItemSelectedListener {
        String newYear;

        OnSpinnerMonthClickAction(String newYear) {
            this.newYear = newYear;

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //recupera o mês selecionado e inicia o fragment retornando uma listview referente ao ano e mes passados
            HMAuxNotes item = (HMAuxNotes) parent.getSelectedItem();
            String month = item.get(NotesDao.MES);
            sp_month.setSelection(position);
            //recupera a posição do fragment atual;
           positionFragment = viewPager.getCurrentItem();

            setFragmentNotes(newYear, month);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void onBackPressed() {

            callActivity(context, MenuActivity.class);
            super.onBackPressed();
    }


}
