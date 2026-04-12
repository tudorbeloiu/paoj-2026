package com.pao.proiect.licitatii.exception;

public class InvalidNumber extends RuntimeException {
    public InvalidNumber() {

        super("[ERROR] Number must be bigger than 0!");
    }
}
