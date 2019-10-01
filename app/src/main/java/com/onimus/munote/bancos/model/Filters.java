package com.onimus.munote.bancos.model;


import android.content.Context;

import com.onimus.munote.R;
import com.onimus.munote.bancos.dao.NotesDao;

import static com.onimus.munote.Constants.*;
import static com.onimus.munote.files.ChangeMonth.changeMonthToExtension;

public class Filters {

    private long idCard = -2L;
    private int year = -2;
    private int month = -2;
    private int type = -2;
    private String descCard = null;
    private String sortBy = null;

    public Filters() {
    }

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(FIELD_DAY);

        return filters;
    }

    public long getIdCard() {
        return idCard;
    }

    public void setIdCard(long idCard) {
        this.idCard = idCard;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setDescCard(String descCard) {
        this.descCard = descCard;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSearchDescription(Context context) {
        if (idCard == -2L) {
            return context.getString(R.string.all_cards);
        } else {
            return descCard;
        }
    }

    public String getSearchYearMonth(Context context) {

        if (month != -2 && year != -2) {
            return (context.getString(R.string.text_in) + " " + changeMonthToExtension(month, context) + " " + year);
        } else {
            return "";
        }

    }
    public boolean getSearchYearMonthTrue() {

        return month != -2 && year != -2;

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
}
