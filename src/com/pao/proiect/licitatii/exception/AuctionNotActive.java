package com.pao.proiect.licitatii.exception;
public class AuctionNotActive extends RuntimeException {
    public AuctionNotActive() {
        super("[ERROR AUCTION] Auction is not active!");
    }
}
