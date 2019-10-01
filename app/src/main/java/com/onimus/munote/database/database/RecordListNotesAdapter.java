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
import com.onimus.munote.database.dao.NotesDao;

import java.util.ArrayList;
import java.util.Objects;

import static com.onimus.munote.files.MoneyTextWatcher.formatTextPrice;
import static com.onimus.munote.files.MoneyTextWatcher.getCurrencySymbol;

public class RecordListNotesAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final ArrayList<HMAuxNotes> hmAux;

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

    public void updateDataChanged(ArrayList<HMAuxNotes> newlist) {
        hmAux.clear();
        hmAux.addAll(newlist);
        this.notifyDataSetChanged();
    }

    private class ViewHolder {

        TextView cel_day_notes, cel_title_notes, cel_desc_notes, cel_price_notes, tv_symbol2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = Objects.requireNonNull(inflater).inflate(layout, null);
            holder.cel_day_notes = row.findViewById(R.id.cel_day_notes);
            holder.cel_title_notes = row.findViewById(R.id.cel_title_notes);
            holder.cel_desc_notes = row.findViewById(R.id.cel_desc_notes);
            holder.cel_price_notes = row.findViewById(R.id.cel_price_notes);
            holder.tv_symbol2 = row.findViewById(R.id.tv_symbol2);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxNotes model = hmAux.get(i);
        holder.cel_day_notes.setText(model.get(NotesDao.DAY));
        holder.cel_title_notes.setText(model.get(NotesDao.TITLE_NOTES));
        holder.cel_desc_notes.setText(model.get(NotesDao.DESC_NOTES));
        String price = model.get(NotesDao.PRICE_NOTES);
        if (price != null) {
            price = formatTextPrice(price);
        }
        holder.cel_price_notes.setText(price);
        holder.tv_symbol2.setText(getCurrencySymbol());

        return row;
    }
}

