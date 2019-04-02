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

import com.onimus.munote.bancos.banco.Dao;
import com.onimus.munote.bancos.banco.HMAuxNotes;
import com.onimus.munote.bancos.model.Notes;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class NotesDao extends Dao {

    private static final String TABLE = "notas";
    public static final String TITLE_NOTES = "titlenotas";
    public static final String ID_NOTES = "idnotas";
    public static final String ID_CARD = "idcartao";
    public static final String DESC_NOTES = "desnotas";
    public static final String PRICE_NOTES = "preconotas";
    public static final String PHOTO_NOTES = "fotonotas";
    public static final String YEAR = "ano";
    public static final String MONTH = "mes";
    public static final String DAY = "dia";
    public static final String TYPE = "tipo";
    public static final String PARCELS = "parcelas";
    public static final String TOTAL = "total";

    public NotesDao(Context context) {
        super(context);
    }

    public void insertNotes(Notes notes) {
        openDataBase();
        //
        ContentValues cv = new ContentValues();
        cv.put(ID_NOTES, notes.getIdNotes());
        cv.put(DESC_NOTES, notes.getDescNotes());
        cv.put(TITLE_NOTES, notes.getTitleNotes());
        cv.put(PRICE_NOTES, notes.getPriceNotes());
        cv.put(PHOTO_NOTES, notes.getPhotoNotes());
        cv.put(ID_CARD, notes.getIdCard());
        cv.put(YEAR, notes.getYear());
        cv.put(MONTH, notes.getMonth());
        cv.put(DAY, notes.getDay());
        cv.put(TYPE, notes.getType());
        cv.put(PARCELS, notes.getParcels());
        //
        db.insert(TABLE, null, cv);
        //
        closeDataBase();
    }

    public void updateNotes(Notes notes) {
        openDataBase();
        //
        String filtro = ID_NOTES + " = ? ";
        String[] argumentos = {String.valueOf(notes.getIdNotes())};
        //
        ContentValues cv = new ContentValues();
        cv.put(DESC_NOTES, notes.getDescNotes());
        cv.put(TITLE_NOTES, notes.getTitleNotes());
        cv.put(PRICE_NOTES, notes.getPriceNotes());
        cv.put(PHOTO_NOTES, notes.getPhotoNotes());
        cv.put(ID_CARD, notes.getIdCard());
        cv.put(YEAR, notes.getYear());
        cv.put(MONTH, notes.getMonth());
        cv.put(DAY, notes.getDay());
        cv.put(TYPE, notes.getType());
        cv.put(PARCELS, notes.getParcels());
        //
        db.update(TABLE, cv, filtro, argumentos);
        //
        closeDataBase();
    }

    public void updateDeletedCard(long idCard) {
        openDataBase();
        //
        String filtro = ID_CARD + " = ? ";
        String[] argumentos = {String.valueOf(idCard)};
        //
        ContentValues cv = new ContentValues();
        cv.put(ID_CARD, -1);
        //
        db.update(TABLE, cv, filtro, argumentos);
        //
        closeDataBase();
    }

    public void deleteNotes(long idNotes) {
        openDataBase();
        //
        String filtro = ID_NOTES + " = ? ";
        String[] argumentos = {String.valueOf(idNotes)};
        //
        db.delete(TABLE, filtro, argumentos);
        //
        closeDataBase();
    }


    public Notes getNotesById(long idNotes) {
        Notes cAux = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select * from " + TABLE + " where " + ID_NOTES + " = ? ";
            String[] argumentos = {String.valueOf(idNotes)};
            //
            cursor = db.rawQuery(comando, argumentos);
            //
            while (cursor.moveToNext()) {
                cAux = new Notes();
                cAux.setIdNotes(cursor.getLong(cursor.getColumnIndex(ID_NOTES)));
                cAux.setIdCard(cursor.getLong(cursor.getColumnIndex(ID_CARD)));
                cAux.setYear(cursor.getLong(cursor.getColumnIndex(YEAR)));
                cAux.setMonth(cursor.getLong(cursor.getColumnIndex(MONTH)));
                cAux.setDay(cursor.getLong(cursor.getColumnIndex(DAY)));
                cAux.setTitleNotes(cursor.getString(cursor.getColumnIndex(TITLE_NOTES)));
                cAux.setDescNotes(cursor.getString(cursor.getColumnIndex(DESC_NOTES)));
                cAux.setPriceNotes(cursor.getString(cursor.getColumnIndex(PRICE_NOTES)));
                cAux.setPhotoNotes(cursor.getString(cursor.getColumnIndex(PHOTO_NOTES)));
                cAux.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
                cAux.setParcels(cursor.getInt(cursor.getColumnIndex(PARCELS)));
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getNotesById: ");
        }
        //
        closeDataBase();
        //
        return cAux;
    }

    public ArrayList<HMAuxNotes> getListYearNotes() {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select distinct " + YEAR +
                    " from " + TABLE + " order by " + YEAR + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();

                hmAux.put(YEAR, cursor.getString(cursor.getColumnIndex(YEAR)));
                //
                notas.add(hmAux);
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getListYearNotes: ");
        }
        //
        closeDataBase();
        //
        return notas;
    }

    public ArrayList<HMAuxNotes> getListMonthNotes(String year) {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select distinct " + MONTH +
                    " from " + TABLE + " where " + YEAR + " = '" + year + "' order by " + MONTH + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();

                hmAux.put(MONTH, cursor.getString(cursor.getColumnIndex(MONTH)));
                //
                notas.add(hmAux);
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getListMonthNotes: ");
        }
        //
        closeDataBase();
        //
        return notas;
    }

    public ArrayList<HMAuxNotes> getListNotes(String year, String month, String idCard, String type, String orderBy) {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        String comando;
        //
        try {

            if (!idCard.equals("-1") && !type.equals("3")) {
                comando = " select " +
                        ID_NOTES + ", " +
                        DESC_NOTES + ", " +
                        TITLE_NOTES + ", " +
                        PRICE_NOTES + ", " + DAY +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' and " + ID_CARD + " = '" + idCard + "' and " + TYPE + " = '" + type + "' order by " + orderBy + " ";
            } else if (idCard.equals("-1") && !type.equals("3")) {
                comando = " select " +
                        ID_NOTES + ", " +
                        DESC_NOTES + ", " +
                        TITLE_NOTES + ", " +
                        PRICE_NOTES + ", " + DAY +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' and " + TYPE + " = '" + type + "' order by " + orderBy + " ";
            } else if (!idCard.equals("-1") && type.equals("3")) {
                comando = " select " +
                        ID_NOTES + ", " +
                        DESC_NOTES + ", " +
                        TITLE_NOTES + ", " +
                        PRICE_NOTES + ", " + DAY +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' and " + ID_CARD + " = '" + idCard + "' order by " + orderBy + " ";
            } else {
                comando = " select " +
                        ID_NOTES + ", " +
                        DESC_NOTES + ", " +
                        TITLE_NOTES + ", " +
                        PRICE_NOTES + ", " + DAY +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' order by " + orderBy + " ";
            }

            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(ID_NOTES, cursor.getString(cursor.getColumnIndex(ID_NOTES)));
                hmAux.put(DESC_NOTES, cursor.getString(cursor.getColumnIndex(DESC_NOTES)));
                hmAux.put(DAY, cursor.getString(cursor.getColumnIndex(DAY)));
                hmAux.put(TITLE_NOTES, cursor.getString(cursor.getColumnIndex(TITLE_NOTES)));
                hmAux.put(PRICE_NOTES, cursor.getString(cursor.getColumnIndex(PRICE_NOTES)));

                //
                notas.add(hmAux);
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getListNotes: ");
        }
        //
        closeDataBase();
        //
        return notas;
    }

    public HMAuxNotes getNotesTotal(String year, String month, String idCard, String type) {
        HMAuxNotes notas = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        String comando;
        //
        try {
            if (!idCard.equals("-1") && !type.equals("3")) {
                comando = " select SUM(" + PRICE_NOTES + ") as " + TOTAL +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' and " + ID_CARD + " = '" + idCard + "' and " + TYPE + " = '" + type + "' order by " + DAY + " ";

            } else if (idCard.equals("-1") && !type.equals("3")) {
                comando = " select SUM(" + PRICE_NOTES + ") as " + TOTAL +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' and " + TYPE + " = '" + type + "' order by " + DAY + " ";

            } else if (!idCard.equals("-1") && type.equals("3")) {
                comando = " select SUM(" + PRICE_NOTES + ") as " + TOTAL +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' and " + ID_CARD + " = '" + idCard + "' order by " + DAY + " ";
            } else {
                comando = " select SUM(" + PRICE_NOTES + ") as " + TOTAL +
                        " from " + TABLE + " where " + YEAR + " = '" + year + "' and " + MONTH + " = '" + month + "' order by " + DAY + " ";
            }
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(TOTAL, String.valueOf(cursor.getDouble(cursor.getColumnIndex(TOTAL))));

                //
                notas = hmAux;
            }
            //
            cursor.close();

        } catch (Exception e) {
            Log.e(TAG, "getNotesTotal: ");
        }
        //
        closeDataBase();
        //
        return notas;
    }

    public long nextID() {
        long proUD = -1L;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select max(" + ID_NOTES + ") + 1 as id from " + TABLE + " ";
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
