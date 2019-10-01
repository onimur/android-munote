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

package com.onimus.munote.database.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.onimus.munote.files.ManageDirectory;

import static com.onimus.munote.Constants.*;

import java.io.File;

public class Dao {

    private final Context context;
    protected SQLiteDatabase db;

    protected Dao(Context context) {
        this.context = context;
    }

    protected void openDataBase() {
        String pathDB = FOLDER_NAME + FOLDER_NAME_DBASE;
        ManageDirectory md = new ManageDirectory(context);
        File dir = md.createInRoot(pathDB);

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
