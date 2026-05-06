package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.repository.TransactionRepository;
import com.pao.proiect.licitatii.model.transactions.Transaction;
import com.pao.proiect.licitatii.model.entities.*;

import java.util.TreeSet;

public class TransactionService{
    private final AuditService auditService = AuditService.getInstance();
    private static TransactionService instance;
    private static TransactionRepository transactionRepository = TransactionRepository.getInstance();

    private TreeSet<Transaction> transactions;

    private TransactionService(){
        transactionRepository.initCounter();
        
        transactions = new TreeSet<>();
        for(Transaction t: transactionRepository.readAll()){
            transactions.add(t);
        }
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
        auditService.log("Print all transactions");
    }
    public void printTransactionsOfUser(User user){
        System.out.println("--------------------------------------------------");
        for(Transaction transaction: transactions){
            if(transaction.getAuctionWinner().getId().equals(user.getId()) || transaction.getAuctionSeller().getId().equals(user.getId())){
                System.out.println(transaction);
            }
        }
        System.out.println("--------------------------------------------------");
        auditService.log("Print transactions made by user");
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        transactionRepository.create(transaction);
    }
    public TreeSet<Transaction> getTransactions(){
        return new TreeSet<>(transactions);
    }


}
