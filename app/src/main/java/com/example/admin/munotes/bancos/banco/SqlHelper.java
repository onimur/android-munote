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

package com.example.admin.munotes.bancos.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;



public class SqlHelper extends SQLiteOpenHelper {

    private static final String TABLE_CARTAO =      "[cartao]";
    private static final String TABLE_CARTAO_BCK =  "[cartao_bck]";
    private static final String COLUNA_IDCARTAO =   "[idcartao]";
    private static final String COLUNA_DESCARTAO =  "[descartao]";
    private static final String COLUNA_NUMBERCARD = "[numbercard]";
    private static final String COLUNA_TIPOCRED =   "[tipocred]";
    private static final String COLUNA_TIPODEB =    "[tipodeb]";
    private static final String COLUNA_TIPO =       "[tipo]";
    //
    private static final String TABLE_NOTAS =       "[notas]";
    private static final String COLUNA_IDNOTAS =    "[idnotas]";
    private static final String COLUNA_TITLENOTAS = "[titlenotas]";
    private static final String COLUNA_DESNOTAS =   "[desnotas]";
    private static final String COLUNA_PRECONOTAS = "[preconotas]";
    private static final String COLUNA_FOTONOTAS =  "[fotonotas]"; //caminho da imagem
    private static final String COLUNA_ANO =        "[ano]";
    private static final String COLUNA_MES =        "[mes]";
    private static final String COLUNA_DIA =        "[dia]";



    SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            String sb = (
                    //Cria Tabela Cartão
                    "CREATE TABLE IF NOT EXISTS" + TABLE_CARTAO + "(\n" +
                    "  " + COLUNA_IDCARTAO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DESCARTAO +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_NUMBERCARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPO +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDCARTAO + "));\n"+
                    //Cria Tabela Notes
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTAS + "(\n" +
                    "  " + COLUNA_IDNOTAS +     " INT NOT NULL, \n" +
                    "  " + COLUNA_TITLENOTAS +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_DESNOTAS +    " TEXT NOT NULL, \n" +
                    "  " + COLUNA_PRECONOTAS +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_FOTONOTAS +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_IDCARTAO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_ANO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_MES +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DIA +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDNOTAS + "));");
            String[] comandos = sb.split(";");

            for (String comando : comandos) {
                db.execSQL(comando.toLowerCase());
            }

        } catch (Exception e) {
            Log.e(TAG, "onCreate: ");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            //
            bancoVersao3(db);
        }
        if (oldVersion < 4){
            //Transforma as tabelas tipoDEB e tipoCred em uma só como tipo
            //sendo 1 = Credito; 2 = Debito e 3 = Cred/Deb
            bancoVersao4(db);
        }

        //

        onCreate(db);
    }

    private void bancoVersao3(SQLiteDatabase db) {
        try {
            String sb = ("BEGIN TRANSACTION;\n" +
                    //Cria uma nova tabela pra guardar os dados da tabela a ser atualizada ou deletada
                    "CREATE TABLE " + TABLE_CARTAO_BCK + " (\n" +
                    "  " + COLUNA_IDCARTAO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DESCARTAO +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_NUMBERCARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPOCRED +    " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPODEB +     " TEXT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDCARTAO + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARTAO_BCK + " (" + COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPOCRED + ", " + COLUNA_TIPODEB + ")\n" +
                    "  SELECT " +       COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPOCRED + ", " + COLUNA_TIPODEB + "\n" +
                    "  FROM " + TABLE_CARTAO + ";\n" +
                    "DROP TABLE " + TABLE_CARTAO + ";\n" +
                    //Recria a tabela principal e recupera os dados da tabela de backup
                    "CREATE TABLE " + TABLE_CARTAO + " (\n" +
                    "  " + COLUNA_IDCARTAO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DESCARTAO +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_NUMBERCARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPOCRED +    " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPODEB +     " TEXT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDCARTAO + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARTAO + " (" + COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPOCRED + ", " + COLUNA_TIPODEB + ")\n" +
                    "  SELECT " +       COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPOCRED + ", " + COLUNA_TIPODEB + "\n" +
                    "  FROM " + TABLE_CARTAO_BCK + ";\n" +
                    "DROP TABLE " + TABLE_CARTAO_BCK + ";\n" +
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTAS + "(\n" +
                    "  " + COLUNA_IDNOTAS +    " INT NOT NULL, \n" +
                    "  " + COLUNA_TITLENOTAS +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_DESNOTAS +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_PRECONOTAS +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_FOTONOTAS +     " TEXT NOT NULL, \n" +
                    "  " + COLUNA_IDCARTAO +     " INT NOT NULL, \n" +
                    "  " + COLUNA_ANO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_MES +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DIA +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDNOTAS + "));\n"+
                    "COMMIT;");
            String[] comandos = sb.split(";");

            for (String comando : comandos) {
                db.execSQL(comando.toLowerCase());
            }

        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: ");
        }
    }

    private void bancoVersao4(SQLiteDatabase db) {
        try {
            String sb = ("BEGIN TRANSACTION;\n" +
                    //Cria uma nova tabela pra guardar os dados da tabela a ser atualizada ou deletada
                    "CREATE TABLE " + TABLE_CARTAO_BCK + " (\n" +
                    "  " + COLUNA_IDCARTAO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DESCARTAO +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_NUMBERCARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPOCRED +    " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPODEB +     " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPO +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDCARTAO + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARTAO_BCK + " (" + COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPOCRED + ", " + COLUNA_TIPODEB + ", " + COLUNA_TIPO +  ")\n" +
                    "  SELECT " +       COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPOCRED + ", " + COLUNA_TIPODEB + ", \n" +
                        "(CASE  WHEN "+ COLUNA_TIPOCRED +" = 'true' AND "+ COLUNA_TIPODEB + " = 'false' THEN 1 \n" +
                                "WHEN "+ COLUNA_TIPOCRED + " = 'false' AND "+ COLUNA_TIPODEB + " = 'true' THEN 2 ELSE 3 END) as "+ COLUNA_TIPO +"\n" +
                    "  FROM " + TABLE_CARTAO + ";\n" +
                    "DROP TABLE " + TABLE_CARTAO + ";\n" +
                    //Recria a tabela principal e recupera os dados da tabela de backup
                    "CREATE TABLE " + TABLE_CARTAO + " (\n" +
                    "  " + COLUNA_IDCARTAO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DESCARTAO +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_NUMBERCARD +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_TIPO +        " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDCARTAO + ")); \n" +
                    "\n" +
                    "INSERT INTO " + TABLE_CARTAO + " (" + COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPO  + ")\n" +
                    "  SELECT " +       COLUNA_IDCARTAO + ", " + COLUNA_DESCARTAO + ", " + COLUNA_NUMBERCARD + ", " + COLUNA_TIPO + "\n" +
                    "  FROM " + TABLE_CARTAO_BCK + ";\n" +
                    "DROP TABLE " + TABLE_CARTAO_BCK + ";\n" +
                    "CREATE TABLE IF NOT EXISTS" + TABLE_NOTAS + "(\n" +
                    "  " + COLUNA_IDNOTAS +    " INT NOT NULL, \n" +
                    "  " + COLUNA_TITLENOTAS +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_DESNOTAS +   " TEXT NOT NULL, \n" +
                    "  " + COLUNA_PRECONOTAS +  " TEXT NOT NULL, \n" +
                    "  " + COLUNA_FOTONOTAS +     " TEXT NOT NULL, \n" +
                    "  " + COLUNA_IDCARTAO +     " INT NOT NULL, \n" +
                    "  " + COLUNA_ANO +    " INT NOT NULL, \n" +
                    "  " + COLUNA_MES +    " INT NOT NULL, \n" +
                    "  " + COLUNA_DIA +    " INT NOT NULL, \n" +
                    "  CONSTRAINT [] PRIMARY KEY (" + COLUNA_IDNOTAS + "));\n"+
                    "COMMIT;");
            String[] comandos = sb.split(";");

            for (String comando : comandos) {
                db.execSQL(comando.toLowerCase());
            }

        } catch (Exception e) {
            Log.e(TAG, "onUpgrade: ");
        }
    }
}
