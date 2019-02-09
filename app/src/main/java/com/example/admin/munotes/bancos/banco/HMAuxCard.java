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

package com.example.admin.munotes.bancos.banco;

import com.example.admin.munotes.bancos.dao.CardDao;

import java.util.HashMap;

public class HMAuxCard extends HashMap<String, String> {
    @Override
    public String toString() {
        return get(CardDao.DESCARTAO);
    }
}
