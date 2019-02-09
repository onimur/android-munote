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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;


import com.example.admin.munotes.R;
import com.example.admin.munotes.files.MenuToolbar;
import com.example.admin.munotes.fragmentos.NotesFragment1;

import static com.example.admin.munotes.bancos.DBaseDirectory.createDirectoryDbase;

public class NotesActivity extends MenuToolbar {


    private Context context;
    private Toolbar toolbar;
    private Fragment nF01;
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
        //
        nF01 = new NotesFragment1();
        //

    }

    private void startAction() {
        createDirectoryDbase(context);
        setSupportActionBar(toolbar);
        //
        setFragmentNotes();
        //

        setActionOnClickActivity(R.id.btn_adicionar, NotesAddActivity.class, -1L);


    }

    private void setFragmentNotes() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.ll_note_fragment, nF01, "f1name");
        //faz retornar o fragment ao apertar voltar
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            getSupportFragmentManager().popBackStack();
        } else {
            callActivity(context, MenuActivity.class);
            super.onBackPressed();

        }
    }


}
