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

package com.onimus.munotes.bancos.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.onimus.munotes.Constants;

import java.io.File;

public class Dao {


    private Context context;
    protected SQLiteDatabase db;


    public Dao(Context context) {
        this.context = context;
    }

    protected void openDataBase(){

        String folderall = Constants.FOLDER_NAME + Constants.FOLDER_NAME_DBASE;
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path, folderall);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("Dao Dir:", "Pasta não criada");
            }
        }

        String caminhodbase = dir.getPath() + "/" + Constants.BANCO_NOME;




        SqlHelper varHelp = new SqlHelper(
                context,
                caminhodbase,
                null,
                Constants.VERSAO_BANCO
        );

        db = varHelp.getWritableDatabase(); // momento solicitacao banco
    }
    protected void closeDataBase(){
        if (db != null && db.isOpen()){
            db.close();
            db = null;
        }
    }
}
