package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS{
    private List<String> smsTrimise = new ArrayList<>();
    private double sold;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
    }

    public double getSold(){
        return sold;
    }

    @Override
    public void autentificare(String user, String parola){
        if(user == null || parola == null || parola.isBlank() || user.isBlank()){
            throw new IllegalArgumentException("User/parola este GOOOOOOL!!!");
        }
        System.out.println("Persoana juridica autentificat cu succes: " + user);
    }

    @Override
    public double consultareSold(){
        return this.sold;
    }

    @Override
    public boolean efectuarePlata(double suma){
        if(suma <= 0 || suma > sold)
            return false;
        sold = sold - suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj){
        if(getTelefon() == null || getTelefon().isBlank()){
            return false;
        }
        if(mesaj == null || mesaj.isBlank()){
            return false;
        }

        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise(){
        return this.smsTrimise;
    }

    @Override
    public String toString(){
        return "PersoanaJuridica[" + getNume() + ", " + getPrenume() + ", " + getTelefon() + ", " + getSold() + "]";
    }
}
