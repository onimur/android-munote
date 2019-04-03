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

public class Card {

    private long idCard;
    private String descCard;
    private int type;

    public Card() {
        this.idCard = -1L;
        this.descCard = "";
        this.type = -1;
    }

    public long getIdCard() {
        return idCard;
    }

    public void setIdCard(long idCard) {
        this.idCard = idCard;
    }

    public String getDescCard() {
        return descCard;
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
}
