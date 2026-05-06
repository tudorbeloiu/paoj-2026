package com.pao.proiect.licitatii.model.enums;

public enum AuctionType {
    ENGLISH("English"),
    DUTCH("Dutch"),
    SEALED_BID("Sealed-bid");

    private final String type;

    AuctionType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
