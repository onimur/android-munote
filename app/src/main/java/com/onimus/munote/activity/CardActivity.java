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


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.HMAuxCard;
import com.onimus.munote.bancos.banco.RecordListCardAdapter;
import com.onimus.munote.bancos.dao.CardDao;
import com.onimus.munote.files.MenuToolbar;

import static com.onimus.munote.bancos.DBaseDirectory.createDirectoryDbase;
import static com.onimus.munote.files.ConvertType.convertToLong;

public class CardActivity extends MenuToolbar {

    private Context context;

    private ListView lv_card;

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
        lv_card = findViewById(R.id.lv_card);
        toolbar = findViewById(R.id.toolbar);
        //
        createDirectoryDbase(context);
        //
        loadAdmob();
        //
        RecordListCardAdapter adapter = new RecordListCardAdapter(context, R.layout.cel_listview_card_layout, cardDao.getListCard());
        lv_card.setAdapter(adapter);
    }

    private void startAction() {

        lv_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HMAuxCard item = (HMAuxCard) parent.getItemAtPosition(position);
                //
                callListView(CardViewActivity.class, convertToLong(item.get(CardDao.ID_CARD)));
            }
        });

        setSupportActionBar(toolbar);
        setActionOnClickActivity(R.id.btn_add, CardAddActivity.class, -1L);
    }

    public void onBackPressed() {
        callActivity(context, MenuActivity.class);
    }

}
