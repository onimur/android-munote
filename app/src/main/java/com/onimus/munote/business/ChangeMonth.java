package com.onimus.munote.business;

import android.content.Context;

import com.onimus.munote.R;

public class ChangeMonth {

    public static String changeMonthToExtension(int month, Context context) {


        String monthText = null;
        if (month > 0 && month < 13) {
            switch (month) {
                case 1:
                    monthText = context.getString(R.string.month_janeiro);
                    break;

                case 2:
                    monthText = context.getString(R.string.month_fevereiro);
                    break;

                case 3:
                    monthText = context.getString(R.string.month_março);
                    break;

                case 4:
                    monthText = context.getString(R.string.month_abril);
                    break;

                case 5:
                    monthText = context.getString(R.string.month_maio);
                    break;

                case 6:
                    monthText = context.getString(R.string.month_june);
                    break;

                case 7:
                    monthText = context.getString(R.string.month_july);
                    break;

                case 8:
                    monthText = context.getString(R.string.month_agosto);
                    break;

                case 9:
                    monthText = context.getString(R.string.month_setembro);
                    break;

                case 10:
                    monthText = context.getString(R.string.month_outubro);
                    break;

                case 11:
                    monthText = context.getString(R.string.month_novembro);
                    break;

                case 12:
                    monthText = context.getString(R.string.month_dezembro);
                    break;
            }
        }
        return monthText;
    }
}
