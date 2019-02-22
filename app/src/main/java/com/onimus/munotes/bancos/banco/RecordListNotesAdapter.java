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

package com.onimus.munotes.bancos.banco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onimus.munotes.R;
import com.onimus.munotes.bancos.dao.NotesDao;

import java.util.ArrayList;

import static com.onimus.munotes.files.MoneyTextWatcher.formatTextPrice;
import static com.onimus.munotes.files.MoneyTextWatcher.getCurrencySymbol;

public class RecordListNotesAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HMAuxNotes> hmAux;


    public RecordListNotesAdapter(Context context, int layout, ArrayList<HMAuxNotes> hmAux) {
        this.hmAux = hmAux;
        this.context = context;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return hmAux.size();
    }

    @Override
    public Object getItem(int i) {
        return hmAux.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {

        TextView celula_dia_notas, celula_title_notas, celula_desc_notas, celula_price_notas, tv_symbol2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.celula_dia_notas = row.findViewById(R.id.celula_dia_notas);
            holder.celula_title_notas = row.findViewById(R.id.celula_title_notas);
            holder.celula_desc_notas = row.findViewById(R.id.celula_desc_notas);
            holder.celula_price_notas = row.findViewById(R.id.celula_price_notas);
            holder.tv_symbol2 = row.findViewById(R.id.tv_symbol2);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxNotes model = hmAux.get(i);
        holder.celula_dia_notas.setText(model.get(NotesDao.DIA));
        holder.celula_title_notas.setText(model.get(NotesDao.TITLENOTAS));
        holder.celula_desc_notas.setText(model.get(NotesDao.DESNOTAS));
        String price = model.get(NotesDao.PRECONOTAS);
        if (price != null){
            price = formatTextPrice(price);
        }
        holder.celula_price_notas.setText(price);
        holder.tv_symbol2.setText(getCurrencySymbol());

        return row;
    }
}

