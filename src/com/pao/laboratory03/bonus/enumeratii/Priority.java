package com.pao.laboratory03.bonus.enumeratii;

public enum Priority {
    LOW(1, 1.0),
    MEDIUM(2, 1.5),
    HIGH(3, 2.0),
    CRITICAL(4, 3.0);

    private final int level;
    private final double multiplier;

    Priority(int level, double multiplier){
        this.level = level;
        this.multiplier = multiplier;
    }
    public int getLevel(){
        return this.level;
    }
    public double getMultiplier(){
        return this.multiplier;
    }

    public double calculateScore(int baseDays){
        return baseDays * multiplier;
    }
}
