package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        LinkedList<Tranzactie> coada = new LinkedList<>();
        Scanner  scanner = new Scanner(System.in);

        while(scanner.hasNextLine()){
            String linie = scanner.nextLine();

            if(linie.isEmpty()){
                continue;
            }

            String[] parti = linie.split(" ");
            String comanda = parti[0];

            switch (comanda){
                case "ENQUEUE":
                    int id = Integer.parseInt(parti[1]);
                    double suma = Double.parseDouble(parti[2]);
                    String data = parti[3];
                    TipTranzactie tip = TipTranzactie.valueOf(parti[4]);

                    coada.addLast(new Tranzactie(id, suma , data, tip));
                    break;

                case "PUSH":
                    int id_push = Integer.parseInt(parti[1]);
                    double suma_push = Double.parseDouble(parti[2]);
                    String data_push = parti[3];
                    TipTranzactie tip_push = TipTranzactie.valueOf(parti[4]);

                    coada.addFirst(new Tranzactie(id_push, suma_push, data_push, tip_push));
                    break;

                case "DEQUEUE":
                    if(coada.isEmpty()){
                        System.out.println("Coada goala.");
                    }
                    else{
                        System.out.println("Procesat: " + coada.removeFirst());
                    }
                    break;

                case "POP":
                    if(coada.isEmpty()){
                        System.out.println("Coada goala.");
                    }
                    else{
                        System.out.println("Extras: " + coada.removeFirst());
                    }
                    break;

                case "REMOVE_DEBIT":
                    int cnt = 0;
                    Iterator<Tranzactie> it = coada.iterator();
                    while(it.hasNext()){
                        Tranzactie t = it.next();
                        if(t.getTip() == TipTranzactie.DEBIT){
                            it.remove();
                            cnt++;
                        }
                    }
                    System.out.println("Eliminat " + cnt + " tranzactii DEBIT.");
                    break;

                case "REMOVE_BELOW":
                    int cnt_rb = 0;
                    double threshold = Double.parseDouble(parti[1]);
                    Iterator<Tranzactie> it_rb = coada.iterator();

                    while(it_rb.hasNext()){
                        Tranzactie t = it_rb.next();
                        if(t.getSuma() < threshold){
                            cnt_rb++;
                            it_rb.remove();
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", cnt_rb, threshold);
                    break;

                case "PRINT":
                    for(Tranzactie t: coada){
                        System.out.println(t);
                    }
                    break;

                case "SIZE":
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
            }
        }
    }
}






















