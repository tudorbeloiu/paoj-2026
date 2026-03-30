package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer>{
    private double sold;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
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
        System.out.println("Inginer autentificat cu succes: " + user);
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
    public int compareTo(Inginer other){
        return this.getNume().compareTo(other.getNume());
    }

    @Override
    public String toString(){
        return "Inginer[" + getNume() + ", " + getPrenume() + ", " + getSalariu() + ", " + getSold() + "]";
    }
}
