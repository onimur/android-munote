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

package com.onimus.munote.repository.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;



class SqlHelper extends SQLiteOpenHelper {

    private static final String TABLE_CARD =      "[cartao]";
    private static final String TABLE_CARD_BCK =  "[cartao_bck]";
    private static final String COLUMN_ID_CARD =   "[idcartao]";
    private static final String COLUMN_DESC_CARD =  "[descartao]";
    private static final String COLUMN_NUMBER_CARD = "[numbercard]";
    private static final String COLUMN_TYPE_CRED =   "[tipocred]";
    private static final String COLUMN_TYPE_DEB =    "[tipodeb]";
    private static final String COLUMN_TYPE =       "[tipo]";
    //
    private static final String TABLE_NOTES =       "[notas]";
    private static final String TABLE_NOTES_BCK =    "[notas_bck]";
    private static final String COLUMN_ID_NOTES =    "[idnotas]";
    private static final String COLUMN_TITLE_NOTES = "[titlenotas]";
    private static final String COLUMN_DESC_NOTES =   "[desnotas]";
    private static final String COLUMN_PRICE_NOTES = "[preconotas]";
    private static final String COLUMN_PHOTO_NOTES =  "[fotonotas]"; //caminho da imagem
    private static final String COLUMN_YEAR =        "[ano]";
    private static final String COLUMN_MONTH =        "[mes]";
    private static final String COLUMN_DAY =        "[dia]";
    //COLUMN TYPE pra table notas também
    private static final String COLUMN_PARCELS =    "[parcelas]";

    SqlHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            String sb = (
                    //Cria Tabela Cartão
                    "CREATE TABLE IF NOT EXISTS" + TABLE_CARD + "(\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + "));\n"+
                    //Cria Tabela Notes
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTES + "(\n" +
                    "  " + COLUMN_ID_NOTES +     " INT NOT NULL, \n" +
                    "  " + COLUMN_TITLE_NOTES +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_DESC_NOTES +    " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PRICE_NOTES +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PHOTO_NOTES +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_YEAR +         " INT NOT NULL, \n" +
                    "  " + COLUMN_MONTH +         " INT NOT NULL, \n" +
                    "  " + COLUMN_DAY +         " INT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  " + COLUMN_PARCELS +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_NOTES + "));");
            String[] commands = sb.split(";");

            for (String command : commands) {

                db.execSQL(command.toLowerCase());

            }

        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error create db");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            //
            bancoVersao3(db);
            //
        }
        if (oldVersion < 4){
            //Transforma as tabelas tipoDEB e tipoCred em uma só como tipo
            //sendo 1 = Credito; 2 = Debito e 3 = Cred/Deb
            dBaseVersion4(db);
        }

        if (oldVersion < 5){
            //Cria na Tabela Notas as colunas TYPE e PARCELS
            dBaseVersion5(db);
        }
        if (oldVersion < 6){
            //Deleta na tabela cartão o número
            dBaseVersion6(db);
        }

        //

        onCreate(db);
    }

    private void bancoVersao3(SQLiteDatabase db) {
        try {
            String sb = ("BEGIN TRANSACTION;\n" +
                    //Cria uma nova tabela pra guardar os dados da tabela a ser atualizada ou deletada
                    "CREATE TABLE " + TABLE_CARD_BCK + " (\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_NUMBER_CARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE_CRED +    " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE_DEB +     " TEXT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARD_BCK + " (" + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE_CRED + ", " + COLUMN_TYPE_DEB + ")\n" +
                    "  SELECT " + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE_CRED + ", " + COLUMN_TYPE_DEB + "\n" +
                    "  FROM " + TABLE_CARD + ";\n" +
                    "DROP TABLE " + TABLE_CARD + ";\n" +
                    //Recria a tabela principal e recupera os dados da tabela de backup
                    "CREATE TABLE " + TABLE_CARD + " (\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_NUMBER_CARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE_CRED +    " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE_DEB +     " TEXT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARD + " (" + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE_CRED + ", " + COLUMN_TYPE_DEB + ")\n" +
                    "  SELECT " + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE_CRED + ", " + COLUMN_TYPE_DEB + "\n" +
                    "  FROM " + TABLE_CARD_BCK + ";\n" +
                    "DROP TABLE " + TABLE_CARD_BCK + ";\n" +
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTES + "(\n" +
                    "  " + COLUMN_ID_NOTES +    " INT NOT NULL, \n" +
                    "  " + COLUMN_TITLE_NOTES +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_DESC_NOTES +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PRICE_NOTES +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PHOTO_NOTES +     " TEXT NOT NULL, \n" +
                    "  " + COLUMN_ID_CARD +     " INT NOT NULL, \n" +
                    "  " + COLUMN_YEAR +    " INT NOT NULL, \n" +
                    "  " + COLUMN_MONTH +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DAY +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_NOTES + "));\n"+
                    "COMMIT;");
            String[] commands = sb.split(";");

            for (String command : commands) {

                db.execSQL(command.toLowerCase());

            }

        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: Error, version 3 db");
        }
    }

    private void dBaseVersion4(SQLiteDatabase db) {
        try {
            String sb = ("BEGIN TRANSACTION;\n" +
                    //Cria uma nova tabela pra guardar os dados da tabela a ser atualizada ou deletada
                    "CREATE TABLE " + TABLE_CARD_BCK + " (\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_NUMBER_CARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE_CRED +    " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE_DEB +     " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARD_BCK + " (" + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE_CRED + ", " + COLUMN_TYPE_DEB + ", " + COLUMN_TYPE +  ")\n" +
                    "  SELECT " + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE_CRED + ", " + COLUMN_TYPE_DEB + ", \n" +
                        "(CASE  WHEN "+ COLUMN_TYPE_CRED +" = 'true' AND "+ COLUMN_TYPE_DEB + " = 'false' THEN 1 \n" +
                                "WHEN "+ COLUMN_TYPE_CRED + " = 'false' AND "+ COLUMN_TYPE_DEB + " = 'true' THEN 2 ELSE 3 END) as "+ COLUMN_TYPE +"\n" +
                    "  FROM " + TABLE_CARD + ";\n" +
                    "DROP TABLE " + TABLE_CARD + ";\n" +
                    //Recria a tabela principal e recupera os dados da tabela de backup
                    "CREATE TABLE " + TABLE_CARD + " (\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_NUMBER_CARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARD + " (" + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE  + ")\n" +
                    "  SELECT " + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_NUMBER_CARD + ", " + COLUMN_TYPE + "\n" +
                    "  FROM " + TABLE_CARD_BCK + ";\n" +
                    "DROP TABLE " + TABLE_CARD_BCK + ";\n" +
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTES + "(\n" +
                    "  " + COLUMN_ID_NOTES +    " INT NOT NULL, \n" +
                    "  " + COLUMN_TITLE_NOTES +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_DESC_NOTES +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PRICE_NOTES +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PHOTO_NOTES +     " TEXT NOT NULL, \n" +
                    "  " + COLUMN_ID_CARD +     " INT NOT NULL, \n" +
                    "  " + COLUMN_YEAR +    " INT NOT NULL, \n" +
                    "  " + COLUMN_MONTH +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DAY +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_NOTES + "));\n"+
                    "COMMIT;");
            String[] commands = sb.split(";");

            for (String command : commands) {
                db.execSQL(command.toLowerCase());
            }

        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: Error, version 4 db");
        }
    }

    private void dBaseVersion5(SQLiteDatabase db) {
        try {
            String sb = ("BEGIN TRANSACTION;\n" +
                    //Cria uma nova tabela pra guardar os dados da tabela a ser atualizada ou deletada
                    "CREATE TABLE " + TABLE_NOTES_BCK + " (\n" +
                            "  " + COLUMN_ID_NOTES +     " INT NOT NULL, \n" +
                            "  " + COLUMN_TITLE_NOTES +  " TEXT NOT NULL, \n" +
                            "  " + COLUMN_DESC_NOTES +    " TEXT NOT NULL, \n" +
                            "  " + COLUMN_PRICE_NOTES +  " TEXT NOT NULL, \n" +
                            "  " + COLUMN_PHOTO_NOTES +   " TEXT NOT NULL, \n" +
                            "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                            "  " + COLUMN_YEAR +         " INT NOT NULL, \n" +
                            "  " + COLUMN_MONTH +         " INT NOT NULL, \n" +
                            "  " + COLUMN_DAY +         " INT NOT NULL, \n" +
                            "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                            "  " + COLUMN_PARCELS +    " INT NOT NULL, \n" +
                            "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_NOTES + ")); \n"  +
                            "\n" +
                            "INSERT INTO " + TABLE_NOTES_BCK + " (" + COLUMN_ID_NOTES + ", " + COLUMN_TITLE_NOTES + ", " + COLUMN_DESC_NOTES + ", " + COLUMN_PRICE_NOTES + ", " + COLUMN_PHOTO_NOTES + ", " + COLUMN_ID_CARD + ", " + COLUMN_YEAR + ", " + COLUMN_MONTH + ", " + COLUMN_DAY + ", " + COLUMN_TYPE + ", " + COLUMN_PARCELS +  ")\n" +
                            "  SELECT " + COLUMN_ID_NOTES + ", " + COLUMN_TITLE_NOTES + ", " + COLUMN_DESC_NOTES + ", " + COLUMN_PRICE_NOTES + ", " + COLUMN_PHOTO_NOTES + ", " + COLUMN_ID_CARD + ", " + COLUMN_YEAR + ", " + COLUMN_MONTH + ", " + COLUMN_DAY + ", 1, 1 \n" +
                            "  FROM " + TABLE_NOTES + ";\n" +
                            "DROP TABLE " + TABLE_NOTES + ";\n" +
                            //Recria a tabela principal e recupera os dados da tabela de backup
                            "CREATE TABLE " + TABLE_NOTES + " (\n" +
                            "  " + COLUMN_ID_NOTES +     " INT NOT NULL, \n" +
                            "  " + COLUMN_TITLE_NOTES +  " TEXT NOT NULL, \n" +
                            "  " + COLUMN_DESC_NOTES +    " TEXT NOT NULL, \n" +
                            "  " + COLUMN_PRICE_NOTES +  " TEXT NOT NULL, \n" +
                            "  " + COLUMN_PHOTO_NOTES +   " TEXT NOT NULL, \n" +
                            "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                            "  " + COLUMN_YEAR +         " INT NOT NULL, \n" +
                            "  " + COLUMN_MONTH +         " INT NOT NULL, \n" +
                            "  " + COLUMN_DAY +         " INT NOT NULL, \n" +
                            "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                            "  " + COLUMN_PARCELS +    " INT NOT NULL, \n" +
                            "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_NOTES + ")); \n"  +
                            "\n" +
                            "INSERT INTO " + TABLE_NOTES + " (" + COLUMN_ID_NOTES + ", " + COLUMN_TITLE_NOTES + ", " + COLUMN_DESC_NOTES + ", " + COLUMN_PRICE_NOTES + ", " + COLUMN_PHOTO_NOTES + ", " + COLUMN_ID_CARD + ", " + COLUMN_YEAR + ", " + COLUMN_MONTH + ", " + COLUMN_DAY + ", " + COLUMN_TYPE + ", " + COLUMN_PARCELS +  ")\n" +
                            "  SELECT " + COLUMN_ID_NOTES + ", " + COLUMN_TITLE_NOTES + ", " + COLUMN_DESC_NOTES + ", " + COLUMN_PRICE_NOTES + ", " + COLUMN_PHOTO_NOTES + ", " + COLUMN_ID_CARD + ", " + COLUMN_YEAR + ", " + COLUMN_MONTH + ", " + COLUMN_DAY + ", " + COLUMN_TYPE + ", " + COLUMN_PARCELS + "\n" +
                            "  FROM " + TABLE_NOTES_BCK + ";\n" +
                            "DROP TABLE " + TABLE_NOTES_BCK + ";\n" +
                            "CREATE TABLE IF NOT EXISTS" + TABLE_CARD + "(\n" +
                            "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                            "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                            "  " + COLUMN_NUMBER_CARD +  " TEXT NOT NULL, \n" +
                            "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                            "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + "));\n"+
                            "COMMIT;");
            String[] commands = sb.split(";");

            for (String command : commands) {

                db.execSQL(command.toLowerCase());
            }

        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: Error, version 5 db");
        }
    }

    private void dBaseVersion6(SQLiteDatabase db) {
        try {
            String sb = ("BEGIN TRANSACTION;\n" +
                    //Cria uma nova tabela pra guardar os dados da tabela a ser atualizada ou deletada
                    "CREATE TABLE " + TABLE_CARD_BCK + " (\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARD_BCK + " (" + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_TYPE + ")\n" +
                    "  SELECT " + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_TYPE + "\n" +
                    "  FROM " + TABLE_CARD + ";\n" +
                    "DROP TABLE " + TABLE_CARD + ";\n" +
                    //Recria a tabela principal e recupera os dados da tabela de backup
                    "CREATE TABLE " + TABLE_CARD + " (\n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_DESC_CARD +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_CARD + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARD + " (" + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_TYPE + ")\n" +
                    "  SELECT " + COLUMN_ID_CARD + ", " + COLUMN_DESC_CARD + ", " + COLUMN_TYPE + "\n" +
                    "  FROM " + TABLE_CARD_BCK + ";\n" +
                    "DROP TABLE " + TABLE_CARD_BCK + ";\n" +
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTES + "(\n" +
                    "  " + COLUMN_ID_NOTES +     " INT NOT NULL, \n" +
                    "  " + COLUMN_TITLE_NOTES +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_DESC_NOTES +    " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PRICE_NOTES +  " TEXT NOT NULL, \n" +
                    "  " + COLUMN_PHOTO_NOTES +   " TEXT NOT NULL, \n" +
                    "  " + COLUMN_ID_CARD +    " INT NOT NULL, \n" +
                    "  " + COLUMN_YEAR +         " INT NOT NULL, \n" +
                    "  " + COLUMN_MONTH +         " INT NOT NULL, \n" +
                    "  " + COLUMN_DAY +         " INT NOT NULL, \n" +
                    "  " + COLUMN_TYPE +        " INT NOT NULL, \n" +
                    "  " + COLUMN_PARCELS +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUMN_ID_NOTES + ")); \n" +
                    "COMMIT;");
            String[] commands = sb.split(";");

            for (String command : commands) {

                db.execSQL(command.toLowerCase());
            }
        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: Error, version 6 db");
        }
    }
}
