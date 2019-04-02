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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import static com.onimus.munote.Constants.*;

//Código para pedir permissão ao usuário
public class Permission {
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //salva se o usuários cancela as permissões
    public static void setShouldShowStatus(Context context, String... permissions) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String aPermissions : permissions) {
            editor.putBoolean(aPermissions, true);
        }
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(Activity activity, String permission) {
        boolean prevShouldShowStatus = getRationaleDisplayStatus(activity, permission);
        boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(Activity activity, String... permission) {
        int t = 0;
        //conta quantos verdadeiros retornam do método acima, se tiver a mesma quantidade
        //de verdadeiro e permissões, então o usuário clicou na caixa de dialogo de não perguntar novamente
        for (String aPermission : permission) {
            if (neverAskAgainSelected(activity, aPermission)) {
                t++;
            }
        }
        return t == permission.length;
    }

    public static boolean getRationaleDisplayStatus(Context context, String permission) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(permission, false);
    }
}
