package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica{
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
        return "PFA";
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }

    @Override
    public double calculeazaVenitNetAnual(){
        double salariuMinimBrutAnual = 48600.0;
        double venitNet = (this.venitLunar - this.cheltuieliLunare) * 12;
        double impozit = 0.1 * venitNet;

        double cass = 0;
        if (venitNet < 6 * salariuMinimBrutAnual) {
            cass = 0.1 * (6 * salariuMinimBrutAnual);
        } else if (venitNet >= 6 * salariuMinimBrutAnual && venitNet <= 72 * salariuMinimBrutAnual) {
            cass = 0.1 * venitNet;
        } else {
            cass = 0.1 * (72 * salariuMinimBrutAnual);
        }

        double cas = 0;
        if (venitNet < 12 * salariuMinimBrutAnual) {
            cas = 0;
        } else if (venitNet >= 12 * salariuMinimBrutAnual && venitNet <= 24 * salariuMinimBrutAnual) {
            cas = 0.25 * (12 * salariuMinimBrutAnual);
        } else {
            cas = 0.25 * (24 * salariuMinimBrutAnual);
        }

        return venitNet - impozit - cass - cas;
    }

    @Override
    public void afiseaza() {
        System.out.printf("PFA: %s %s, venit net anual: %.2f lei\n", nume, prenume, calculeazaVenitNetAnual());
    }
}
