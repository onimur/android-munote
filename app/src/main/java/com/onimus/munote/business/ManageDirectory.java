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

package com.onimus.munote.business;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.Objects;

import static com.onimus.munote.Constants.TAG;

public class ManageDirectory {
    private final Context context;

    public ManageDirectory(Context context) {
        this.context = context;
    }

    public File createPublicDirectoryFileForVariousApi(String path) {
        File pathRoot = takeAPIPatch(null);
        Log.i(TAG, "Root");
        return createFolder(pathRoot, path);
    }

    public File createPublicDirectoryFileForVariousApi(String path, String environmentDir) {
        File pathPicture = takeAPIPatch(environmentDir);
        Log.i(TAG, environmentDir);
        return createFolder(pathPicture, path);

    }

    private File takeAPIPatch(@Nullable String whichDirectory) {
        if (whichDirectory != null) {
            if (isAndroidPieOrLowerVersion()) {
                return Environment.getExternalStoragePublicDirectory(whichDirectory);
            } else {
                return Objects.requireNonNull(context.getExternalFilesDir(whichDirectory)).getAbsoluteFile();
            }
        } else {
            if (isAndroidPieOrLowerVersion()) {
                return Environment.getExternalStorageDirectory();
            } else {
                return Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsoluteFile();
            }
        }
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

    /////////////////////////////////////////
    private boolean isAndroidPieOrLowerVersion() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.P;
    }
}
