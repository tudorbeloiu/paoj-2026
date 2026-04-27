package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Tranzactie> tranzactii = new ArrayList<>();

        scanner.nextLine();

        for(int i=0; i<n; i++){
            String[] parts = scanner.nextLine().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip, null);
            t.setNote("procesat");
            tranzactii.add(t);
        }

        new File("output").mkdirs();

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("output/lab09_ex1.ser"))){
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> deserializate = new ArrayList<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("output/lab09_ex1.ser"))){
            deserializate = (List<Tranzactie>) ois.readObject();
        }


        while(scanner.hasNextLine()){
            String linie = scanner.nextLine();
            if(linie.isEmpty()){
                continue;
            }

            if(linie.equals("LIST")){
                for(Tranzactie t: deserializate){
                    System.out.println(t);
                }
            }
            else if(linie.startsWith("FILTER")){
                String dataSmek = linie.substring(7).trim();
                List<Tranzactie> filtrate = deserializate.stream()
                        .filter(t -> t.getData().startsWith(dataSmek))
                        .toList();
                if(filtrate.size() == 0){
                    System.out.println("Niciun rezultat.");
                }
                else{
                    for(Tranzactie t: filtrate){
                        System.out.println(t);
                    }
                }
            }
            else if(linie.startsWith("NOTE")){
                int id = Integer.parseInt(linie.split(" ")[1]);
                Optional<Tranzactie> tr = deserializate.stream()
                        .filter(t -> t.getId() == id)
                        .findFirst();
                if(!tr.isEmpty()){
                    System.out.println("NOTE[" + id + "]: " + tr.get().getNote());
                }
                else{
                    System.out.println("NOTE[" + id + "]: not found");
                }
            }
        }
    }
}
