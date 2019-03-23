package com.onimus.munotes.files;


import android.content.Context;
import android.text.TextUtils;

import com.onimus.munotes.Constants;
import com.onimus.munotes.R;
import com.onimus.munotes.bancos.dao.NotesDao;

/**
 * Object for passing filters around.
 */
public class Filters {

    private String card = null;
    private String year = null;
    private String month = null;
    private String tipo = null;
    private String cardDesc = null;
    private String sortBy = null;

    private Context context;

    public Filters() {
    }

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(Constants.FIELD_DAY);

        return filters;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSortBy() {

        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSearchDescription(Context context) {
        this.context = context;
        if (card == null || card.equals("-1")) {
            return context.getString(R.string.all_cards);
        } else {
            return cardDesc;
        }
    }

    public String getSearchYearMonth(Context context) {
        this.context = context;

        if (month != null && year != null) {
            return (context.getString(R.string.text_in) + " " + changeMonthToExtension() + " " + year);
        } else {
            return "";
        }

    }
    public boolean getSearchYearMonthTrue(Context context) {
        this.context = context;

        return month != null && year != null;

    }

    public String getOrderDescription(Context context) {
        if (Constants.FIELD_PRICE.equals(sortBy)) {
            return context.getString(R.string.sorted_by_price);
        } else if (Constants.FIELD_TITLE.equals(sortBy)) {
            return context.getString(R.string.sorted_by_title);
        } else {
            return context.getString(R.string.sorted_by_day);
        }
    }

    public String getOrderDescriptionDB() {
        if (Constants.FIELD_PRICE.equals(sortBy)) {
            return NotesDao.PRECONOTAS;
        } else if (Constants.FIELD_TITLE.equals(sortBy)) {
            return NotesDao.TITLENOTAS;
        } else {
            return NotesDao.DIA;
        }
    }

    private String changeMonthToExtension() {
        String textMonth = null;
        if (month != null) {
            switch (month) {
                case "1":
                    textMonth = context.getString(R.string.month_janeiro);
                    break;

                case "2":
                    textMonth = context.getString(R.string.month_fevereiro);
                    break;

                case "3":
                    textMonth = context.getString(R.string.month_março);
                    break;

                case "4":
                    textMonth = context.getString(R.string.month_abril);
                    break;

                case "5":
                    textMonth = context.getString(R.string.month_maio);
                    break;

                case "6":
                    textMonth = context.getString(R.string.month_june);
                    break;

                case "7":
                    textMonth = context.getString(R.string.month_july);
                    break;

                case "8":
                    textMonth = context.getString(R.string.month_agosto);
                    break;

                case "9":
                    textMonth = context.getString(R.string.month_setembro);
                    break;

                case "10":
                    textMonth = context.getString(R.string.month_outubro);
                    break;

                case "11":
                    textMonth = context.getString(R.string.month_novembro);
                    break;

                case "12":
                    textMonth = context.getString(R.string.month_dezembro);
                    break;
            }
        }
        return textMonth;
    }
}
