package com.pao.proiect.licitatii.model.transactions;

import com.pao.proiect.licitatii.model.enums.BidStatus;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.products.Auction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bid {

    private static int contorId = 0;
    private final String id;

    private Buyer bidder;
    private Auction auction;

    private double amount;
    private LocalDateTime placedTime;

    private BidStatus status;

    public Bid(Buyer bidder, Auction auction, double amount){
        this.id = "BID-" + (++contorId);

        this.bidder = bidder;
        this.auction = auction;
        this.amount = amount;
        this.placedTime = LocalDateTime.now();
        this.status = BidStatus.WINNING;
    }
    public Bid(String id, Buyer bidder, Auction auction, double amount,
               LocalDateTime placedTime, BidStatus status) {
        this.id = id;
        this.bidder = bidder;
        this.auction = auction;
        this.amount = amount;
        this.placedTime = placedTime;
        this.status = status;
    }

    public static void initCounter(int max){
        contorId = max;
    }

    public String getId(){
        return id;
    }
    public Buyer getBidder(){
        return bidder;
    }
    public Auction getAuction(){
        return auction;
    }
    public double getAmount(){
        return amount;
    }
    public LocalDateTime getPlacedTime(){
        return placedTime;
    }
    public BidStatus getStatus(){
        return status;
    }

    public void setStatus(BidStatus status){
        this.status = status;
    }

    @Override
    public String toString(){
        return "[" + id + "] | Bidder: " + bidder.getName() +
                " | Amount: " + amount +
                " | Status: " + status.name()+
                " | Placed: " + placedTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

}
