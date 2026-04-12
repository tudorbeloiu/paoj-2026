package com.pao.proiect.licitatii.exception;

public class ProductAlreadyAtAuction extends RuntimeException {
    public ProductAlreadyAtAuction() {
        super("[ERROR] Product is already at auction!");
    }
}
