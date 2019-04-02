package com.onimus.munote.files;


import android.content.Context;

import com.onimus.munote.R;
import com.onimus.munote.bancos.dao.NotesDao;

import static com.onimus.munote.Constants.*;

/**
 * Object for passing filters around.
 */
public class Filters {

    private String idCard = null;
    private String year = null;
    private String month = null;
    private String type = null;
    private String descCard = null;
    private String sortBy = null;

    private Context context;

    public Filters() {
    }

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(FIELD_DAY);

        return filters;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getDescCard() {
        return descCard;
    }

    public void setDescCard(String descCard) {
        this.descCard = descCard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearchDescription(Context context) {
        this.context = context;
        if (idCard == null || idCard.equals("-1")) {
            return context.getString(R.string.all_cards);
        } else {
            return descCard;
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
        if (FIELD_PRICE.equals(sortBy)) {
            return context.getString(R.string.sorted_by_price);
        } else if (FIELD_TITLE.equals(sortBy)) {
            return context.getString(R.string.sorted_by_title);
        } else {
            return context.getString(R.string.sorted_by_day);
        }
    }

    public String getOrderDescriptionDB() {
        if (FIELD_PRICE.equals(sortBy)) {
            return NotesDao.PRICE_NOTES;
        } else if (FIELD_TITLE.equals(sortBy)) {
            return NotesDao.TITLE_NOTES;
        } else {
            return NotesDao.DAY;
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
