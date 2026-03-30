package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica{
    private boolean bonus;

    @Override
    public void citeste(Scanner in){
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();

        if (in.hasNext("DA") || in.hasNext("NU")) {
            String b = in.next();
            this.bonus = b.equalsIgnoreCase("DA");
        } else {
            this.bonus = false;
        }
    }

    @Override
    public boolean areBonus(){
        return this.bonus;
    }
    @Override
    public String tipContract(){
        return "CIM";
    }
    @Override
    public TipColaborator getTip(){
        return TipColaborator.CIM;
    }

    @Override
    public double calculeazaVenitNetAnual(){
        double venitNetAnual = this.venitLunar * 12 * 0.55;
        if(areBonus()){
            venitNetAnual = venitNetAnual + 0.1 * venitNetAnual;
        }
        return venitNetAnual;
    }
    @Override
    public void afiseaza() {
        System.out.printf("CIM: %s %s, venit net anual: %.2f lei\n", nume, prenume, calculeazaVenitNetAnual());
    }

}
