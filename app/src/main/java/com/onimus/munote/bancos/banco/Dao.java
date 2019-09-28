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

package com.onimus.munote.bancos.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import static com.onimus.munote.Constants.*;

import java.io.File;
import java.util.Objects;

public class Dao {

    private Context context;
    protected SQLiteDatabase db;

    public Dao(Context context) {
        this.context = context;
    }

    protected void openDataBase() {

        String folderFull = FOLDER_NAME + FOLDER_NAME_DBASE;
        File path;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsoluteFile();
        }

        File dir = new File(path, folderFull);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(LOG_D, "Folder not created");
            }
        }

        String dBasePath = dir.getPath() + "/" + DBASE_NAME;

        SqlHelper varHelp = new SqlHelper(
                context,
                dBasePath,
                null,
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
