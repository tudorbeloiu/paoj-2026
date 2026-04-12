package com.pao.proiect.licitatii.model.enums;

public enum BidStatus {
    WINNING("Winning"),
    OUTBID("Outbid"),
    INVALID("Invalid");

    private final String displayStatus;

    BidStatus(String displayStatus){
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus(){
        return displayStatus;
    }
}
