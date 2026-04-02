package com.pao.laboratory07.exercise1;

public enum StareComanda {
    PLACED,
    PROCESSED,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELED;
    }

    public StareComanda moveNext(){
        StareComanda nextStare;
        switch (this){
            case PLACED:
                nextStare = PROCESSED;
                break;
            case PROCESSED:
                nextStare = SHIPPED;
                break;
            case SHIPPED:
                nextStare = DELIVERED;
                break;
            default:
                nextStare = this;
                break;
        }
        return nextStare;
    }
}
