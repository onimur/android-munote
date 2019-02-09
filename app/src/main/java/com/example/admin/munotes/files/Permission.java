package com.example.admin.munotes.files;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

//Código para pedir permissão ao usuário
public class Permission {
    public static boolean hasPermissoes(Context context, String... permissoes) {
        if (context != null && permissoes != null) {
            for (String permissao : permissoes) {
                if (ActivityCompat.checkSelfPermission(context, permissao) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
