package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.repository.AuctionRepository;
import com.pao.proiect.licitatii.repository.BidRepository;
import com.pao.proiect.licitatii.model.entities.*;
import com.pao.proiect.licitatii.exception.*;
import com.pao.proiect.licitatii.model.products.*;
import com.pao.proiect.licitatii.model.enums.*;
import com.pao.proiect.licitatii.model.transactions.Bid;
import com.pao.proiect.licitatii.model.transactions.Transaction;
import com.pao.proiect.licitatii.repository.UserRepository;
import com.pao.proiect.licitatii.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;


public class AuctionService {
    private static final AuditService auditService = AuditService.getInstance();
    private static final AuctionRepository auctionRepository = AuctionRepository.getInstance();
    private static final BidRepository bidRepository = BidRepository.getInstance();
    private static final UserRepository userRepository = UserRepository.getInstance();

    private static AuctionService instance;

    private BidService bidService = BidService.getInstance();
    private NotificationService notificationService = NotificationService.getInstance();
    private TransactionService transactionService = TransactionService.getInstance();
    private TreeMap<String, Auction> auctions;


    private AuctionService(){
        auctionRepository.initCounter();
        bidRepository.initCounter();

        this.auctions = new TreeMap<>();
        for(Auction auction: auctionRepository.readAll()){
            for(Bid bid: bidRepository.readAllByAuction(auction)){
                auction.addBid(bid);
            }
            auctions.put(auction.getId(), auction);
        }
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

        auctionRepository.create(auction);
        auditService.log("Create auction");
    }

    public void placeBid(Buyer bidder, Auction auction, double amount){
        if(auction.getAuctionStatus() != AuctionStatus.ACTIVE){
            throw new AuctionNotActive();
        }
        Bid previousBid = null;
        Connection conn = DbConnection.getInstance().getConn();
        try{
            conn.setAutoCommit(false);
            try{
                previousBid = auction.getLastBid();
                if(previousBid != null){
                    previousBid.setStatus(BidStatus.OUTBID);
                    bidRepository.update(previousBid);
                }
                bidService.addNewBid(bidder, auction, amount);

                auctionRepository.update(auction);
                userRepository.update(bidder);

                conn.commit();
            }
            catch(Exception e){
                conn.rollback();
                throw e;
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        if(previousBid != null){
            String previousMessage = "You have been outbid on " + auction.getProduct().getName() + " | New bid: " + amount;
            notificationService.addNotification(previousBid.getBidder(), previousMessage);
        }

        String message = "You are the highest bidder on " + auction.getProduct().getName() + " | Current bid: " + amount;
        notificationService.addNotification(bidder, message);

        auditService.log("Place bid");

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
            auctionRepository.update(auction);
            String noSaleMessage = "Your auction for " + auction.getProduct().getName() + " has ended with no bids and was cancelled";
            notificationService.addNotification(auction.getSeller(), noSaleMessage);
            throw new NoBiddingsError(auction.getProduct());
        }
        auction.setAuctionStatus(AuctionStatus.ENDED);
        Bid lastBid = auction.getLastBid();
        auction.setWinner(lastBid.getBidder());
        auctionRepository.update(auction);

        String winnerMessage = "CONGRATULATIONS! You just have won "+ auction.getProduct().getName() + " with only " + auction.getCurrentPrice() + " RON";
        String sellerMessage = "You just sold " + auction.getProduct().getName() + " with " + auction.getCurrentPrice() + " RON";

        notificationService.addNotification(lastBid.getBidder(),winnerMessage);
        notificationService.addNotification(auction.getSeller(), sellerMessage);

        transactionService.addTransaction(new Transaction(auction, auction.getWinner(), auction.getSeller(), auction.getCurrentPrice()));

        auditService.log("Close Auction");

    }

    public void cancelAuction(Auction auction){
        auction.setAuctionStatus(AuctionStatus.CANCELLED);
        auctionRepository.update(auction);

        Bid lastBid = auction.getLastBid();
        if(lastBid != null){
            lastBid.getBidder().returnBidAmount(lastBid.getAmount());
            lastBid.setStatus(BidStatus.INVALID);
            bidRepository.update(lastBid);
            userRepository.update(lastBid.getBidder());

            String buyerMessage = "You were refunded " + lastBid.getAmount() + " RON, following the cancellation of the auction " + auction.getId();
            notificationService.addNotification(lastBid.getBidder(), buyerMessage);
        }

        String cancelMessage = "Auction number " + auction.getId() + " has been cancelled";
        notificationService.addNotification(auction.getSeller(), cancelMessage);

        auditService.log("Cancel auction");

    }

    public void showActiveAuctions(){
        for(Auction auction: auctions.values()){
            if(auction.getAuctionStatus() == AuctionStatus.ACTIVE){
                System.out.println(auction);
            }
        }
        auditService.log("Show active auctions");
    }

    public List<Auction> getAuctionsByCategory(Category category){
        List<Auction> auctionsByCateg = new ArrayList<>();
        for(Auction auction: auctions.values()){
            if(auction.getProduct().getCategory().getId().equals(category.getId())){
                auctionsByCateg.add(auction);
            }
        }
        auditService.log("Get auctions by category");
        return auctionsByCateg;
    }
    public List<Auction> getAuctionsBySeller(Seller seller){
        List<Auction> auctionsBySeller = new ArrayList<>();
        for(Auction auction: auctions.values()){
            if(auction.getSeller().getId().equals(seller.getId())){
                auctionsBySeller.add(auction);
            }
        }
        auditService.log("Get auctions by seller");
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
        auditService.log("Show auctions in interval");
    }
    public void showBidsForAuction(Auction auction){
        System.out.println("--------------------------------------------------");
        for(Bid bid: auction.getBids()){
            System.out.println(bid);
        }
        System.out.println("--------------------------------------------------");
        auditService.log("Show bids for auction");
    }

    public Auction getAuctionById(String auctionId){
        if(!auctions.containsKey(auctionId)){
            throw new AuctionNotExists();
        }
        return auctions.get(auctionId);
    }
    public void deleteAuction(String id){
        if(!auctions.containsKey(id)){
            throw new AuctionNotExists();
        }
        auctions.remove(id);
        auctionRepository.delete(id);
        auditService.log("Delete auction");
    }

    public List<String> getActiveAuctionsProductCategory(){
        auditService.log("Get active auctions with product details");
        return auctionRepository.getActiveAuctionsProductCategory();
    }
}
