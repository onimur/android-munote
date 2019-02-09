package com.example.admin.munotes.bancos.banco;

import com.example.admin.munotes.bancos.dao.NotesDao;

import java.util.HashMap;


public class HMAuxNotes extends HashMap<String, String> {
    @Override
    public String toString() {
        return get(NotesDao.TITLENOTAS);
    }





}
