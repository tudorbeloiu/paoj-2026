package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends Colaborator implements PersoanaJuridica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return "SRL";
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.SRL;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (this.venitLunar - this.cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public void afiseaza() {
        System.out.printf("SRL: %s %s, venit net anual: %.2f lei\n", nume, prenume, calculeazaVenitNetAnual());
    }
}
