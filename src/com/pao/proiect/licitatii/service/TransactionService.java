package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.transactions.Transaction;
import com.pao.proiect.licitatii.model.entities.*;

import java.util.TreeSet;

public class TransactionService{
    private static TransactionService instance;

    private TreeSet<Transaction> transactions;

    private TransactionService(){
        transactions = new TreeSet<>();
    }

    public static TransactionService getInstance(){
        if(instance == null){
            instance = new TransactionService();
        }
        return instance;
    }
    public void printAllTransactions(){
        System.out.println("--------------------------------------------------");
        for(Transaction transaction: transactions){
            System.out.println(transaction);
        }
        System.out.println("--------------------------------------------------");
    }
    public void printTransactionsOfUser(User user){
        System.out.println("--------------------------------------------------");
        for(Transaction transaction: transactions){
            if(transaction.getAuctionWinner().getId().equals(user.getId()) || transaction.getAuctionSeller().getId().equals(user.getId())){
                System.out.println(transaction);
            }
        }
        System.out.println("--------------------------------------------------");
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
    public TreeSet<Transaction> getTransactions(){
        return new TreeSet<>(transactions);
    }


}
