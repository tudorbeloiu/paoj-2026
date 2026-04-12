package com.pao.proiect.licitatii.exception;

public class InsufficientFunds extends RuntimeException {
    public InsufficientFunds(double amount, double balance) {
        super("[ERROR] You don't have enough money to bid " + amount + "! Current balance: " + balance);
    }
}
