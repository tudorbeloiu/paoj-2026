package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RiskScorer {

    public static int score(BigDecimal amount, String country, String channel){
//        min(50, floor(amount / 1000) * 10)
        int amountScore = Math.min(50, (amount.divide(new BigDecimal("1000"), 0, RoundingMode.DOWN).intValue()) * 10);
        int countryScore = 0;
        if(Rules.RISKY_COUNTRIES.contains(country)){
            countryScore = 20;
        }
        int channelScore = 0;
        switch(channel){
            case "WEB":
                channelScore = 45;
                break;
            case "CRYPTO":
                channelScore = 50;
                break;
            case "APP":
                channelScore = 35;
                break;
            case "MOBILE":
                channelScore = 30;
                break;
            case "ATM":
                channelScore = 20;
                break;
            case "POS":
                channelScore = 25;
                break;
        };

        return amountScore + countryScore + channelScore;
    }

}
