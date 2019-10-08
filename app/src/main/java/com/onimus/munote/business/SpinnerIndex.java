package com.onimus.munote.business;

import android.widget.Spinner;

import com.onimus.munote.repository.database.HMAuxCard;
import com.onimus.munote.repository.database.HMAuxNotes;

import java.util.ArrayList;

import static com.onimus.munote.business.ConvertType.convertToInt;
import static com.onimus.munote.business.ConvertType.convertToLong;

public class SpinnerIndex {
    //Verifica em qual posição o IDCartão desejado está;
   public static int getSpinnerIndex(Spinner spinner, long value, ArrayList<HMAuxCard> hmAux, String daoType){
       int index = 0;
       for (int i = 0; i < spinner.getCount(); i++) {
           HMAuxCard model = hmAux.get(i);
           long modelS = convertToLong(model.get(daoType));
           if (modelS >= 0) {
               if (modelS == value) {
                   index = i;
               }
           }
       }
       return index;
    }

    public static int getSpinnerIndex(Spinner spinner, int value, ArrayList<HMAuxNotes> hmAux, String daoType){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            HMAuxNotes model = hmAux.get(i);
            int modelS = convertToInt(model.get(daoType));
            if (modelS >= 0) {
                if (modelS == value) {
                    index = i;
                }
            }
        }
        return index;
    }
}
