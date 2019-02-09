package com.example.admin.munotes.bancos.model;

public class Card {

    private long idcartao;
    private String descartao;
    private int tipo;
    private String numbercard;

    public Card() {
        this.idcartao = -1L;
        this.descartao = "";
        this.tipo = -1;
        this.numbercard = "";
    }

    public Card(long idcartao, String descartao, int tipo, String numbercard) {
        this.idcartao = idcartao;
        this.descartao = descartao;
        this.tipo = tipo;
        this.numbercard = numbercard;
    }

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

    public String getNumbercard() {
        return numbercard;
    }

    public void setNumbercard(String numbercard) {
        this.numbercard = numbercard;
    }
}
