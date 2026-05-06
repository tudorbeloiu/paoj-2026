package com.pao.proiect.licitatii.exception;

public class AuctionNotExists extends RuntimeException {
    public AuctionNotExists() {

        super("[ERROR] Auction does not exist!");
    }
}
