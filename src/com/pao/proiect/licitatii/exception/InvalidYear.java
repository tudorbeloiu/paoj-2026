package com.pao.proiect.licitatii.exception;

public class InvalidYear extends RuntimeException {
    public InvalidYear(int year) {

        super("[ERROR] " + year + " is not a valid year!");
    }
}
