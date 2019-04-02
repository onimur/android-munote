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

package com.onimus.munote.bancos.model;


public class Notes {

    private long idCard;
    private long idNotes;
    private long year;
    private long month;
    private long day;
    private String titleNotes;
    private String descNotes;
    private String priceNotes;
    private String photoNotes;
    private int type;
    private int parcels;

    public Notes() {
        this.idCard = -1L;
        this.idNotes = -1L;
        this.titleNotes = "";
        this.descNotes = "";
        this.priceNotes = "";
        this.photoNotes = "";
        this.year = -1L;
        this.month = -1L;
        this.day = -1L;
        this.type = -1;
        this.parcels = -1;
    }
    public Notes(long idCard, long idNotes, long year, long month, long day, String titleNotes, String descNotes, String priceNotes, String photoNotes, int type, int parcels) {
        this.idCard = idCard;
        this.idNotes = idNotes;
        this.year = year;
        this.month = month;
        this.day = day;
        this.titleNotes = titleNotes;
        this.descNotes = descNotes;
        this.priceNotes = priceNotes;
        this.photoNotes = photoNotes;
        this.type = type;
        this.parcels = parcels;

    }

    public long getIdCard() {
        return idCard;
    }

    public void setIdCard(long idCard) {
        this.idCard = idCard;
    }

    public long getIdNotes() {
        return idNotes;
    }

    public void setIdNotes(long idNotes) {
        this.idNotes = idNotes;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public String getTitleNotes() {
        return titleNotes;
    }

    public void setTitleNotes(String titleNotes) {
        this.titleNotes = titleNotes;
    }

    public String getDescNotes() {
        return descNotes;
    }

    public void setDescNotes(String descNotes) {
        this.descNotes = descNotes;
    }

    public String getPriceNotes() {
        return priceNotes;
    }

    public void setPriceNotes(String priceNotes) {
        this.priceNotes = priceNotes;
    }

    public String getPhotoNotes() {
        return photoNotes;
    }

    public void setPhotoNotes(String photoNotes) {
        this.photoNotes = photoNotes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParcels() {
        return parcels;
    }

    public void setParcels(int parcels) {
        this.parcels = parcels;
    }

}

