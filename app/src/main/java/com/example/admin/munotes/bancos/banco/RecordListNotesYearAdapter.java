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

public class RecordListNotesYearAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<HMAuxNotes> hmAux;


    public RecordListNotesYearAdapter(Context context, int layout, ArrayList<HMAuxNotes> hmAux) {
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

        TextView celula_ano_mes;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.celula_ano_mes = row.findViewById(R.id.celula_ano_mes);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
//monta a listview
        HMAuxNotes model = hmAux.get(i);
        holder.celula_ano_mes.setText(model.get(NotesDao.ANO));
        return row;
    }
}

