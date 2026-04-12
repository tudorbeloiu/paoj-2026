package com.pao.proiect.licitatii.model.transactions;

import com.pao.proiect.licitatii.model.products.Auction;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.entities.Seller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Transaction implements Comparable<Transaction> {
    private static int contorId = 0;
    private final String id;

    private final Auction endedAuction;
    private final Buyer auctionWinner;
    private final Seller auctionSeller;

    private final double finalAmount;
    private final LocalDateTime transactionTime;

    public Transaction(Auction endedAuction, Buyer auctionWinner, Seller auctionSeller, double finalAmount){
        this.id = "T-" + (++contorId);

        this.endedAuction = endedAuction;
        this.auctionWinner = auctionWinner;
        this.auctionSeller = auctionSeller;
        this.finalAmount = finalAmount;
        this.transactionTime = LocalDateTime.now();

    }

    public Auction getEndedAuction(){
        return endedAuction;
    }
    public Buyer getAuctionWinner(){
        return auctionWinner;
    }
    public Seller getAuctionSeller(){
        return auctionSeller;
    }
    public double getFinalAmount(){
        return finalAmount;
    }
    public String getId(){
        return id;
    }
    public LocalDateTime getTransactionTime(){
        return transactionTime;
    }

    @Override
    public String toString(){
        return "[" +id + "] | Auction: " + endedAuction.getId() + " | Winner: " + auctionWinner.getName() +
                " | Seller: " + auctionSeller.getName() +
                " | Final amount: " + finalAmount +
                " | Time: " + transactionTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @Override
    public int compareTo(Transaction other){
        int cmp = Double.compare(other.getFinalAmount(), this.finalAmount);
        if(cmp != 0) 
            return cmp;
        return this.id.compareTo(other.id);
    }
}
