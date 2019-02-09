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

package com.example.admin.munotes.bancos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import com.example.admin.munotes.bancos.banco.Dao;
import com.example.admin.munotes.bancos.banco.HMAuxCard;
import com.example.admin.munotes.bancos.model.Card;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CardDao extends Dao {

    private static final String TABELA = "cartao";
    public static final String IDCARTAO = "idcartao";
    public static final String DESCARTAO = "descartao";
    public static final String TIPO = "tipo";
    public static final String NUMBERCARD = "numbercard";

    public CardDao(Context context) {
        super(context);
    }

    public void insertCard(Card card) {
        openDataBase();
        //
        ContentValues cv = new ContentValues();
        cv.put(IDCARTAO, card.getIdcartao());
        cv.put(DESCARTAO, card.getDescartao());
        cv.put(TIPO, card.getTipo());
        cv.put(NUMBERCARD, card.getNumbercard());
        //
        db.insert(TABELA, null, cv);
        //
        closeDataBase();
    }

    public void updateCard(Card card) {
        openDataBase();
        //
        String filtro = IDCARTAO + " = ? ";
        String[] argumentos = {String.valueOf(card.getIdcartao())};
        //
        ContentValues cv = new ContentValues();
        cv.put(DESCARTAO, card.getDescartao());
        cv.put(TIPO, card.getTipo());
        cv.put(NUMBERCARD, card.getNumbercard());
        //
        db.update(TABELA, cv, filtro, argumentos);
        //
        closeDataBase();
    }

    public void deleteCard(long idcartao) {
        openDataBase();
        //
        String filtro = IDCARTAO + " = ? ";
        String[] argumentos = {String.valueOf(idcartao)};
        //
        db.delete(TABELA, filtro, argumentos);
        //
        closeDataBase();
    }


    public Card getCardById(long idcartao) {
        Card cAux = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select * from " + TABELA + " where " + IDCARTAO + " = ? ";
            String[] argumentos = {String.valueOf(idcartao)};
            //
            cursor = db.rawQuery(comando, argumentos);
            //
            while (cursor.moveToNext()) {
                cAux = new Card();
                cAux.setIdcartao(cursor.getLong(cursor.getColumnIndex(IDCARTAO)));
                cAux.setDescartao(cursor.getString(cursor.getColumnIndex(DESCARTAO)));
                cAux.setTipo(cursor.getInt(cursor.getColumnIndex(TIPO)));
                cAux.setNumbercard(cursor.getString(cursor.getColumnIndex(NUMBERCARD)));
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
        ArrayList<HMAuxCard> cartao = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select " + IDCARTAO + ", " + DESCARTAO + ", " + NUMBERCARD + ", " + TIPO + " from " + TABELA + " order by " + DESCARTAO + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxCard hmAux = new HMAuxCard();

                hmAux.put(IDCARTAO, cursor.getString(cursor.getColumnIndex(IDCARTAO)));
                hmAux.put(DESCARTAO, cursor.getString(cursor.getColumnIndex(DESCARTAO)));
                hmAux.put(NUMBERCARD, cursor.getString(cursor.getColumnIndex(NUMBERCARD)));
                hmAux.put(TIPO, cursor.getString(cursor.getColumnIndex(TIPO)));
                //
                cartao.add(hmAux);
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getListCard: ");
        }
        //
        closeDataBase();
        //
        return cartao;
    }

    public long nextID() {
        long proUD = -1L;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select max(" + IDCARTAO + ") + 1 as id from " + TABELA + " ";
            //
            cursor = db.rawQuery(comando, null);
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
