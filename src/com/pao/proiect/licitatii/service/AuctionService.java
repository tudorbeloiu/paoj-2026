package com.pao.proiect.licitatii.service;


import com.pao.proiect.licitatii.model.entities.*;
import com.pao.proiect.licitatii.exception.*;
import com.pao.proiect.licitatii.model.products.*;
import com.pao.proiect.licitatii.model.enums.*;
import com.pao.proiect.licitatii.model.transactions.Bid;
import com.pao.proiect.licitatii.model.transactions.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;


public class AuctionService {
    private static AuctionService instance;

    private BidService bidService = BidService.getInstance();
    private NotificationService notificationService = NotificationService.getInstance();
    private TransactionService transactionService = TransactionService.getInstance();
    private TreeMap<String, Auction> auctions;


    private AuctionService(){
        this.auctions = new TreeMap<>();
    }

    public static AuctionService getInstance(){
        if(instance == null){
            instance = new AuctionService();
        }
        return instance;
    }

    public void createAuction(Seller seller, Product product, double startingPrice,
                              LocalDateTime startingTime, LocalDateTime endingTime, AuctionType auctionType){
        if(seller.checkAlreadyAtAuction(product)){
            throw new ProductAlreadyAtAuction();
        }

        Auction auction = new Auction(seller, product, startingPrice, startingTime, endingTime, auctionType);
        seller.addAuction(auction);
        auctions.put(auction.getId(), auction);

    }

    public void placeBid(Buyer bidder, Auction auction, double amount){
        if(auction.getAuctionStatus() != AuctionStatus.ACTIVE){
            throw new AuctionNotActive();
        }
        Bid previousBid = auction.getLastBid();

        bidService.addNewBid(bidder, auction, amount);

        if(previousBid != null){
            String previousMessage = "You have been outbid on " + auction.getProduct().getName() + " | New bid: " + amount;
            notificationService.addNotification(previousBid.getBidder(), previousMessage);
        }

        String message = "You are the highest bidder on " + auction.getProduct().getName() + " | Current bid: " + amount;
        notificationService.addNotification(bidder, message);

    }

    public void closeAuction(Auction auction){
        if(auction.getAuctionStatus() != AuctionStatus.ACTIVE){
            throw new AuctionNotActive();
        }
        if(LocalDateTime.now().isBefore(auction.getEndingTime())){
            throw new AuctionStillActive();
        }
        if(auction.getBids().isEmpty()){
            auction.setAuctionStatus(AuctionStatus.CANCELLED);
            throw new NoBiddingsError(auction.getProduct());
        }
        auction.setAuctionStatus(AuctionStatus.ENDED);
        Bid lastBid = auction.getLastBid();
        auction.setWinner(lastBid.getBidder());

        String winnerMessage = "CONGRATULATIONS! You just have won "+ auction.getProduct().getName() + " with only " + auction.getCurrentPrice() + " RON";
        String sellerMessage = "You just sold " + auction.getProduct().getName() + " with " + auction.getCurrentPrice() + " RON";

        notificationService.addNotification(lastBid.getBidder(),winnerMessage);
        notificationService.addNotification(auction.getSeller(), sellerMessage);

        transactionService.addTransaction(new Transaction(auction, auction.getWinner(), auction.getSeller(), auction.getCurrentPrice()));

    }

    public void cancelAuction(Auction auction){
        if(auction.getAuctionStatus() != AuctionStatus.ACTIVE){
            throw new AuctionNotActive();
        }
        auction.setAuctionStatus(AuctionStatus.CANCELLED);

        Bid lastBid = auction.getLastBid();
        if(lastBid != null){
            lastBid.getBidder().returnBidAmount(lastBid.getAmount());
            lastBid.setStatus(BidStatus.OUTBID);

            String buyerMessage = "You were refunded " + lastBid.getAmount() + " RON, following the cancellation of the auction " + auction.getId();
            notificationService.addNotification(lastBid.getBidder(), buyerMessage);
        }

        String cancelMessage = "Auction number " + auction.getId() + " has been cancelled";
        notificationService.addNotification(auction.getSeller(), cancelMessage);

    }

    public void showActiveAuctions(){
        for(Auction auction: auctions.values()){
            if(auction.getAuctionStatus() == AuctionStatus.ACTIVE){
                System.out.println(auction);
            }
        }

    }

    public List<Auction> getAuctionsByCategory(Category category){
        List<Auction> auctionsByCateg = new ArrayList<>();
        for(Auction auction: auctions.values()){
            if(auction.getProduct().getCategory().getId().equals(category.getId())){
                auctionsByCateg.add(auction);
            }
        }
        return auctionsByCateg;
    }
    public List<Auction> getAuctionsBySeller(Seller seller){
        List<Auction> auctionsBySeller = new ArrayList<>();
        for(Auction auction: auctions.values()){
            if(auction.getSeller().getId().equals(seller.getId())){
                auctionsBySeller.add(auction);
            }
        }
        return auctionsBySeller;
    }

    public void showAuctionsInInterval(double startValue, double endValue){
        if(startValue > endValue){
            throw new InvalidNumber();
        }
        System.out.println("--------------------------------------------------");
        for(Auction auction: auctions.values()){
            if(auction.getStartingPrice() >= startValue && auction.getStartingPrice() <= endValue){
                System.out.println(auction);
            }
        }
        System.out.println("--------------------------------------------------");
    }
    public void showBidsForAuction(Auction auction){
        System.out.println("--------------------------------------------------");
        for(Bid bid: auction.getBids()){
            System.out.println(bid);
        }
        System.out.println("--------------------------------------------------");
    }

    public Auction getAuctionById(String auctionId){
        if(!auctions.containsKey(auctionId)){
            throw new AuctionNotExists();
        }
        return auctions.get(auctionId);
    }

}
