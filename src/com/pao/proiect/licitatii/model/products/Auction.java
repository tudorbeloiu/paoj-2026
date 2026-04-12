package com.pao.proiect.licitatii.model.products;

import com.pao.proiect.licitatii.model.enums.AuctionStatus;
import com.pao.proiect.licitatii.model.enums.AuctionType;
import com.pao.proiect.licitatii.model.entities.Buyer;
import com.pao.proiect.licitatii.model.entities.Seller;
import com.pao.proiect.licitatii.exception.*;
import com.pao.proiect.licitatii.model.transactions.Bid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Auction {

    private static int counterId = 0;

    private String id;
    private double startingPrice;
    private double currentPrice;

    private LocalDateTime startingTime;
    private LocalDateTime endingTime;

    private Seller seller;
    private Product product;
    private Buyer winner = null;

    private AuctionStatus auctionStatus;
    private AuctionType auctionType;

    private List<Bid> bids;



    public Auction(Seller seller, Product product, double startingPrice, LocalDateTime startingTime, LocalDateTime endingTime, AuctionType auctionType){
        if(startingPrice < 0){
            throw new InvalidNumber();
        }
        if(endingTime.isBefore(startingTime)){
            throw new InvalidAuctionDate();
        }

        this.id = "A-" + (++counterId);
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.seller = seller;
        this.product = product;

        this.auctionStatus = AuctionStatus.ACTIVE;
        this.auctionType = auctionType;

        this.bids = new ArrayList<>();
    }


    public String getId(){
        return id;
    }
    public double getStartingPrice(){
        return startingPrice;
    }
    public LocalDateTime getStartingTime(){
        return startingTime;
    }
    public LocalDateTime getEndingTime(){
        return endingTime;
    }
    public Seller getSeller(){
        return seller;
    }
    public Product getProduct(){
        return product;
    }
    public Buyer getWinner(){
        return winner;
    }
    public AuctionStatus getAuctionStatus(){
        return auctionStatus;
    }
    public double getCurrentPrice(){
        return currentPrice;
    }
    public AuctionType getAuctionType(){
        return auctionType;
    }
    public List<Bid> getBids(){
        return new ArrayList<>(bids);
    }
    public Bid getLastBid(){
        if(bids.isEmpty()){
            return null;
        }
        return bids.getLast();
    }

    public void setStartingPrice(double startingPrice){
        if(startingPrice <0){
            throw new InvalidNumber();
        }
        this.startingPrice = startingPrice;
    }
    public void setStartingTime(LocalDateTime startingTime){
        this.startingTime = startingTime;
    }
    public void setEndingTime(LocalDateTime endingTime){
        if(endingTime.isBefore(startingTime)){
            throw new InvalidAuctionDate();
        }
        this.endingTime = endingTime;
    }
    public void setSeller(Seller seller){
        this.seller = seller;
    }
    public void setProduct(Product product){
        this.product = product;
    }
    public void setWinner(Buyer winner){
        this.winner = winner;
    }
    public void setAuctionStatus(AuctionStatus auctionStatus){
        this.auctionStatus = auctionStatus;
    }
    public void setCurrentPrice(double currentPrice){
        if(currentPrice < startingPrice){
            throw new InvalidNumber();
        }
        this.currentPrice = currentPrice;
    }
    public void addBid(Bid bid){
        bids.add(bid);
    }

    @Override
    public String toString(){
        return "[" + id + "] " + product.getName() + " | Starting price: " + startingPrice +
                " | Current price: " + currentPrice +
                " | Status: " + auctionStatus.name() +
                " | Type: " + auctionType.name()
                + " | Starting time: " + startingTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                + " | Ending time: " + endingTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

}
