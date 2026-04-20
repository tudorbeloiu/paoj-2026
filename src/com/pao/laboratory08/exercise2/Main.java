package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        List<Student> studenti = new ArrayList<>();
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
        int pragVarsta = scanner.nextInt();

        List<Student> filteredStudents = studenti.stream()
                .filter(s -> s.getVarsta() >= pragVarsta)
                .toList();

        System.out.println("Filtru: varsta >= " + pragVarsta);
        System.out.println("Rezultate: " + filteredStudents.size() + " studenti\n" );

        BufferedWriter fout = new BufferedWriter(new FileWriter("src/com/pao/laboratory08/tests/rezultate.txt"));

        for(Student s: filteredStudents){
            System.out.println(s);
            fout.write(s.toString());
            fout.newLine();
        }

        fout.close();

        System.out.println("\nScris in: rezultate.txt");

    }
}

