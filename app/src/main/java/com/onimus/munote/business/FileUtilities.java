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
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


import com.onimus.munote.BuildConfig;

import java.io.File;

public class FileUtilities {
    //finalidade de separar o tipo de chamada, pela versão do android no dispositivo:
    public static Uri getUri(Context context, File file) {
        Uri uri;
        if (isAndroidMarshmallowOrSuperiorVersion()) {
            uri = getUriFrom(context, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private static Uri getUriFrom(Context context, File file) {
        return FileProvider.getUriForFile(context, getAuthority(), file);
    }

    @NonNull
    private static String getAuthority() {
        return BuildConfig.APPLICATION_ID + ".provider";
    }

    public static boolean isAndroidMarshmallowOrSuperiorVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}

