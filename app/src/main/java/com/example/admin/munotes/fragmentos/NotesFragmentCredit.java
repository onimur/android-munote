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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.munotes.R;
import com.example.admin.munotes.activity.NotesViewActivity;
import com.example.admin.munotes.bancos.banco.HMAuxNotes;
import com.example.admin.munotes.bancos.banco.RecordListNotesAdapter;
import com.example.admin.munotes.bancos.dao.NotesDao;
import com.example.admin.munotes.Constants;

import java.text.DecimalFormat;
import java.util.Objects;

import static android.telephony.PhoneNumberUtils.formatNumber;
import static com.example.admin.munotes.Constants.DATA1;
import static com.example.admin.munotes.Constants.DATA2;
import static com.example.admin.munotes.bancos.dao.NotesDao.TOTAL;
import static com.example.admin.munotes.files.MoneyTextWatcher.formatPrice;
import static com.example.admin.munotes.files.MoneyTextWatcher.formatTextPrice;
import static com.example.admin.munotes.files.MoneyTextWatcher.getCurrencySymbol;

public class NotesFragmentCredit extends Fragment {

    private String mData1;
    private String mData2;
    private ListView lv_note_fragment;
    private View view;
    private TextView tv_total;
    private TextView tv_symbol1;
    private Activity activity;
    private NotesDao notesDao;

    public static NotesFragmentCredit newInstance(String data1, String data2) {
        NotesFragmentCredit fragment = new NotesFragmentCredit();
        Bundle args = new Bundle();
        args.putString(DATA1, data1);
        args.putString(DATA2, data2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.note_fragment, container, false);


        startVariables();
        startAction();

        return view;
    }

    private void startVariables() {
        lv_note_fragment = view.findViewById(R.id.lv_note_fragment);
        tv_total = view.findViewById(R.id.tv_total);
        tv_symbol1 = view.findViewById(R.id.tv_symbol1);

        notesDao = new NotesDao(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null){
            mData1 = bundle.getString(DATA1);
            mData2 = bundle.getString(DATA2);
        }


        RecordListNotesAdapter adapter = new RecordListNotesAdapter(getContext(), R.layout.celula_listview_notas_layout, notesDao.getListDayNotesCredit(mData1, mData2));
        lv_note_fragment.setAdapter(adapter);
    }

    private void startAction() {

        HMAuxNotes text = notesDao.getNotesCreditTotal(mData1, mData2);
        String total = text.get(TOTAL);
        if (total != null){
            total = formatTextPrice(total);
        }
        tv_total.setText(total);
        //
        tv_symbol1.setText(getCurrencySymbol());

        lv_note_fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAuxNotes item = (HMAuxNotes) parent.getItemAtPosition(position);
                //
                callActivityById(NotesViewActivity.class, Long.parseLong(Objects.requireNonNull(item.get(NotesDao.IDNOTAS))));
                //
            }
        });
    }

    public void callActivityById(Class<?> _class, long id) {
        Intent mIntent = new Intent(getContext(), _class);
        mIntent.putExtra(Constants.ID_BANCO, id);
        //
        startActivity(mIntent);
        //
        activity.finish();
    }

}
