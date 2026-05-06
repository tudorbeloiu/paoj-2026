package com.pao.proiect.licitatii.service;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static final String file_path = "resources/data/audit.csv";


    private static AuditService instance;

    private AuditService() {
        Path cale = Path.of(file_path);
        try{
            Files.createDirectories(cale.getParent());

            if(!Files.exists(cale)){
                Files.writeString(cale, "nume_actiune,timestamp\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static AuditService getInstance(){
        if(instance == null){
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void log(String nume_actiune){
        String linie = nume_actiune + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) + "\n";
        try{
            Files.writeString(
                    Path.of(file_path),
                    linie,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND
            );
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

}
