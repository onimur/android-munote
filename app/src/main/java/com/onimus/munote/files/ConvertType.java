package com.onimus.munote.files;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.onimus.munote.Constants.LOG_E;

public class ConvertType {

    public static Integer convertToInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            Log.e(LOG_E, "Error to convert integer");
            return -1;
        }
    }

    public static Long convertToLong(String number) {
        try {
            return Long.valueOf(number);
        } catch (Exception e) {

            Log.e(LOG_E, "Error to convert Long");
            return -1L;
        }
    }

    //Converte um formato data para uma data padrão
    public static Date convertToDate(String format, Date calendarTime) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(new SimpleDateFormat(format, Locale.ENGLISH).format(calendarTime));

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(LOG_E, "Error to convert Date");
            return null;
        }
    }

    //Converte uma String com o formato de Data para uma data padrão
    public static Date convertToDate(String format, String date) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(LOG_E, "Error to convert Date");
            return null;
        }
    }
}
