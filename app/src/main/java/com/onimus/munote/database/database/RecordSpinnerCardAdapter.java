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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onimus.munote.R;
import com.onimus.munote.database.dao.CardDao;

import java.util.ArrayList;
import java.util.Objects;

import static com.onimus.munote.files.ConvertType.convertToInt;

public class RecordSpinnerCardAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final ArrayList<HMAuxCard> hmAux;

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

        TextView cel_card, cel_type;
        LinearLayout ll_cel_type, ll_main_sp_card;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = Objects.requireNonNull(inflater).inflate(layout, viewGroup, false);
            holder.cel_card = row.findViewById(R.id.cel_card);
            holder.cel_type = row.findViewById(R.id.cel_type);
            holder.ll_cel_type = row.findViewById(R.id.ll_cel_type);
            holder.ll_main_sp_card = row.findViewById(R.id.ll_main_sp_card);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxCard model = hmAux.get(i);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, row.getResources().getDisplayMetrics());
        holder.cel_card.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.ll_cel_type.removeView(holder.cel_type);
        holder.ll_cel_type.addView(holder.cel_type);
        holder.ll_cel_type.setBackgroundResource(R.color.colorWhiteTransparence);

        holder.cel_card.setText(model.get(CardDao.DESC_CARD));
        if (convertToInt(model.get(CardDao.TYPE)) == 1) {
            holder.cel_type.setText(context.getString(R.string.cb_credit));
        }
        if (convertToInt(model.get(CardDao.TYPE)) == 2) {
            holder.cel_type.setText(context.getString(R.string.cb_debit));
        }
        if (convertToInt(model.get(CardDao.TYPE)) == 3) {
            holder.cel_type.setText(context.getString(R.string.text_cred_deb));
        }
        if (convertToInt(model.get(CardDao.TYPE)) == -1) {
            holder.cel_card.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
            holder.ll_cel_type.removeView(holder.cel_type);
            holder.cel_type.setText("");
        }

        return row;
    }

}