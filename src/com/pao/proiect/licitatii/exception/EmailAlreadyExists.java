package com.pao.proiect.licitatii.exception;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String emailInUse) {

        super("[ERROR REGISTERING] This email already exists!");
    }
}
