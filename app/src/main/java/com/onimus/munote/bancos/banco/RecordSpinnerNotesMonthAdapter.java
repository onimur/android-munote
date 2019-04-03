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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.bancos.dao.NotesDao;

import java.util.ArrayList;

import static com.onimus.munote.files.ChangeMonth.changeMonthToExtension;
import static com.onimus.munote.files.ConvertType.convertToInt;

public class RecordSpinnerNotesMonthAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HMAuxNotes> hmAux;

    public RecordSpinnerNotesMonthAdapter(Context context, int layout, ArrayList<HMAuxNotes> hmAux) {
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

        TextView cel_month;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, viewGroup, false);
            holder.cel_month = row.findViewById(R.id.cel_month);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxNotes model = hmAux.get(i);
        int month = convertToInt(model.get(NotesDao.MONTH));

        holder.cel_month.setText(changeMonthToExtension(month, context));
        return row;
    }
}