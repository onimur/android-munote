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

    private long idcartao;
    private long idnotas;
    private long ano;
    private long mes;
    private long dia;
    private String titlenotas;
    private String desnotas;
    private String preconotas;
    private String fotonotas;
    private int tipo;
    private int parcelas;

    public Notes() {
        this.idcartao = -1L;
        this.idnotas = -1L;
        this.titlenotas = "";
        this.desnotas = "";
        this.preconotas = "";
        this.fotonotas = "";
        this.ano = -1L;
        this.mes = -1L;
        this.dia = -1L;
        this.tipo = -1;
        this.parcelas = -1;
    }
    /*public Notes(long idcartao, long idnotas, long ano, long mes, long dia, String titlenotas, String desnotas, String preconotas, String fotonotas, int tipo, int parcelas) {
        this.idcartao = idcartao;
        this.idnotas = idnotas;
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
        this.titlenotas = titlenotas;
        this.desnotas = desnotas;
        this.preconotas = preconotas;
        this.fotonotas = fotonotas;
        this.tipo = tipo;
        this.parcelas = parcelas;

    }*/

    public long getIdcartao() {
        return idcartao;
    }

    public void setIdcartao(long idcartao) {
        this.idcartao = idcartao;
    }

    public long getIdnotas() {
        return idnotas;
    }

    public void setIdnotas(long idnotas) {
        this.idnotas = idnotas;
    }

    public long getAno() {
        return ano;
    }

    public void setAno(long ano) {
        this.ano = ano;
    }

    public long getMes() {
        return mes;
    }

    public void setMes(long mes) {
        this.mes = mes;
    }

    public long getDia() {
        return dia;
    }

    public void setDia(long dia) {
        this.dia = dia;
    }

    public String getTitlenotas() {
        return titlenotas;
    }

    public void setTitlenotas(String titlenotas) {
        this.titlenotas = titlenotas;
    }

    public String getDesnotas() {
        return desnotas;
    }

    public void setDesnotas(String desnotas) {
        this.desnotas = desnotas;
    }

    public String getPreconotas() {
        return preconotas;
    }

    public void setPreconotas(String preconotas) {
        this.preconotas = preconotas;
    }

    public String getFotonotas() {
        return fotonotas;
    }

    public void setFotonotas(String fotonotas) {
        this.fotonotas = fotonotas;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

}

