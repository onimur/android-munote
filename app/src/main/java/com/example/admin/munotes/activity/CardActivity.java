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


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.admin.munotes.R;
import com.example.admin.munotes.bancos.banco.HMAuxCard;
import com.example.admin.munotes.bancos.banco.RecordListCardAdapter;
import com.example.admin.munotes.bancos.dao.CardDao;
import com.example.admin.munotes.files.MenuToolbar;

import java.util.Objects;

import static com.example.admin.munotes.bancos.DBaseDirectory.createDirectoryDbase;

public class CardActivity extends MenuToolbar {


    private Context context;

    private ListView lv_cartao;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_screen);


        startVariables();
        startAction();


    }

    private void startVariables() {

        context = getBaseContext();

        //
        CardDao cardDao = new CardDao(context);
        //
        lv_cartao = findViewById(R.id.lv_cartao);
        toolbar = findViewById(R.id.toolbar);
        //
        createDirectoryDbase(context);
        //
        setAds();
        RecordListCardAdapter adapter = new RecordListCardAdapter(context, R.layout.celula_listview_card_layout, cardDao.getListCard());
        lv_cartao.setAdapter(adapter);
    }


    private void startAction() {

        lv_cartao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAuxCard item = (HMAuxCard) parent.getItemAtPosition(position);
                //
                callListView(CardViewActivity.class, Long.parseLong(Objects.requireNonNull(item.get(CardDao.IDCARTAO))));
            }
        });

        setSupportActionBar(toolbar);
        setActionOnClickActivity(R.id.btn_adicionar, CardAddActivity.class, -1L);


    }



    public void onBackPressed() {
        callActivity(context, MenuActivity.class);
    }


}
