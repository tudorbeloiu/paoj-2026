package com.pao.proiect.licitatii.exception;

public class AuctionStillActive extends RuntimeException {
    public AuctionStillActive() {
        super("[ERROR AUCTION] Auction is still active!");
    }
}
