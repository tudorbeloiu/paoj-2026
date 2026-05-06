package com.pao.proiect.licitatii.exception;

public class CategoryNotExist extends RuntimeException {
    public CategoryNotExist(String id) {

        super("[ERROR] The category with id " + id + " does not exist!");
    }
}
