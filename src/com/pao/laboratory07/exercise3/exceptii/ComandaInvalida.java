package com.pao.laboratory07.exercise3.exceptii;

public class ComandaInvalida extends RuntimeException {
    public ComandaInvalida() {
        super("Detaliile comenzii sunt invalide!");
    }
}
