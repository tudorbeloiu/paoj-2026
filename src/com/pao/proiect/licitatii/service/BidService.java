package com.pao.proiect.licitatii.service;

import com.pao.proiect.licitatii.model.enums.BidStatus;
import com.pao.proiect.licitatii.exception.*;
import com.pao.proiect.licitatii.model.products.Auction;
import com.pao.proiect.licitatii.model.transactions.Bid;
import com.pao.proiect.licitatii.model.entities.*;

import java.util.HashMap;

public class BidService {

    private static BidService instance;
    private HashMap<String, Bid> listOfBids;

    private BidService(){
        this.listOfBids = new HashMap<>();
    }

    public static BidService getInstance(){
        if(instance == null){
            instance = new BidService();
        }
        return instance;
    }
    public void addNewBid(Buyer bidder, Auction auction, double amount){

        if(amount < auction.getCurrentPrice()){
            throw new InvalidBiddingValue(auction.getCurrentPrice(), amount);
        }
        Bid previousBid = auction.getLastBid();
        bidder.tryToBid(amount);

        Bid newBid = new Bid(bidder, auction, amount);

        if(previousBid != null){
            previousBid.getBidder().returnBidAmount(previousBid.getAmount());
            previousBid.setStatus(BidStatus.OUTBID);
        }

        auction.setCurrentPrice(amount);

        auction.addBid(newBid);
        bidder.addBid(newBid);
        listOfBids.put(newBid.getId(), newBid);

    }

    public HashMap<String, Bid> getListOfBids(){
        return new HashMap<>(listOfBids);
    }

}
