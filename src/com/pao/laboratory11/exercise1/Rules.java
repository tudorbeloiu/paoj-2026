package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Predicate;

public final class Rules {
    public static final BigDecimal AMOUNT_THRESHOLD = new BigDecimal("1000.00");
    public static final Set<String> RISKY_COUNTRIES = Set.of("RU", "NG", "IR", "KP", "SY");
    public static final Set<String> SUSPICIOUS_CHANNELS = Set.of("WEB", "APP", "CRYPTO");

    public static final int FLAG_THRESHOLD = 60;


    public static Predicate<Transaction> amountOver(){
        return t -> t.getAmount().compareTo(AMOUNT_THRESHOLD) >= 0;
    }

    public static Predicate<Transaction> countryIn(){
        return t -> RISKY_COUNTRIES.contains(t.getCountry());
    }

    public static Predicate<Transaction> channelSuspicious = t -> SUSPICIOUS_CHANNELS.contains(t.getChannel());


    public static Predicate<Transaction> flaggedRule = amountOver().or(countryIn()).or(channelSuspicious);
}
