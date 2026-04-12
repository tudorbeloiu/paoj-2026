package com.pao.proiect.licitatii.exception;

import com.pao.proiect.licitatii.model.products.Product;

public class NoBiddingsError extends RuntimeException {
    public NoBiddingsError(Product product) {
        super("[AUCTION CANCELLED] " + product.getName() + " didn't receive any biddings!");
    }
}
