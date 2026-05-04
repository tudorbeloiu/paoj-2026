package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

public class TranzactieExtinsa extends Tranzactie {
    private String contSursa;

    public TranzactieExtinsa(int id, double suma, String data, TipTranzactie tip, String contSursa) {
        super(id, suma, data, tip);
        this.contSursa = contSursa;
    }

    public String getContSursa(){
        return contSursa;
    }
}
