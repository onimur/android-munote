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
import com.example.admin.munotes.bancos.banco.HMAuxNotes;
import com.example.admin.munotes.bancos.model.Notes;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;



public class NotesDao extends Dao {

    private static final String TABELANOTAS = "notas";
    public static final String TITLENOTAS = "titlenotas";
    public static final String IDNOTAS = "idnotas";
    public static final String IDCARTAO = "idcartao";
    public static final String DESNOTAS = "desnotas";
    public static final String PRECONOTAS = "preconotas";
    public static final String FOTONOTAS = "fotonotas";
    public static final String ANO = "ano";
    public static final String MES = "mes";
    public static final String DIA = "dia";
    public static final String TIPO = "tipo";
    public static final String PARCELAS = "parcelas";
    public static final String TOTAL = "total";


    public NotesDao(Context context) {
        super(context);
    }

    public void insertNotes(Notes notes) {
        openDataBase();
        //
        ContentValues cv = new ContentValues();
        cv.put(IDNOTAS, notes.getIdnotas());
        cv.put(DESNOTAS, notes.getDesnotas());
        cv.put(TITLENOTAS, notes.getTitlenotas());
        cv.put(PRECONOTAS, notes.getPreconotas());
        cv.put(FOTONOTAS, notes.getFotonotas());
        cv.put(IDCARTAO, notes.getIdcartao());
        cv.put(ANO, notes.getAno());
        cv.put(MES, notes.getMes());
        cv.put(DIA, notes.getDia());
        cv.put(TIPO,notes.getTipo());
        cv.put(PARCELAS, notes.getParcelas());
        //
        db.insert(TABELANOTAS, null, cv);
        //
        closeDataBase();
    }

    public void updateNotes(Notes notes) {
        openDataBase();
        //
        String filtro = IDNOTAS + " = ? ";
        String[] argumentos = {String.valueOf(notes.getIdnotas())};
        //
        ContentValues cv = new ContentValues();
        cv.put(DESNOTAS, notes.getDesnotas());
        cv.put(TITLENOTAS, notes.getTitlenotas());
        cv.put(PRECONOTAS, notes.getPreconotas());
        cv.put(FOTONOTAS, notes.getFotonotas());
        cv.put(IDCARTAO, notes.getIdcartao());
        cv.put(ANO, notes.getAno());
        cv.put(MES, notes.getMes());
        cv.put(DIA, notes.getDia());
        cv.put(TIPO,notes.getTipo());
        cv.put(PARCELAS, notes.getParcelas());
        //
        db.update(TABELANOTAS, cv, filtro, argumentos);
        //
        closeDataBase();
    }

    public void updateDeletedCard(long idCartao) {
        openDataBase();
        //
        String filtro = IDCARTAO + " = ? ";
        String[] argumentos = {String.valueOf(idCartao)};
        //
        ContentValues cv = new ContentValues();
        cv.put(IDCARTAO, -1);
       //
        db.update(TABELANOTAS, cv, filtro, argumentos);
        //
        closeDataBase();
    }

    public void deleteNotes(long idnotas) {
        openDataBase();
        //
        String filtro = IDNOTAS + " = ? ";
        String[] argumentos = {String.valueOf(idnotas)};
        //
        db.delete(TABELANOTAS, filtro, argumentos);
        //
        closeDataBase();
    }


    public Notes getNotesById(long idnotas) {
        Notes cAux = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select * from " + TABELANOTAS + " where " + IDNOTAS + " = ? ";
            String[] argumentos = {String.valueOf(idnotas)};
            //
            cursor = db.rawQuery(comando, argumentos);
            //
            while (cursor.moveToNext()) {
                cAux = new Notes();
                cAux.setIdnotas(cursor.getLong(cursor.getColumnIndex(IDNOTAS)));
                cAux.setIdcartao(cursor.getLong(cursor.getColumnIndex(IDCARTAO)));
                cAux.setAno(cursor.getLong(cursor.getColumnIndex(ANO)));
                cAux.setMes(cursor.getLong(cursor.getColumnIndex(MES)));
                cAux.setDia(cursor.getLong(cursor.getColumnIndex(DIA)));
                cAux.setTitlenotas(cursor.getString(cursor.getColumnIndex(TITLENOTAS)));
                cAux.setDesnotas(cursor.getString(cursor.getColumnIndex(DESNOTAS)));
                cAux.setPreconotas(cursor.getString(cursor.getColumnIndex(PRECONOTAS)));
                cAux.setFotonotas(cursor.getString(cursor.getColumnIndex(FOTONOTAS)));
                cAux.setTipo(cursor.getInt(cursor.getColumnIndex(TIPO)));
                cAux.setParcelas(cursor.getInt(cursor.getColumnIndex(PARCELAS)));
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

    public ArrayList<HMAuxNotes> getListNotes() {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select " +
                    IDNOTAS + ", " + IDCARTAO + ", " + DESNOTAS + ", " +
                    PRECONOTAS + ", " + ANO + ", " + FOTONOTAS + ", " + MES + ", " + DIA +
                    " from " + TABELANOTAS + " order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();

                hmAux.put(IDNOTAS, cursor.getString(cursor.getColumnIndex(IDNOTAS)));
                hmAux.put(IDCARTAO, cursor.getString(cursor.getColumnIndex(IDCARTAO)));
                hmAux.put(ANO, cursor.getString(cursor.getColumnIndex(ANO)));
                hmAux.put(MES, cursor.getString(cursor.getColumnIndex(MES)));
                hmAux.put(DIA, cursor.getString(cursor.getColumnIndex(DIA)));
                hmAux.put(DESNOTAS, cursor.getString(cursor.getColumnIndex(DESNOTAS)));
                hmAux.put(TITLENOTAS, cursor.getString(cursor.getColumnIndex(TITLENOTAS)));
                hmAux.put(PRECONOTAS, cursor.getString(cursor.getColumnIndex(PRECONOTAS)));
                hmAux.put(TIPO, cursor.getString(cursor.getColumnIndex(TIPO)));
                hmAux.put(PARCELAS, cursor.getString(cursor.getColumnIndex(PARCELAS)));

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

    public ArrayList<HMAuxNotes> getListYearNotes() {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select distinct " + ANO +
                    " from " + TABELANOTAS + " order by " + ANO + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();

                hmAux.put(ANO, cursor.getString(cursor.getColumnIndex(ANO)));
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

    public ArrayList<HMAuxNotes> getListMonthNotes(String ano) {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select distinct " + MES +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' order by " + MES + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();

                hmAux.put(MES, cursor.getString(cursor.getColumnIndex(MES)));
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
    public ArrayList<HMAuxNotes> getListDayNotesBoth(String ano, String mes) {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select " +
                    IDNOTAS + ", " +
                    DESNOTAS + ", " +
                    TITLENOTAS + ", " +
                    PRECONOTAS + ", " + DIA +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' and "+ MES +" = '"+ mes + "' order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(IDNOTAS, cursor.getString(cursor.getColumnIndex(IDNOTAS)));
                hmAux.put(DESNOTAS, cursor.getString(cursor.getColumnIndex(DESNOTAS)));
                hmAux.put(DIA, cursor.getString(cursor.getColumnIndex(DIA)));
                hmAux.put(TITLENOTAS, cursor.getString(cursor.getColumnIndex(TITLENOTAS)));
                hmAux.put(PRECONOTAS, cursor.getString(cursor.getColumnIndex(PRECONOTAS)));

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

    public ArrayList<HMAuxNotes> getListDayNotesCredit(String ano, String mes) {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select " +
                    IDNOTAS + ", " +
                    DESNOTAS + ", " +
                    TITLENOTAS + ", " +
                    PRECONOTAS + ", " + DIA +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' and "+ MES +" = '"+ mes + "' and "+ TIPO + " = 1" + " order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(IDNOTAS, cursor.getString(cursor.getColumnIndex(IDNOTAS)));
                hmAux.put(DESNOTAS, cursor.getString(cursor.getColumnIndex(DESNOTAS)));
                hmAux.put(DIA, cursor.getString(cursor.getColumnIndex(DIA)));
                hmAux.put(TITLENOTAS, cursor.getString(cursor.getColumnIndex(TITLENOTAS)));
                hmAux.put(PRECONOTAS, cursor.getString(cursor.getColumnIndex(PRECONOTAS)));

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

    public ArrayList<HMAuxNotes> getListDayNotesDebit(String ano, String mes) {
        ArrayList<HMAuxNotes> notas = new ArrayList<>();
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select " +
                    IDNOTAS + ", " +
                    DESNOTAS + ", " +
                    TITLENOTAS + ", " +
                    PRECONOTAS + ", " + DIA +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' and "+ MES +" = '"+ mes + "' and "+ TIPO + " = 2" + " order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(IDNOTAS, cursor.getString(cursor.getColumnIndex(IDNOTAS)));
                hmAux.put(DESNOTAS, cursor.getString(cursor.getColumnIndex(DESNOTAS)));
                hmAux.put(DIA, cursor.getString(cursor.getColumnIndex(DIA)));
                hmAux.put(TITLENOTAS, cursor.getString(cursor.getColumnIndex(TITLENOTAS)));
                hmAux.put(PRECONOTAS, cursor.getString(cursor.getColumnIndex(PRECONOTAS)));

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

    public HMAuxNotes getNotesCreditTotal(String ano, String mes) {
        HMAuxNotes notas = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select SUM(" +PRECONOTAS +") as " + TOTAL +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' and "+ MES +" = '"+ mes + "' and "+ TIPO + " = 1" + " order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(TOTAL, cursor.getString(cursor.getColumnIndex(TOTAL)));

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

    public HMAuxNotes getNotesDebitTotal(String ano, String mes) {
        HMAuxNotes notas = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select SUM(" +PRECONOTAS +") as " + TOTAL +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' and "+ MES +" = '"+ mes + "' and "+ TIPO + " = 2" + " order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(TOTAL, cursor.getString(cursor.getColumnIndex(TOTAL)));

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

    public HMAuxNotes getNotesBothTotal(String ano, String mes) {
        HMAuxNotes notas = null;
        //
        openDataBase();
        //
        Cursor cursor = null;
        //
        try {

            String comando = " select SUM(" +PRECONOTAS +") as " + TOTAL +
                    " from " + TABELANOTAS + " where " + ANO + " = '" + ano + "' and "+ MES +" = '"+ mes  + "' order by " + DIA + " ";
            //
            cursor = db.rawQuery(comando, null);
            //
            while (cursor.moveToNext()) {
                HMAuxNotes hmAux = new HMAuxNotes();
                hmAux.put(TOTAL, cursor.getString(cursor.getColumnIndex(TOTAL)));

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

            String comando = " select max(" + IDNOTAS + ") + 1 as id from " + TABELANOTAS + " ";
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
