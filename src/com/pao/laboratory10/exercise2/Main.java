package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        // 2. Procesează comenzile din stdin până la EOF:
        //
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Tranzactie> tranzactii = new ArrayList<>();
        for(int i=0; i<n; i++){
            String[] parti = scanner.nextLine().split(" ");
            int id = Integer.parseInt(parti[0]);
            double suma = Double.parseDouble(parti[1]);
            String data = parti[2];
            TipTranzactie tip = TipTranzactie.valueOf(parti[3]);

            tranzactii.add(new Tranzactie(id, suma, data ,tip));
        }

        while(scanner.hasNextLine()){
            String linie = scanner.nextLine();
            if(linie.isEmpty()){
                continue;
            }

            String[] parti = linie.split(" ");
            String cmd = parti[0];

            switch (cmd){
                case "UNIQUE_IDS":
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();
                    for(Tranzactie t: tranzactii){
                        ids.add(t.getId());
                    }
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);

                    break;

                case "MONTHLY_REPORT":
                    TreeMap<String, double[]> raport = new TreeMap<>();

                    for(Tranzactie t: tranzactii){
                        String luna = t.getData().substring(0,7);
                        raport.putIfAbsent(luna, new double[]{0.0, 0.0});
                        if(t.getTip() == TipTranzactie.CREDIT){
                            raport.get(luna)[0] += t.getSuma();
                        }
                        else{
                            raport.get(luna)[1] += t.getSuma();
                        }
                    }
                    for(Map.Entry<String, double[]> e: raport.entrySet()){
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",e.getKey(), e.getValue()[0], e.getValue()[1]);
                    }
                    break;

                case "TOP":
                    int nn = Integer.parseInt(parti[1]);
                    ArrayList<Tranzactie> copie = new ArrayList<>(tranzactii);
                    copie.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                    System.out.println("Top " + nn + ":");
                    for (int i = 0; i < nn; i++) {
                        System.out.println(copie.get(i));
                    }
                    break;

                case "SORT_ASC":
                    tranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    for(Tranzactie t: tranzactii){
                        System.out.println(t);
                    }
                    break;

                case "SORT_DESC":
                    tranzactii.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for(Tranzactie t: tranzactii){
                        System.out.println(t);
                    }
                    break;

                case "REVERSE":
                    Collections.reverse(tranzactii);
                    for(Tranzactie t: tranzactii){
                        System.out.println(t);
                    }
                    break;

                case "MIN_MAX":
                    System.out.println("MIN: " + Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma)));
                    System.out.println("MAX: " + Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma)));
                    break;

                case "CME_DEMO":
                    try{
                        for(Tranzactie t: tranzactii){
                            tranzactii.remove(t);
                        }
                    }
                    catch(ConcurrentModificationException e){
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
            }
        }

    }
}
