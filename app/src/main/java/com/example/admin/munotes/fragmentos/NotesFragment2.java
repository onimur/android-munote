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
import com.example.admin.munotes.bancos.banco.RecordListNotesMonthAdapter;
import com.example.admin.munotes.bancos.dao.NotesDao;

public class NotesFragment2 extends Fragment {
    private ListView lv_note_fragment;
    private View view;
    String data2;
    String data1;
    Bundle bundle;

    private NotesFragment3 nF03;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.note_fragment, container, false);


        startVariables();
        startAction();

        return view;
    }

    private void startVariables() {
        lv_note_fragment = view.findViewById(R.id.lv_note_fragment);
        NotesDao notesDao = new NotesDao(getContext());
        bundle = this.getArguments();
        assert bundle != null;
        data1 = bundle.getString("data");
        data2 = bundle.getString("data2");

        RecordListNotesMonthAdapter adapter = new RecordListNotesMonthAdapter(getContext(), R.layout.celula_listview_ano_mes_notas, notesDao.getListMonthNotes(data1));
        lv_note_fragment.setAdapter(adapter);
    }

    private void startAction() {
            lv_note_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HMAuxNotes item = (HMAuxNotes) parent.getItemAtPosition(position);
                    //
                    data2 = (item.get(NotesDao.MES));
                    setNotesFragment();
                    //

                    //
                }
            });
       }

    private void setNotesFragment() {
        nF03 = new NotesFragment3();
        //Bundle criada pra passar a data pro fragment3;
        Bundle bundle = new Bundle();
        bundle.putString("data", data1);
        bundle.putString("data2", data2);
        nF03.setArguments(bundle);
        //
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = null;
        if (fm != null) {
            ft = fm.beginTransaction();
            ft.replace(R.id.ll_note_fragment, nF03);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}