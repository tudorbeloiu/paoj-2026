package com.pao.proiect.licitatii.model.entities;

import com.pao.proiect.licitatii.exception.InsufficientFunds;
import com.pao.proiect.licitatii.exception.InvalidNumber;
import com.pao.proiect.licitatii.model.transactions.Bid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Buyer extends User{

    private double balance;
    private final List<Bid> bidList;

    public Buyer(String name, String email, String password, double balance){
        super(name, email, password);

        if(balance < 0){
            throw new InvalidNumber();
        }
        this.balance = balance;
        this.bidList = new ArrayList<>();
    }
    public Buyer(String id, String name, String email, String password,
                 double balance, LocalDateTime createdAt) {
        super(id, name, email, password, createdAt);
        if(balance < 0){
            throw new InvalidNumber();
        }
        this.balance = balance;
        this.bidList = new ArrayList<>();
    }

    @Override
    public String getRole(){
        return "Buyer";
    }

    @Override
    public String toString(){
        return super.toString() + " | " +balance;
    }

    public void tryToBid(double amount){
        if(amount <= 0){
            throw new InvalidNumber();
        }
        if(balance - amount < 0){
            throw new InsufficientFunds(amount, balance);
        }
        balance = balance - amount;
    }
    public void returnBidAmount(double amount){
        balance = balance + amount;
    }
    public void addBid(Bid newBid){
       bidList.add(newBid);
    }

    public void setBalance(double balance){
        if(balance < 0 ){
            throw new InvalidNumber();
        }
        this.balance = balance;
    }

    public double getBalance(){
        return balance;
    }
    public List<Bid> getBidList(){

        return new ArrayList<>(bidList);
    }


}
