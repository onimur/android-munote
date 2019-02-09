package com.example.admin.munotes.bancos.banco;

import com.example.admin.munotes.bancos.dao.CardDao;

import java.util.HashMap;

public class HMAuxCard extends HashMap<String, String> {
    @Override
    public String toString() {
        return get(CardDao.DESCARTAO);
    }
}
