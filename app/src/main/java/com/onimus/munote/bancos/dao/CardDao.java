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

package com.onimus.munote.bancos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import com.onimus.munote.R;
import com.onimus.munote.bancos.banco.Dao;
import com.onimus.munote.bancos.banco.HMAuxCard;
import com.onimus.munote.bancos.model.Card;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CardDao extends Dao {

    private static final String TABLE = "cartao";
    public static final String ID_CARD = "idcartao";
    public static final String DESC_CARD = "descartao";
    public static final String TYPE = "tipo";
    private Context context;

    public CardDao(Context context) {
        super(context);
        this.context = context;
    }

    public void insertCard(Card card) {
        openDataBase();
        //
        ContentValues cv = new ContentValues();
        cv.put(ID_CARD, card.getIdCard());
        cv.put(DESC_CARD, card.getDescCard());
        cv.put(TYPE, card.getType());
        //
        db.insert(TABLE, null, cv);
        //
        closeDataBase();
    }

    public void updateCard(Card card) {
        openDataBase();
        //
        String filter = ID_CARD + " = ? ";
        String[] arguments = {String.valueOf(card.getIdCard())};
        //
        ContentValues cv = new ContentValues();
        cv.put(DESC_CARD, card.getDescCard());
        cv.put(TYPE, card.getType());
        //
        db.update(TABLE, cv, filter, arguments);
        //
        closeDataBase();
    }

    public void deleteCard(long idCard) {
        openDataBase();
        //
        String filter = ID_CARD + " = ? ";
        String[] arguments = {String.valueOf(idCard)};
        //
        db.delete(TABLE, filter, arguments);
        //
        closeDataBase();
    }


    public Card getCardById(long idCard) {
        Card cAux = null;
        //
        openDataBase();
        //
        Cursor cursor;
        //
        try {

            String command = " select * from " + TABLE + " where " + ID_CARD + " = ? ";
            String[] arguments = {String.valueOf(idCard)};
            //
            cursor = db.rawQuery(command, arguments);
            //
            while (cursor.moveToNext()) {
                cAux = new Card();
                cAux.setIdCard(cursor.getLong(cursor.getColumnIndex(ID_CARD)));
                cAux.setDescCard(cursor.getString(cursor.getColumnIndex(DESC_CARD)));
                cAux.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getCardById: ");
        }
        //
        closeDataBase();
        //
        return cAux;
    }

    public ArrayList<HMAuxCard> getListCard() {
        ArrayList<HMAuxCard> card = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor;
        //
        try {

            String command = " select " + ID_CARD + ", " + DESC_CARD + ", " + TYPE + " from " + TABLE + " order by " + DESC_CARD + " ";
            //
            cursor = db.rawQuery(command, null);
            //
            while (cursor.moveToNext()) {
                HMAuxCard hmAux = new HMAuxCard();

                hmAux.put(ID_CARD, cursor.getString(cursor.getColumnIndex(ID_CARD)));
                hmAux.put(DESC_CARD, cursor.getString(cursor.getColumnIndex(DESC_CARD)));
                hmAux.put(TYPE, cursor.getString(cursor.getColumnIndex(TYPE)));
                //
                card.add(hmAux);
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getListCard: ");
        }
        //
        closeDataBase();
        //
        return card;
    }

    public ArrayList<HMAuxCard> getListCardOnFilter() {
        ArrayList<HMAuxCard> card = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor;
        //
        try {

            String command = " select " + ID_CARD + ", " + DESC_CARD + ", " + TYPE + " from " + TABLE + " order by " + DESC_CARD + " ";
            //
            cursor = db.rawQuery(command, null);
            //
            while (cursor.moveToNext()) {
                HMAuxCard hmAux = new HMAuxCard();

                hmAux.put(ID_CARD, cursor.getString(cursor.getColumnIndex(ID_CARD)));
                hmAux.put(DESC_CARD, cursor.getString(cursor.getColumnIndex(DESC_CARD)));
                hmAux.put(TYPE, cursor.getString(cursor.getColumnIndex(TYPE)));
                //
                card.add(hmAux);
            }
            //
            HMAuxCard hmAux = new HMAuxCard();
            hmAux.put(DESC_CARD, context.getString(R.string.all_cards));
            hmAux.put(ID_CARD, "-1");
            hmAux.put(TYPE, "-1");
            card.add(0, hmAux);
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getListCard: ");
        }
        //
        closeDataBase();
        //
        return card;
    }

    public long nextID() {
        long proUD = -1L;
        //
        openDataBase();
        //
        Cursor cursor;
        //
        try {

            String command = " select max(" + ID_CARD + ") + 1 as id from " + TABLE + " ";
            //
            cursor = db.rawQuery(command, null);
            //
            while (cursor.moveToNext()) {
                proUD = cursor.getLong(cursor.getColumnIndex("id"));

            }
            //
            if (proUD == 0) {
                proUD = 1;
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "nextID: ");
        }
        //
        closeDataBase();

        //
        return proUD;
    }

}
