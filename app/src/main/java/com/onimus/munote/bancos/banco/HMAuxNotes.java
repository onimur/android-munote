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

import androidx.annotation.NonNull;
import android.util.Log;

import com.onimus.munote.bancos.dao.NotesDao;

import java.util.HashMap;

import static com.onimus.munote.Constants.TAG;


public class HMAuxNotes extends HashMap<String, String> {
    @NonNull
    @Override
    public String toString() {
        String r = get(NotesDao.TITLE_NOTES);
        if (r != null) {
            return r;
        } else {
            Log.e(TAG, "Error HMAuxNotes toString - null");
            return "";
        }
    }
}
