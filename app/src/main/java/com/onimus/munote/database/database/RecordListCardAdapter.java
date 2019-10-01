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

package com.onimus.munote.database.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.database.dao.CardDao;

import java.util.ArrayList;

import static com.onimus.munote.files.ConvertType.convertToInt;
import static java.util.Objects.requireNonNull;

public class RecordListCardAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final ArrayList<HMAuxCard> hmAux;

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

        TextView cel_card, cel_type;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = requireNonNull(inflater).inflate(layout, null);
            holder.cel_card = row.findViewById(R.id.cel_card);
            holder.cel_type = row.findViewById(R.id.cel_type);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxCard model = hmAux.get(i);

        holder.cel_card.setText(model.get(CardDao.DESC_CARD));
        //  holder.cel_type.setText(model.get(CardDao.TIPOCRED));
        if (convertToInt(model.get(CardDao.TYPE)) == 1) {
            holder.cel_type.setText(context.getString(R.string.cb_credit));
        }
        if (convertToInt(model.get(CardDao.TYPE)) == 2) {
            holder.cel_type.setText(context.getString(R.string.cb_debit));
        }
        if (convertToInt(model.get(CardDao.TYPE)) == 3) {
            holder.cel_type.setText(context.getString(R.string.cb_string_creditdebit));
        }

        return row;
    }
}