package com.pao.proiect.licitatii.model.entities;

import com.pao.proiect.licitatii.model.products.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Seller extends User{

    private final List<Product> products;
    private final List<Auction> auctions;


    public Seller(String name, String email, String password){
        super(name, email, password);

        this.products = new ArrayList<>();
        this.auctions = new ArrayList<>();
    }
    public Seller(String id, String name, String email, String password, LocalDateTime createdAt) {
        super(id, name, email, password, createdAt);
        this.products = new ArrayList<>();
        this.auctions = new ArrayList<>();
    }

    @Override
    public String getRole(){
        return "Seller";
    }

    public boolean checkAlreadyAtAuction(Product product){
        for(Auction auction: auctions){
            if(auction.getProduct().getId().equals(product.getId())){
                return true;
            }
        }
        return false;
    }

    public void addProduct(Product product){
        products.add(product);
    }
    public void addAuction(Auction auction){
        auctions.add(auction);
    }

    public List<Product> getProducts(){
        return new ArrayList<>(products);
    }
    public List<Auction> getAuctions(){
        return new ArrayList<>(auctions);
    }



}
