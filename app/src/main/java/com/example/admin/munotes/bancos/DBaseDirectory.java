package com.example.admin.munotes.bancos;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.admin.munotes.files.FileUtilities;
import com.example.admin.munotes.Constants;

import java.io.File;

public class DBaseDirectory {

    public static void createDirectoryDbase(Context context) {
        String folderall = Constants.FOLDER_NAME + Constants.FOLDER_NAME_DBASE;
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path, folderall);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("LabCamera", "Pasta não criada");
            }
        }

        String caminhodbase = dir.getPath() + "/" + Constants.BANCO_NOME;
        FileUtilities.getUri(context, new File(caminhodbase));
    }


}
