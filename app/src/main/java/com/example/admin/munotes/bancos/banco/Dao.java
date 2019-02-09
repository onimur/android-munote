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
import android.os.Environment;
import android.util.Log;

import com.example.admin.munotes.Constants;

import java.io.File;

/**
 * Created by Administrador on 18/09/2018.
 */

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
                Log.d("LabCamera", "Pasta não criada");
            }
        }

        String caminhodbase = dir.getPath() + "/" + Constants.BANCO_NOME;
        //FileUtilities.getUri(context, new File(caminhodbase));



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
