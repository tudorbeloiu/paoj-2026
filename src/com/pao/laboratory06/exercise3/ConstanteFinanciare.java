package com.pao.laboratory06.exercise3;

public enum ConstanteFinanciare {
    TVA(0.19),
    SALARIU_MINIM(4050),
    COTA_IMPOZIT(0.10);

    private final double val;

    ConstanteFinanciare(double val){
        this.val = val;
    }

    public double getVal(){
        return val;
    }

}
