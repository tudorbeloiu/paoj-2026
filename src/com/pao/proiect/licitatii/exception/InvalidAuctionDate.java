package com.pao.proiect.licitatii.exception;

public class InvalidAuctionDate extends RuntimeException {
    public InvalidAuctionDate() {
        super("[ERROR] Ending time cannot be before starting time!");
    }
}
