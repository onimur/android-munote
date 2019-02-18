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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.munotes.R;
import com.example.admin.munotes.bancos.dao.NotesDao;

import java.util.ArrayList;

public class RecordSpinnerNotesMonthAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HMAuxNotes> hmAux;
    private String mes;
    private String mesText;

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

    private class ViewHolder{

        TextView celula_month;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, viewGroup,false);
            holder.celula_month = row.findViewById(R.id.celula_month);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }
//monta a listview
        HMAuxNotes model = hmAux.get(i);
        mes = model.get(NotesDao.MES);

        changeMesParaExtenso();

      holder.celula_month.setText(mesText);
                   return row;
    }

    private void changeMesParaExtenso() {
        if (mes != null) {
            switch (mes){
                case "1":
                    mesText = context.getString(R.string.month_janeiro);
                    break;

                case "2":
                    mesText = context.getString(R.string.month_fevereiro);
                    break;

                case "3":
                    mesText = context.getString(R.string.month_março);
                    break;

                case "4":
                    mesText = context.getString(R.string.month_abril);
                    break;

                case "5":
                    mesText = context.getString(R.string.month_maio);
                    break;

                case "6":
                    mesText = context.getString(R.string.month_june);
                    break;

                case "7":
                    mesText = context.getString(R.string.month_july);
                    break;

                case "8":
                    mesText = context.getString(R.string.month_agosto);
                    break;

                case "9":
                    mesText = context.getString(R.string.month_setembro);
                    break;

                case "10":
                    mesText = context.getString(R.string.month_outubro);
                    break;

                case "11":
                    mesText = context.getString(R.string.month_novembro);
                    break;

                case "12":
                    mesText = context.getString(R.string.month_dezembro);
                    break;
            }
        }
    }

}