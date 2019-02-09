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

package com.example.admin.munotes.fragmentos;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.munotes.R;
import com.example.admin.munotes.bancos.banco.HMAuxNotes;
import com.example.admin.munotes.bancos.banco.RecordListNotesYearAdapter;
import com.example.admin.munotes.bancos.dao.NotesDao;

public class Notas_Fragment_1 extends Fragment {
    private ListView lv_note_fragment;
    private View view;
    private Fragment nF02;
    private HMAuxNotes item;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.note_fragment, container, false);


        iniciarVariaveis();
        iniciarAcoes();

        return view;
    }

    private void iniciarVariaveis() {


        lv_note_fragment = view.findViewById(R.id.lv_note_fragment);
        NotesDao notesDao = new NotesDao(getContext());
        //

        RecordListNotesYearAdapter adapter = new RecordListNotesYearAdapter(getContext(), R.layout.celula_listview_ano_mes_notas, notesDao.getListYearNotes());
        lv_note_fragment.setAdapter(adapter);
        nF02 = new Notas_Fragment_2();
    }


    private void iniciarAcoes() {
        lv_note_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (HMAuxNotes) parent.getItemAtPosition(position);
                //
                setFragmentoNotas();
                //
            }
        });
    }
        private void setFragmentoNotas() {
        //Bundle criada pra passar a data pro fragment2;
        String data = (item.get(NotesDao.ANO));
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        nF02.setArguments(bundle);
        //
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = null;
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.replace(R.id.ll_note_fragment, nF02);
            ft.addToBackStack(null);
            ft.commit();
        }

    }

}
