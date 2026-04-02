package com.pao.laboratory07.exercise3;

import java.util.Locale;

public final class ComandaRedusa extends Comanda {
    private double pret;
    private int discountProcent;

    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, client);
        this.pret = pret;
        this.discountProcent = discountProcent;
    }

    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }

    public int getDiscountProcent(){
        return discountProcent;
    }

    @Override
    public String descriere(boolean withStare){
        //DISCOUNTED: Headphones, pret: 160.00 lei (-20%) [PLACED]
//        return "DISCOUNTED: " + nume + ", pret: " +  pret + " lei (- " + discountProcent +  "%) [" + stare + "]";
        String stareStr = "";
        if(withStare){
            stareStr = " [" + stare + "]";
        }
        return String.format(Locale.US, "DISCOUNTED: %s, pret: %.2f lei (-%d%%)%s - client: %s", nume, pretFinal(), discountProcent, stareStr, client);
    }
}
