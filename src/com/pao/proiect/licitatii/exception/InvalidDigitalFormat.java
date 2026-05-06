package com.pao.proiect.licitatii.exception;

public class InvalidDigitalFormat extends RuntimeException {
    public InvalidDigitalFormat(String format) {
        super("[ERROR] The format " + format + " is invalid!");
    }
}
