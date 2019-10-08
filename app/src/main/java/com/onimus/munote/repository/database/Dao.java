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

import com.onimus.munote.business.ManageDirectory;

import java.io.File;

import static com.onimus.munote.Constants.DBASE_NAME;
import static com.onimus.munote.Constants.DBASE_VERSION;
import static com.onimus.munote.Constants.FOLDER_NAME;
import static com.onimus.munote.Constants.FOLDER_NAME_DBASE;

public class Dao {

    private final Context context;
    protected SQLiteDatabase db;

    protected Dao(Context context) {
        this.context = context;
    }

    protected void openDataBase() {
        String pathDB = FOLDER_NAME + FOLDER_NAME_DBASE;
        ManageDirectory md = new ManageDirectory(context);
        File dir = md.createPublicDirectoryFileForVariousApi(pathDB);

        String dBasePath = dir.getPath() + "/" + DBASE_NAME;

        SqlHelper varHelp = new SqlHelper(
                context,
                dBasePath,
                DBASE_VERSION
        );

        db = varHelp.getWritableDatabase(); // momento solicitacao banco
    }

    protected void closeDataBase() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }
}
