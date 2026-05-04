package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe

        List<TranzactieExtinsa> tranzactii = List.of(
                new TranzactieExtinsa(1,  1500.00, "2024-01-05", TipTranzactie.CREDIT, "REVOLT00233"),
                new TranzactieExtinsa(2,   750.50, "2024-01-12", TipTranzactie.DEBIT,  "RO44BTRL004"),
                new TranzactieExtinsa(3,   200.00, "2024-01-20", TipTranzactie.DEBIT,  "RREVOLT0024"),
                new TranzactieExtinsa(4,  3200.00, "2024-02-03", TipTranzactie.CREDIT, "RO33INGB003"),
                new TranzactieExtinsa(5,   430.00, "2024-02-14", TipTranzactie.DEBIT,  "RO22BRDE002"),
                new TranzactieExtinsa(6,  1100.00, "2024-02-22", TipTranzactie.CREDIT, "REVOLT00134"),
                new TranzactieExtinsa(7,   890.75, "2024-03-01", TipTranzactie.DEBIT,  "REVOLT00143"),
                new TranzactieExtinsa(8,  2500.00, "2024-03-10", TipTranzactie.CREDIT, "RO33INGB003"),
                new TranzactieExtinsa(9,   310.00, "2024-03-18", TipTranzactie.DEBIT,  "RO44BTRL004"),
                new TranzactieExtinsa(10,  675.25, "2024-03-28", TipTranzactie.CREDIT, "RO22BRDE002")
        );

        System.out.println("Tranzactii CREDIT =============");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);



        System.out.println("Map to DOUBLE ============");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();

        System.out.printf("total: %.2f RON \n", total);



        System.out.println("Per luna ===============");
        Map<String, Double> perLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        perLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON \n", luna, suma));


        System.out.println("Top 3 tranzactii ===========");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);


        System.out.println("Conturi sursa unice ============");
        List<String> unice = tranzactii.stream()
                .map(TranzactieExtinsa::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("conturi sursa unice: " + unice);


        System.out.println("Suma medie ================");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0);
        System.out.println("suma medie: " + medie);


        System.out.println("Extras de cont ===============");
        Map<String, List<TranzactieExtinsa>> perLunaDinNouCaMaiAmVariabilaAstaPeUndeva = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0,7),
                        TreeMap::new,
                        Collectors.toList()
                ));

        perLunaDinNouCaMaiAmVariabilaAstaPeUndeva.forEach((luna, lista) -> {
            double totalLuna = lista.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();

            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), totalLuna);
        });

    }
}
