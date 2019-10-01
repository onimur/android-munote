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


import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.ListView;

import com.onimus.munote.R;
import com.onimus.munote.database.database.HMAuxCard;
import com.onimus.munote.database.database.RecordListCardAdapter;
import com.onimus.munote.database.dao.CardDao;
import com.onimus.munote.files.MenuToolbar;

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
        loadAdmob();
        //
        RecordListCardAdapter adapter = new RecordListCardAdapter(context, R.layout.cel_listview_card_layout, cardDao.getListCard());
        lv_card.setAdapter(adapter);
    }

    private void startAction() {

        lv_card.setOnItemClickListener((parent, view, position, id) -> {
            HMAuxCard item = (HMAuxCard) parent.getItemAtPosition(position);
            //
            callListView(CardViewActivity.class, convertToLong(item.get(CardDao.ID_CARD)));
        });

        setSupportActionBar(toolbar);
        setActionOnClickActivity(R.id.btn_add, CardAddActivity.class, -1L);
    }

    public void onBackPressed() {
        callActivity(context, MenuActivity.class);
    }

}
