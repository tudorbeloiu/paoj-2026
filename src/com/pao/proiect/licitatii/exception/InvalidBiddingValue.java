package com.pao.proiect.licitatii.exception;

public class InvalidBiddingValue extends RuntimeException {
    public InvalidBiddingValue(double currentBiddingPrice, double biddingAmount) {

        super("[ERROR] The current bidding value " + currentBiddingPrice + " is higher than " + biddingAmount);
    }
}
