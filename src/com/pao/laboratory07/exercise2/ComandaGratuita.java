package com.pao.laboratory07.exercise2;

public final class ComandaGratuita extends Comanda{
    public ComandaGratuita(String nume){
        super(nume);
    }

    @Override
    public double pretFinal() {
        return 0.0;
    }

    @Override
    public String descriere(){
        //GIFT: Sticker, gratuit [PLACED]
        return "GIFT: " + nume + ", gratuit" + " [" + stare + "]";
    }
}
