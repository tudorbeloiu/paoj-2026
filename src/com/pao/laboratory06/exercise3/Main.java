package com.pao.laboratory06.exercise3;

import javax.naming.ldap.UnsolicitedNotification;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Beloiu", "Tudor","0761874451", 9000, 16000),
                new Inginer("Prunea", "Florin", "0767676767", 9800, 19000),
                new Inginer("Popa", "Adi", null, 6300, 10000),
        };

        System.out.println("\n\nIngineri sortati natural: ");
        Arrays.sort(ingineri);
        for(Inginer inginer: ingineri){
            System.out.println(inginer);
        }

        System.out.println("\n\nIngineri sortati descrescator dupa salariu: ");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for(Inginer inginer: ingineri){
            System.out.println(inginer);
        }

        System.out.println("\n\nAccesul la inginer doar prin intefrata de tip PlataOnline: ");
        PlataOnline p = new Inginer("Bilzerian", "Dan", "0722233546", 8400, 12500);
        p.autentificare("dan.bilzerian", "targujiu67");
        System.out.println("Sold: " + p.consultareSold());
        System.out.println("Plata 4067: " + p.efectuarePlata(4067));
        System.out.println("Sold dupa plata: " + p.consultareSold());


        System.out.println("\n\nAccesul la persoana juridica prin intefrata de tip PlataOnlineSMS: ");
        PersoanaJuridica pj1 = new PersoanaJuridica("ASSIST SRL", "Tudor", "07888888210", 49000);
        pj1.autentificare("assist.srl", "jmekContabilitate");
        System.out.println(pj1.trimiteSMS("Plata confirmata"));
        System.out.println(pj1.trimiteSMS("Factura emisa"));
        System.out.println("SMS trimise: " + pj1.getSmsTrimise());

        System.out.println("\n\nPersoana juridica caz fara telefon(cine n are telefon in 2026): ");
        PersoanaJuridica pj2 = new PersoanaJuridica("ASSCON SOFT", "Buloiu", null, 200000);
        System.out.println("trimiteSMS fara telefon: " + pj2.trimiteSMS("mesaj test"));
        System.out.println("SMS trimise: " + pj2.getSmsTrimise());

        System.out.println("\n\nMesaje goale/null: ");
        System.out.println("mesaj null:" + pj1.trimiteSMS(null));
        System.out.println("mesaj GOOOOOOL:" + pj1.trimiteSMS(""));

        System.out.println("\n\nConstante financiare: ");
        for(ConstanteFinanciare c: ConstanteFinanciare.values()){
            System.out.println(c.getVal());
        }


        System.out.println("\n\nTratarea exceptiilor: ");
        try{
            PlataOnline inginerNou = new Inginer("NUME", "PRENUME", "0763726676", 6767, 6767);
            if(!(inginerNou instanceof PlataOnlineSMS)){
                throw new UnsupportedOperationException("Obiectul nu are telefon mobil!");
            }
        }
        catch(UnsupportedOperationException e){
            System.out.println(e.getMessage());
        }

        try{
            p.autentificare(null, "parola");
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }



    }
}
