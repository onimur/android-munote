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

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.bancos.dao.CardDao;

import java.util.ArrayList;

public class RecordSpinnerCardAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HMAuxCard> hmAux;

    public RecordSpinnerCardAdapter(Context context, int layout, ArrayList<HMAuxCard> hmAux) {
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
        LinearLayout ll_celula_tipo, ll_main_sp_card;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, viewGroup, false);
            holder.celula_cartao = row.findViewById(R.id.celula_cartao);
            holder.celula_tipo = row.findViewById(R.id.celula_tipo);
            holder.ll_celula_tipo = row.findViewById(R.id.ll_celula_tipo);
            holder.ll_main_sp_card = row.findViewById(R.id.ll_main_sp_card);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxCard model = hmAux.get(i);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, row.getResources().getDisplayMetrics());
        holder.celula_cartao.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.ll_celula_tipo.removeView(holder.celula_tipo);
        holder.ll_celula_tipo.addView(holder.celula_tipo);
        holder.ll_celula_tipo.setBackgroundResource(R.color.colorWhiteTransparence);

        holder.celula_cartao.setText(model.get(CardDao.DESC_CARD));
        if (String.valueOf(model.get(CardDao.TYPE)).trim().toLowerCase().equals("1")) {
            holder.celula_tipo.setText(context.getString(R.string.cb_credito));
        }
        if (String.valueOf(model.get(CardDao.TYPE)).trim().toLowerCase().equals("2")) {
            holder.celula_tipo.setText(context.getString(R.string.cb_debito));
        }
        if (String.valueOf(model.get(CardDao.TYPE)).trim().toLowerCase().equals("3")) {
            holder.celula_tipo.setText((context.getString(R.string.cb_credito) + " / " + context.getString(R.string.cb_debito)));
        }
        if (String.valueOf(model.get(CardDao.TYPE)).trim().toLowerCase().equals("-1")) {
            holder.celula_cartao.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
            holder.ll_celula_tipo.removeView(holder.celula_tipo);
            holder.celula_tipo.setText("");
        }

        return row;
    }

}