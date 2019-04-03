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

import android.support.annotation.NonNull;
import android.util.Log;

import com.onimus.munote.bancos.dao.CardDao;

import java.util.HashMap;

import static com.onimus.munote.Constants.LOG_E;

public class HMAuxCard extends HashMap<String, String> {
    @NonNull
    @Override
    public String toString() {
        String r = get(CardDao.DESC_CARD);
        if (r != null){
            return  r;
        } else {
            Log.e(LOG_E, "Error HMAuxCard toString() - null");
            return "";
        }

    }
}
