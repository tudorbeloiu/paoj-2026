package com.pao.proiect.licitatii.exception;

public class ProductNotExist extends RuntimeException {
    public ProductNotExist(String id) {

        super("[ERROR] The product with id " + id + " does not exist!");
    }
}
