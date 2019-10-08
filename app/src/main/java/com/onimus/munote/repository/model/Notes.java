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

package com.onimus.munote.repository.model;


public class Notes {

    private long idCard;
    private long idNotes;
    private int year;
    private int month;
    private int day;
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
        this.year = -1;
        this.month = -1;
        this.day = -1;
        this.type = -1;
        this.parcels = -1;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
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

