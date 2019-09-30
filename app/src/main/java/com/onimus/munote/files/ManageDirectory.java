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

package com.onimus.munote.files;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Objects;

import static com.onimus.munote.Constants.*;

public class ManageDirectory {
    private Context context;
    static final String PICTURE = "picture";
    private static final String ROOT = "root";

    public ManageDirectory(Context context) {
        this.context = context;
    }

    public File createInRoot(String path) {
        File pathRoot = takeAPIPatch(ROOT);
        Log.i(TAG, "Root");
        return createFolder(pathRoot, path);
    }

    public File createInPicture(String path) {
        File pathPicture = takeAPIPatch(PICTURE);
        Log.i(TAG, "Pictures");
        return createFolder(pathPicture, path);

    }

    File takeAPIPatch(String whichDirectory) {
        if (whichDirectory.equals(PICTURE)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            } else {
                return Objects.requireNonNull(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)).getAbsoluteFile();
            }
        } else if (whichDirectory.equals(ROOT)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                return Environment.getExternalStorageDirectory();
            } else {
                return Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsoluteFile();
            }
        }
        return null;
    }

    private File createFolder(File pathMain, String path) {
        File dir = new File(pathMain, path);
        if (!dir.exists()) {
            Log.i(TAG, "Folder not created");
            if (!dir.mkdirs()) {
                Log.i(TAG, "Inaccessible folder");
            } else {
                Log.i(TAG, "Accessible folder");
            }
        } else {
            Log.i(TAG, "Folder exists or was created in " + dir.getAbsolutePath());
        }
        return dir;
    }

    public File createFile(File pathMain, String fileName) {
        File dir = new File(pathMain, fileName);
        checkFileLog(dir);
        return dir;
    }

    File createFile(String fullPath) {
        File dir = new File(fullPath);
        checkFileLog(dir);
        return dir;
    }

    private void checkFileLog(File dir) {
        if (!dir.exists()) {
            Log.e(TAG, "File not created");
        } else {
            Log.i(TAG, "File exists or was created in " + dir.getAbsolutePath());
        }
    }
}
