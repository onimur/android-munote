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
import com.onimus.munotes.bancos.dao.CardDao;

import java.util.ArrayList;

public class RecordListCardAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HMAuxCard> hmAux;


    public RecordListCardAdapter(Context context, int layout, ArrayList<HMAuxCard> hmAux) {
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

        TextView celula_cartao, celula_tipo;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.celula_cartao = row.findViewById(R.id.celula_cartao);
            holder.celula_tipo = row.findViewById(R.id.celula_tipo);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxCard model = hmAux.get(i);

        holder.celula_cartao.setText(model.get(CardDao.DESCARTAO));
        //  holder.celula_tipo.setText(model.get(CardDao.TIPOCRED));
        if (String.valueOf(model.get(CardDao.TIPO)).trim().toLowerCase().equals("1")) {
            holder.celula_tipo.setText(context.getString(R.string.cb_credito));
        }
        if (String.valueOf(model.get(CardDao.TIPO)).trim().toLowerCase().equals("2")) {
            holder.celula_tipo.setText(context.getString(R.string.cb_debito));
        }
        if (String.valueOf(model.get(CardDao.TIPO)).trim().toLowerCase().equals("3")) {
            holder.celula_tipo.setText(context.getString(R.string.cb_string_creditdebit));
        }

        return row;
    }
}