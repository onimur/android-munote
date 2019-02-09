package com.example.admin.munotes.files;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;


import com.example.admin.munotes.BuildConfig;

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

    private static boolean isAndroidMarshmallowOrSuperiorVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}

