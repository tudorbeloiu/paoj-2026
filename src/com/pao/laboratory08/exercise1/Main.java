package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while((linie = fin.readLine()) != null){
            String[] parts = linie.split(",");
            //Ana,19,București,Calea Victoriei
            String nume = parts[0].trim();
            int age = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();

            studenti.add(new Student(nume, age, new Adresa(oras, strada)));
        }
        fin.close();

        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine();

        if(comanda.startsWith("PRINT")){
            for(Student s: studenti){
                System.out.println(s);
            }
        }
        else if(comanda.startsWith("SHALLOW")){
            String nume = comanda.split(" ", 2)[1];

            Student original = gasesteStudent(studenti, nume);
            Student clona = original.shallowClone();
            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }
        else if(comanda.startsWith("DEEP")){
            String nume = comanda.split(" ", 2)[1];

            Student original = gasesteStudent(studenti, nume);
            Student clona = original.deepClone();
            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);

        }
        else{
            System.out.println("ciudata comanda broski");
        }
    }

    private static Student gasesteStudent(List<Student> studenti, String nume){
        for(Student s: studenti){
            if(s.getNume().equals(nume)){
                return s;
            }
        }
        return null;
    }
}
