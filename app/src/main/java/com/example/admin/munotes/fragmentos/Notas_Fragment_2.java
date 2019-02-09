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

public class Notas_Fragment_2 extends Fragment {
    private ListView lv_note_fragment;
    private View view;
    String data2;
    String data1;
    Bundle bundle;

    private Notas_Fragment_3 nF03;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.note_fragment, container, false);


        iniciarVariaveis();
        iniciarAcoes();

        return view;
    }

    private void iniciarVariaveis() {
        lv_note_fragment = view.findViewById(R.id.lv_note_fragment);
        NotesDao notesDao = new NotesDao(getContext());
        bundle = this.getArguments();
        assert bundle != null;
        data1 = bundle.getString("data");
        data2 = bundle.getString("data2");

        RecordListNotesMonthAdapter adapter = new RecordListNotesMonthAdapter(getContext(), R.layout.celula_listview_ano_mes_notas, notesDao.getListMonthNotes(data1));
        lv_note_fragment.setAdapter(adapter);
    }

    private void iniciarAcoes() {
            lv_note_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HMAuxNotes item = (HMAuxNotes) parent.getItemAtPosition(position);
                    //
                    data2 = (item.get(NotesDao.MES));
                    setFragmentoNotas();
                    //

                    //
                }
            });
       }

    private void setFragmentoNotas() {
        nF03 = new Notas_Fragment_3();
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
