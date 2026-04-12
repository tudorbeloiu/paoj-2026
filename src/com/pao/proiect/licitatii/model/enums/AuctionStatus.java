package com.pao.proiect.licitatii.model.enums;

public enum AuctionStatus {
    ACTIVE("Active"){
        @Override
        public String getEmoji(){
            return "✅";
        }
    },
    ENDED("Ended"){
        @Override
        public String getEmoji(){
            return "❎";
        }
    },
    CANCELLED("Cancelled"){
        @Override
        public String getEmoji() {
            return "❌";
        }
    };
    private final String status;
    AuctionStatus(String status){
        this.status = status;
    }

    public abstract String getEmoji();
    public String getStatus(){
        return status;
    }
}
