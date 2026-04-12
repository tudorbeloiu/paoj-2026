package com.pao.proiect.licitatii.model.transactions;

import com.pao.proiect.licitatii.model.enums.BidStatus;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.products.Auction;

import com.pao.proiect.licitatii.exception.InvalidBiddingValue;

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
        if(amount < auction.getCurrentPrice()){
            throw new InvalidBiddingValue(auction.getCurrentPrice(), amount);
        }

        this.id = "BID-" + (++contorId);

        this.bidder = bidder;
        this.auction = auction;
        this.amount = amount;
        this.placedTime = LocalDateTime.now();
        this.status = BidStatus.WINNING;
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
