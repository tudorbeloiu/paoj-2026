package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise3.exceptii.ComandaInvalida;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static String tipComanda(Comanda c){
        if (c instanceof ComandaStandard){
            return "STANDARD";
        }
        if (c instanceof ComandaRedusa)   {
            return "DISCOUNTED";
        }
        return "GIFT";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        scanner.nextLine();

        List<Comanda> comenzi = new ArrayList<>();
        for(int i=0; i<n; i++){
            try{
                String line = scanner.nextLine().trim();
                String[] words = line.split("\\s+");
                if(words[0].equals("STANDARD")){
                    comenzi.add(new ComandaStandard(words[1], Double.parseDouble(words[2]), words[3]));
                }
                else if(words[0].equals("DISCOUNTED")){
                    comenzi.add(new ComandaRedusa(words[1], Double.parseDouble(words[2]), Integer.parseInt(words[3]), words[4]));
                }
                else if(words[0].equals("GIFT")){
                    comenzi.add(new ComandaGratuita(words[1], words[2]));
                }
                else{
                    throw new ComandaInvalida();
                }
            }
            catch(ComandaInvalida e){
                System.out.println(e.getMessage());
            }

        }

        for(Comanda c: comenzi){
            System.out.println(c.descriere(true));
        }

        while(scanner.hasNext()){
            String cmdLine = scanner.nextLine();
            String[] words = cmdLine.split("\\s+");

            String cmd = words[0];

            switch(cmd){
                case "STATS":
                    System.out.println("\n--- STATS ---");
                    Map<String, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    Main::tipComanda,
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));
                    for(Map.Entry<String, Double> entry: medii.entrySet()){
                        System.out.printf(Locale.US, "%s: medie = %.2f lei\n", entry.getKey(), entry.getValue());
                    }
                    break;

                case "FILTER":
                    double threshold = Double.parseDouble(words[1]);
                    System.out.printf("\n--- FILTER (>= %.2f) ---\n", threshold);
                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold)
                            .forEach(c -> System.out.println(c.descriere(false)));
                    break;

                case "SORT":
                    System.out.println("\n--- SORT (by client, then by pret) ---");
                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal))
                            .forEach(c -> System.out.println(c.descriere(false)));
                    break;

                case "SPECIAL":
                    System.out.println("\n--- SPECIAL (discount > 15%) ---");
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa r && r.getDiscountProcent() > 15)
                            .forEach(c -> System.out.println(c.descriere(false)));
                    break;

                case "QUIT":
                    return;
            }

        }
    }
}
