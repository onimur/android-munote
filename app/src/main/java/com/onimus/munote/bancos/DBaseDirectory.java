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

package com.onimus.munote.bancos;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.onimus.munote.files.FileUtilities;

import java.io.File;
import java.util.Objects;

import static com.onimus.munote.Constants.*;

public class DBaseDirectory {

    public static void createDirectoryDbase(Context context) {
        String folderall = FOLDER_NAME + FOLDER_NAME_DBASE;
        File path;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsoluteFile();
        }

        File dir = new File(path, folderall);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d(LOG_D, "Folder not created");
            }
        }

        String caminhodbase = dir.getPath() + "/" + DBASE_NAME;
        FileUtilities.getUri(context, new File(caminhodbase));
    }


}
