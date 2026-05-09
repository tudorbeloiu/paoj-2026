package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class RiskScorer {

    public static int compute(Transaction t){
        return computeAmountScore(t.getAmount()) + countryScore(t) +
                channelScore(t.getChannel());
    }

    private static int countryScore(Transaction t){
        if(Rules.countryIn().test(t)){
            return 25;
        }
        return 0;
    }

    private static int channelScore(String channel){
        int score = 0;
        switch (channel){
            case "WEB":
                score = 15;
                break;
            case "APP":
                score = 10;
                break;
            case "CRYPTO":
                score = 30;
                break;
            case "ATM":
                score = 0;
                break;
            case "POS":
                score = 5;
                break;
            default:
                score = 0;
        }
        return score;
    }
    private static int computeAmountScore(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("5000")) >= 0) return 70;
        if (amount.compareTo(new BigDecimal("1000")) >= 0) return 40;
        if (amount.compareTo(new BigDecimal("500"))  >= 0) return 20;
        if (amount.compareTo(new BigDecimal("100"))  <= 0) return 5;
        return 0;
    }
}
