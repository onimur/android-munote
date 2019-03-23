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

    private long idcartao;
    private String descartao;
    private int tipo;

    public Card() {
        this.idcartao = -1L;
        this.descartao = "";
        this.tipo = -1;
    }
  /*  public Card(long idcartao, String descartao, int tipo) {
        this.idcartao = idcartao;
        this.descartao = descartao;
        this.tipo = tipo;
    }*/

    public long getIdcartao() {
        return idcartao;
    }

    public void setIdcartao(long idcartao) {
        this.idcartao = idcartao;
    }

    public String getDescartao() {
        return descartao;
    }

    public void setDescartao(String descartao) {
        this.descartao = descartao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
