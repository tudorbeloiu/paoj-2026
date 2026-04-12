package com.pao.proiect.licitatii.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("[ERROR] User has not been found!");
    }
}
