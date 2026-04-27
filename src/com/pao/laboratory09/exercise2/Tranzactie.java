package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

public class Tranzactie {
    private int id;
    private double suma;
    private String data;
    private TipTranzactie tip;
    private Status status;

    public Tranzactie(int id, double suma, String data, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
        this.status = Status.PENDING;
    }

    public void setStatus(Status status) { this.status = status; }
}
